package at.rc.tacos.server.tasks.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DateUtils;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DialysisPatientService;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.utils.DataSourceResolver;
import at.rc.tacos.platform.services.utils.ServiceAnnotationResolver;
import at.rc.tacos.server.tasks.AbstractTask;

/**
 * The <code>DialysePatientTask</code> checks all {@link DialysisPatient} that
 * are stored in the database and generate {@link Transport} items.
 * <p>
 * A {@link Transport} is generated when the
 * {@link DialysisPatient#getPlannedStartOfTransport()} is within the next two
 * hours and the patient must be transported at the given day.
 * </p>
 * <p>
 * The same will be done for
 * {@link DialysisPatient#getPlannedStartForBackTransport()}
 * </p>
 * 
 * @author Michael
 */
public class DialysisPatientTask extends AbstractTask {

	private Logger log = LoggerFactory.getLogger(TransportTask.class);

	// save the acceptor for later usage
	private SocketAcceptor acceptor;
	private DataSource dataSource;

	// the needed services
	@Service(clazz = TransportService.class)
	private TransportService transportService;
	@Service(clazz = DialysisPatientService.class)
	private DialysisPatientService dialysisPatientService;

	private List<Object> resolvedServices;

	/**
	 * Creates a new instance of this task.
	 * <p>
	 * The task will be scheduled to run every 2 hours to query and generate
	 * transports for the patients. The created <code>Transport</code>s will be
	 * brodcastet to every connected session.
	 * </p>
	 */
	public DialysisPatientTask() {
		super("DialysisPatientTask", 2, TimeUnit.MINUTES);
	}

	@Override
	public void setupTask(ServerContext serverContext, SocketAcceptor acceptor) throws Exception {
		// save for later usage
		this.acceptor = acceptor;
		this.dataSource = serverContext.getDataSource();

		// resolve and inject the needed services
		ServiceAnnotationResolver resolver = new ServiceAnnotationResolver(serverContext.getDbalServiceFactory());
		resolvedServices = resolver.resolveAnnotations(this);
	}

	@Override
	public void runTask() throws Exception {
		// get a new connection
		Connection connection = dataSource.getConnection();
		if (connection == null) {
			throw new ServiceException("Failed to get a valid database connection, the data source returned null");
		}
		// now check if the resolved services need a data source
		DataSourceResolver dataSourceResolver = new DataSourceResolver(connection);
		dataSourceResolver.resolveAnnotations(resolvedServices.toArray());

		// the current date
		Calendar currentDate = Calendar.getInstance();
		// the future date
		Calendar futureDate = Calendar.getInstance();
		futureDate.add(Calendar.HOUR_OF_DAY, 2);

		// query all transports
		List<DialysisPatient> dialysisPatients = dialysisPatientService.listDialysisPatient();

		// the list of transports to send to the clients
		List<Transport> transportList = new ArrayList<Transport>();

		// now check all patients
		for (DialysisPatient patient : dialysisPatients) {
			// check 1: Do we have already generated a transport today
			Calendar lastGenerated = Calendar.getInstance();
			lastGenerated.setTimeInMillis(patient.getLastTransportDate());
			if (DateUtils.isSameDay(lastGenerated, currentDate)) {
				continue;
			}

			// check 2 + 3: stationary and need today?
			if (!doCreateTransport(patient)) {
				continue;
			}

			// set the planned date of the transport
			Calendar plannedTransport = Calendar.getInstance();
			plannedTransport.setTimeInMillis(patient.getPlannedStartOfTransport());
			plannedTransport.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
			plannedTransport.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
			plannedTransport.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));

			// check 4: Is within the next two hours
			if (plannedTransport.after(futureDate)) {
				continue;
			}

			// create a new transport from this dialysis patient
			Transport transport = Transport.createTransport(patient, "Administrator");
			int id = transportService.addTransport(transport);
			transport.setTransportId(id);
			transportList.add(transport);

			// update the patient
			patient.setLastTransportDate(currentDate.getTimeInMillis());
			dialysisPatientService.updateDialysisPatient(patient);

			log.info("Created a new transport " + transport.getTransportId() + " for the patient " + patient.getId());
		}

		// check wether we have to create backtransports
		for (DialysisPatient patient : dialysisPatients) {
			// check 1: Do we have already generated a transport today
			Calendar lastGenerated = Calendar.getInstance();
			lastGenerated.setTimeInMillis(patient.getLastBackTransporDate());
			if (DateUtils.isSameDay(lastGenerated, currentDate)) {
				continue;
			}

			// check 2 + 3: stationary and need today?
			if (!doCreateTransport(patient)) {
				continue;
			}

			// set the planned date of the transport
			Calendar plannedTransport = Calendar.getInstance();
			plannedTransport.setTimeInMillis(patient.getPlannedStartForBackTransport());
			plannedTransport.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
			plannedTransport.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
			plannedTransport.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH));

			// check 4: Is within the next two hours
			if (plannedTransport.after(futureDate)) {
				continue;
			}

			// create a new transport from this dialysis patient
			Transport transport = Transport.createBackTransport(patient, "Administrator");
			int id = transportService.addTransport(transport);
			transport.setTransportId(id);
			transportList.add(transport);

			// update the patient
			patient.setLastBackTransportDate(currentDate.getTimeInMillis());
			dialysisPatientService.updateDialysisPatient(patient);

			log.info("Created a new backtransport " + transport.getTransportId() + " for the patient " + patient.getId());
		}

		// create and brodcast the add message
		AddMessage<Transport> addMessage = new AddMessage<Transport>(transportList);
		for (IoSession session : acceptor.getManagedSessions().values()) {
			session.write(addMessage);

		}

		log.info("Brodcasting " + transportList.size() + " created transports to " + acceptor.getManagedSessionCount() + " sessions");
	}

	/**
	 * Returns whether this dialysis patient needs a transport today.
	 * <p>
	 * 
	 * @param patient
	 *            the patient to check
	 * @return true if we have to create a transport
	 */
	private boolean doCreateTransport(DialysisPatient patient) {
		Calendar currentDate = Calendar.getInstance();

		// transports
		if (patient.isStationary()) {
			return false;
		}

		// do we have to create a transport today?
		switch (currentDate.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				if (!patient.isMonday())
					return false;
				return true;
			case Calendar.TUESDAY:
				if (!patient.isTuesday())
					return false;
				return true;
			case Calendar.WEDNESDAY:
				if (!patient.isWednesday())
					return false;
				return true;
			case Calendar.THURSDAY:
				if (!patient.isThursday())
					return false;
				return true;
			case Calendar.FRIDAY:
				if (!patient.isFriday())
					return false;
				return true;
			case Calendar.SATURDAY:
				if (!patient.isSaturday())
					return false;
				return true;
			case Calendar.SUNDAY:
				if (!patient.isSunday())
					return false;
				return true;
		}
		// noting matched
		return false;
	}
}

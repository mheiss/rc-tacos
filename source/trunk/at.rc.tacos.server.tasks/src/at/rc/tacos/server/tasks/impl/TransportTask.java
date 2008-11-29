package at.rc.tacos.server.tasks.impl;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.utils.DataSourceResolver;
import at.rc.tacos.platform.services.utils.ServiceAnnotationResolver;
import at.rc.tacos.server.tasks.AbstractTask;

/**
 * The <code>TransportTask</code> is a background task that the status of the
 * transports on a regular base. When a prebooked transport is within the next 2
 * hours then the task updates the transport and moves it to the outstanding
 * transports by changing the status and sending a update request.
 * <p>
 * After the transport has been updated a notifcation will be send to the
 * client.
 * </p>
 */
public class TransportTask extends AbstractTask {

	private Logger log = LoggerFactory.getLogger(TransportTask.class);
	private final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	// save the acceptor for later usage
	private SocketAcceptor acceptor;
	private DataSource dataSource;

	// the needed transport service
	@Service(clazz = TransportService.class)
	private TransportService transportService;

	private List<Object> resolvedServices;

	/**
	 * Creates a new transport background task.
	 * <p>
	 * The task will be scheduled to run every 2 minutes to query and update the
	 * transports. The updated <code>Transport</code>s will be brodcastet to
	 * every connected session.
	 * </p>
	 */
	public TransportTask() {
		super("TransportTask", 2, TimeUnit.MINUTES);
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

		// add two hours to the current date
		Calendar future = Calendar.getInstance();
		future.add(Calendar.HOUR_OF_DAY, +2);

		// the result list that contains all updated transports
		List<Transport> updatedTransports = new ArrayList<Transport>();

		// query all open transports
		for (Transport transport : transportService.listPrebookedTransports()) {
			Calendar planned = Calendar.getInstance();
			planned.setTimeInMillis(transport.getPlannedStartOfTransport());
			if (planned.after(future)) {
				log.debug("Skipping transport " + transport.getTransportId() + " scheduled @ " + FORMATTER.format(planned.getTime()));
				continue;
			}
			// update and persist the transport
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
			transportService.updateTransport(transport);
			updatedTransports.add(transport);
			log.info("Updating the status of transport " + transport.getTransportId());
		}
		// create the update message
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(updatedTransports);

		// brodcast the update
		for (IoSession session : acceptor.getManagedSessions().values()) {
			session.write(updateMessage);

		}
		log.info("Brodcasting update of " + updatedTransports.size() + " transports to " + acceptor.getManagedSessionCount());
	}
}

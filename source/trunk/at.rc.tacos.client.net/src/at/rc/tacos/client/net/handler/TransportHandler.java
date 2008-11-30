package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>TransportHandler</code> manages the locally cached
 * {@link Transport} instances.
 * 
 * @author Michael
 */
public class TransportHandler implements Handler<Transport> {

	private List<Transport> transportList = Collections.synchronizedList(new LinkedList<Transport>());
	private Logger log = LoggerFactory.getLogger(TransportHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Transport> message) throws SQLException, ServiceException {
		synchronized (transportList) {
			transportList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Transport> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Transport> message) throws SQLException, ServiceException {
		synchronized (transportList) {
			transportList.clear();
			transportList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Transport> message) throws SQLException, ServiceException {
		synchronized (transportList) {
			transportList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Transport> message) throws SQLException, ServiceException {
		synchronized (transportList) {
			for (Transport updatedTransport : message.getObjects()) {
				if (!transportList.contains(updatedTransport)) {
					continue;
				}
				int index = transportList.indexOf(updatedTransport);
				transportList.set(index, updatedTransport);
			}
		}
	}

	/**
	 * Returns a list of all (underway) transports that are assigned to this
	 * vehicle.
	 * 
	 * @param vehicleName
	 *            the name of the vehicle to list the transports
	 * @return transport list filtered by vehicle
	 */
	public List<Transport> getUnderwayTransportsByVehicle(String vehicleName) {
		synchronized (transportList) {
			List<Transport> filteredList = new ArrayList<Transport>();
			for (Transport transport : transportList) {
				VehicleDetail assignedVehicle = transport.getVehicleDetail();
				if (assignedVehicle == null)
					continue;
				if (transport.getProgramStatus() != IProgramStatus.PROGRAM_STATUS_UNDERWAY)
					continue;
				// check the vehicle
				if (assignedVehicle.getVehicleName().equals(vehicleName))
					filteredList.add(transport);
			}
			return filteredList;
		}
	}

	/**
	 * Returns a list of the transports with the program status 'journal' which
	 * are assigned to this vehicle and have no set transport status S6 yet.
	 * 
	 * @param vehicleName
	 *            the name of the vehicle to list the transports
	 * @return transort list filtered by vehicle, program status 'journal' and
	 *         without transport status S6
	 */
	public List<Transport> getJournalTransportsByVehicleAndStatusSix(String vehicleName) {
		synchronized (transportList) {
			List<Transport> filteredList = new ArrayList<Transport>();
			for (Transport transport : transportList) {
				if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
					continue;
				// get the vehicle
				VehicleDetail vehicle = transport.getVehicleDetail();
				int programStatus = transport.getProgramStatus();
				// assert valid
				if (vehicle == null)
					continue;
				// check the vehicle
				if (vehicle.getVehicleName().equalsIgnoreCase(vehicleName) && programStatus == IProgramStatus.PROGRAM_STATUS_JOURNAL)
					filteredList.add(transport);
			}
			return filteredList;
		}
	}

	/**
	 * Returns a new array containing the managed <code>Transport</code>
	 * instances.
	 * 
	 * @return an array containing the <code>Transport</code> instances.
	 */
	public Transport[] toArray() {
		return transportList.toArray(new Transport[transportList.size()]);
	}
}

package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class VehicleHandler implements Handler<VehicleDetail> {

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Service(clazz = TransportService.class)
	private TransportService transportService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and add the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.addVehicle(vehicle))
				throw new ServiceException("Failed to add the vehicle: " + vehicle);
		}
		// brodcast the added transports
		session.writeResponseBrodcast(message, vehicleList);
	}

	@Override
	public void get(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> list = vehicleService.listVehicles();
		if (list == null)
			throw new ServiceException("Failed to list the vehicles");

		// check for locks
		for (VehicleDetail detail : list) {
			if (!lockableService.containsLock(detail)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(detail);
			detail.setLocked(lockable.isLocked());
			detail.setLockedBy(lockable.getLockedBy());
		}

		// return the requested vehicles
		session.writeResponse(message, list);
	}

	@Override
	public void remove(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and remove the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.removeVehicle(vehicle))
				throw new ServiceException("Failed to remove the vehicle " + vehicle);
			// remove the lock
			lockableService.removeLock(vehicle);
		}
		// brodcast the removed vehicles
		session.writeResponseBrodcast(message, vehicleList);
	}

	@Override
	public void update(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		List<VehicleDetail> vehicleList = message.getObjects();
		// loop and update the vehicles
		for (VehicleDetail vehicle : vehicleList) {
			if (!vehicleService.updateVehicle(vehicle))
				throw new ServiceException("Failed to update the vehicle " + vehicle);
			// update the lock
			lockableService.updateLock(vehicle);

			// check if the vehicle has a transport
			updateUnderwayTransports(session, vehicle);
		}
		// brodcast the updated vehicles
		session.writeResponseBrodcast(message, vehicleList);
	}

	@Override
	public void execute(MessageIoSession session, Message<VehicleDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("setStatusSix".equalsIgnoreCase(command)) {
			// get the vehicle to query the transports
			VehicleDetail detail = message.getFirstElement();
			updateArchivedTransports(session, detail);
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	/**
	 * Helper method to update all underway transports that are assigned to this
	 * vehicle.
	 */
	private void updateUnderwayTransports(MessageIoSession session, VehicleDetail vehicleDetail) throws SQLException {
		List<Transport> transportList = transportService.listUnderwayTransportsByVehicle(vehicleDetail.getVehicleName());
		if (transportList == null || transportList.isEmpty()) {
			return;
		}
		// update each transport with the new vehicle
		for (Transport transport : transportList) {
			transport.setVehicleDetail(vehicleDetail);
			transportService.updateTransport(transport);
		}
		// brodcast the updated transports
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transportList);
		session.write(updateMessage);
	}

	/**
	 * Helper method to update the archived transports if the vehicle is at
	 * home.
	 */
	private void updateArchivedTransports(MessageIoSession session, VehicleDetail vehicleDetail) throws SQLException, ServiceException {
		List<Transport> transportList = transportService.listArchivedWithoutStatusSix(vehicleDetail.getVehicleName());
		if (transportList == null || transportList.isEmpty()) {
			// noting to do
			return;
		}

		// the current time
		long timestamp = Calendar.getInstance().getTimeInMillis();
		// set the transport status S6
		for (Transport transport : transportList) {
			transport.addStatus(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION, timestamp);
			if (transportService.updateTransport(transport)) {
				throw new ServiceException("Failed to set status at home for transport " + transport);
			}
		}

		// brodcast the update to the clients
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transportList);
		session.brodcastMessage(updateMessage);
	}

	@Override
	public VehicleDetail[] toArray() {
		return null;
	}
}

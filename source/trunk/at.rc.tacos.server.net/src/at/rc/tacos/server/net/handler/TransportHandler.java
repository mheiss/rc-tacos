package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.iface.IProgramStatus;
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
import at.rc.tacos.platform.util.MyUtils;

public class TransportHandler implements Handler<Transport> {

	@Service(clazz = TransportService.class)
	private TransportService transportService;

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Transport> message) throws ServiceException, SQLException {
		List<Transport> transports = message.getObjects();
		// loop and add the transports
		for (Transport transport : transports) {
			int id = transportService.addTransport(transport);
			if (id == TransportService.TRANSPORT_ERROR) {
				throw new ServiceException("Failed to add the transport: " + transport);
			}
			transport.setTransportId(id);
			VehicleDetail transportVehicle = transport.getVehicleDetail();
			// for the direct car assign to a transport (in the transport form)
			if (transportVehicle != null && transport.getTransportNumber() == 0) {
				// set the current year to generate a valid transport numer
				transport.setYear(Calendar.getInstance().get(Calendar.YEAR));
				int transportNr = transportService.generateTransportNumber(transport);
				if (transportNr == TransportService.TRANSPORT_ERROR)
					throw new ServiceException("Failed to generate a valid transport number for transport " + transport);
				transport.setTransportNumber(transportNr);
				// to set the AE- status
				if (!transportService.updateTransport(transport))
					throw new ServiceException("Failed to update the newly added transport: " + transport);
			}
			// query the status of the vehicle from the database
			VehicleDetail vehicleDetail = vehicleService.getVehicleByName(transportVehicle.getVehicleName());
			if (vehicleDetail != null) {
				// query all underway transports of this vehicle
				List<Transport> currentTransports = transportService.listUnderwayTransportsByVehicle(vehicleDetail.getVehicleName());

				// update the status color of the vehicle
				checkVehicleColorState(currentTransports, vehicleDetail);

				// persist the vehicle changes
				if (!vehicleService.updateVehicle(vehicleDetail)) {
					throw new SQLException("Failed to update the assigned vehicle '" + vehicleDetail + "' from the transport");
				}

				// brodcast the updated vehicle
				UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(vehicleDetail);
				session.brodcastMessage(updateMessage);

			}
		}
		// brodcast the updated transports
		session.writeResponseBrodcast(message, transports);
	}

	@Override
	public void get(MessageIoSession session, Message<Transport> message) throws ServiceException, SQLException {
		// get the params out of the message
		Map<String, String> params = message.getParams();

		List<Transport> list = new ArrayList<Transport>();
		List<Transport> tmpList = new ArrayList<Transport>();

		// online transport listing for journal short if a vehicle and a
		// location is selected
		if (params.containsKey(IFilterTypes.TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER) && params.containsKey(IFilterTypes.TRANSPORT_ARCHIVED_FILTER)
				&& params.containsKey(IFilterTypes.TRANSPORT_LOCATION_FILTER)) {
			// for online transport listing
			// list only the archived transports (journal)
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			final String locationFilter = params.get(IFilterTypes.TRANSPORT_LOCATION_FILTER);
			final String vehicleFilter = params.get(IFilterTypes.TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER);
			final int locationId = Integer.valueOf(locationFilter).intValue();

			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();

			// show the transports in the journal which have a assigned vehicle
			tmpList = transportService.listArchivedTransportsByVehicleLocationAndDateAndVehicle(dateStart, dateEnd, locationId, vehicleFilter);
			// check archived
			if (tmpList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by date from " + time);
			}
			list.addAll(tmpList);

			// show the transports in the journal which have no assigned vehicle
			// - so the location is the planned location
			tmpList = transportService.listArchivedTransportsByTransportLocationAndDateAndVehicle(dateStart, dateEnd, locationId, vehicleFilter);
			// check archived
			if (tmpList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by date from " + time);
			}

			// add the transports without a vehicle
			for (Transport transport : tmpList) {
				if (transport.getVehicleDetail() != null)
					continue;
				list.add(transport);
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}
		// online transport listing for journal short if a vehicle is selected
		if (params.containsKey(IFilterTypes.TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER) && params.containsKey(IFilterTypes.TRANSPORT_ARCHIVED_FILTER)) {
			// for online transport listing
			// list only the archived transports (journal)
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			final String vehicleFilter = params.get(IFilterTypes.TRANSPORT_JOURNAL_SHORT_VEHICLE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();

			// show the transports in the journal
			list = transportService.listArchivedTransportsByVehicle(dateStart, dateEnd, vehicleFilter);
			if (list == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by date and vehicle from " + time + " " + vehicleFilter);
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}

		// online transport listing
		// *** transports todo (prebooking and outstanding) ***
		if (params.containsKey(IFilterTypes.TRANSPORT_TODO_FILTER)) {
			// for online transport listing
			// list only the prebooked transports
			// show the prebooked transports
			list = transportService.listTransportsTodo();
			// check the prebooked
			if (list == null) {
				throw new ServiceException("Failed to list the transports todo (prebooked and outstanding)");
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}
		// online transport listing
		// *** only running transports (outstanding and underway) ***
		if (params.containsKey(IFilterTypes.TRANSPORT_UNDERWAY_FILTER)) {
			// for online transport listing
			// list only the running transports (outstanding and underway)
			// show the running transports
			// show current transports that are in progress
			list = transportService.listUnderwayTransports();
			if (list == null) {
				throw new ServiceException("Failed to list the underway transports");
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}
		// online transport listing *** archived by location ***
		if (params.containsKey(IFilterTypes.TRANSPORT_ARCHIVED_FILTER) && params.containsKey(IFilterTypes.TRANSPORT_LOCATION_FILTER)) {
			// for online transport listing
			// list only the archived transports (journal)
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			final String locationFilter = params.get(IFilterTypes.TRANSPORT_LOCATION_FILTER);
			final int locationId = Integer.valueOf(locationFilter).intValue();

			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();

			// show the transports in the journal which have a assigned vehicle
			tmpList = transportService.listArchivedTransportsByVehicleLocationAndDate(dateStart, dateEnd, locationId);
			// check archived
			if (list == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by vehicle and date from " + time);
			}
			// add all
			list.addAll(tmpList);

			// show the transports in the journal which have no assigned vehicle
			// - so the location is the planned location
			tmpList = transportService.listArchivedTransportsByTransportLocationAndDate(dateStart, dateEnd, locationId);
			if (tmpList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by location and date from " + time);
			}
			// add only transports without a vehicle
			for (Transport transport : tmpList) {
				if (transport.getVehicleDetail() != null) {
					continue;
				}
				list.add(transport);
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}
		// online transport listing *** only archived transports ***
		if (params.containsKey(IFilterTypes.TRANSPORT_ARCHIVED_FILTER)) {
			// for online transport listing
			// list only the archived transports (journal)
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();

			// show the transports in the journal
			list = transportService.listArchivedTransports(dateStart, dateEnd);
			if (list == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by date from " + time);
			}

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}
		if (params.containsKey(IFilterTypes.DATE_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();

			// check the prebooked
			tmpList = transportService.listPrebookedTransports();
			if (tmpList == null) {
				throw new ServiceException("Failed to list the prebooked transports");
			}
			list.addAll(tmpList);

			// check the running
			tmpList = transportService.listRunningTransports();
			if (tmpList == null) {
				throw new ServiceException("Failed to list the running and open transports");
			}
			list.addAll(tmpList);

			// check archived
			tmpList = transportService.listArchivedTransports(dateStart, dateEnd);
			if (tmpList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the archived transports by date from " + time);
			}
			list.addAll(tmpList);

			// check for locks
			syncronizeLocks(list);

			// send back the requested transports
			session.writeResponse(message, list);
			return;
		}

		// no filter criteria matched
		throw new ServiceException("Listing of all transport entries is not supported");
	}

	@Override
	public void remove(MessageIoSession session, Message<Transport> message) throws ServiceException, SQLException {
		throw new ServiceException("Removing of transports is not allowed, try to CANCEL or STORNO the transport");
	}

	@Override
	public void update(MessageIoSession session, Message<Transport> message) throws ServiceException, SQLException {
		List<Transport> transports = message.getObjects();
		// loop and update the transports
		for (Transport transport : transports) {
			// generate a transport id if we do not have one
			if (transport.getVehicleDetail() != null && transport.getTransportNumber() == 0) {
				// set the current year to generate a valid transport numer
				transport.setYear(Calendar.getInstance().get(Calendar.YEAR));
				int transportNr = transportService.generateTransportNumber(transport);
				if (transportNr == TransportService.TRANSPORT_ERROR)
					throw new ServiceException("Failed to generate a valid transport number for transport " + transport);
				transport.setTransportNumber(transportNr);
			}

			// Vehicle is removed but we have a transport number -> cancel
			if (transport.getVehicleDetail() == null && transport.getTransportNumber() > 0) {
				// remove assigned vehicle, reset the transport number (to 0 and
				// restore the given number), set program status to outstanding
				if (!transportService.removeVehicleFromTransport(transport))
					throw new ServiceException("Failed to remove the transport from the vehicle");
			}

			// STORNO OR FORWARD
			if (transport.getTransportNumber() == Transport.TRANSPORT_CANCLED || transport.getTransportNumber() == Transport.TRANSPORT_FORWARD) {
				if (transport.getVehicleDetail() != null) {
					if (!transportService.removeVehicleFromTransport(transport))
						throw new ServiceException("Failed to remove the transport from the vehicle");
					transport.clearVehicleDetail();
					// reset the transportNumber to CANCELED
					transport.setTransportNumber(Transport.TRANSPORT_CANCLED);
				}
				// cancel or forward the transport
				// set the transportNumber to -1 or -2
				// (to the value in the transport) and set program status
				// journal
				if (!transportService.cancelTransport(transport))
					throw new ServiceException("Failed to cancle the transport " + transport);
			}

			// send a simple update request to the dao
			if (!transportService.updateTransport(transport))
				throw new ServiceException("Failed to update the transport: " + transport);

			// update the lock
			lockableService.updateLock(transport);

			// only update outstanding transports
			if (transport.getProgramStatus() != IProgramStatus.PROGRAM_STATUS_OUTSTANDING) {
				continue;
			}

			// update the 'readyVehicles' list
			List<VehicleDetail> readyVehicleList = vehicleService.listReadyVehicles();
			for (VehicleDetail vehicleDetail : readyVehicleList) {
				// query the underway transports for all vehicles
				List<Transport> transportList = transportService.listUnderwayTransportsByVehicle(vehicleDetail.getVehicleName());
				// check and update the vehicle status
				checkVehicleColorState(transportList, vehicleDetail);

				// persist the vehicle changes
				if (!vehicleService.updateVehicle(vehicleDetail)) {
					throw new SQLException("Failed to update the assigned vehicle '" + vehicleDetail + "' from the transport");
				}

				// brodcast the updated vehicle
				UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(vehicleDetail);
				session.brodcastMessage(updateMessage);
			}
		}
		// brodcast the updated transports
		session.writeResponseBrodcast(message, transports);
	}

	@Override
	public void execute(MessageIoSession session, Message<Transport> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	/**
	 * Helper method to syncronize the locks
	 */
	private void syncronizeLocks(List<Transport> transportList) {
		for (Transport transport : transportList) {
			if (!lockableService.containsLock(transport)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(transport);
			transport.setLocked(lockable.isLocked());
			transport.setLockedBy(lockable.getLockedBy());
		}
	}

	/**
	 * Helper method to update the status of the vehicles
	 */
	private void checkVehicleColorState(List<Transport> transportList, VehicleDetail detail) {
		// simplest calculation comes first ;)
		// green (30) is for a 'underway'(program status) vehicle not possible
		if (transportList.isEmpty()) {
			if (!detail.getLastDestinationFree().equalsIgnoreCase(""))
				detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_BLUE);
			else if (detail.getLastDestinationFree().equalsIgnoreCase(""))
				detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);

			if (!detail.isReadyForAction() || detail.isOutOfOrder())
				detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
			return;
		}

		ArrayList<Integer> list = new ArrayList<Integer>();
		// get the most important status of each transport
		for (Transport transport : transportList) {
			int mostImportantStatus = transport.getMostImportantStatusMessageOfOneTransport();
			list.add(mostImportantStatus);
		}

		// for a 'red' status
		if (list.contains(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)
				|| list.contains(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA)) {
			detail.setTransportStatus(VehicleDetail.TRANSPROT_STATUS_RED); // 10
			return;
		}

		// for a 'yellow' status
		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_YELLOW); // 20

		// for a 'gray' status
		if (detail.isOutOfOrder() | !detail.isReadyForAction() || detail.getDriver() == null)
			detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
	}

	@Override
	public Transport[] toArray() {
		return null;
	}
}

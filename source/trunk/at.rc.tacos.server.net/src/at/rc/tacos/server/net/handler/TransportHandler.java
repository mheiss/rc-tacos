package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;
import at.rc.tacos.platform.util.MyUtils;

public class TransportHandler implements INetHandler<Transport> {

	@Service(clazz = TransportService.class)
	private TransportService transportService;

	@Override
	public Transport add(Transport model) throws ServiceException, SQLException {
		int id = transportService.addTransport(model);
		if (id == Transport.TRANSPORT_ERROR) {
			throw new ServiceException("Failed to add the transport: " + model);
		}
		model.setTransportId(id);

		// for the direct car assign to a transport (in the transport form)
		if (model.getVehicleDetail() != null && model.getTransportNumber() == 0) {
			// set the current year to generate a valid transport numer
			model.setYear(Calendar.getInstance().get(Calendar.YEAR));
			int transportNr = transportService.generateTransportNumber(model);
			if (transportNr == Transport.TRANSPORT_ERROR)
				throw new ServiceException("Failed to generate a valid transport number for transport " + model);
			model.setTransportNumber(transportNr);
			// to set the AE- status
			if (!transportService.updateTransport(model))
				throw new ServiceException("Failed to update the newly added transport: " + model);
		}

		return model;
	}

	@Override
	public List<Transport> execute(String command, List<Transport> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Transport> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Transport> list = new ArrayList<Transport>();
		List<Transport> tmpList = new ArrayList<Transport>();
		// if there is no filter -> request all
		if (params == null || params.isEmpty()) {
			throw new ServiceException("Listing of all transport entries is not supported");
		}
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
			return list;
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
			return list;
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
			return list;
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
			return list;
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
			return list;
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
		}
		return list;
	}

	@Override
	public Transport remove(Transport model) throws ServiceException, SQLException {
		throw new ServiceException("Removing of transports is not allowed, try to CANCEL or STORNO the transport");
	}

	@Override
	public Transport update(Transport model) throws ServiceException, SQLException {
		// generate a transport id if we do not have one
		if (model.getVehicleDetail() != null && model.getTransportNumber() == 0) {
			// set the current year to generate a valid transport numer
			model.setYear(Calendar.getInstance().get(Calendar.YEAR));
			int transportNr = transportService.generateTransportNumber(model);
			if (transportNr == Transport.TRANSPORT_ERROR)
				throw new ServiceException("Failed to generate a valid transport number for transport " + model);
			model.setTransportNumber(transportNr);
		}

		// Vehicle is removed but we have a transport number -> cancel
		if (model.getVehicleDetail() == null && model.getTransportNumber() > 0) {
			// remove assigned vehicle, reset the transport number (to 0 and
			// restore the given number), set program status to outstanding
			if (!transportService.removeVehicleFromTransport(model))
				throw new ServiceException("Failed to remove the transport from the vehicle");
		}

		// STORNO OR FORWARD
		if (model.getTransportNumber() == Transport.TRANSPORT_CANCLED || model.getTransportNumber() == Transport.TRANSPORT_FORWARD) {
			if (model.getVehicleDetail() != null) {
				if (!transportService.removeVehicleFromTransport(model))
					throw new ServiceException("Failed to remove the transport from the vehicle");
				model.clearVehicleDetail();
				// reset the transportNumber to CANCELED
				model.setTransportNumber(Transport.TRANSPORT_CANCLED);
			}
			// cancel or forward the transport
			// set the transportNumber to -1 or -2
			// (to the value in the transport) and set program status journal
			if (!transportService.cancelTransport(model))
				throw new ServiceException("Failed to cancle the transport " + model);
		}

		// send a simple update request to the dao
		if (!transportService.updateTransport(model))
			throw new ServiceException("Failed to update the transport: " + model);
		return model;
	}

}

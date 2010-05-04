package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.dbal.RosterService;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.util.MyUtils;

public class RosterHandler implements Handler<RosterEntry> {

	@Service(clazz = RosterService.class)
	private RosterService rosterService;

	@Service(clazz = VehicleService.class)
	private VehicleService vehicleService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and add the roster entries
		for (RosterEntry entry : rosterList) {
			int id = rosterService.addRosterEntry(entry);
			if (id == -1)
				throw new ServiceException("Failed to add the roster entry " + entry);
			entry.setRosterId(id);
		}
		// brodcast the added roster entries
		session.writeResponseBrodcast(message, rosterList);
	}

	@Override
	public void get(MessageIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		// get the params of the request
		Map<String, String> params = message.getParams();
		if (params.containsKey(IFilterTypes.DATE_FILTER) && params.containsKey(IFilterTypes.ROSTER_LOCATION_FILTER)) {
			// get the query filter and parse it to a date time
			final String dateFilter = params.get(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			int filterLocationId = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_FILTER));
			List<RosterEntry> rosterList = rosterService.listRosterEntriesByDateAndLocation(dateStart, dateEnd, filterLocationId);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}
			// check for locks
			for (RosterEntry entry : rosterList) {
				if (!lockableService.containsLock(entry)) {
					continue;
				}
				Lockable lockable = lockableService.getLock(entry);
				entry.setLocked(lockable.isLocked());
				entry.setLockedBy(lockable.getLockedBy());
			}

			// send the result back
			session.writeResponse(message, rosterList);
			return;
		}
		if (params.containsKey(IFilterTypes.ROSTER_MONTH_FILTER) && params.containsKey(IFilterTypes.ROSTER_YEAR_FILTER)) {
			int monthFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_MONTH_FILTER));
			int yearFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_YEAR_FILTER));
			int locationFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_LOCATION_FILTER)) {
				locationFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_FILTER));
			}
			int locationStaffMemberFilter = -1;
			String functionStaffMemberCompetenceFilter = null;
			if (params.containsKey(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER)) {
				functionStaffMemberCompetenceFilter = params.get(IFilterTypes.ROSTER_FUNCTION_STAFF_MEMBER_COMPETENCE_FILTER);
			}
			if (params.containsKey(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER)) {
				locationStaffMemberFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_LOCATION_STAFF_MEMBER_FILTER));
			}
			int staffMemberFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER)) {
				staffMemberFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_STAFF_MEMBER_FILTER));
			}
			String statisticFilter = null;
			if (params.containsKey(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER)) {
				statisticFilter = params.get(IFilterTypes.ROSTER_MONTH_STATISTIC_FILTER);
			}
			int serviceTypeFilter = -1;
			if (params.containsKey(IFilterTypes.ROSTER_SERVICE_TYPE_FILTER)) {
				serviceTypeFilter = Integer.parseInt(params.get(IFilterTypes.ROSTER_SERVICE_TYPE_FILTER));
			}
			List<RosterEntry> rosterList = rosterService.listRosterEntriesForRosterMonth(locationFilter, monthFilter, yearFilter,
					locationStaffMemberFilter, functionStaffMemberCompetenceFilter, staffMemberFilter, statisticFilter, serviceTypeFilter);

			// assert valid list
			if (rosterList == null) {
				throw new ServiceException("Failed to list the roster entries by date from.");
			}

			// check for locks
			for (RosterEntry entry : rosterList) {
				if (!lockableService.containsLock(entry)) {
					continue;
				}
				Lockable lockable = lockableService.getLock(entry);
				entry.setLocked(lockable.isLocked());
				entry.setLockedBy(lockable.getLockedBy());
			}

			// send the result back
			session.writeResponse(message, rosterList);
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
			List<RosterEntry> rosterList = rosterService.listRosterEntryByDate(dateStart, dateEnd);
			if (rosterList == null) {
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) + " bis "
						+ MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new ServiceException("Failed to list the roster entries by date from " + time);
			}

			// check for locks
			for (RosterEntry entry : rosterList) {
				if (!lockableService.containsLock(entry)) {
					continue;
				}
				Lockable lockable = lockableService.getLock(entry);
				entry.setLocked(lockable.isLocked());
				entry.setLockedBy(lockable.getLockedBy());
			}

			// send the result back
			session.writeResponse(message, rosterList);
			return;
		}
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			// get the query filter
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			RosterEntry entry = rosterService.getRosterEntryById(id);
			if (entry == null)
				throw new ServiceException("Failed to list the roster entry by id:" + id);

			// check for locks
			if (lockableService.containsLock(entry)) {
				Lockable lockable = lockableService.getLock(entry);
				entry.setLocked(lockable.isLocked());
				entry.setLockedBy(lockable.getLockedBy());
			}

			// send the result back
			session.writeResponse(message, entry);
			return;
		}

		// no parameter matched
		throw new ServiceException("Listing of all roster entries is denied");
	}

	@Override
	public void remove(MessageIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and remove the roster entries
		for (RosterEntry entry : rosterList) {
			if (!rosterService.removeRosterEntry(entry.getRosterId()))
				throw new ServiceException("Failed to remove the roster entry:" + entry);
			// remove the lock
			lockableService.removeLock(entry);
		}
		// brodcast the removed roster objects
		session.writeResponseBrodcast(message, rosterList);
	}

	@Override
	public void update(MessageIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		List<RosterEntry> rosterList = message.getObjects();
		// loop and update the roster entries
		for (RosterEntry entry : rosterList) {
			if (!rosterService.updateRosterEntry(entry))
				throw new ServiceException("Failed to update the roster entry:" + entry);
			// update the lock
			lockableService.updateLock(entry);

			// check and detach staff if needed
			updateVehicleOfEntry(session, entry);
		}
		// brodcast the updated roster objects
		session.writeResponseBrodcast(message, rosterList);
	}

	@Override
	public void execute(MessageIoSession session, Message<RosterEntry> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	/**
	 * Helper method to update a staff member entry
	 */
	private void updateVehicleOfEntry(MessageIoSession session, RosterEntry entry) throws ServiceException, SQLException {
		// we need only to check staff members who signed out
		if (entry.getRealEndOfWork() == 0)
			return;

		// get the staff member of this entry
		StaffMember staffmember = entry.getStaffMember();
		if (staffmember == null) {
			return;
		}

		// check if this staff member is assigned to a vehicle
		VehicleDetail vehicleDetail = vehicleService.getVehicleByStaffMember(staffmember.getStaffMemberId());
		if (vehicleDetail == null) {
			return;
		}

		// setup the current day
		Date currentDay = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DAY_OF_MONTH);
		Date nextDay = DateUtils.addDays(currentDay, 1);

		// check if this staff member has another roster entry for this day
		List<RosterEntry> rosterList = rosterService.listRosterEntriesByDateAndStaff(currentDay.getTime(), nextDay.getTime(), staffmember
				.getStaffMemberId());
		for (RosterEntry rosterEntry : rosterList) {
			if (rosterEntry.getRealEndOfWork() == 0) {
				return;
			}
		}

		// remove the staff member from the vehicle
		if (staffmember.equals(vehicleDetail.getDriver())) {
			vehicleDetail.setDriver(null);
		}
		// paramedic
		if (staffmember.equals(vehicleDetail.getFirstParamedic())) {
			vehicleDetail.setFirstParamedic(null);
		}
		// paramedic
		if (staffmember.equals(vehicleDetail.getSecondParamedic())) {
			vehicleDetail.setSecondParamedic(null);
		}

		// adjust the status
		vehicleDetail.setReadyForAction(false);
		vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);

		// persist the status of the vehicle
		if (!vehicleService.updateVehicle(vehicleDetail)) {
			throw new ServiceException("Failed to update the vehicle status during staff member detach.");
		}
		// brodcast the new status of the vehicle
		UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(vehicleDetail);
		session.brodcastMessage(updateMessage);

		// send a message to the originator
		SystemMessage message = new SystemMessage();
		message.setMessage("Der Mitarbeiter " + staffmember.getFirstName() + " " + staffmember.getLastName() + " hat sich abgemeldet.\n"
				+ "Er wurde vom Fahrzeug " + vehicleDetail.getVehicleName() + " abgezogen");

		// send the message back
		ExecMessage<SystemMessage> execMessage = new ExecMessage<SystemMessage>("info", message);
		session.write(execMessage);
	}

	@Override
	public RosterEntry[] toArray() {
		return null;
	}
}

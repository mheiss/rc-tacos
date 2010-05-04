package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class StaffMemberHandler implements Handler<StaffMember> {

	@Service(clazz = StaffMemberService.class)
	private StaffMemberService staffService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		List<StaffMember> staffList = message.getObjects();
		// loop and add the new staff members
		for (StaffMember member : staffList) {
			if (!staffService.addStaffMember(member)) {
				throw new ServiceException("Failed to add the staff member: " + member);
			}
		}
		// brodcast the added persons
		session.writeResponseBrodcast(message, staffList);
	}

	@Override
	public void get(MessageIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		// get the params out of the request
		Map<String, String> params = message.getParams();
		List<StaffMember> staffList = null;

		// query a single staff member by the id
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			// get the query filter
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			StaffMember member = staffService.getStaffMemberByID(id);
			if (member == null) {
				throw new ServiceException("Failed to get the staff member by id:" + id);
			}

			// check for locks
			if (lockableService.containsLock(member)) {
				Lockable lockable = lockableService.getLock(member);
				member.setLocked(lockable.isLocked());
				member.setLockedBy(lockable.getLockedBy());
			}

			// send the requested staff member back
			session.writeResponse(message, member);
			return;
		}

		// query the locked and unlocked staff members
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCKED_UNLOCKED_FILTER)) {
			// filter by location
			if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
				final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
				int locationId = Integer.parseInt(filter);
				staffList = staffService.getLockedAndUnlockedStaffMembersFromLocation(locationId);
				if (staffList == null) {
					throw new ServiceException("Failed to list all staff members by location " + locationId);
				}
				// check for locks
				syncronizeLocks(staffList);
				session.writeResponse(message, staffList);
				return;
			}
			// return all staff members
			staffList = staffService.getLockedAndUnlockedStaffMembers();
			if (staffList == null) {
				throw new ServiceException("Failed to list all staff members by primary location");
			}
			// check for locks
			syncronizeLocks(staffList);
			// send the requested members back
			session.writeResponse(message, staffList);
			return;
		}
		// query only the locked staff members
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCKED_FILTER)) {
			// query the locked by location
			if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
				final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
				int locationId = Integer.parseInt(filter);
				staffList = staffService.getLockedStaffMembersFromLocation(locationId);
				if (staffList == null) {
					throw new ServiceException("Failed to list the locked members by location " + locationId);
				}
				// check for locks
				syncronizeLocks(staffList);
				// send the requested members back
				session.writeResponse(message, staffList);
				return;
			}
			staffList = staffService.getLockedStaffMembers();
			if (staffList == null) {
				throw new ServiceException("Failed to list the locked staff members");
			}
			// check for locks
			syncronizeLocks(staffList);
			// send the requested members back
			session.writeResponse(message, staffList);
			return;
		}

		// query staff members by location
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
			final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
			int locationId = Integer.parseInt(filter);
			staffList = staffService.getStaffMembersFromLocation(locationId);
			if (staffList == null) {
				throw new ServiceException("Failed to list staff members by primary location");
			}
			// check for locks
			syncronizeLocks(staffList);
			// send the requested members back
			session.writeResponse(message, staffList);
			return;
		}

		// if no filter criteria matched send back all
		staffList = staffService.getAllStaffMembers();
		if (staffList == null) {
			throw new ServiceException("Failed to list all staff members");
		}
		// check for locks
		syncronizeLocks(staffList);
		session.writeResponse(message, staffList);
	}

	@Override
	public void remove(MessageIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		// throw an execption because the 'remove' command is not implemented
		String command = MessageType.REMOVE.toString();
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public void update(MessageIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		List<StaffMember> staffList = message.getObjects();
		// loop and update the staff members
		for (StaffMember member : staffList) {
			if (!staffService.updateStaffMember(member))
				throw new ServiceException("Failed to update the staff member: " + member);
			// update the lock
			lockableService.updateLock(member);
		}
		// brodcast the updated members
		session.writeResponseBrodcast(message, staffList);
	}

	@Override
	public void execute(MessageIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<StaffMember> updateMessage = new UpdateMessage<StaffMember>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			//brodcast the lock
			UpdateMessage<StaffMember> updateMessage = new UpdateMessage<StaffMember>(message.getObjects());
			session.brodcastMessage(updateMessage);
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	/**
	 * Helper method to syncronize the locks
	 */
	private void syncronizeLocks(List<StaffMember> staffList) {
		for (StaffMember staffMember : staffList) {
			if (!lockableService.containsLock(staffMember)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(staffMember);
			staffMember.setLocked(lockable.isLocked());
			staffMember.setLockedBy(lockable.getLockedBy());
		}
	}

	@Override
	public StaffMember[] toArray() {
		return null;
	}
}

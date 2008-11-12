package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class StaffMemberHandler implements Handler<StaffMember> {

	@Service(clazz = StaffMemberService.class)
	private StaffMemberService staffService;

	@Override
	public void add(ServerIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		if (!staffService.addStaffMember(model)) {
			throw new ServiceException("Failed to add the staff member: " + model);
		}
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		List<StaffMember> list = new ArrayList<StaffMember>();
		// if there is no filter -> request all
		if (params == null || params.isEmpty()) {
			list = staffService.getAllStaffMembers();
			if (list == null) {
				throw new ServiceException("Failed to list all staff members");
			}
			return list;
		}
		// query a single staff member by the id
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			// get the query filter
			final String filter = params.get(IFilterTypes.ID_FILTER);
			int id = Integer.parseInt(filter);
			StaffMember member = staffService.getStaffMemberByID(id);
			if (member == null) {
				throw new ServiceException("Failed to get the staff member by id:" + id);
			}
			list.add(member);
			return list;
		}
		// query the locked and unlocked staff members
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCKED_UNLOCKED_FILTER)) {
			// filter by location
			if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
				final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
				int locationId = Integer.parseInt(filter);
				list = staffService.getLockedAndUnlockedStaffMembersFromLocation(locationId);
				if (list == null) {
					throw new ServiceException("Failed to list all staff members by location " + locationId);
				}
				return list;
			}
			// return all staff members
			list = staffService.getLockedAndUnlockedStaffMembers();
			if (list == null) {
				throw new ServiceException("Failed to list all staff members by primary location");
			}
			return list;
		}
		// query only the locked staff members
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCKED_FILTER)) {
			// query the locked by location
			if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
				final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
				int locationId = Integer.parseInt(filter);
				list = staffService.getLockedStaffMembersFromLocation(locationId);
				if (list == null) {
					throw new ServiceException("Failed to list the locked members by location " + locationId);
				}
				return list;
			}
			list = staffService.getLockedStaffMembers();
			if (list == null) {
				throw new ServiceException("Failed to list the locked staff members");
			}
			return list;
		}
		// query staff members by location
		if (params.containsKey(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER)) {
			final String filter = params.get(IFilterTypes.STAFF_MEMBER_LOCATION_FILTER);
			int locationId = Integer.parseInt(filter);
			list = staffService.getStaffMembersFromLocation(locationId);
			if (list == null) {
				throw new ServiceException("Failed to list staff members by primary location");
			}
			return list;
		}
		return null;
	}

	@Override
	public void remove(ServerIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		throw new NoSuchCommandException("remove");
	}

	@Override
	public void update(ServerIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		if (!staffService.updateStaffMember(model))
			throw new ServiceException("Failed to update the staff member: " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<StaffMember> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}

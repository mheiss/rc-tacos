package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>StaffHandler</code> manages the locally chached {@link StaffMember}
 * instances.
 * 
 * @author Michael
 */
public class StaffHandler implements Handler<StaffMember> {

	private List<StaffMember> staffList = Collections.synchronizedList(new ArrayList<StaffMember>());
	private Logger log = LoggerFactory.getLogger(StaffHandler.class);

	@Override
	public void add(MessageIoSession session, Message<StaffMember> message) throws SQLException, ServiceException {
		synchronized (staffList) {
			staffList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<StaffMember> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<StaffMember> message) throws SQLException, ServiceException {
		synchronized (staffList) {
			// add or update the staff
			for (StaffMember member : message.getObjects()) {
				int index = staffList.indexOf(member);
				if (index == -1) {
					staffList.add(member);
				}
				else {
					staffList.set(index, member);
				}
			}
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<StaffMember> message) throws SQLException, ServiceException {
		synchronized (staffList) {
			staffList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<StaffMember> message) throws SQLException, ServiceException {
		synchronized (staffList) {
			for (StaffMember updatedMember : message.getObjects()) {
				if (!staffList.contains(updatedMember)) {
					continue;
				}
				int index = staffList.indexOf(updatedMember);
				staffList.set(index, updatedMember);
			}
		}
	}

	/**
	 * Returns the first <code>StaffMember</code> instance that exactly matches
	 * the string returned by {@link StaffMember#getUserName()}.
	 * 
	 * @param username
	 *            the name of the <code>StaffMember</code> instance to search
	 * @return the matched <code>Login</code> or null if nothing found
	 */
	public StaffMember getStaffMemberByUsername(String username) {
		synchronized (username) {
			for (StaffMember member : staffList) {
				if (member.getUserName().equals(username))
					return member;
			}
			// Nothing found
			return null;
		}
	}

	/**
	 * Returns a new array containing the managed <code>StaffMember</code>
	 * instances.
	 * 
	 * @return an array containing the <code>StaffMember</code> instances.
	 */
	@Override
	public StaffMember[] toArray() {
		return staffList.toArray(new StaffMember[staffList.size()]);
	}

	/**
	 * Returns a list of all staff members that are not assigned to a vehicle
	 * and checked in by location
	 * 
	 * @return list of staff members with no vehicle
	 */
	public List<StaffMember> getFreeStaffMembers(VehicleDetail vehicleDetail) {
		VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);
		RosterHandler rosterHandler = (RosterHandler) NetWrapper.getHandler(RosterEntry.class);
		List<StaffMember> filteredList = new ArrayList<StaffMember>();
		// list all cheked in memebrs
		List<RosterEntry> checkdIn = rosterHandler.getCheckedInRosterEntriesByLocation(vehicleDetail.getCurrentStation());
		for (RosterEntry entry : checkdIn) {
			// this staff member is signed at the current location, so go on
			StaffMember member = entry.getStaffMember();

			// check the vehicle of the staff
			VehicleDetail detail = vehicleHandler.getVehicleOfStaff(member.getStaffMemberId());

			// this member is not assigend
			if (detail == null) {
				filteredList.add(member);
				continue;
			}

			// this member is assigned so check if it is his vehicle
			if (detail.equals(vehicleDetail))
				filteredList.add(member);
		}
		return filteredList;
	}

}

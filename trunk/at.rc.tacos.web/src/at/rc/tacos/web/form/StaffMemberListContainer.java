package at.rc.tacos.web.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.StaffMember;

/**
 * Container for Staff Members View
 * @author Payer Martin
 * @version 1.0
 */
public class StaffMemberListContainer {
	private List<StaffMember> staffMemberList;
	private SortedMap<Location, List<StaffMember>> staffMemberContainerMap;
	public StaffMemberListContainer(List<AbstractMessage> staffMemberList) {
		this.staffMemberList = new ArrayList<StaffMember>();
		for (AbstractMessage abstractMessage : staffMemberList) {
			final StaffMember staffMember = (StaffMember)abstractMessage;
			this.staffMemberList.add(staffMember);
		}
	}
	public List<StaffMember> getStaffMemberList() {
		return staffMemberList;
	}
	public void setStaffMemberList(List<StaffMember> staffMemberList) {
		this.staffMemberList = staffMemberList;
	}
	public SortedMap<Location, List<StaffMember>> getStaffMemberContainerMap() {
		return staffMemberContainerMap;
	}
	public void setStaffMemberContainerMap(
			SortedMap<Location, List<StaffMember>> staffMemberContainerMap) {
		this.staffMemberContainerMap = staffMemberContainerMap;
	}
	
	public void groupStaffMembersBy(final Comparator locationComparator) {
		SortedMap<Location, List<StaffMember>> map = new TreeMap<Location, List<StaffMember>>(locationComparator);
		for (StaffMember staffMember : staffMemberList) {
			final Location location = staffMember.getPrimaryLocation();
			List<StaffMember> locationStaffMemberList = map.get(location);
			if (locationStaffMemberList == null) {
				locationStaffMemberList = new ArrayList<StaffMember>();
				map.put(location, locationStaffMemberList);
			}
			locationStaffMemberList.add(staffMember);
		}
		staffMemberContainerMap = map;
	}
	
	public void sortRosterEntries(final Comparator staffMemberComparator) {
		for (List<StaffMember> staffMemberList : staffMemberContainerMap.values()) {
			Collections.sort(staffMemberList, staffMemberComparator);		
		}
	}
}

package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;

/**
 * Container for Staff Members View
 * @author Payer Martin
 * @version 1.0
 */
public class StaffMemberListContainer {
	private List<Login> loginList;
	private SortedMap<Location, List<Login>> loginMap;
	public StaffMemberListContainer(List<Login> loginList) {
		this.loginList = loginList;
	}
	public List<Login> getLoginList() {
		return loginList;
	}
	public void setLoginList(List<Login> loginList) {
		this.loginList = loginList;
	}
	public SortedMap<Location, List<Login>> getLoginMap() {
		return loginMap;
	}
	public void setLoginMap(
			SortedMap<Location, List<Login>> loginMap) {
		this.loginMap = loginMap;
	}
	
	public void groupStaffMembersBy(final Comparator locationComparator) {
		SortedMap<Location, List<Login>> map = new TreeMap<Location, List<Login>>(locationComparator);
		for (Login login : loginList) {
			final Location location = login.getUserInformation().getPrimaryLocation();
			List<Login> locationLoginList = map.get(location);
			if (locationLoginList == null) {
				locationLoginList = new ArrayList<Login>();
				map.put(location, locationLoginList);
			}
			locationLoginList.add(login);
		}
		loginMap = map;
	}
	
	public void sortRosterEntries(final Comparator loginComparator) {
		for (List<Login> loginList : loginMap.values()) {
			Collections.sort(loginList, loginComparator);		
		}
	}
}

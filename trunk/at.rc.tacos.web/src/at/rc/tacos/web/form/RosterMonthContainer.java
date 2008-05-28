package at.rc.tacos.web.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.model.StaffMember;

/**
 * Roster Month Container
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthContainer {
	private final List<RosterEntryContainer> rosterEntryContainerList;
	private SortedMap<Day, Map<StaffMember, List<RosterEntryContainer>>> rosterEntryContainerMap;
	
	public enum Month {
		JANUARY(1),
		FEBRUARY(2),
		MARCH(3),
		APRIL(4),
		MAY(5),
		JUNE(6),
		JULY(7),
		AUGUST(8),
		SEPTEMBER(9),
		OCTOBER(10),
		NOVEMBER(11),
		DECEMBER(12);
		private final int property;

		public int getProperty() {
			return property;
		}
		
		private Month(int property) {
			this.property = property;
		}
		
	}
	
	public class Day {
		private int day;

		public Day(int day) {
			this.day = day;
		}
		
		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}
	}
	
	public void groupRosterEntriesBy(final Comparator dayComparator, final Comparator staffMemberComparator)
	{
		SortedMap<Day, Map<StaffMember, List<RosterEntryContainer>>> map = new TreeMap<Day, Map<StaffMember, List<RosterEntryContainer>>>(dayComparator);
		for (RosterEntryContainer rosterEntryContainer : rosterEntryContainerList) {
			final Date plannedStartOfWork = rosterEntryContainer.getPlannedStartOfWork();
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(plannedStartOfWork);
			final Day day = new Day(calendar.get(Calendar.DAY_OF_MONTH));
			
			final StaffMember staffMember = rosterEntryContainer.getRosterEntry().getStaffMember();
			Map<StaffMember, List<RosterEntryContainer>> monthRosterEntryContainerMap = map.get(day);
			if (monthRosterEntryContainerMap == null) {
				monthRosterEntryContainerMap = new TreeMap<StaffMember, List<RosterEntryContainer>>(staffMemberComparator);
				map.put(day, monthRosterEntryContainerMap);
			}
			List<RosterEntryContainer> staffMemberRosterEntryContainerList = monthRosterEntryContainerMap.get(staffMember);
			if (staffMemberRosterEntryContainerList == null) {
				staffMemberRosterEntryContainerList = new ArrayList<RosterEntryContainer>();
				monthRosterEntryContainerMap.put(staffMember, staffMemberRosterEntryContainerList);
			}
			staffMemberRosterEntryContainerList.add(rosterEntryContainer);
		}
		rosterEntryContainerMap = map;
	}
	
	public void sortRosterEntries(final Comparator rosterEntryContainerComparator) {
		for (Map<StaffMember, List<RosterEntryContainer>> monthRosterEntryContainerMap : rosterEntryContainerMap.values()) {
			for (List<RosterEntryContainer> staffMemberRosterEntryContainerList : monthRosterEntryContainerMap.values()) {
				Collections.sort(staffMemberRosterEntryContainerList, rosterEntryContainerComparator);
			}
		}
	}
	
	public RosterMonthContainer(List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
	}

	public SortedMap<Day, Map<StaffMember, List<RosterEntryContainer>>> getRosterEntryContainerMap() {
		return rosterEntryContainerMap;
	}

	public void setRosterEntryContainerMap(
			SortedMap<Day, Map<StaffMember, List<RosterEntryContainer>>> rosterEntryContainerMap) {
		this.rosterEntryContainerMap = rosterEntryContainerMap;
	}

	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}


}
package at.rc.tacos.web.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.model.Competence;
import at.rc.tacos.model.StaffMember;

/**
 * Roster Month Container
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthContainer {
	private List<RosterEntryContainer> rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
	private List<StaffMember> staffMemberList = new ArrayList<StaffMember>();
	private List<Competence> functionList = new ArrayList<Competence>();
	private SortedMap<Function, SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>> rosterEntryContainerMap;
	
	public enum Month {
		JANUARY(0),
		FEBRUARY(1),
		MARCH(2),
		APRIL(3),
		MAY(4),
		JUNE(5),
		JULY(6),
		AUGUST(7),
		SEPTEMBER(8),
		OCTOBER(9),
		NOVEMBER(10),
		DECEMBER(11);
		private final int property;

		public int getProperty() {
			return property;
		}
		
		private Month(int property) {
			this.property = property;
		}
		
	}
	
	public class Function {
		private String function;

		public Function(String function) {
			this.function = function;
		}
		
		public String getFunction() {
			return function;
		}

		public void setFunction(String function) {
			this.function = function;
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
	
	// Insert function comparator
	public void createTimetable(final Comparator functionComparator, final Comparator dayComparator, final Comparator staffMemberComparator, int month, int year)
	{
		SortedMap<Function, SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>> map = new TreeMap<Function, SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>>(functionComparator);
		for (RosterEntryContainer rosterEntryContainer : rosterEntryContainerList) {
			final Function function = new Function(rosterEntryContainer.getFunction());
			final Date plannedStartOfWork = rosterEntryContainer.getPlannedStartOfWork();
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(plannedStartOfWork);
			final Day day = new Day(calendar.get(Calendar.DAY_OF_MONTH));		
			final StaffMember staffMember = rosterEntryContainer.getRosterEntry().getStaffMember();
			SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> functionRosterEntryContainerMap = map.get(function);
			if (functionRosterEntryContainerMap == null) {
				functionRosterEntryContainerMap = new TreeMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>(dayComparator);
				map.put(function, functionRosterEntryContainerMap);
			}
			SortedMap<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap = functionRosterEntryContainerMap.get(day);
			if (dayRosterEntryContainerMap == null) {
				dayRosterEntryContainerMap = new TreeMap<StaffMember, List<RosterEntryContainer>>(staffMemberComparator);
				functionRosterEntryContainerMap.put(day, dayRosterEntryContainerMap);
			}
			List<RosterEntryContainer> staffMemberRosterEntryContainerList = dayRosterEntryContainerMap.get(staffMember);
			if (staffMemberRosterEntryContainerList == null) {
				staffMemberRosterEntryContainerList = new ArrayList<RosterEntryContainer>();
				dayRosterEntryContainerMap.put(staffMember, staffMemberRosterEntryContainerList);
			}
			staffMemberRosterEntryContainerList.add(rosterEntryContainer);
		}
		rosterEntryContainerMap = map;
		
		// Fill in days without roster entries with keys (depens on month and year)
		for (SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> functionRosterEntryContainerMap : rosterEntryContainerMap.values()) {
			final Set<Day> daySet = functionRosterEntryContainerMap.keySet();
			int daysOfMonth = 31;
			switch (month) {
				case 0:
					daysOfMonth = 31;
					break;
				case 1:
					if (year % 4 == 0) {
						daysOfMonth = 29;
					} else {
						daysOfMonth = 28;
					}
					break;
				case 2:
					daysOfMonth = 31;
					break;
				case 3:
					daysOfMonth = 30;
					break;
				case 4:
					daysOfMonth = 31;
					break;
				case 5:
					daysOfMonth = 30;
					break;
				case 6:
					daysOfMonth = 31;
					break;
				case 7:
					daysOfMonth = 31;
					break;
				case 8:
					daysOfMonth = 30;
					break;
				case 9:
					daysOfMonth = 31;
					break;
				case 10:
					daysOfMonth = 30;
					break;
				case 11:
					daysOfMonth = 31;
					break;
			}
			for (int i = 1; i <= daysOfMonth; i++) {
				boolean dayFound = false;
				for (Day day : daySet) {
					if (i == day.getDay()) {
						dayFound = true;
					}
				}
				if (!dayFound) {
					final Day d = new Day(i);
					final SortedMap<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap = new TreeMap<StaffMember, List<RosterEntryContainer>>(staffMemberComparator);
					functionRosterEntryContainerMap.put(d, dayRosterEntryContainerMap);
				}
			}
		}
		
		// Fill in staff members without roster entries
		for (final SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> functionRosterEntryContainerMap : rosterEntryContainerMap.values()) {
			for (final SortedMap<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap : functionRosterEntryContainerMap.values()) {
				final Set<StaffMember> staffMemberSet = dayRosterEntryContainerMap.keySet();
				for (final StaffMember sm1 : staffMemberList) {
					boolean staffMemberFound = false;
					for (final StaffMember sm2 : staffMemberSet) {
						if (sm1.getStaffMemberId() == sm2.getStaffMemberId()) {
							staffMemberFound = true;
						}
					}
					if (!staffMemberFound) {
						final List<RosterEntryContainer> staffMemberRosterEntryContainerList = new ArrayList<RosterEntryContainer>();
						dayRosterEntryContainerMap.put(sm1, staffMemberRosterEntryContainerList);
					}
				}
			}
		}
	}
	
	public void sortRosterEntries(final Comparator rosterEntryContainerComparator) {
		for (SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> functionRosterEntryContainerMap : rosterEntryContainerMap.values()) {
			for (Map<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap : functionRosterEntryContainerMap.values()) {						
				for (List<RosterEntryContainer> staffMemberRosterEntryContainerList : dayRosterEntryContainerMap.values()) {
					Collections.sort(staffMemberRosterEntryContainerList, rosterEntryContainerComparator);
				}
			}
		}
	}


	public SortedMap<Function, SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>> getRosterEntryContainerMap() {
		return rosterEntryContainerMap;
	}

	public void setRosterEntryContainerMap(
			SortedMap<Function, SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>> rosterEntryContainerMap) {
		this.rosterEntryContainerMap = rosterEntryContainerMap;
	}

	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}

	public List<StaffMember> getStaffMemberList() {
		return staffMemberList;
	}

	public List<Competence> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<Competence> functionList) {
		this.functionList = functionList;
	}

	public void setRosterEntryContainerList(
			List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
	}

	public void setStaffMemberList(List<StaffMember> staffMemberList) {
		this.staffMemberList = staffMemberList;
	}


}
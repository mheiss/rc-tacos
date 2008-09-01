package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;

/**
 * Container for Roster Month View
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthContainer {
	private List<RosterEntryContainer> rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
	private List<StaffMember>staffMemberList = new ArrayList<StaffMember>();
	private List<Day>dayList = new ArrayList<Day>();
	private List<Job>jobList = new ArrayList<Job>();
	private List<ServiceType>serviceTypeList = new ArrayList<ServiceType>();
	private SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> rosterEntryContainerMap;
	private SortedMap<StaffMember, RosterMonthStat> staffMemberRosterMonthStatMap;
	
	// Insert function comparator
	public void createTimetable(final Comparator dayComparator, final Comparator staffMemberComparator)
	{
		SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> map = new TreeMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>>(dayComparator);
		SortedMap<StaffMember, RosterMonthStat> map2 = new TreeMap<StaffMember, RosterMonthStat>(staffMemberComparator);
		for (RosterEntryContainer rosterEntryContainer : rosterEntryContainerList) {
			final Date plannedStartOfWork = rosterEntryContainer.getPlannedStartOfWork();
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(plannedStartOfWork);
			final Day day = new Day(calendar.get(Calendar.DAY_OF_MONTH));		
			final StaffMember staffMember = rosterEntryContainer.getRosterEntry().getStaffMember();
			SortedMap<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap = map.get(day);
			if (dayRosterEntryContainerMap == null) {
				dayRosterEntryContainerMap = new TreeMap<StaffMember, List<RosterEntryContainer>>(staffMemberComparator);
				map.put(day, dayRosterEntryContainerMap);
			}
			List<RosterEntryContainer> staffMemberRosterContainerList = dayRosterEntryContainerMap.get(staffMember);
			if (staffMemberRosterContainerList == null) {
				staffMemberRosterContainerList = new ArrayList<RosterEntryContainer>();
				dayRosterEntryContainerMap.put(staffMember, staffMemberRosterContainerList);
			}
			staffMemberRosterContainerList.add(rosterEntryContainer);
			
			RosterMonthStat rosterMonthStat = map2.get(staffMember);
			if (rosterMonthStat == null) {
				rosterMonthStat = new RosterMonthStat();
				map2.put(staffMember, rosterMonthStat);
			}
			rosterMonthStat.addPlannedDuration(rosterEntryContainer.getPlannedDuration());
		}
		rosterEntryContainerMap = map;
		staffMemberRosterMonthStatMap = map2;
	}
	
	public void sortRosterEntries(final Comparator rosterEntryContainerComparator) {
		for (Map<StaffMember, List<RosterEntryContainer>> dayRosterEntryContainerMap : rosterEntryContainerMap.values()) {						
			for (List<RosterEntryContainer> staffMemberRosterContainerList : dayRosterEntryContainerMap.values()) {
				Collections.sort(staffMemberRosterContainerList, rosterEntryContainerComparator);
			}
		}	
	}
	
	public void fillDayList(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		
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
			calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, i);
			final Day day = new Day(i);
			day.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
			day.setDateOfDay(calendar.getTime());
			dayList.add(day);
		}
	}
	
	public void sortStaffMemberList(final Comparator staffMemberComparator) {
		Collections.sort(staffMemberList, staffMemberComparator);
	}

	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}

	public SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> getRosterEntryContainerMap() {
		return rosterEntryContainerMap;
	}

	public void setRosterEntryContainerMap(
			SortedMap<Day, SortedMap<StaffMember, List<RosterEntryContainer>>> rosterEntryContainerMap) {
		this.rosterEntryContainerMap = rosterEntryContainerMap;
	}

	public SortedMap<StaffMember, RosterMonthStat> getStaffMemberRosterMonthStatMap() {
		return staffMemberRosterMonthStatMap;
	}

	public void setStaffMemberRosterMonthStatMap(
			SortedMap<StaffMember, RosterMonthStat> staffMemberRosterMonthStatMap) {
		this.staffMemberRosterMonthStatMap = staffMemberRosterMonthStatMap;
	}

	public List<StaffMember> getStaffMemberList() {
		return staffMemberList;
	}

	public void setRosterEntryContainerList(
			List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
	}

	public void setStaffMemberList(List<StaffMember> staffMemberList) {
		this.staffMemberList = staffMemberList;
	}

	public List<Day> getDayList() {
		return dayList;
	}

	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public List<ServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<ServiceType> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}
}
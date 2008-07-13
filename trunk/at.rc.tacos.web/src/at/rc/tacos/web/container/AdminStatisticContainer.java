package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.model.Job;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

/**
 * Adminstatistic Container
 * @author Payer Martin
 * @version 1.0
 */
public class AdminStatisticContainer {
	private List<RosterEntryContainer> rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
	private List<Job>jobList = new ArrayList<Job>();
	private List<ServiceType>serviceTypeList = new ArrayList<ServiceType>();
	private SortedMap<StaffMember, List<RosterEntryContainer>> rosterEntryContainerMap;
	private SortedMap<StaffMember, RosterMonthStat> staffMemberRosterMonthStatMap;
	
	public void createTimetable(final Comparator staffMemberComparator)
	{
		SortedMap<StaffMember, List<RosterEntryContainer>> map = new TreeMap<StaffMember, List<RosterEntryContainer>>(staffMemberComparator);
		SortedMap<StaffMember, RosterMonthStat> map2 = new TreeMap<StaffMember, RosterMonthStat>(staffMemberComparator);
		for (RosterEntryContainer rosterEntryContainer : rosterEntryContainerList) {	
			final StaffMember staffMember = rosterEntryContainer.getRosterEntry().getStaffMember();
			List<RosterEntryContainer> staffMemberRosterContainerList = map.get(staffMember);
			if (staffMemberRosterContainerList == null) {
				staffMemberRosterContainerList = new ArrayList<RosterEntryContainer>();
				map.put(staffMember, staffMemberRosterContainerList);
			}
			staffMemberRosterContainerList.add(rosterEntryContainer);
			
			RosterMonthStat rosterMonthStat = map2.get(staffMember);
			if (rosterMonthStat == null) {
				rosterMonthStat = new RosterMonthStat();
				map2.put(staffMember, rosterMonthStat);
			}
			rosterMonthStat.addPlannedDuration(rosterEntryContainer.getPlannedDuration());
			rosterMonthStat.addPlannedDurationWeighted(rosterEntryContainer.getPlannedDurationWeighted());
			rosterMonthStat.addRealDuration(rosterEntryContainer.getRealDuration());
			rosterMonthStat.addRealDurationWeighted(rosterEntryContainer.getRealDurationWeighted());
		}
		rosterEntryContainerMap = map;
		staffMemberRosterMonthStatMap = map2;
		
	}
	
	public void sortRosterEntries(final Comparator rosterEntryContainerComparator) {
		for (List<RosterEntryContainer> staffMemberRosterContainerList : rosterEntryContainerMap.values()) {
			Collections.sort(staffMemberRosterContainerList, rosterEntryContainerComparator);
		}
		
	}
	
	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}


	public void setRosterEntryContainerList(
			List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
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

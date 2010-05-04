package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.platform.model.Location;

/**
 * Container for Roster View
 * @author Payer Martin
 * @version 1.0
 */
public class RosterEntryContainerListContainer {
	private List<RosterEntryContainer> rosterEntryContainerList;
	private SortedMap<Location, List<RosterEntryContainer>> rosterEntryContainerMap;
	
	public RosterEntryContainerListContainer(List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
	}
	
	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}
	
	public void setRosterEntryContainerList(
			List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
	}
	
	public SortedMap<Location, List<RosterEntryContainer>> getRosterEntryContainerMap() {
		return rosterEntryContainerMap;
	}
	
	public void setRosterEntryContainerMap(
			SortedMap<Location, List<RosterEntryContainer>> rosterEntryContainerMap) {
		this.rosterEntryContainerMap = rosterEntryContainerMap;
	}
	
	public void groupRosterEntriesBy(final Comparator<Location> locationComparator)
	{
		SortedMap<Location, List<RosterEntryContainer>> map = new TreeMap<Location, List<RosterEntryContainer>>(locationComparator);
		for (RosterEntryContainer rosterEntryContainer : rosterEntryContainerList) {
			final Location location = rosterEntryContainer.getRosterEntry().getStation();
			List<RosterEntryContainer> locationRosterEntryContainerList = map.get(location);
			if (locationRosterEntryContainerList == null) {
				locationRosterEntryContainerList = new ArrayList<RosterEntryContainer>();
				map.put(location, locationRosterEntryContainerList);
			}
			locationRosterEntryContainerList.add(rosterEntryContainer);
		}
		rosterEntryContainerMap = map;
	}
	
	public void sortRosterEntries(final Comparator rosterEntryContainerComparator) {
		for (List<RosterEntryContainer> vehicleContainerList : rosterEntryContainerMap.values()) {
			Collections.sort(vehicleContainerList, rosterEntryContainerComparator);		
		}
	}

}

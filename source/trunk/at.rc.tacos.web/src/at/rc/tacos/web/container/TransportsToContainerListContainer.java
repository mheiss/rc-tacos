package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.platform.model.Location;

/**
 * 
 * @author Birgit
 * @version 1.0
 */
public class TransportsToContainerListContainer {
	private List<TransportsToContainer> transportsToContainerList;
	private SortedMap<Location, List<TransportsToContainer>> transportsToContainerMap;
	
	public TransportsToContainerListContainer(List<TransportsToContainer> transportContainerList) {
		this.transportsToContainerList = transportContainerList;
	}

	public List<TransportsToContainer> getTransportContainerList() {
		return transportsToContainerList;
	}

	public void setTranspportList(List<TransportsToContainer> transportsToContainerList) {
		this.transportsToContainerList = transportsToContainerList;
	}

	public SortedMap<Location, List<TransportsToContainer>> getTransportsToContainerMap() 
	{
		return transportsToContainerMap;
	}

	public void setTransportMap(SortedMap<Location, List<TransportsToContainer>> transportContainerMap) {
		this.transportsToContainerMap = transportContainerMap;
	}
	
	public void groupTransportsToBy(final Comparator<Location> locationComparator)
	{
		SortedMap<Location, List<TransportsToContainer>> map = new TreeMap<Location, List<TransportsToContainer>>(locationComparator);
		for (TransportsToContainer transportsToContainer : transportsToContainerList) {
			final Location location =transportsToContainer.getPlannedLocation();
			List<TransportsToContainer> locationTransportsToContainerList = map.get(location);
			if (locationTransportsToContainerList == null) {
				locationTransportsToContainerList = new ArrayList<TransportsToContainer>();
				map.put(location, locationTransportsToContainerList);
			}
			locationTransportsToContainerList.add(transportsToContainer);
		}
		transportsToContainerMap = map;
	}
	
	public void sortTransportsTo(final Comparator transportsToContainerComparator) {
		for (List<TransportsToContainer> transportsToContainerList : transportsToContainerMap.values()) {
			Collections.sort(transportsToContainerList, transportsToContainerComparator);		
		}
	}

}

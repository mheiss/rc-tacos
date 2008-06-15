package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.model.Location;


/**
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class VehicleContainerListContainer {
	private List<VehicleContainer> vehicleContainerList;
	private SortedMap<Location, List<VehicleContainer>> vehicleContainerMap;
	
	public VehicleContainerListContainer(List<VehicleContainer> vehicleDetailContainerList) {
		this.vehicleContainerList = vehicleDetailContainerList;
	}

	public List<VehicleContainer> getVehicleDetailContainerList() {
		return vehicleContainerList;
	}

	public void setVehicleDetailList(List<VehicleContainer> vehicleContainerList) {
		this.vehicleContainerList = vehicleContainerList;
	}

	public SortedMap<Location, List<VehicleContainer>> getVehicleContainerMap() {
		return vehicleContainerMap;
	}

	public void setVehicleDetailMap(SortedMap<Location, List<VehicleContainer>> vehicleDetailContainerMap) {
		this.vehicleContainerMap = vehicleDetailContainerMap;
	}
	
	public void groupVehiclesBy(final Comparator<Location> locationComparator)
	{
		SortedMap<Location, List<VehicleContainer>> map = new TreeMap<Location, List<VehicleContainer>>(locationComparator);
		for (VehicleContainer vehicleContainer : vehicleContainerList) {
			final Location location = vehicleContainer.getCurrentStation();
			List<VehicleContainer> locationVehicleContainerList = map.get(location);
			if (locationVehicleContainerList == null) {
				locationVehicleContainerList = new ArrayList<VehicleContainer>();
				map.put(location, locationVehicleContainerList);
			}
			locationVehicleContainerList.add(vehicleContainer);
		}
		vehicleContainerMap = map;
	}
	
	public void sortVehicles(final Comparator vehicleContainerComparator) {
		for (List<VehicleContainer> vehicleContainerList : vehicleContainerMap.values()) {
			Collections.sort(vehicleContainerList, vehicleContainerComparator);		
		}
	}

}

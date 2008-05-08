package at.rc.tacos.web.form;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import at.rc.tacos.model.Location;
import at.rc.tacos.model.VehicleDetail;

/**
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class VehicleListContainer {
	private List<VehicleDetail> vehicleDetailList;
	private SortedMap<Location, List<VehicleDetail>> vehicleDetailMap;
	
	public VehicleListContainer(List<VehicleDetail> vehicleDetailList) {
		this.vehicleDetailList = vehicleDetailList;
	}

	public List<VehicleDetail> getVehicleDetailList() {
		return vehicleDetailList;
	}

	public void setVehicleDetailList(List<VehicleDetail> vehicleDetailList) {
		this.vehicleDetailList = vehicleDetailList;
	}

	public SortedMap<Location, List<VehicleDetail>> getVehicleDetailMap() {
		return vehicleDetailMap;
	}

	public void setVehicleDetailMap(SortedMap<Location, List<VehicleDetail>> vehicleDetailMap) {
		this.vehicleDetailMap = vehicleDetailMap;
	}
	
	public void groupMarksBy(final Comparator<Location> locationComparator)
	{
		SortedMap<Location, List<VehicleDetail>> map = new TreeMap<Location, List<VehicleDetail>>(locationComparator);
		for (VehicleDetail vehicleDetail : vehicleDetailList) {
			final Location location = vehicleDetail.getCurrentStation();
			List<VehicleDetail> locationVehicleDetailList = map.get(location);
			if (locationVehicleDetailList == null) {
				locationVehicleDetailList = new ArrayList<VehicleDetail>();
				map.put(location, locationVehicleDetailList);
			}
		}
		vehicleDetailMap = map;
	}

}

package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.VehicleDetail;

public interface VehicleDAO 
{
    public int addVehicle(VehicleDetail vehicle);
    public boolean updateVehicle(VehicleDetail vehicle);
    public boolean removeVehicle(VehicleDetail vehicle);
	
    public VehicleDetail getVehicleById(String vehicleId);
    public List<VehicleDetail> listVehicles();
}

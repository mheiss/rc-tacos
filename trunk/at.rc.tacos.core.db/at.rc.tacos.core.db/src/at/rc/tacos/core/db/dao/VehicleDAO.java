package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.VehicleDetail;

public interface VehicleDAO 
{
    public int addVehicle(VehicleDetail vehicle);
    public void updateVehicle(VehicleDetail vehicle);
    public void removeVehicle(VehicleDetail vehicle);
	
    public VehicleDetail getVehicleById(int vehicleId);
    public List<VehicleDetail> listVehicles();
}

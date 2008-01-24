package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.VehicleDetail;

public interface VehicleDAO 
{
	/**
	 * Adds a new vehicle to the list of vehicles
	 * @param vehicle the vehicle to add
	 * @return the id of the added vehicle
	 */
    public int addVehicle(VehicleDetail vehicle);
    
    /**
     * Updates the vehicle in the database
     * @param vehicle the vehicl to update
     * @return true if the update was successfull.
     */
    public boolean updateVehicle(VehicleDetail vehicle);
    
    /**
     * Remove the vehicle from the database
     * @param id the id of the vehicle to remve
     * @return true if the remove was successfull
     */
    public boolean removeVehicle(VehicleDetail vehicle);
	
    /**
     * Returns the vehicle accociated with the given name.
     * The method will return null if no vehicle was found.
     * @param vehicleName the name of the vehicle to get
     * @return the vehicle or null if nothing was found
     */
    public VehicleDetail getVehicleByName(String vehicleName);
    
    /**
     * Returns a list of all vehicles
     * @return the list of all vehicles
     */
    public List<VehicleDetail> listVehicles();
}

package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon vehicle detail changes
 * @author Michael
 */
public class VehicleDetailListener extends ServerListenerAdapter
{
    private VehicleDAO vehicleDao = DaoService.getInstance().getFactory().createVehicleDetailDAO();
    
    /**
     * Vehicle added
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        VehicleDetail vehicle = (VehicleDetail)addObject;
        int id = vehicleDao.addVehicle(vehicle);
        vehicle.setVehicleId(id);
        return vehicle;
    }

    /**
     * Vehicle listing
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest()
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(vehicleDao.listVehicles());
        return list;
    }

    /**
     * Remove a vehicle
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        VehicleDetail vehicle = (VehicleDetail)removeObject;
        vehicleDao.removeVehicle(vehicle);
        return vehicle;
    }

    /**
     * Updates a vehicle
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        VehicleDetail vehicle = (VehicleDetail)updateObject;
        vehicleDao.updateVehicle(vehicle);
        return vehicle;
    }
}

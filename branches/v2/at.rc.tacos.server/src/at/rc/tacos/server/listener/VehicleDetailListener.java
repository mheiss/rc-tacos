package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class will be notified uppon vehicle detail changes
 * @author Michael
 */
public class VehicleDetailListener extends ServerListenerAdapter
{
    private VehicleDAO vehicleDao = DaoFactory.SQL.createVehicleDetailDAO();
  //the logger
	private static Logger logger = Logger.getLogger(VehicleDetailListener.class);
        
    /**
     * Vehicle added
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException,SQLException
    {
        VehicleDetail vehicle = (VehicleDetail)addObject;
        if(!vehicleDao.addVehicle(vehicle))
        	throw new DAOException("VehicleDetailListener","Failed to add the vehicle:"+vehicle);
        logger.info("added by:" +username +";" +vehicle);
        return vehicle;
    }

    /**
     * Vehicle listing
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        List<VehicleDetail> vehicleList = vehicleDao.listVehicles();
        if(vehicleList == null)
        	throw new DAOException("VehicleDetailListener","Failed to list the vehicles");
        list.addAll(vehicleList);
        return list;
    }

    /**
     * Remove a vehicle
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
    {
        VehicleDetail vehicle = (VehicleDetail)removeObject;
        if(!vehicleDao.removeVehicle(vehicle))
        	throw new DAOException("VehicleDetailListener","Failed to remove the vehicle "+vehicle);
        return vehicle;
    }

    /**
     * Updates a vehicle
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username) throws DAOException,SQLException
    {
        VehicleDetail vehicle = (VehicleDetail)updateObject;
        if(!vehicleDao.updateVehicle(vehicle))
        	throw new DAOException("VehicleDetailListener","Failed to update the vehicle "+vehicle);
        logger.info("updated by: " +username +";" +vehicle);
        return vehicle;
    }
}
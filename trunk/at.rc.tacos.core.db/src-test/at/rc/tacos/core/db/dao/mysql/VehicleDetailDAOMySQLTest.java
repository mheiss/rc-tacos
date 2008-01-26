package at.rc.tacos.core.db.dao.mysql;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.VehicleDetail;

/**
 * This is a test class to test the functionality of the vehicle detail 
 * @author Michael
 */
public class VehicleDetailDAOMySQLTest
{
    //the dao class
    private final VehicleDAO vehicleDAO = DaoFactory.MYSQL.createVehicleDetailDAO();
    
    @Test
    public void testListVehicles()
    {
        //create the array list
        List<VehicleDetail> resultList = new ArrayList<VehicleDetail>(); 
        
        //request the listing
        resultList = vehicleDAO.listVehicles();
        
        Assert.assertEquals(3, resultList.size());
    }
}

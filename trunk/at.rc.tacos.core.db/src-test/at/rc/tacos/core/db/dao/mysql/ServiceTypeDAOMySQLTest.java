package at.rc.tacos.core.db.dao.mysql;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDAOMySQLTest extends DBTestBase
{
    //the dao class
    private ServiceTypeDAO serviceTypeDao = DaoFactory.MYSQL.createServiceTypeDAO();
    
    //test data
    ServiceType serviceType1 = new ServiceType("serviceType1");
    ServiceType serviceType2 = new ServiceType("serviceType2");
    
    @Before
    public void setUp() 
    {
        //insert test data
        int id1 = serviceTypeDao.addServiceType(serviceType1);
        int id2 = serviceTypeDao.addServiceType(serviceType2);
        serviceType1.setId(id1);
        serviceType2.setId(id2);
    }
    
    @After
    public void tearDown()
    {
        deleteTable(ServiceTypeDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindById()
    {
        ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());   
        Assert.assertEquals("serviceType1", serviceType.getServiceName());
    }
    
    @Test
    public void testRemoveServiceType()
    {
        serviceTypeDao.removeServiceType(serviceType1.getId());
        //list all
        List<ServiceType> list = serviceTypeDao.listServiceTypes();
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testListServiceType()
    {
        List<ServiceType> list = serviceTypeDao.listServiceTypes();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateServiceType()
    {
        //create two indivdual block
        {
            ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());   
            serviceType.setServiceName("newServiceTypeName");
            serviceTypeDao.updateServiceType(serviceType);
        }
        {
            ServiceType serviceType = serviceTypeDao.getServiceTypeId(serviceType1.getId());
            Assert.assertEquals("newServiceTypeName", serviceType.getServiceName());
        }
    }
}

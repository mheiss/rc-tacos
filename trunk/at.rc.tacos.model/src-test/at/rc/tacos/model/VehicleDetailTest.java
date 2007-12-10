package at.rc.tacos.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for vehicles
 * @author Michael
 */
public class VehicleDetailTest
{
    @Test
    public void testVehicleProperties()  
    {
        VehicleDetail detail = new VehicleDetail("BM1","RTW","Bruck");
        Assert.assertEquals("BM1", detail.getVehicleName());
        Assert.assertEquals("RTW", detail.getVehicleType());
        Assert.assertEquals("Bruck", detail.getBasicStation());
    }
    
    @Test
    public void testVehicleEqualsAndHashCode()
    {
        VehicleDetail v1 = new VehicleDetail("BM1","RTW","Bruck");
        v1.setVehicleId(1);
        VehicleDetail v2 = new VehicleDetail("BM2","KTW","Kapfenberg");
        v2.setVehicleId(1);
        Assert.assertTrue(v1.equals(v2));
        Assert.assertTrue(v2.equals(v1));
        Assert.assertEquals(v1.hashCode(), v2.hashCode());
    }
    
    @Test
    public void testDriverProperties()
    {
        StaffMember sm = new StaffMember("fn","ln","username");
        VehicleDetail v1 = new VehicleDetail("BM1","RTW","Bruck");
        v1.setDriverName(sm);
        Assert.assertEquals("fn", v1.getDriverName().getFirstName());
        Assert.assertEquals("ln", v1.getDriverName().getLastname());
        Assert.assertEquals("username", v1.getDriverName().getUserName());
    }
    
    @Test
    public void testMedic1Properties()
    {
        StaffMember sm = new StaffMember("fn","ln","username");
        VehicleDetail v1 = new VehicleDetail("BM1","RTW","Bruck");
        v1.setParamedicIName(sm);
        Assert.assertEquals("fn", v1.getParamedicIName().getFirstName());
        Assert.assertEquals("ln", v1.getParamedicIName().getLastname());
        Assert.assertEquals("username", v1.getParamedicIName().getUserName());
    }
    
    @Test
    public void testMedic2Properties()
    {
        StaffMember sm = new StaffMember("fn","ln","username");
        VehicleDetail v1 = new VehicleDetail("BM1","RTW","Bruck");
        v1.setParamedicIIName(sm);
        Assert.assertEquals("fn", v1.getParamedicIIName().getFirstName());
        Assert.assertEquals("ln", v1.getParamedicIIName().getLastname());
        Assert.assertEquals("username", v1.getParamedicIIName().getUserName());
    }
    
    @Test
    public void testDataSource()
    {
        VehicleDetail v1 = new TestDataSource().vehicleList.get(0);
        Assert.assertEquals("Staff1", v1.getDriverName().getFirstName());
        Assert.assertEquals("Staff1", v1.getDriverName().getLastname());
        Assert.assertEquals("nick.staff1", v1.getDriverName().getUserName());
    }
}

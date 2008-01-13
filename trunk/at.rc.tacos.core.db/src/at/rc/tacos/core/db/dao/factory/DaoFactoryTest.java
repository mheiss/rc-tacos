package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.core.db.dao.ItemDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.memory.ItemDAOMemory;
import at.rc.tacos.core.db.dao.memory.MobilePhoneDAOMemory;
import at.rc.tacos.core.db.dao.memory.NotifierDAOMemory;
import at.rc.tacos.core.db.dao.memory.RosterEntryDAOMemory;
import at.rc.tacos.core.db.dao.memory.StaffMemberDAOMemory;
import at.rc.tacos.core.db.dao.memory.TransportDAOMemory;
import at.rc.tacos.core.db.dao.memory.UserDAOMemory;
import at.rc.tacos.core.db.dao.memory.VehicleDetailDAOMemory;

/**
 * Creates the data access objects for testing.<br>
 * @author Michael
 */
public class DaoFactoryTest implements DaoFactory
{
    @Override
    public ItemDAO createItemDAO()
    {
        return ItemDAOMemory.getInstance();
    }

    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
        return  MobilePhoneDAOMemory.getInstance();
    }

    @Override
    public CallerDAO createNotifierDAO()
    {
        return NotifierDAOMemory.getInstance();
    }

    @Override
    public RosterDAO createRosterEntryDAO()
    {
        return RosterEntryDAOMemory.getInstance();
    }

    @Override
    public EmployeeDAO createStaffMemberDAO()
    {
        return StaffMemberDAOMemory.getInstance();
    }

    @Override
    public TransportDAO createTransportDAO()
    {
        return TransportDAOMemory.getInstance();
    }

    @Override
    public UserLoginDAO createUserDAO()
    {
        return UserDAOMemory.getInstance();
    }

    @Override
    public VehicleDAO createVehicleDetailDAO()
    {
        return VehicleDetailDAOMemory.getInstance();
    }
}

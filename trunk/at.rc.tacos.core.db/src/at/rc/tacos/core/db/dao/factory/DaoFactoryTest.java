package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.core.db.dao.ItemDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.PatientDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.test.ItemDAOTest;
import at.rc.tacos.core.db.dao.test.MobilePhoneDAOTest;
import at.rc.tacos.core.db.dao.test.NotifierDAOTest;
import at.rc.tacos.core.db.dao.test.PatientDAOTest;
import at.rc.tacos.core.db.dao.test.RosterEntryDAOTest;
import at.rc.tacos.core.db.dao.test.StaffMemberDAOTest;
import at.rc.tacos.core.db.dao.test.TransportDAOTest;
import at.rc.tacos.core.db.dao.test.UserDAOTest;
import at.rc.tacos.core.db.dao.test.VehicleDetailDAOTest;

/**
 * Creates the data access objects for testing.<br>
 * @author Michael
 */
public class DaoFactoryTest implements DaoFactory
{
    @Override
    public ItemDAO createItemDAO()
    {
        return ItemDAOTest.getInstance();
    }

    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
        return  MobilePhoneDAOTest.getInstance();
    }

    @Override
    public CallerDAO createNotifierDAO()
    {
        return NotifierDAOTest.getInstance();
    }

    @Override
    public PatientDAO createPatientDAO()
    {
        return PatientDAOTest.getInstance();
    }

    @Override
    public RosterDAO createRosterEntryDAO()
    {
        return RosterEntryDAOTest.getInstance();
    }

    @Override
    public EmployeeDAO createStaffMemberDAO()
    {
        return StaffMemberDAOTest.getInstance();
    }

    @Override
    public TransportDAO createTransportDAO()
    {
        return TransportDAOTest.getInstance();
    }

    @Override
    public UserLoginDAO createUserDAO()
    {
        return UserDAOTest.getInstance();
    }

    @Override
    public VehicleDAO createVehicleDetailDAO()
    {
        return VehicleDetailDAOTest.getInstance();
    }
}

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

/**
 * We can make the Data Access Object creation more flexible by adopting the 
 * Abstract Factory pattern.
 */
public interface DaoFactory
{
    //the provided factories
    final DaoFactory TEST = new DaoFactoryTest();
    final DaoFactory MYSQL = new DaoFactoryTest();

    //the provided factory methods
    public ItemDAO createItemDAO();
    public MobilePhoneDAO createMobilePhoneDAO();
    public CallerDAO createNotifierDAO();
    public PatientDAO createPatientDAO();
    public RosterDAO createRosterEntryDAO();
    public EmployeeDAO createStaffMemberDAO();
    public TransportDAO createTransportDAO();
    public UserLoginDAO createUserDAO();
    public VehicleDAO createVehicleDetailDAO();
}

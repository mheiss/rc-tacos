package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
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
    final DaoFactory TEST = new DaoFactoryMemory();
    final DaoFactoryMySql MYSQL = new DaoFactoryMySql();

    //the provided factory methods
    public MobilePhoneDAO createMobilePhoneDAO();
    public CallerDAO createNotifierDAO();
    public RosterDAO createRosterEntryDAO();
    public StaffMemberDAO createStaffMemberDAO();
    public TransportDAO createTransportDAO();
    public UserLoginDAO createUserDAO();
    public VehicleDAO createVehicleDetailDAO();
    public DialysisPatientDAO createDialysisPatientDAO();
    public DayInfoDAO createDayInfoDAO();
}

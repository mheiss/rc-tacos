package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.mysql.CallerDAOMySQL;
import at.rc.tacos.core.db.dao.mysql.RosterDAOMySQL;
import at.rc.tacos.core.db.dao.mysql.StaffMemberDAOMySQL;
import at.rc.tacos.core.db.dao.mysql.UserLoginDAOMySQL;

/**
 * Provides the specialized dao factory methods to access a sql source
 * @author Michael
 */
public class DaoFactoryMySql implements DaoFactory
{
    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
        return null; 
    }

    @Override
    public CallerDAO createNotifierDAO()
    {
        return new CallerDAOMySQL();
    }

    @Override
    public RosterDAO createRosterEntryDAO()
    {
        return new RosterDAOMySQL();
    }

    @Override
    public StaffMemberDAO createStaffMemberDAO()
    {
       return new StaffMemberDAOMySQL();
    }

    @Override
    public TransportDAO createTransportDAO()
    {
        return null;
    }

    @Override
    public UserLoginDAO createUserDAO()
    {
        return new UserLoginDAOMySQL();
    }

    @Override
    public VehicleDAO createVehicleDetailDAO()
    {
        return null;
    }

	@Override
	public DialysisPatientDAO createDialysisPatientDAO() 
	{
		return null;
	}
}

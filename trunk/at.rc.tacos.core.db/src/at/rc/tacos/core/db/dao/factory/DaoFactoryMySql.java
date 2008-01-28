package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.mysql.*;

/**
 * Provides the specialized dao factory methods to access a sql source
 * @author Michael
 */
public class DaoFactoryMySql implements DaoFactory
{
    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
    	return new MobilePhoneDAOMySQL();
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
        return new TransportDAOMySQL();
    }

    @Override
    public UserLoginDAO createUserDAO()
    {
        return new UserLoginDAOMySQL();
    }

    @Override
    public VehicleDAO createVehicleDetailDAO()
    {
    	return new VehicleDetailDAOMySQL();
    }

	@Override
	public DialysisPatientDAO createDialysisPatientDAO() 
	{
		return new DialysisPatientDAOMySQL();
	}

	@Override
	public DayInfoDAO createDayInfoDAO() 
	{
		return new DayInfoDAOMySQL();
	}

	@Override
	public LocationDAO createLocationDAO() 
	{
		return new LocationDAOMySQL();
	}

	@Override
	public CompetenceDAO createCompetenceDAO() 
	{
		return new CompetenceDAOMySQL();
	}

	@Override
	public JobDAO createJobDAO() 
	{
		return new JobDAOMySQL();
	}

	@Override
	public ServiceTypeDAO createServiceTypeDAO() 
	{
		return new ServiceTypeDAOMySQL();
	}
}

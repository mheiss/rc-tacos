package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.memory.CompetenceDAOMemory;
import at.rc.tacos.core.db.dao.memory.DayInfoDAOMemory;
import at.rc.tacos.core.db.dao.memory.DialysisDAOMemory;
import at.rc.tacos.core.db.dao.memory.JobDAOMemory;
import at.rc.tacos.core.db.dao.memory.LocationDAOMemory;
import at.rc.tacos.core.db.dao.memory.MobilePhoneDAOMemory;
import at.rc.tacos.core.db.dao.memory.NotifierDAOMemory;
import at.rc.tacos.core.db.dao.memory.RosterEntryDAOMemory;
import at.rc.tacos.core.db.dao.memory.ServiceTypeDAOMemory;
import at.rc.tacos.core.db.dao.memory.StaffMemberDAOMemory;
import at.rc.tacos.core.db.dao.memory.TransportDAOMemory;
import at.rc.tacos.core.db.dao.memory.UserDAOMemory;
import at.rc.tacos.core.db.dao.memory.VehicleDetailDAOMemory;

/**
 * Creates the data access objects for testing.<br>
 * @author Michael
 */
public class DaoFactoryMemory implements DaoFactory
{
    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
        return MobilePhoneDAOMemory.getInstance();
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
    public StaffMemberDAO createStaffMemberDAO()
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
    
    @Override 
    public DialysisPatientDAO createDialysisPatientDAO()
    {
    	return DialysisDAOMemory.getInstance();
    }

	@Override
	public DayInfoDAO createDayInfoDAO() 
	{
		return DayInfoDAOMemory.getInstance();
	}

	@Override
	public LocationDAO createLocationDAO() 
	{
		return LocationDAOMemory.getInstance();
	}

	@Override
	public CompetenceDAO createCompetenceDAO() 
	{
		return CompetenceDAOMemory.getInstance();
	}

	@Override
	public JobDAO createJobDAO() 
	{
		return JobDAOMemory.getInstance();
	}

	@Override
	public ServiceTypeDAO createServiceTypeDAO() 
	{
		return ServiceTypeDAOMemory.getInstance();
	}
}

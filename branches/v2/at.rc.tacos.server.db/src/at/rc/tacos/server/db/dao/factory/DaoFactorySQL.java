package at.rc.tacos.server.db.dao.factory;

//import at.rc.tacos.core.db.dao.mysql.*;
import at.rc.tacos.server.db.dao.AddressDAO;
import at.rc.tacos.server.db.dao.CallerDAO;
import at.rc.tacos.server.db.dao.CompetenceDAO;
import at.rc.tacos.server.db.dao.DayInfoDAO;
import at.rc.tacos.server.db.dao.DialysisPatientDAO;
import at.rc.tacos.server.db.dao.DiseaseDAO;
import at.rc.tacos.server.db.dao.JobDAO;
import at.rc.tacos.server.db.dao.LinkDAO;
import at.rc.tacos.server.db.dao.LocationDAO;
import at.rc.tacos.server.db.dao.MobilePhoneDAO;
import at.rc.tacos.server.db.dao.PeriodsDAO;
import at.rc.tacos.server.db.dao.RosterDAO;
import at.rc.tacos.server.db.dao.ServiceTypeDAO;
import at.rc.tacos.server.db.dao.SickPersonDAO;
import at.rc.tacos.server.db.dao.StaffMemberDAO;
import at.rc.tacos.server.db.dao.TransportDAO;
import at.rc.tacos.server.db.dao.UserLoginDAO;
import at.rc.tacos.server.db.dao.VehicleDAO;
import at.rc.tacos.server.db.dao.sqlserver.*;

/**
 * Provides the specialized dao factory methods to access a sql source
 * @author Michael
 */
public class DaoFactorySQL implements DaoFactory
{
    @Override
    public MobilePhoneDAO createMobilePhoneDAO()
    {
    	return new MobilePhoneDAOSQL();
    }

    @Override
    public CallerDAO createNotifierDAO()
    {
        return new CallerDAOSQL();
    }

    @Override
    public RosterDAO createRosterEntryDAO()
    {
        return new RosterDAOSQL();
    }

    @Override
    public StaffMemberDAO createStaffMemberDAO()
    {
       return new StaffMemberDAOSQL();
    }

    @Override
    public TransportDAO createTransportDAO()
    {
        return new TransportDAOSQL();
    }

    @Override
    public UserLoginDAO createUserDAO()
    {
        return new UserLoginDAOSQL();
    }

    @Override
    public VehicleDAO createVehicleDetailDAO()
    {
    	return new VehicleDetailDAOSQL();
    }

	@Override
	public DialysisPatientDAO createDialysisPatientDAO() 
	{
		return new DialysisPatientDAOSQL();
	}

	@Override
	public DayInfoDAO createDayInfoDAO() 
	{
		return new DayInfoDAOSQL();
	}

	@Override
	public LocationDAO createLocationDAO() 
	{
		return new LocationDAOSQL();
	}

	@Override
	public CompetenceDAO createCompetenceDAO() 
	{
		return new CompetenceDAOSQL();
	}

	@Override
	public JobDAO createJobDAO() 
	{
		return new JobDAOSQL();
	}

	@Override
	public ServiceTypeDAO createServiceTypeDAO() 
	{
		return new ServiceTypeDAOSQL();
	}

	@Override
	public DiseaseDAO createDiseaseDAO() 
	{
		return new DiseaseDAOSQL();
	}
	
	@Override
	public SickPersonDAO createSickPersonDAO()
	{
		return new SickPersonDAOSQL();
	}
	
	@Override
	public AddressDAO createAddressDAO()
	{
		return new AddressDAOSQL();
	}
	
	@Override
	public PeriodsDAO createPeriodsDAO()
	{
		return new PeriodsDAOSQL();
	}

	@Override
	public LinkDAO createLinkDAO() {
		return new LinkDAOSQL();
	}
}

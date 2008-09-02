package at.rc.tacos.server.dbal.factory;

//import at.rc.tacos.core.db.dao.mysql.*;
import at.rc.tacos.server.dbal.dao.AddressDAO;
import at.rc.tacos.server.dbal.dao.CallerDAO;
import at.rc.tacos.server.dbal.dao.CompetenceDAO;
import at.rc.tacos.server.dbal.dao.DayInfoDAO;
import at.rc.tacos.server.dbal.dao.DialysisPatientDAO;
import at.rc.tacos.server.dbal.dao.DiseaseDAO;
import at.rc.tacos.server.dbal.dao.JobDAO;
import at.rc.tacos.server.dbal.dao.LinkDAO;
import at.rc.tacos.server.dbal.dao.LocationDAO;
import at.rc.tacos.server.dbal.dao.MobilePhoneDAO;
import at.rc.tacos.server.dbal.dao.PeriodsDAO;
import at.rc.tacos.server.dbal.dao.RosterDAO;
import at.rc.tacos.server.dbal.dao.ServiceTypeDAO;
import at.rc.tacos.server.dbal.dao.SickPersonDAO;
import at.rc.tacos.server.dbal.dao.StaffMemberDAO;
import at.rc.tacos.server.dbal.dao.TransportDAO;
import at.rc.tacos.server.dbal.dao.UserLoginDAO;
import at.rc.tacos.server.dbal.dao.VehicleDAO;
import at.rc.tacos.server.dbal.dao.sqlserver.AddressDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.CallerDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.CompetenceDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.DayInfoDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.DialysisPatientDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.DiseaseDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.JobDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.LinkDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.LocationDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.MobilePhoneDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.PeriodsDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.RosterDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.ServiceTypeDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.SickPersonDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.StaffMemberDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.TransportDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.UserLoginDAOSQL;
import at.rc.tacos.server.dbal.dao.sqlserver.VehicleDetailDAOSQL;

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
	public LinkDAO createLinkDAO() 
	{
		return new LinkDAOSQL();
	}
}

package at.rc.tacos.server.dbal.factory;

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

/**
 * We can make the Data Access Object creation more flexible by adopting the 
 * Abstract Factory pattern.
 */
public interface DaoFactory
{	
	//factory for SQL
	final DaoFactorySQL SQL = new DaoFactorySQL();
	
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
    public LocationDAO createLocationDAO();
    public JobDAO createJobDAO();
    public ServiceTypeDAO createServiceTypeDAO();
    public CompetenceDAO createCompetenceDAO();
    public DiseaseDAO createDiseaseDAO();
    public SickPersonDAO createSickPersonDAO();
    public AddressDAO createAddressDAO();
    public PeriodsDAO createPeriodsDAO();
    public LinkDAO createLinkDAO();
}

package at.rc.tacos.core.db.dao.factory;

import at.rc.tacos.core.db.dao.AddressDAO;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.DiseaseDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.LinkDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.PeriodsDAO;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.SickPersonDAO;
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
	//factory for MYSQL
	@Deprecated
    final DaoFactoryMySql MYSQL = new DaoFactoryMySql();
	
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

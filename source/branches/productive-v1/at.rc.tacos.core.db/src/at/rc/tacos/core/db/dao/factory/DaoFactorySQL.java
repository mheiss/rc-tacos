/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
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
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.PeriodsDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.SickPersonDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.sqlserver.AddressDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.CallerDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.CompetenceDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.DayInfoDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.DialysisPatientDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.DiseaseDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.JobDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.LinkDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.LocationDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.MobilePhoneDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.PeriodsDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.RosterDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.ServiceTypeDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.SickPersonDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.StaffMemberDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.TransportDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.UserLoginDAOSQL;
import at.rc.tacos.core.db.dao.sqlserver.VehicleDetailDAOSQL;

/**
 * Provides the specialized dao factory methods to access a sql source
 * 
 * @author Michael
 */
public class DaoFactorySQL implements DaoFactory {

	@Override
	public MobilePhoneDAO createMobilePhoneDAO() {
		return new MobilePhoneDAOSQL();
	}

	@Override
	public CallerDAO createNotifierDAO() {
		return new CallerDAOSQL();
	}

	@Override
	public RosterDAO createRosterEntryDAO() {
		return new RosterDAOSQL();
	}

	@Override
	public StaffMemberDAO createStaffMemberDAO() {
		return new StaffMemberDAOSQL();
	}

	@Override
	public TransportDAO createTransportDAO() {
		return new TransportDAOSQL();
	}

	@Override
	public UserLoginDAO createUserDAO() {
		return new UserLoginDAOSQL();
	}

	@Override
	public VehicleDAO createVehicleDetailDAO() {
		return new VehicleDetailDAOSQL();
	}

	@Override
	public DialysisPatientDAO createDialysisPatientDAO() {
		return new DialysisPatientDAOSQL();
	}

	@Override
	public DayInfoDAO createDayInfoDAO() {
		return new DayInfoDAOSQL();
	}

	@Override
	public LocationDAO createLocationDAO() {
		return new LocationDAOSQL();
	}

	@Override
	public CompetenceDAO createCompetenceDAO() {
		return new CompetenceDAOSQL();
	}

	@Override
	public JobDAO createJobDAO() {
		return new JobDAOSQL();
	}

	@Override
	public ServiceTypeDAO createServiceTypeDAO() {
		return new ServiceTypeDAOSQL();
	}

	@Override
	public DiseaseDAO createDiseaseDAO() {
		return new DiseaseDAOSQL();
	}

	@Override
	public SickPersonDAO createSickPersonDAO() {
		return new SickPersonDAOSQL();
	}

	@Override
	public AddressDAO createAddressDAO() {
		return new AddressDAOSQL();
	}

	@Override
	public PeriodsDAO createPeriodsDAO() {
		return new PeriodsDAOSQL();
	}

	@Override
	public LinkDAO createLinkDAO() {
		// TODO Auto-generated method stub
		return new LinkDAOSQL();
	}
}

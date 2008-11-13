package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for staff member.
 * 
 * @author Michael
 */
public class StaffMemberSqlService implements StaffMemberService {

	@Resource(name = "sqlConnection")
	protected Connection connection;
	
	@Service(clazz = LocationService.class)
	private LocationService locationDAO;

	@Service(clazz = CompetenceService.class)
	private CompetenceService competenceDAO;

	@Service(clazz = MobilePhoneService.class)
	private MobilePhoneService mobilePhoneDAO;
	
	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public boolean addStaffMember(StaffMember staffMember) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.staffmember"));
		query.setInt(1, staffMember.getStaffMemberId());
		query.setInt(2, staffMember.getPrimaryLocation().getId());
		query.setString(3, staffMember.getFirstName());
		query.setString(4, staffMember.getLastName());
		query.setBoolean(5, staffMember.isMale());
		query.setString(6, staffMember.getPhone1());
		query.setString(7, staffMember.getPhone2());
		query.setString(8, staffMember.getBirthday());
		query.setString(9, staffMember.getEMail());
		query.setString(10, staffMember.getStreetname());
		query.setString(11, staffMember.getCityname());
		query.setString(12, staffMember.getUserName());
		if (query.executeUpdate() == 0)
			return false;
		// remove all competences from the staff member and assign them
		// again
		if (!updateCompetenceList(staffMember))
			return false;
		// update connection phonenumber - staffmember
		if (!updateMobilePhoneList(staffMember))
			return false;
		// successfully
		return true;
	}

	@Override
	public boolean updateStaffMember(StaffMember staffmember) throws SQLException {
		// primaryLocation = ?, firstname = ?, lastname = ?, sex = ?,
		// birthday = ?, email = ?, street = ?, city = ?, username = ? where
		// staffmember_ID = ?;
		final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("update.staffmember"));
		query1.setInt(1, staffmember.getPrimaryLocation().getId());
		query1.setString(2, staffmember.getFirstName());
		query1.setString(3, staffmember.getLastName());
		query1.setBoolean(4, staffmember.isMale());
		query1.setString(5, staffmember.getPhone1());
		query1.setString(6, staffmember.getPhone2());
		query1.setString(7, staffmember.getBirthday());
		query1.setString(8, staffmember.getEMail());
		query1.setString(9, staffmember.getStreetname());
		query1.setString(10, staffmember.getCityname());
		query1.setInt(11, staffmember.getStaffMemberId());

		// check if the update was successfully
		if (query1.executeUpdate() == 0)
			return false;

		// remove all competences from the staff member and assign them
		// again
		if (!updateCompetenceList(staffmember))
			return false;

		// update connection phonenumber - staffmember
		if (!updateMobilePhoneList(staffmember))
			return false;
		return true;
	}

	/**
	 * Returns the not locked staff members
	 */
	public List<StaffMember> getAllStaffMembers() throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembers"));
		final ResultSet rs = query.executeQuery();
		// create the staff list and loop over the result
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}

	/**
	 * Returns the not locked staff members
	 */
	public List<StaffMember> getLockedStaffMembers() throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedStaffmembers"));
		final ResultSet rs = query.executeQuery();
		// create the staff list and loop over the result
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}

	/**
	 * Returns the not locked staff members of the given lacation
	 */
	public List<StaffMember> getStaffMembersFromLocation(int locationId) throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembersFromLocation"));
		query.setInt(1, locationId);
		// create the staff list and loop over the result
		final ResultSet rs = query.executeQuery();
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}

	/**
	 * Returns the not locked staff members of the given lacation
	 */
	public List<StaffMember> getLockedStaffMembersFromLocation(int locationId) throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedStaffmembersFromLocation"));
		query.setInt(1, locationId);
		// create the staff list and loop over the result
		final ResultSet rs = query.executeQuery();
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}

	public StaffMember getStaffMemberByID(int id) throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberByID"));
		query.setInt(1, id);
		final ResultSet rs = query.executeQuery();
		// assert we have a result
		if (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int locationId = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(locationId));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			return staff;
		}
		// no result set
		return null;
	}

	public StaffMember getStaffMemberByUsername(String username) throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberbyUsername"));
		query.setString(1, username);
		final ResultSet rs = query.executeQuery();
		if (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int locationId = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(locationId));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			return staff;
		}
		// no result set
		return null;
	}

	@Override
	public boolean updateCompetenceList(StaffMember staff) throws SQLException {
		// delete all competences of staffmember
		final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.competencesOfStaffMember"));
		clearStmt.setInt(1, staff.getStaffMemberId());
		clearStmt.executeUpdate();
		final PreparedStatement assignQuery = connection.prepareStatement(queries.getStatment("add.competenceToStaffMember"));
		// inserts all new competences of staffmember
		// staffmember_ID, competence_ID
		for (Competence comp : staff.getCompetenceList()) {
			assignQuery.setInt(1, staff.getStaffMemberId());
			assignQuery.setInt(2, comp.getId());
			// check if the update was successfully
			if (assignQuery.executeUpdate() == 0)
				return false;
		}
		return true;
	}

	@Override
	public boolean updateMobilePhoneList(StaffMember staff) throws SQLException {
		// delete all phonenumbers of staffmember
		final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.phonesOfStaffMember"));
		clearStmt.setInt(1, staff.getStaffMemberId());
		clearStmt.executeUpdate();
		// add all mobile phones to the database
		final PreparedStatement assignPhonesStmt = connection.prepareStatement(queries.getStatment("insert.Phonestaffmember"));
		for (MobilePhoneDetail detail : staff.getPhonelist()) {
			assignPhonesStmt.setInt(1, staff.getStaffMemberId());
			assignPhonesStmt.setInt(2, detail.getId());
			if (assignPhonesStmt.executeUpdate() == 0)
				return false;
		}
		return true;
	}

	@Override
	public List<StaffMember> getLockedAndUnlockedStaffMembers() throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedAndUnlockedStaffmembers"));
		final ResultSet rs = query.executeQuery();
		// create the staff list and loop over the result
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}

	@Override
	public List<StaffMember> getLockedAndUnlockedStaffMembersFromLocation(int locationId) throws SQLException {
		// u.username, e.primaryLocation, lo.locationname, e.staffmember_ID,
		// e.firstname, e.lastname, e.sex, e.birthday, e.email,
		// *u.authorization, *u.isloggedin, *u.locked, e.city, e.street
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedAndUnlockedStaffmembersFromLocation"));
		query.setInt(1, locationId);
		// create the staff list and loop over the result
		final ResultSet rs = query.executeQuery();
		List<StaffMember> staffMembers = new ArrayList<StaffMember>();
		while (rs.next()) {
			StaffMember staff = new StaffMember();
			staff.setStaffMemberId(rs.getInt("staffmember_ID"));
			staff.setLastName(rs.getString("lastname"));
			staff.setFirstName(rs.getString("firstname"));
			staff.setStreetname(rs.getString("street"));
			staff.setCityname(rs.getString("city"));
			staff.setMale(rs.getBoolean("sex"));
			staff.setPhone1(rs.getString("phone1"));
			staff.setPhone2(rs.getString("phone2"));
			staff.setBirthday(rs.getString("birthday"));
			staff.setEMail(rs.getString("email"));
			staff.setUserName(rs.getString("username"));
			// query and set the location, phone and competence
			int id = rs.getInt("primaryLocation");
			staff.setPrimaryLocation(locationDAO.getLocation(id));
			staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
			staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
			staffMembers.add(staff);
		}
		return staffMembers;
	}
}

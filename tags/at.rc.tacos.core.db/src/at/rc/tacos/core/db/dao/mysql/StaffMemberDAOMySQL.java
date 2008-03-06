package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.*;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class StaffMemberDAOMySQL implements StaffMemberDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();
	//the dependent dao classes
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	
	@Override
	public boolean addStaffMember(StaffMember staffMember) throws SQLException 
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.staffmember"));
			query.setInt(1, staffMember.getStaffMemberId());
			query.setInt(2, staffMember.getPrimaryLocation().getId());
			query.setString(3, staffMember.getFirstName());
			query.setString(4, staffMember.getLastName());
			query.setBoolean(5, staffMember.isMale());
			query.setString(6, MyUtils.timestampToString(staffMember.getBirthday(), MyUtils.sqlDate));
			query.setString(7, staffMember.getEMail());
			query.setString(8, staffMember.getStreetname());
			query.setString(9, staffMember.getCityname());
			query.setString(10, staffMember.getUserName());
			if(query.executeUpdate() == 0)
				return false;
			//remove all competences from the staff member and assign them again
			if(!updateCompetenceList(staffMember))
				return false;
			//update connection phonenumber - staffmember
			if(!updateMobilePhoneList(staffMember))
				return false;
			//successfully
			return true;
		}
		finally
		{
			connection.close();
		}
	}
	
	@Override
	public boolean updateStaffMember(StaffMember staffmember) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// primaryLocation = ?, firstname = ?, lastname = ?, sex = ?, birthday = ?, email = ?, street = ?, city = ?, username = ? where staffmember_ID = ?;
			final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("update.staffmember"));
			query1.setInt(1, staffmember.getPrimaryLocation().getId());
			query1.setString(2, staffmember.getFirstName());
			query1.setString(3, staffmember.getLastName());
			query1.setBoolean(4, staffmember.isMale());
			query1.setString(5, MyUtils.timestampToString(staffmember.getBirthday(), MyUtils.sqlDate));
			query1.setString(6, staffmember.getEMail());
			query1.setString(7, staffmember.getStreetname());
			query1.setString(8, staffmember.getCityname());
			query1.setInt(9, staffmember.getStaffMemberId());
			
			//check if the update was successfully
			if(query1.executeUpdate() == 0)
				return false;
			
			//remove all competences from the staff member and assign them again
			if(!updateCompetenceList(staffmember))
				return false;
			
			//update connection phonenumber - staffmember
			if(!updateMobilePhoneList(staffmember))
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	public List<StaffMember> getAllStaffMembers() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembers"));
			final ResultSet rs = query.executeQuery();
			//create the staff list and loop over the result
			List<StaffMember> staffMembers = new ArrayList<StaffMember>();
			while(rs.next())
			{
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));
				//query and set the location, phone and competence
				int id = rs.getInt("e.primaryLocation");
				staff.setPrimaryLocation(locationDAO.getLocation(id));
				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
				staffMembers.add(staff);
			}
			return staffMembers;
		}
		finally
		{
			connection.close();
		}
	}

	public List<StaffMember> getStaffMembersFromLocation(int locationId) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembersFromLocation"));
			query.setInt(1, locationId);
			//create the staff list and loop over the result
			final ResultSet rs = query.executeQuery();
			List<StaffMember> staffMembers = new ArrayList<StaffMember>();
			while(rs.next())
			{
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));
				//query and set the location, phone and competence
				int id = rs.getInt("e.primaryLocation");
				staff.setPrimaryLocation(locationDAO.getLocation(id));
				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
				staffMembers.add(staff);
			}
			return staffMembers;
		}
		finally
		{
			connection.close();
		}
	}

	public StaffMember getStaffMemberByID(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberByID"));
			query.setInt(1, id);
			final ResultSet rs = query.executeQuery();
			//assert we have a result
			if(rs.first())
			{
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));
				//query and set the location, phone and competence
				int locationId = rs.getInt("e.primaryLocation");
				staff.setPrimaryLocation(locationDAO.getLocation(locationId));
				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
				return staff;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	public StaffMember getStaffMemberByUsername(String username) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//u.username, e.primaryLocation, lo.locationname, e.staffmember_ID, e.firstname, e.lastname, e.sex, e.birthday, e.email,
			//*u.authorization, *u.isloggedin, *u.locked, e.city, e.street
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberbyUsername"));
			query.setString(1, username);
			final ResultSet rs = query.executeQuery();
			if(rs.first())
			{
				StaffMember staff = new StaffMember();
				staff.setStaffMemberId(rs.getInt("e.staffmember_ID"));
				staff.setLastName(rs.getString("e.lastname"));
				staff.setFirstName(rs.getString("e.firstname"));
				staff.setStreetname(rs.getString("e.street"));
				staff.setCityname(rs.getString("e.city"));
				staff.setMale(rs.getBoolean("e.sex"));
				staff.setBirthday(MyUtils.stringToTimestamp(rs.getString("e.birthday"), MyUtils.sqlDate));
				staff.setEMail(rs.getString("e.email"));
				staff.setUserName(rs.getString("u.username"));
				//query and set the location, phone and competence
				int locationId = rs.getInt("e.primaryLocation");
				staff.setPrimaryLocation(locationDAO.getLocation(locationId));
				staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
				staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
				return staff;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateCompetenceList(StaffMember staff) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//delete all competences of staffmember
			final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.competencesOfStaffMember"));
			clearStmt.setInt(1, staff.getStaffMemberId());
			clearStmt.executeUpdate();
			final PreparedStatement assignQuery = connection.prepareStatement(queries.getStatment("add.competenceToStaffMember"));
			//inserts all new competences of staffmember
			//staffmember_ID, competence_ID
			for(Competence comp:staff.getCompetenceList())
			{
				assignQuery.setInt(1, staff.getStaffMemberId());
				assignQuery.setInt(2, comp.getId());
				//check if the update was successfully
				if(assignQuery.executeUpdate() == 0)
					return false;
			}
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateMobilePhoneList(StaffMember staff) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//delete all phonenumbers of staffmember
			final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.phonesOfStaffMember"));
			clearStmt.setInt(1, staff.getStaffMemberId());
			clearStmt.executeUpdate();
			//add all mobile phones to the database
			final PreparedStatement assignPhonesStmt = connection.prepareStatement(queries.getStatment("insert.Phonestaffmember"));
			for(MobilePhoneDetail detail:staff.getPhonelist())
			{
				assignPhonesStmt.setInt(1, staff.getStaffMemberId());
				assignPhonesStmt.setInt(2, detail.getId());
				if(assignPhonesStmt.executeUpdate() == 0)
					return false;
			}
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}
package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.StaffMember;

public interface StaffMemberDAO 
{
    public static final String TABLE_NAME = "staffmembers";
    
	public boolean addStaffMember(StaffMember staffMember) throws SQLException;
	public List<StaffMember> getAllStaffMembers() throws SQLException;
	public List<StaffMember> getStaffMembersFromLocation(int locationId) throws SQLException;
	public StaffMember getStaffMemberByID(int id) throws SQLException;
	public StaffMember getStaffMemberByUsername(String username) throws SQLException;
	public boolean updateStaffMember(StaffMember staffmember) throws SQLException;
	public boolean updateMobilePhoneList(StaffMember staff) throws SQLException;
	public boolean updateCompetenceList(StaffMember staff) throws SQLException;
}

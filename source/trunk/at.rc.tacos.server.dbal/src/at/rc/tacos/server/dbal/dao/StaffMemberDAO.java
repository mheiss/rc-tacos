package at.rc.tacos.server.dbal.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.StaffMember;

public interface StaffMemberDAO 
{
    public static final String TABLE_NAME = "staffmembers";
    
	public boolean addStaffMember(StaffMember staffMember) throws SQLException;
	public List<StaffMember> getAllStaffMembers() throws SQLException;
	public List<StaffMember> getLockedStaffMembers() throws SQLException;
	public List<StaffMember> getLockedAndUnlockedStaffMembers() throws SQLException;
	public List<StaffMember> getStaffMembersFromLocation(int locationId) throws SQLException;
	public List<StaffMember> getLockedStaffMembersFromLocation(int locationId) throws SQLException;
	public List<StaffMember> getLockedAndUnlockedStaffMembersFromLocation(int locationId) throws SQLException;
	public StaffMember getStaffMemberByID(int id) throws SQLException;
	public StaffMember getStaffMemberByUsername(String username) throws SQLException;
	public boolean updateStaffMember(StaffMember staffmember) throws SQLException;
	public boolean updateMobilePhoneList(StaffMember staff) throws SQLException;
	public boolean updateCompetenceList(StaffMember staff) throws SQLException;
}

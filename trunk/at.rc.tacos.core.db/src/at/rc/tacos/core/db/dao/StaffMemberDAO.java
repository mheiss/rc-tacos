package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.StaffMember;

public interface StaffMemberDAO 
{
	public List<StaffMember> getAllStaffMembers() throws SQLException;
	public List<StaffMember> getStaffMembersFromLocation(String locationname) throws SQLException;
	public StaffMember getStaffMemberByID(int id) throws SQLException;
	public StaffMember getStaffMemberByUsername(String username) throws SQLException;
	public Integer addStaffMember(StaffMember staffMember, String pwdHash) throws SQLException;
	public boolean updateStaffMember(StaffMember staffMember) throws SQLException;
	
    public boolean deleteStaffMember(StaffMember member) throws SQLException;
}

package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.StaffMember;

public interface StaffMemberDAO 
{
	//NOTE: add update and remove of staff members is handled in the userLoginDAO
	public List<StaffMember> getAllStaffMembers();
	public List<StaffMember> getStaffMembersFromLocation(int locationId);
	public StaffMember getStaffMemberByID(int id);
	public StaffMember getStaffMemberByUsername(String username);
}

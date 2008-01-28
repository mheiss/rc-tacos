package at.rc.tacos.core.db.dao;

import java.util.List;

import at.rc.tacos.model.StaffMember;

public interface StaffMemberDAO 
{
    public static final String TABLE_NAME = "staffmembers";
    
	// note: add update and remove of staff members is handled in the userLoginDAO
	public List<StaffMember> getAllStaffMembers();
	public List<StaffMember> getStaffMembersFromLocation(int locationId);
	public StaffMember getStaffMemberByID(int id);
	public StaffMember getStaffMemberByUsername(String username);
	public boolean updateStaffMember(StaffMember staffmember);
	public boolean updateMobilePhoneList(StaffMember staff);
	public boolean updateCompetenceList(StaffMember staff);
}

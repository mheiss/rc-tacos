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
package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.StaffMember;

public interface StaffMemberDAO {

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

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
package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;

public class StaffMemberEditorInput implements IEditorInput {

	// properties
	private StaffMember staffMember;
	private Login loginInfo;
	private boolean isNew;

	/**
	 * Default class constructor for the staff member editor.
	 * 
	 * @param staffMember
	 *            the staffMember to edit
	 * @param login
	 *            the login information accociated with the staff member
	 * @param isNew
	 *            a flag to determine whether the member is new
	 */
	public StaffMemberEditorInput(StaffMember staffMember, Login login, boolean isNew) {
		this.staffMember = staffMember;
		this.loginInfo = login;
		this.isNew = isNew;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		if (isNew)
			return "Neuer Mitarbeiter";
		return staffMember.getLastName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Mitarbeiter: " + staffMember.getLastName() + " " + staffMember.getFirstName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof StaffMemberEditorInput) {
			StaffMemberEditorInput otherEditor = (StaffMemberEditorInput) other;
			return staffMember.equals(otherEditor.getStaffMember());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return staffMember.hashCode();
	}

	/**
	 * Returns the staff member
	 * 
	 * @return the staff member
	 */
	public StaffMember getStaffMember() {
		return staffMember;
	}

	/**
	 * Returns the login information accociated with the staff member
	 * 
	 * @return the login info
	 */
	public Login getLoginInformation() {
		return loginInfo;
	}

	/**
	 * Returns whether or not the staff member is new.
	 * 
	 * @return true if the staff member is created new
	 */
	public boolean isNew() {
		return isNew;
	}
}

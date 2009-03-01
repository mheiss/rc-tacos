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

import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneEditorInput implements IEditorInput {

	// properties
	private MobilePhoneDetail mobilePhone;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public MobilePhoneEditorInput(MobilePhoneDetail mobilePhone, boolean isNew) {
		this.mobilePhone = mobilePhone;
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
			return "Neues Mobiltelefon";
		return mobilePhone.getMobilePhoneName() + " " + mobilePhone.getMobilePhoneNumber();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Mobiltelefon: " + mobilePhone.getMobilePhoneName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof MobilePhoneEditorInput) {
			MobilePhoneEditorInput otherEditor = (MobilePhoneEditorInput) other;
			return mobilePhone.equals(otherEditor.getMobilePhone());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return mobilePhone.hashCode();
	}

	/**
	 * Returns the mobile phone managed by the editor
	 * 
	 * @return the managed phone
	 */
	public MobilePhoneDetail getMobilePhone() {
		return mobilePhone;
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

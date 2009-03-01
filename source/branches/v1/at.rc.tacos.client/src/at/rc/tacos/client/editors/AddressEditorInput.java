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

import at.rc.tacos.model.Address;

public class AddressEditorInput implements IEditorInput {

	// properties
	private Address address;
	private boolean isNew;

	/**
	 * Default class constructor for a address editor input.
	 * 
	 * @param address
	 *            the address to manage
	 * @param isNew
	 *            flag to treat this entry as new
	 */
	public AddressEditorInput(Address address, boolean isNew) {
		this.address = address;
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
			return "Neue Adresse";
		return address.getZip() + "," + address.getCity() + "," + address.getStreet();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Adresse: " + address.getZip() + "," + address.getCity() + "," + address.getStreet();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AddressEditorInput) {
			AddressEditorInput otherEditor = (AddressEditorInput) other;
			return address.equals(otherEditor.getAddress());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}

	/**
	 * Returns the address managed by the editor
	 * 
	 * @return the managed address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * Returns whether or not the address is new.
	 * 
	 * @return true if the address is created new
	 */
	public boolean isNew() {
		return isNew;
	}
}

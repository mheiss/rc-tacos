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

import at.rc.tacos.model.ServiceType;

public class ServiceTypeEditorInput implements IEditorInput {

	// properties
	private ServiceType serviceType;
	private boolean isNew;

	/**
	 * Default class constructor for the serviceType editor.
	 * 
	 * @param serviceType
	 *            the serviceType to edit
	 * @param isNew
	 *            a flag to determine whether the serviceType is new
	 */
	public ServiceTypeEditorInput(ServiceType serviceType, boolean isNew) {
		this.serviceType = serviceType;
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
			return "Neues Dienstverhältnis";
		return serviceType.getServiceName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Dienstverhältnis: " + serviceType.getServiceName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ServiceTypeEditorInput) {
			ServiceTypeEditorInput otherEditor = (ServiceTypeEditorInput) other;
			return serviceType.equals(otherEditor.getServiceType());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return serviceType.hashCode();
	}

	/**
	 * Returns the service type managed by the editor
	 * 
	 * @return the managed service type
	 */
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * Returns whether or not the service type is new.
	 * 
	 * @return true if the service type is created new
	 */
	public boolean isNew() {
		return isNew;
	}
}

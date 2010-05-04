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
package at.rc.tacos.client.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class StaffMemberVehicleContentProvider implements IStructuredContentProvider {

	private VehicleDetail vehicle;

	/**
	 * Default class constructor for a vontent provider
	 */
	public StaffMemberVehicleContentProvider(VehicleDetail detail) {
		this.vehicle = detail;
	}

	@Override
	public Object[] getElements(Object arg0) {
		List<StaffMember> list = ModelFactory.getInstance().getStaffManager().getFreeStaffMembers(vehicle);
		return list.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}
}

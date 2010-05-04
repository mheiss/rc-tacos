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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.model.Transport;

public class MultiTransportContentProvider implements IStructuredContentProvider {

	// the item list
	private List<Transport> objectList = new ArrayList<Transport>();

	public MultiTransportContentProvider() {
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		// do nothing
	}

	public void dispose() {
		// do nothing
	}

	public Object[] getElements(Object parent) {
		return objectList.toArray();
	}

	public void addTransport(Transport transport) {
		objectList.add(transport);
	}

	public List<Transport> getObjectList() {
		return objectList;
	}

	public void removeTransport(int index) {
		objectList.remove(index);
	}

	public void removeAllTransports() {
		objectList.clear();
	}
}

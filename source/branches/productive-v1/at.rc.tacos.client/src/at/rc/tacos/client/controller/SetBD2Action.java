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
package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

/**
 * Sets or clears the bd2 option for the transport
 * 
 * @author b.thek
 */
public class SetBD2Action extends Action implements IProgramStatus {

	// properties
	private TableViewer viewer;
	private Transport transport;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public SetBD2Action(TableViewer viewer) {
		this.viewer = viewer;
		setText("BD2");
		setToolTipText("Setzt BD2 für diesen Transport");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		transport.setBlueLightToGoal(!transport.isBlueLightToGoal());
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		if (transport.isBlueLightToGoal())
			return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		else
			return null;
	}
}

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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.factory.ImageFactory;

/**
 * Opens the form to open the transport form with transport type
 * "emergencyTransport" the transport type is used within the form to en- or
 * disable buttons and show/hide groups
 * 
 * @author b.thek
 */
public class OpenEmergencyTransportAction extends Action {

	/**
	 * Returns the tool tip text for the action
	 * 
	 * @return the tool tip text
	 */
	@Override
	public String getToolTipText() {
		return "�ffnet ein Fenster um einen neuen Notfall Transport zu erstellen";
	}

	/**
	 * Returns the text to show in the tool bar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Notfall";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createTransportEmergency");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		// get the active shell
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		// create the window
		TransportForm window = new TransportForm(parent, "emergencyTransport");
		window.create();

		// get the parent and the window shell
		Shell myShell = window.getShell();
		myShell.setSize(1080, 680);

		// calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width) / 2 + workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height) / 2 + workbenchSize.y;
		myShell.setLocation(locationX, locationY);

		// now open the window
		myShell.open();
		myShell.setVisible(true);
	}
}

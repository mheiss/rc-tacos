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

import at.rc.tacos.client.view.DialysisForm;
import at.rc.tacos.factory.ImageFactory;

public class OpenDialysisTransportAction extends Action {

	/**
	 * Returns the tool tip text for the action
	 * 
	 * @return the tool tip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnet ein Fenster um einen neuen Dialyseeintrag zu erstellen";
	}

	/**
	 * Returns the text to show in the tool bar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Dialyse";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.createTransportDialyse");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		DialysisForm window = new DialysisForm();
		window.open();
	}

}

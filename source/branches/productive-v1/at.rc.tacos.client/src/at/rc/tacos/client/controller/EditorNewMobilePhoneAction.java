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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.editors.MobilePhoneEditor;
import at.rc.tacos.client.editors.MobilePhoneEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * Opens a new editor to create a new mobile phone
 * 
 * @author Michael
 */
public class EditorNewMobilePhoneAction extends Action {

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewMobilePhoneAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um ein Mobiltelefon anzulegen.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Mobiltelefon hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.mobilePhoneAdd");
	}

	/**
	 * Opens the editor to create the mobile phone
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new mobile phone
			MobilePhoneDetail newPhone = new MobilePhoneDetail();
			// create the editor input
			MobilePhoneEditorInput input = new MobilePhoneEditorInput(newPhone, true);
			// try to open the editor
			page.openEditor(input, MobilePhoneEditor.ID);
		}
		catch (PartInitException e) {
			Activator.getDefault().log("Failed to create a new phone editor", IStatus.ERROR);
		}
	}
}

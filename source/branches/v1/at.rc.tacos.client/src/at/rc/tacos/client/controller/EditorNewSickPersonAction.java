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
import at.rc.tacos.client.editors.SickPersonEditor;
import at.rc.tacos.client.editors.SickPersonEditorInput;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.SickPerson;

/**
 * Opens an empty editor to create a new location
 * 
 * @author Birgit
 */
public class EditorNewSickPersonAction extends Action {

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewSickPersonAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "�ffnen ein neues Fenster um einen Patienten zu erfassen.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Patient hinzuf�gen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.patientAdd");
	}

	/**
	 * Opens the editor to create the location
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new sick person
			SickPerson newPerson = new SickPerson();
			// create the editor input
			SickPersonEditorInput input = new SickPersonEditorInput(newPerson, true);
			// try to open the editor
			page.openEditor(input, SickPersonEditor.ID);
		}
		catch (PartInitException e) {
			Activator.getDefault().log("Failed to create a new sick person editor", IStatus.ERROR);
		}
	}
}

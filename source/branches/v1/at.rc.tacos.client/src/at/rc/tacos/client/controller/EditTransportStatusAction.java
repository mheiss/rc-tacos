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

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.view.TransportStatiForm;
import at.rc.tacos.model.Transport;

/**
 * Opens the form to edit the transport stati
 * 
 * @author b.thek
 */
public class EditTransportStatusAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public EditTransportStatusAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Stati bearbeiten");
		setToolTipText("�ffnet ein Fenster um die Stati zu bearbeiten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currently locked
		String resultLockMessage = LockManager.sendLock(Transport.ID, transport.getTransportId());

		// check the result of the lock
		if (resultLockMessage != null) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport den Sie bearbeiten m�chten wird bereits von " + resultLockMessage + " bearbeitet\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern f�hren!\n\n"
							+ "M�chten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log(
					"Der Eintrag " + transport + " wird trotz Sperrung durch " + resultLockMessage + " von " + username + " bearbeitet",
					Status.WARNING);
		}

		// open the editor
		TransportStatiForm form = new TransportStatiForm(transport);
		form.open();
	}
}

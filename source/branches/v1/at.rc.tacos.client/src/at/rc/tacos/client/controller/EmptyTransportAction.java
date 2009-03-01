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
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Assigns the transport as an empty transport
 * 
 * @author b.thek
 */
public class EmptyTransportAction extends Action implements ITransportStatus, IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public EmptyTransportAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Leerfahrt");
		setToolTipText("Macht aus dem Transport eine Leerfahrt");
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
					"Der Transport,den Sie bearbeiten möchten wird bereits von " + resultLockMessage + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Es wird dringend empfohlen, den Transport erst nach Freigabe durch " + resultLockMessage
							+ " als Leerfahrt zu kennzeichnen!\n\n" + "Möchten Sie das Fahrzeug trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log(
					"Der Eintrag " + transport + " wird trotz Sperrung durch " + resultLockMessage + " von " + username + " bearbeitet",
					Status.WARNING);
		}

		// confirm the cancel
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Leerfahrt",
				"Bitte geben Sie Informationen zur Leerfahrt ein", null, null);
		if (dlg.open() == Window.OK) {
			transport.setNotes(transport.getNotes() + " Leerfahrtinformation: " + dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}

		// remove the lock from the object
		LockManager.removeLock(Transport.ID, transport.getTransportId());
	}
}

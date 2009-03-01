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

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.util.MyUtils;

/**
 * Opens a dialog to confirm and then create a Transport form the selected
 * dialysis entry
 * 
 * @author b.thek
 */
public class DialysisTransportNowAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public DialysisTransportNowAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Jetzt durchf�hren");
		setToolTipText("Dialyseeintrag wird in Tabelle offene Transporte angezeigt");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected dialyse transport
		DialysisPatient dia = (DialysisPatient) ((IStructuredSelection) selection).getFirstElement();

		// check if we have already a transport for today
		if (MyUtils.isEqualDate(dia.getLastTransportDate(), Calendar.getInstance().getTimeInMillis())) {
			boolean confirmCreate = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Dialysetransport best�tigen", "F�r diesen Dialysepatienten wurde heute bereits ein Transport erstellt.\n"
							+ "Wollen Sie wirklich noch einen Transport erstellen?");
			if (!confirmCreate)
				return;
		}

		// confirm the action
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Dialysetransport jetzt durchf�hren", "M�chten Sie den Dialysetransport wirklich jetzt durchf�hren?");
		if (!cancelConfirmed)
			return;

		// set the time and send a update request to the server
		dia.setLastTransportDate(Calendar.getInstance().getTimeInMillis());// to
																			// inform
																			// the
																			// system,
																			// that
																			// no
																			// further
																			// transport
																			// for
																			// this
																			// day
																			// should
																			// be
																			// created
																			// automatically
																			// (by
																			// the
																			// job)
		NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, dia);

		// create a transport for this patient
		CreateTransportFromDialysis createAction = new CreateTransportFromDialysis(dia, Calendar.getInstance());
		createAction.run();
	}
}

package at.rc.tacos.client.ui.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.util.MyUtils;

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
		setText("Jetzt durchführen");
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
					"Dialysetransport bestätigen", "Für diesen Dialysepatienten wurde heute bereits ein Transport erstellt.\n"
							+ "Wollen Sie wirklich noch einen Transport erstellen?");
			if (!confirmCreate)
				return;
		}

		// confirm the action
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Dialysetransport jetzt durchführen", "Möchten Sie den Dialysetransport wirklich jetzt durchführen?");
		if (!cancelConfirmed)
			return;

		// set the time and send a update request to the server
		dia.setLastTransportDate(Calendar.getInstance().getTimeInMillis());

		// to inform the system, that no further transport for this day should
		// be created automatically (by the job)
		UpdateMessage<DialysisPatient> updateMessage = new UpdateMessage<DialysisPatient>(dia);
		updateMessage.asnchronRequest(NetWrapper.getSession());

		// create a transport for this patient
		Transport transport = Transport.createTransport(dia, NetWrapper.getSession().getUsername());

		AddMessage<Transport> addMessage = new AddMessage<Transport>(transport);
		addMessage.asnchronRequest(NetWrapper.getSession());
	}
}

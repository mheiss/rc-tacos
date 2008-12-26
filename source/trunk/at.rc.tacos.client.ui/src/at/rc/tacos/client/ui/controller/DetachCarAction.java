package at.rc.tacos.client.ui.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.RemoveMessage;

/**
 * Detaches the car from the transport
 * 
 * @author b.thek
 */
public class DetachCarAction extends Action implements IProgramStatus {

	private Logger log = LoggerFactory.getLogger(DetachCarAction.class);

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public DetachCarAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Abziehen des Fahrzeuges");
		setToolTipText("Zieht das Fahrzeug vom Transport ab");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport,von dem Sie das Fahrzeug abziehen möchten wird bereits von " + transport.getLockedBy() + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Es wird dringend empfohlen, das Fahrzeug erst nach Freigabe durch " + transport.getLockedBy() + " abzuziehen!\n\n"
							+ "Möchten Sie das Fahrzeug trotzdem abziehen?");
			if (!forceEdit)
				return;
			// log the override of the lock
			log.warn("Der Eintrag " + transport + " wird trotz Sperrung durch " + transport.getLockedBy() + " von "
					+ NetWrapper.getSession().getUsername() + " bearbeitet", Status.WARNING);
		}

		// confirm the cancel
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Transport Stornierung",
				"Bitte geben Sie eine Begründung für das Abziehen des Fahrzeuges" + " " + transport.getVehicleDetail().getVehicleName() + " ein",
				null, null);
		if (dlg.open() == Window.OK) {
			transport.getStatusMessages().clear();
			transport.clearVehicleDetail();
			transport.setNotes(transport.getNotes() + "Fahrzeugabzug: " + dlg.getValue() + "; ");
			transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
			// remove the lock
			transport.setLocked(false);
			transport.setLockedBy(null);

			// send the remove request
			RemoveMessage<Transport> removeMessage = new RemoveMessage<Transport>(transport);
			removeMessage.asnchronRequest(NetWrapper.getSession());
		}
	}
}

package at.rc.tacos.client.ui.controller;

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
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Assigns the transport as orwarded
 * 
 * @author b.thek
 */
public class ForwardTransportAction extends Action implements ITransportStatus, IProgramStatus {

	private Logger log = LoggerFactory.getLogger(ForwardTransportAction.class);

	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public ForwardTransportAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Weiterleiten");
		setToolTipText("Verschiebt den Transport in das Journalblatt und setzt die Transportnummer auf \"WLTG\"");
	}

	@Override
	public void run() {
		// tget the selected transport
		ISelection selection = viewer.getSelection();
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currently locked
		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport den Sie weiterleiten möchten wird bereits von " + transport.getLockedBy() + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Es wird dringend empfohlen, den Eintrag erst nach Freigabe durch " + transport.getLockedBy() + " zu stornieren!\n\n"
							+ "Möchten Sie den Eintrag trotzdem sofort weiterleiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + transport + " wird trotz Sperrung durch " + transport.getLockedBy() + " von " + username + " bearbeitet");
		}

		// confirm the cancel
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Weiterleitung",
				"Bitte geben Sie Informationen zur Weiterleitung ein", null, null);
		if (dlg.open() == Window.OK) {
			transport.setNotes(transport.getNotes() + " Weiterleitungsinformation: " + dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			transport.setTransportNumber(Transport.TRANSPORT_FORWARD);

			// remove the lock
			transport.setLocked(false);
			transport.setLockedBy(null);

			// send the update
			UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
			updateMessage.asnchronRequest(NetWrapper.getSession());
		}
	}
}

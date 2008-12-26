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
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Opens the editor to ask for a storno reason and set the needed transport
 * properties for a storno
 * 
 * @author b.thek
 */
public class CancelTransportAction extends Action {

	private Logger log = LoggerFactory.getLogger(CancelTransportAction.class);

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public CancelTransportAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Storno");
		setToolTipText("Storniert den Transport und verschiebt ihn in das Journalblatt");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport den Sie stornieren möchten wird bereits von " + transport.getLockedBy() + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Es wird dringend empfohlen, den Eintrag erst nach Freigabe durch " + transport.getLockedBy() + " zu stornieren!\n\n"
							+ "Möchten Sie den Eintrag trotzdem sofort stornieren?");
			if (!forceEdit)
				return;
			// log the override of the lock
			log.warn("Der Transport " + transport.getTransportNumber() + " wird trotz Sperrung durch " + transport.getLockedBy() + " von "
					+ NetWrapper.getSession().getUsername() + " bearbeitet");
		}

		// confirm the cancel
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Transport Stornierung",
				"Bitte geben Sie Informationen zur Stornierung ein", null, null);
		if (dlg.open() == Window.OK) {
			transport.setNotes(transport.getNotes() + " Stornoinformation: " + dlg.getValue());
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			transport.setTransportNumber(Transport.TRANSPORT_CANCLED);

			// update the lock
			transport.setLocked(false);
			transport.setLockedBy(null);

			UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
			updateMessage.asnchronRequest(NetWrapper.getSession());
		}
	}
}

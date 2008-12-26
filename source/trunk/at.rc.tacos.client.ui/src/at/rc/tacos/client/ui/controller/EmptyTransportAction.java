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
 * Assigns the transport as an empty transport
 * 
 * @author b.thek
 */
public class EmptyTransportAction extends Action implements ITransportStatus, IProgramStatus {

	private Logger log = LoggerFactory.getLogger(EmptyTransportAction.class);

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
		// get the selected transport
		ISelection selection = viewer.getSelection();
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currently locked
		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport,den Sie bearbeiten möchten wird bereits von " + transport.getLockedBy() + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Es wird dringend empfohlen, den Transport erst nach Freigabe durch " + transport.getLockedBy()
							+ " als Leerfahrt zu kennzeichnen!\n\n" + "Möchten Sie das Fahrzeug trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + transport + " wird trotz Sperrung durch " + transport.getLockedBy() + " von " + username + " bearbeitet");
		}

		// confirm the cancel
		InputDialog dlg = new InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Leerfahrt",
				"Bitte geben Sie Informationen zur Leerfahrt ein", null, null);
		if (dlg.open() == Window.OK) {
			transport.setNotes(transport.getNotes() + " Leerfahrtinformation: " + dlg.getValue());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			// remove the lock
			transport.setLocked(false);
			transport.setLockedBy(null);

			// send the update
			UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
			updateMessage.asnchronRequest(NetWrapper.getSession());
		}
	}
}

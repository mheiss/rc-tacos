package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.dialog.TransportStatusDialog;
import at.rc.tacos.platform.model.Transport;

/**
 * Opens the form to edit the transport stati
 * 
 * @author b.thek
 */
public class EditTransportStatusAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditTransportStatusAction.class);

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
		setToolTipText("Öffnet ein Fenster um die Stati zu bearbeiten");
	}

	@Override
	public void run() {
		// the selected transport
		ISelection selection = viewer.getSelection();
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currently locked
		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport den Sie bearbeiten möchten wird bereits von " + transport.getLockedBy() + " bearbeitet\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Möchten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + transport + " wird trotz Sperrung durch " + transport.getLockedBy() + " von " + username + " bearbeitet");
		}

		// open the editor
		Shell shell = Display.getCurrent().getActiveShell();
		TransportStatusDialog form = new TransportStatusDialog(shell, transport);
		form.open();
	}
}

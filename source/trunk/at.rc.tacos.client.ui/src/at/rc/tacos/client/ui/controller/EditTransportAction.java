package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.dialog.TransportForm;
import at.rc.tacos.platform.model.Transport;

/**
 * Opens the editor to edit the selected entry
 * 
 * @author b.thek
 */
public class EditTransportAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditTransportAction.class);

	// properties
	private TableViewer viewer;
	private String editingType;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public EditTransportAction(TableViewer viewer, String editingType) {
		this.viewer = viewer;
		this.editingType = editingType;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Transport zu bearbeiten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currenlty locked
		if (transport.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Transport den Sie bearbeiten möchten wird bereits von " + transport.getLockedBy() + " bearbeitet.\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Möchten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			log.warn("Der Eintrag " + transport + " wird trotz Sperrung durch " + transport.getLockedBy() + " von "
					+ NetWrapper.getSession().getUsername() + " bearbeitet");
		}

		// open the editor
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		TransportForm form = new TransportForm(shell, transport, editingType);
		form.open();
	}
}

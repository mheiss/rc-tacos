package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.dialog.DialysisForm;
import at.rc.tacos.platform.model.DialysisPatient;

/**
 * Opens the editor to edit the selected dialysis entry
 * 
 * @author b.thek
 */
public class DialysisEditAction extends Action {

	private Logger log = LoggerFactory.getLogger(DialysisEditAction.class);

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public DialysisEditAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster, um den Dialyseeintrag zu bearbeiten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		DialysisPatient dia = (DialysisPatient) ((IStructuredSelection) selection).getFirstElement();

		// check the result of the lock
		if (dia.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Der Dialysepatient den Sie bearbeiten möchten wird bereits von " + dia.getLockedBy() + " bearbeitet\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Möchten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// logg the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + dia + " wird trotz Sperrung durch " + dia.getLockedBy() + " von " + username + " bearbeitet");
		}

		// edit the entry
		DialysisForm form = new DialysisForm(dia, false);
		form.open();
	}
}

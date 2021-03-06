package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Opens the form to confirm the cancel and sets the sign in cancel
 * 
 * @author b.thek
 */
public class PersonalCancelSignInAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public PersonalCancelSignInAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Anmeldung aufheben");
		setToolTipText("Hebt eine zuvor getätigte Anmeldung wieder auf");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		RosterEntry entry = (RosterEntry) ((IStructuredSelection) selection).getFirstElement();
		// confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Aufhebung bestätigen",
				"Die Anmeldung wirklich aufheben?");
		if (!cancelConfirmed)
			return;

		// reset the field
		entry.setRealStartOfWork(0);

		UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(entry);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}
}

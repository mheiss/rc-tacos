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
 * Cancels a previouse made sign out.
 * 
 * @author Michael
 */
public class PersonalCancelSignOutAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public PersonalCancelSignOutAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Abmeldung aufheben");
		setToolTipText("Hebt eine zuvor get�tigte Abmeldung wieder auf");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		RosterEntry entry = (RosterEntry) ((IStructuredSelection) selection).getFirstElement();
		// confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Aufhebung best�tigen",
				"Die Abmeldung wirklich aufheben?");
		if (!cancelConfirmed)
			return;

		// reset the field
		entry.setRealEndOfWork(0);

		// send the update message
		UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(entry);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}
}

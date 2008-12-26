package at.rc.tacos.client.ui.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Opens the editor to edit the selected entry
 * 
 * @author Michael
 */
public class PersonalDeleteEntryAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class construtor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public PersonalDeleteEntryAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Eintrag l�schen");
		setToolTipText("L�scht den Dienstplaneintrag");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		RosterEntry entry = (RosterEntry) ((IStructuredSelection) selection).getFirstElement();
		// confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Dienstplaneintrag l�schen", "M�chten Sie den Dienstplaneintrag wirklich l�schen?");
		if (!cancelConfirmed)
			return;

		// sign out the entry to reject assigned staff member from vehicle
		// get the hour and the minutes
		Calendar cal = Calendar.getInstance();
		// the hour and the minutes
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		// now set up a new calendar with the current time and overwrite the
		// minutes and the hours
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		// send the update message
		entry.setRealEndOfWork(cal.getTimeInMillis());

		// first send the update message
		UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(entry);
		updateMessage.asnchronRequest(NetWrapper.getSession());

		// then delete the entry
		RemoveMessage<RosterEntry> removeMessage = new RemoveMessage<RosterEntry>(entry);
		removeMessage.asnchronRequest(NetWrapper.getSession());
	}
}

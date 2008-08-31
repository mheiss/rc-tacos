package at.rc.tacos.client.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.RosterEntry;

/**
 * Opens the editor to edit the selected entry
 * @author Michael
 */
public class PersonalDeleteEntryAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public PersonalDeleteEntryAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag löschen");
		setToolTipText("Löscht den Dienstplaneintrag");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		RosterEntry entry = (RosterEntry)((IStructuredSelection)selection).getFirstElement();
		//confirm the cancel
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Dienstplaneintrag löschen", "Möchten Sie den Dienstplaneintrag wirklich löschen?");
		if (!cancelConfirmed) 
			return;
		
		//sign out the entry to reject assigned staff member from vehicle
		 //get the hour and the minutes
		Calendar cal = Calendar.getInstance();
        //the hour and the minutes
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        //now set up a new calendar with the current time and overwrite the 
        //minutes and the hours
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE,minutes);
        //send the update message
        entry.setRealEndOfWork(cal.getTimeInMillis());
        NetWrapper.getDefault().sendUpdateMessage(RosterEntry.ID, entry);
        
        
		//request to delete
		NetWrapper.getDefault().sendRemoveMessage(RosterEntry.ID, entry);
	}
}

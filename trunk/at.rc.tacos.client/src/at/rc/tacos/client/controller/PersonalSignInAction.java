package at.rc.tacos.client.controller;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.util.Util;
import at.rc.tacos.client.validators.DateValidator;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.RosterEntry;

public class PersonalSignInAction extends Action
{
	//properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public PersonalSignInAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Anmelden");
		setToolTipText("Meldet eine Person zum Dienst an");
	}

	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		RosterEntry entry = (RosterEntry)((IStructuredSelection)selection).getFirstElement();
		//confirm the cancel
		InputDialog dlg = new InputDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Anmeldezeit", 
				"Bitte geben Sie die Anmeldezeit ein", 
				Util.formatTime(new Date().getTime()), new DateValidator());
		if (dlg.open() == Window.OK) 
		{
			//set the time
			System.out.println("Value: "+dlg.getValue());
			long time = Util.getTimestampFromTime(dlg.getValue());
			entry.setRealStartOfWork(time);
			NetWrapper.getDefault().sendUpdateMessage(RosterEntry.ID, entry);
		}
	}
}

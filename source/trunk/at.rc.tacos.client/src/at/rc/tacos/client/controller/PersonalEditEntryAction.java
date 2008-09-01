package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.view.RosterEntryForm;
import at.rc.tacos.platform.model.RosterEntry;

/**
 * Opens the editor to edit the selected entry
 * @author Michael
 */
public class PersonalEditEntryAction extends Action
{
	//properties
	private TableViewer viewer;

	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public PersonalEditEntryAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Dienstplaneintrag zu bearbeiten");
	}

	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		RosterEntry entry = (RosterEntry)((IStructuredSelection)selection).getFirstElement();
		
		//check if the object is currenlty locked
		String resultLockMessage = LockManager.sendLock(RosterEntry.ID, entry.getRosterId());
		
		//check the result of the lock
		if(resultLockMessage != null)
		{
			boolean forceEdit =  MessageDialog.openQuestion(
					Display.getCurrent().getActiveShell(), 
					"Information: Eintrag wird bearbeitet", 
					"Der Eintrag den Sie bearbeiten möchten wird bereits von "+ resultLockMessage+ " bearbeitet\n"+
					"Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"+
					"Möchten Sie den Eintrag trotzdem bearbeiten?");
			if(!forceEdit)
				return;
			//logg the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log("Der Eintrag "+entry+" wird trotz Sperrung durch "+resultLockMessage +" von "+username+" bearbeitet",Status.WARNING);
		}

		//open the editor
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		//get the parent and the window shell
		RosterEntryForm window = new RosterEntryForm(parent,entry);
		window.create();

		//get the shell and resize
		Shell myShell = window.getShell();
		myShell.setSize(540, 620);

		//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		myShell.setLocation(locationX,locationY);

		//now open the window
		myShell.open();
		myShell.setVisible(true);
	}
}

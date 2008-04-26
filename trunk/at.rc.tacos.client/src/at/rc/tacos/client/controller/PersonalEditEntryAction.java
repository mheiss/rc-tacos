package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.RosterEntryForm;
import at.rc.tacos.model.RosterEntry;

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

		//open the editor
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();

		//get the parent and the window shell
		RosterEntryForm window = new RosterEntryForm(parent,entry);
		window.getShell().setVisible(false);
		window.create();

		//get the shell and resize
		Shell myShell = window.getShell();
		myShell.setSize(500, 600);

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

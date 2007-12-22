package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

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
		RosterEntryForm form = new RosterEntryForm(entry);
		form.open();
	}
}

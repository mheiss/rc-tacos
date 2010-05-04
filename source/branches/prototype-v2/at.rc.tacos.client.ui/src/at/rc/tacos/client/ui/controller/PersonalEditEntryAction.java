package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.ui.dialog.RosterEntryDialog;
import at.rc.tacos.platform.model.RosterEntry;

/**
 * Opens the editor to edit the selected entry
 * 
 * @author Michael
 */
public class PersonalEditEntryAction extends Action {

	private TableViewer viewer;

	/**
	 * Default class construtor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public PersonalEditEntryAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Dienstplaneintrag zu bearbeiten");
	}

	@Override
	public void run() {
		// get the selected entry
		ISelection selection = viewer.getSelection();
		RosterEntry entry = (RosterEntry) ((IStructuredSelection) selection).getFirstElement();

		// open the editor
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		// get the parent and the window shell
		RosterEntryDialog window = new RosterEntryDialog(parent, entry);
		window.open();
	}
}

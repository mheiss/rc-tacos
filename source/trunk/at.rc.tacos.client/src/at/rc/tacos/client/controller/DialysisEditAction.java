package at.rc.tacos.client.controller;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.view.DialysisForm;
import at.rc.tacos.platform.model.DialysisPatient;

/**
 * Opens the editor to edit the selected dialysis entry
 * @author b.thek
 */
public class DialysisEditAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public DialysisEditAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster, um den Dialyseeintrag zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		DialysisPatient dia = (DialysisPatient)((IStructuredSelection)selection).getFirstElement();
		
		//check if the object is currenlty locked
		String resultLockMessage = LockManager.sendLock(DialysisPatient.ID, dia.getId());
		
		//check the result of the lock
		if(resultLockMessage != null)
		{
			boolean forceEdit =  MessageDialog.openQuestion(
					Display.getCurrent().getActiveShell(), 
					"Information: Eintrag wird bearbeitet", 
					"Der Dialysepatient den Sie bearbeiten möchten wird bereits von "+ resultLockMessage+ " bearbeitet\n"+
					"Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"+
					"Möchten Sie den Eintrag trotzdem bearbeiten?");
			if(!forceEdit)
				return;
			//logg the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log("Der Eintrag "+dia+" wird trotz Sperrung durch "+resultLockMessage +" von "+username+" bearbeitet",Status.WARNING);
		}
	
		//edit the entry
		DialysisForm form = new DialysisForm(dia, false);
		form.open();
	}
}

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
import at.rc.tacos.client.view.TransportStatiForm;
import at.rc.tacos.platform.model.Transport;

/**
 * Opens the form to edit the transport stati
 * @author b.thek
 */
public class EditTransportStatusAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public EditTransportStatusAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Stati bearbeiten");
		setToolTipText("Öffnet ein Fenster um die Stati zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		
		//check if the object is currently locked
		String resultLockMessage = LockManager.sendLock(Transport.ID, transport.getTransportId());
		
		//check the result of the lock
		if(resultLockMessage != null)
		{
			boolean forceEdit =  MessageDialog.openQuestion(
					Display.getCurrent().getActiveShell(), 
					"Information: Eintrag wird bearbeitet", 
					"Der Transport den Sie bearbeiten möchten wird bereits von "+ resultLockMessage+ " bearbeitet\n"+
					"Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"+
					"Möchten Sie den Eintrag trotzdem bearbeiten?");
			if(!forceEdit)
				return;
			//log the override of the lock
			String username = SessionManager.getInstance().getLoginInformation().getUsername();
			Activator.getDefault().log("Der Eintrag "+transport+" wird trotz Sperrung durch "+resultLockMessage +" von "+username+" bearbeitet",Status.WARNING);
		}
		
		//open the editor
		TransportStatiForm form = new TransportStatiForm(transport);
		form.open();
	}
}

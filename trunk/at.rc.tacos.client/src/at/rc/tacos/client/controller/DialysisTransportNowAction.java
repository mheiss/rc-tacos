package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DialysisPatient;

/**
 * Opens a dialog to confirm and then create a Transport form the selected dialysis entry
 * @author b.thek
 */
public class DialysisTransportNowAction extends Action
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public DialysisTransportNowAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Transport jetzt durchführen");
		setToolTipText("Dialyseeintrag wird in Tabelle offene Transporte angezeigt");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected dialyse transport
		DialysisPatient dia = (DialysisPatient)((IStructuredSelection)selection).getFirstElement();
		//confirm the action
		boolean cancelConfirmed = MessageDialog.openQuestion(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				"Dialysetransport jetzt durchführen", "Möchten Sie den Dialysetransport wirklich jetzt durchführen?");
		if (!cancelConfirmed) 
			return;
		//set the time to now
		dia.setPlannedStartOfTransport(Calendar.getInstance().getTimeInMillis());
		//TODO neuen Transport anlegen- program status: outstanding
		NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, dia);
	}
}

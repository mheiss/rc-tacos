package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Sets the given transport status
 * @author b.thek
 */
public class SetTransportStatusAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	private int status;
	/** 
	 * Constructor to set the given transport status
	 * @param viewer the table viewer
	 * @param status the transport status to set
	 */
	public SetTransportStatusAction(TableViewer viewer, int status, String shownAs)
	{
		this.viewer = viewer;
		this.status = status;
		setText(shownAs);
		setToolTipText("Setzt den Transportstatus " +" " +shownAs);
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//create the time stamp
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		//set the status
		transport.addStatus(status, timestamp);
		
		if(status == TRANSPORT_STATUS_DESTINATION_FREE)
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
		
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

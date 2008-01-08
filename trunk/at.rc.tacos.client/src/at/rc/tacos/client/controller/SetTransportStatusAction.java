package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Sets the given transport status
 * @author b.thek
 */
public class SetTransportStatusAction extends Action
{
	//properties
	private TableViewer viewer;
	private int status;
	private String shownAs;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetTransportStatusAction(TableViewer viewer)
	{
		
	}
	
	/** 
	 * Constructor to set the given transport status
	 * @param viewer the table viewer
	 * @param status the transport status to set
	 */
	public SetTransportStatusAction(TableViewer viewer, int status, String shownAs)
	{
		this.viewer = viewer;
		this.status = status;
		this.shownAs = shownAs;
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
		System.out.println("___________SetTransportStatusAction, transport: " +transport);
		System.out.println("..status: " +status);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

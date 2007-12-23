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
	
	/**
	 * Default class construtor.
	 * @param viewer the table viewer
	 */
	public SetTransportStatusAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Transport zu bearbeiten");
	}
	
	/** 
	 * Constructor to set the given transport status
	 * @param viewer the table viewer
	 * @param status the transport status to set
	 */
	public SetTransportStatusAction(TableViewer viewer, int status)
	{
		this.viewer = viewer;
		this.status = status;
		setText("Eintrag bearbeiten");
		setToolTipText("Öffnet ein Fenster um den Transport zu bearbeiten");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//create the timestamp
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		//set the status
		transport.addStatus(status, timestamp);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

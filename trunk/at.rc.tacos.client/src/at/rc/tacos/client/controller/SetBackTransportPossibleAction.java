package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

/**
 * Sets the option back transport possible for the transport
 * @author b.thek
 */
public class SetBackTransportPossibleAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	private Transport transport;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetBackTransportPossibleAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Rücktransport möglich");
		setToolTipText("Setzt die Option Rücktransport möglich für diesen Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();

		transport.setBackTransport(!transport.isBackTransport());
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() 
    {
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		if(transport.isBackTransport())
			return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		else 
			return null;
    }
}

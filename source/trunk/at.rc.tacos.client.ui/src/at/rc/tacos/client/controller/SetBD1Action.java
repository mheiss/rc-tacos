package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.ImageFactory;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;

/**
 * Sets or clears the bd1 option for the transport
 * @author b.thek
 */
public class SetBD1Action extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	private Transport transport;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetBD1Action(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("BD1");
		setToolTipText("Setzt BD1 für diesen Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();

		transport.setBlueLight1(!transport.isBlueLight1());
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() 
    {
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		if(transport.isBlueLight1())
			return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		else 
			return null;
    }
}

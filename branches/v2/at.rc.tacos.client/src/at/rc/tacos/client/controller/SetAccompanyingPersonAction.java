package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

/**
 * Sets an accompanying persion for the transport
 * @author b.thek
 */
public class SetAccompanyingPersonAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	private Transport transport;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetAccompanyingPersonAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Begleitperson");
		setToolTipText("Setzt eine Begleitperson für diesen Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();

		transport.setAssistantPerson(!transport.isAssistantPerson());
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() 
    {
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		if(transport.isAssistantPerson())
			return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		else 
			return null;
    }
}

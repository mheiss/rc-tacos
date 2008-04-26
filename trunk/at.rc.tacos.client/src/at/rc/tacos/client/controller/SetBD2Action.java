package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Sets the bd2 option for the transport
 * @author b.thek
 */
public class SetBD2Action extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetBD2Action(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("BD2");
		setToolTipText("Setzt BD2 für diesen Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();

		transport.setBlueLightToGoal(true);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

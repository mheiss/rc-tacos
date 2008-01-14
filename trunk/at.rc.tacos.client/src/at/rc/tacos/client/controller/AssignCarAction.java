package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * Assigns the in the context menu selected car to the in the table viewer selected transport
 * @author b.thek
 */
public class AssignCarAction extends Action
{
	//properties
	private TableViewer viewer;
	private VehicleDetail vehicle;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public AssignCarAction(TableViewer viewer, VehicleDetail vehicle)
	{
		this.viewer = viewer;
		this.vehicle = vehicle;
		setText(vehicle.getVehicleName());
		setToolTipText("Weist dem markierten Transport das im Kontextmenü ausgewählte Fahrzeug zu");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//open the editor
		transport.setVehicleDetail(vehicle);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

package at.rc.tacos.client.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class VehicleTableAtStationAction extends Action implements ITransportStatus, IProgramStatus
{
	 private List<Transport> objectList = new ArrayList<Transport>();
	 
	//properties
	private TableViewer viewer;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleTableAtStationAction(TableViewer viewer)
	{
		this.viewer = viewer;
	}


	/**
	 * Returns the tooltip text for the action
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() 
	{
		return "S6: Fahrzeug eingerückt setzen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Fahrzeug eingerückt";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.vehicle");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run()
	{	
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected entry
		VehicleDetail detail = (VehicleDetail)((IStructuredSelection)selection).getFirstElement();
		
		objectList = ModelFactory.getInstance().getTransportManager().getJournalTransportsByVehicleAndStatusSix(detail.getVehicleName());
		//create a timestamp for the transport state S6
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		//set the transport status S6 for each to the list given transport and update the transport
		for(Transport transport : objectList)
		{
			transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION, timestamp );
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
		
		//set the vehicle status to green and update the vehicle
		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	}
}

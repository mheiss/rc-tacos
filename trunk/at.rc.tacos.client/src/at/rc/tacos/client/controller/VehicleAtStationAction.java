package at.rc.tacos.client.controller;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.client.view.VehicleForm;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

public class VehicleAtStationAction extends Action implements ITransportStatus, IProgramStatus
{
	 private List<Transport> objectList = new ArrayList<Transport>();
	 
	//properties
	private VehicleDetail detail;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleAtStationAction(VehicleDetail detail)
	{
		this.detail = detail;
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
		TransportManager transportManager = new TransportManager();
		objectList = transportManager.getJournalTransportsByVehicleAndStatusSix(detail.getVehicleName());
		System.out.println(".:.:.:.:.:VehicleAtStationAction, objectlist(0)-from city in run(): " +objectList.get(0).getFromStreet());
		//create a timestamp
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		for(Transport transport : objectList)
		{
			transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION, timestamp );
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
		
		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	}
}

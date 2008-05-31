package at.rc.tacos.client.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;

/**
 * Sets the given transport status
 * @author b.thek
 */
public class SetTransportStatusAction extends Action implements ITransportStatus, IProgramStatus
{
	//properties
	private TableViewer viewer;
	private int status;
	private Transport transport;
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
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		//create the time stamp
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		//set the status 
		transport.addStatus(status, timestamp);
		
		if(status == TRANSPORT_STATUS_DESTINATION_FREE)
		{
			Calendar cal = Calendar.getInstance();
			String now = MyUtils.timestampToString(cal.getTimeInMillis(), MyUtils.timeFormat);
			VehicleManager manager = ModelFactory.getInstance().getVehicleManager();
			VehicleDetail vehicle = manager.getVehicleByName(transport.getVehicleDetail().getVehicleName());
			transport.setProgramStatus(PROGRAM_STATUS_JOURNAL);
			vehicle.setLastDestinationFree(now + " " +transport.getToStreet() +"/" +transport.getToCity());
			NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, vehicle);
		}
		
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() 
    {
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		if(transport.getStatusMessages().containsKey(status))
		{
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else return null;
    }
}

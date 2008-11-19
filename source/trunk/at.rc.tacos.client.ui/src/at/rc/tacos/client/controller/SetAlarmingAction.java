package at.rc.tacos.client.controller;

import java.util.Calendar;

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
 * Sets or clears the alarming option and timestamp for the given alarming
 * @author b.thek
 */
public class SetAlarmingAction extends Action implements IProgramStatus
{
	private String type;
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public SetAlarmingAction(TableViewer viewer, String type)
	{
		this.viewer = viewer;
		this.type = type;
		setText(type);
		setToolTipText("Setzt eine Alarmierung für den Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();

        //Calendar to get the timestamp
        Calendar cal = Calendar.getInstance();
        
		if(type.equalsIgnoreCase("NA extern"))
		{
			if(transport.isEmergencyDoctorAlarming())
			{
				transport.setEmergencyDoctorAlarming(false);
				transport.settimestampNA(0);
			}
			else
			{
				transport.setEmergencyDoctorAlarming(true);
				transport.settimestampNA(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("RTH"))
		{
			if(transport.isHelicopterAlarming())
			{
				transport.setHelicopterAlarming(false);
				transport.settimestampRTH(0);
			}
			else
			{
				transport.setHelicopterAlarming(true);
				transport.settimestampRTH(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("DF/Inspektion"))
		{
			if(transport.isDfAlarming())
			{
				transport.setDfAlarming(false);
				transport.settimestampDF(0);
			}
			else
			{
				transport.setDfAlarming(true);
				transport.settimestampDF(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("BRKDT"))
		{
			if(transport.isBrkdtAlarming())
			{
				transport.setBrkdtAlarming(false);
				transport.settimestampBRKDT(0);
			}
			else
			{
				transport.setBrkdtAlarming(true);
				transport.settimestampBRKDT(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("FW"))
		{
			if(transport.isFirebrigadeAlarming())
			{
				transport.setFirebrigadeAlarming(false);
				transport.settimestampFW(0);
			}
			else
			{
				transport.setFirebrigadeAlarming(true);
				transport.settimestampFW(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("Polizei"))
		{
			if(transport.isPoliceAlarming())
			{
				transport.setPoliceAlarming(false);
				transport.settimestampPolizei(0);
			}
			else
			{
				transport.setPoliceAlarming(true);
				transport.settimestampPolizei(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("Bergrettung"))
		{
			if(transport.isMountainRescueServiceAlarming())
			{
				transport.setMountainRescueServiceAlarming(false);
				transport.settimestampBergrettung(0);
			}
			else
			{
				transport.setMountainRescueServiceAlarming(true);
				transport.settimestampBergrettung(cal.getTimeInMillis());
			}
		}
		else if(type.equalsIgnoreCase("KIT"))
		{
			if(transport.isKITAlarming())
			{
				transport.setKITAlarming(false);
				transport.settimestampKIT(0);
			}
			else
			{
				transport.setKITAlarming(true);
				transport.settimestampKIT(cal.getTimeInMillis());
			}
		}
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() 
    {
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport transport = (Transport)((IStructuredSelection)selection).getFirstElement();
		
		if(type.equalsIgnoreCase("NA extern"))
		{
			if(transport.isEmergencyDoctorAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("RTH"))
		{
			if(transport.isHelicopterAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("DF/Inspektion"))
		{
			if(transport.isDfAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("BRKDT"))
		{
			if(transport.isBrkdtAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("FW"))
		{
			if(transport.isFirebrigadeAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("Polizei"))
		{
			if(transport.isPoliceAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("Bergrettung"))
		{
			if(transport.isMountainRescueServiceAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else if(type.equalsIgnoreCase("KIT"))
		{
			if(transport.isKITAlarming())
				return ImageFactory.getInstance().getRegisteredImageDescriptor("vehicle.ready");
		}
		else return null;
		
		return null;
    }
}

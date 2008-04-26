package at.rc.tacos.client.controller;
import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Sets the alarming option and timestamp for the given alarming
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
			transport.setEmergencyDoctorAlarming(true);
			transport.settimestampNA(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("RTH"))
		{
			transport.setHelicopterAlarming(true);
			transport.settimestampRTH(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("DF/Inspektion"))
		{
			transport.setDfAlarming(true);
			transport.settimestampDF(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("BRKDT"))
		{
			transport.setBrkdtAlarming(true);
			transport.settimestampBRKDT(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("FW"))
		{
			transport.setFirebrigadeAlarming(true);
			transport.settimestampFW(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("Polizei"))
		{
			transport.setPoliceAlarming(true);
			transport.settimestampPolizei(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("Bergrettung"))
		{
			transport.setMountainRescueServiceAlarming(true);
			transport.settimestampBergrettung(cal.getTimeInMillis());
		}
		else if(type.equalsIgnoreCase("KIT"))
		{
			transport.setKITAlarming(true);
			transport.settimestampKIT(cal.getTimeInMillis());
		}
		transport.setAssistantPerson(true);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

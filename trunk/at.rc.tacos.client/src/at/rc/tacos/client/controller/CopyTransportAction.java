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
 * Duplicates the transport
 * @author b.thek
 */
public class CopyTransportAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public CopyTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Transport kopieren");
		setToolTipText("Dupliziert den ausgewählten Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport t1 = (Transport)((IStructuredSelection)selection).getFirstElement();
		//open the editor
		Transport t2 = new Transport(t1.getFromStreet(),t1.getFromCity(),t1.getPlanedLocation(),t1.getDateOfTransport(),t1.getPlannedStartOfTransport(),t1.getTransportPriority(),t1.getDirection());
		
		t2.setBackTransport(t1.isBackTransport());  	
    	t2.setPatient(t1.getPatient());
    	t2.setAssistantPerson(t1.isAssistantPerson());
    	t2.setAppointmentTimeAtDestination(t1.getAppointmentTimeAtDestination());
    	t2.setBlueLightToGoal(t1.isBlueLightToGoal());
    	t2.setBrkdtAlarming(t1.isBrkdtAlarming());
    	t2.setCallerDetail(t1.getCallerDetail());
    	t2.setDfAlarming(t1.isDfAlarming());
    	t2.setNotes(t1.getNotes());
    	t2.setEmergencyDoctorAlarming(t1.isEmergencyDoctorAlarming());
    	t2.setEmergencyPhone(t1.isEmergencyPhone());
    	t2.setFeedback(t1.getFeedback());
    	t2.setFirebrigadeAlarming(t1.isFirebrigadeAlarming());
    	t2.setHelicopterAlarming(t1.isHelicopterAlarming());
    	t2.setKindOfIllness(t1.getKindOfIllness());
    	t2.setKindOfTransport(t1.getKindOfTransport());
    	t2.setLongDistanceTrip(t1.isLongDistanceTrip());
    	t2.setMountainRescueServiceAlarming(t1.isMountainRescueServiceAlarming());
    	t2.setPlannedTimeAtPatient(t1.getPlannedTimeAtPatient());
    	t2.setPoliceAlarming(t1.isPoliceAlarming());
    	t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
    	t2.setToStreet(t1.getToStreet());
    	t2.setToCity(t1.getToCity());
    	
    	if(t1.getProgramStatus()== PROGRAM_STATUS_UNDERWAY)
    		t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
    	else
    		t2.setProgramStatus(t1.getProgramStatus());
    	
    	NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
	}
}

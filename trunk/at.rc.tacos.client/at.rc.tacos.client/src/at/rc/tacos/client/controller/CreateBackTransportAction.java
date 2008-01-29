package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.view.TransportForm;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

/**
 * Creates a backtransport for the selected transport
 * @author b.thek
 */
public class CreateBackTransportAction extends Action implements IProgramStatus
{
	//properties
	private TableViewer viewer;
	private String editingType;
	
	/**
	 * Default class constructor.
	 * @param viewer the table viewer
	 */
	public CreateBackTransportAction(TableViewer viewer)
	{
		this.viewer = viewer;
		setText("Rücktransport erstellen");
		setToolTipText("Erstellt den Rücktransport zum ausgewählten Transport");
	}
	
	@Override
	public void run()
	{
		//the selection
		ISelection selection = viewer.getSelection();
		//get the selected transport
		Transport t1 = (Transport)((IStructuredSelection)selection).getFirstElement();
		
		//create the back transport
		Transport t2 = new Transport();
		
		t2.setFromStreet(t1.getToStreet());//!! switch for the back transport
		t2.setFromCity(t1.getToCity());//!! switch for the back transport
		t2.setResponsibleStation(t1.getResponsibleStation());
		
		GregorianCalendar gcal = new GregorianCalendar();
		long now = gcal.getTimeInMillis();
		t2.setDateOfTransport(now);
		t2.setPlannedStartOfTransport(now);
		t2.setTransportPriority("D");
		t2.setDirection(0);
		
		t2.setBackTransport(t1.isBackTransport());  	
    	t2.setPatient(t1.getPatient());
    	t2.setAccompanyingPerson(t1.isAccompanyingPerson());
    	t2.setAppointmentTimeAtDestination(t1.getAppointmentTimeAtDestination());
    	t2.setBlueLightToGoal(t1.isBlueLightToGoal());
    	t2.setBrkdtAlarming(t1.isBrkdtAlarming());
    	t2.setCallerDetail(t1.getCallerDetail());
    	t2.setDfAlarming(t1.isDfAlarming());
    	t2.setDiseaseNotes(t1.getDiseaseNotes());
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
    	t2.setReceiveTime(now);
    	t2.setToStreet(t1.getFromStreet());//!! switch for the back transport
    	t2.setToCity(t1.getFromCity());//!! switch for the back transport
    	
    	t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
    	
    	NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);
		
		
		
		
		
	}
}

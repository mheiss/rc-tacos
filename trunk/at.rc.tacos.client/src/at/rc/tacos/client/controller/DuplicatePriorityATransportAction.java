package at.rc.tacos.client.controller;

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;


/**
 * The action that is triggered by creating a new transport
 * @author b.thek
 */
public class DuplicatePriorityATransportAction extends Action implements IProgramStatus
{
    private Transport t1;
    
    /**
     * Creates a new TransportAction.
     * @param entry the new transport
     */
    public DuplicatePriorityATransportAction(Transport transport) 
    {
        this.t1 = transport;
    }

    public void run() 
    {
    	Transport t2 = new Transport(t1.getFromStreet(),t1.getFromCity(),t1.getResponsibleStation(),t1.getDateOfTransport(),t1.getPlannedStartOfTransport(),t1.getTransportPriority(),t1.getDirection());
		
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
    	GregorianCalendar gcal = new GregorianCalendar();
    	long now = gcal.getTimeInMillis();
    	t2.setReceiveTime(now);
    	t2.setToStreet(t1.getToStreet());
    	t2.setToCity(t1.getToCity());
    	
    	t2.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
    	//TODO assign NEF

    	NetWrapper.getDefault().sendAddMessage(Transport.ID, t2);

    }
}
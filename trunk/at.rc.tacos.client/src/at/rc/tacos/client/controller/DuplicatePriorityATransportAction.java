package at.rc.tacos.client.controller;

import java.util.Calendar;
import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportPriority;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;


/**
 * The action that is triggered by creating a new transport
 * @author b.thek
 */
public class DuplicatePriorityATransportAction extends Action implements IProgramStatus
{
    private Transport transport;

    /**
     * Creates a new TransportAction.
     * @param entry the new transport
     */
    public DuplicatePriorityATransportAction(Transport transport) 
    {
        this.transport = transport;
    }

    public void run() 
    {
        //copy the transport
        Transport newTransport = new Transport();
        //reset the values for the second transport
        newTransport.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        newTransport.setTransportId(0);
        //mark transport number (no number for the nef)
        newTransport.setTransportNumber(Transport.TRANSPORT_NEF);
        //assig nef vehicle
        VehicleDetail nef = ModelFactory.getInstance().getVehicleList().getNEFVehicle();
        transport.setVehicleDetail(nef);
        newTransport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
        newTransport.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL);
        newTransport.getStatusMessages().clear();
        //date and time
        newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
        newTransport.setYear(Calendar.getInstance().get(Calendar.YEAR));
        newTransport.setDateOfTransport(transport.getDateOfTransport());
        newTransport.setAppointmentTimeAtDestination(transport.getAppointmentTimeAtDestination());
        newTransport.setPlannedStartOfTransport(transport.getPlannedStartOfTransport());
        newTransport.setPlannedTimeAtPatient(transport.getPlannedTimeAtPatient());
        //alarming
        newTransport.setHelicopterAlarming(transport.isHelicopterAlarming());
        newTransport.setPoliceAlarming(transport.isPoliceAlarming());
        newTransport.setAssistantPerson(transport.isAssistantPerson());
        newTransport.setBackTransport(transport.isBackTransport());
        newTransport.setBlueLightToGoal(transport.isBlueLightToGoal());
        newTransport.setBrkdtAlarming(transport.isBrkdtAlarming());
        newTransport.setFirebrigadeAlarming(transport.isFirebrigadeAlarming());
        newTransport.setDfAlarming(transport.isDfAlarming());
        newTransport.setEmergencyDoctorAlarming(transport.isEmergencyDoctorAlarming());
        newTransport.setEmergencyPhone(transport.isEmergencyPhone());
        newTransport.setLongDistanceTrip(transport.isLongDistanceTrip());
        newTransport.setMountainRescueServiceAlarming(transport.isMountainRescueServiceAlarming());
        //assert valid
        newTransport.setKindOfIllness(transport.getKindOfIllness());
        newTransport.setKindOfTransport(transport.getKindOfTransport());
        if(transport.getCallerDetail() != null)
            newTransport.setCallerDetail(transport.getCallerDetail());
        if(transport.getFeedback() != null)
            newTransport.setFeedback(transport.getFeedback());
        //destionation and target
        newTransport.setPlanedLocation(transport.getPlanedLocation());
        newTransport.setPatient(transport.getPatient());
        newTransport.setDirection(transport.getDirection());
        newTransport.setFromCity(transport.getFromCity());
        newTransport.setFromStreet(transport.getFromStreet());
        newTransport.setToCity(transport.getToCity());
        newTransport.setToStreet(transport.getToStreet());
        //add the new transport
        NetWrapper.getDefault().sendAddMessage(Transport.ID,newTransport);
    }
}
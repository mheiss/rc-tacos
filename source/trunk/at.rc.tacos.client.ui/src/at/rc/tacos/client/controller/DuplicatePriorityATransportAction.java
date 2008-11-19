package at.rc.tacos.client.controller;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportPriority;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;


/**
 * The action that is triggered by creating a new transport if priority A (1 NEF) is choosen
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

    @Override
	public void run() 
    {
        //copy the transport
        Transport newTransport = new Transport();
        
        //reset the values for the second transport
        newTransport.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        newTransport.setTransportId(0);      
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
        newTransport.setBlueLight1(transport.isBlueLight1());
        newTransport.setBrkdtAlarming(transport.isBrkdtAlarming());
        newTransport.setFirebrigadeAlarming(transport.isFirebrigadeAlarming());
        newTransport.setDfAlarming(transport.isDfAlarming());
        newTransport.setEmergencyDoctorAlarming(transport.isEmergencyDoctorAlarming());
        newTransport.setEmergencyPhone(transport.isEmergencyPhone());
        newTransport.setLongDistanceTrip(transport.isLongDistanceTrip());
        newTransport.setMountainRescueServiceAlarming(transport.isMountainRescueServiceAlarming());
        
        //assert valid
        if(transport.getKindOfIllness() != null)
        	newTransport.setKindOfIllness(transport.getKindOfIllness());
        if(transport.getKindOfTransport()!=null)
        	newTransport.setKindOfTransport(transport.getKindOfTransport());
        if(transport.getCallerDetail() != null)
            newTransport.setCallerDetail(transport.getCallerDetail());
        if(transport.getNotes() != null)
        	newTransport.setNotes(transport.getNotes());
        if(transport.getFeedback() != null)
        	newTransport.setFeedback(transport.getFeedback());
       
        //destination and target
        newTransport.setPlanedLocation(transport.getPlanedLocation());
        newTransport.setPatient(transport.getPatient());
        newTransport.setDirection(transport.getDirection());
        newTransport.setFromCity(transport.getFromCity());
        newTransport.setFromStreet(transport.getFromStreet());
        newTransport.setToCity(transport.getToCity());
        newTransport.setToStreet(transport.getToStreet());
        
        //assign NEF vehicle
        VehicleDetail nef = ModelFactory.getInstance().getVehicleManager().getNEFVehicle();
        newTransport.setVehicleDetail(nef);
        newTransport.setDisposedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
       
        //mark transport number (no number for the NEF)
        newTransport.setTransportNumber(Transport.TRANSPORT_NEF);
  
        //stati
		GregorianCalendar cal1 = new GregorianCalendar();
		long now = cal1.getTimeInMillis();
		newTransport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, now);
		newTransport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
	
        //add the new transport
        NetWrapper.getDefault().sendAddMessage(Transport.ID,newTransport);
    }
}
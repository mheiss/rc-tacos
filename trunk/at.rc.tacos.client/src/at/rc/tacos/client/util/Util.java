package at.rc.tacos.client.util;

import java.util.Calendar;

import org.eclipse.swt.graphics.Color;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.model.Transport;

/**
 * Contains convinient methods for common use
 * @author Michael
 */
public class Util implements IProgramStatus
{
	/**
	 * Returns the color object for the given RGB values
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return the color object
	 */
	public final static Color getColor(int r,int g,int b)
	{
		return new Color(null,r,g,b);
	}
	
	
	/**
	 * Returns a new (copied) transport
	 * @param Transport the transport to copy
	 */
	public final static Transport copyTransport(Transport t1)
	{

		//the new transport
        Transport t2 = new Transport();
        
		//copy the transport
        //reset the values for the second transport
        t2.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        t2.setTransportId(0);
        t2.setTransportNumber(0);
        t2.clearVehicleDetail();
        t2.setCreationTime(Calendar.getInstance().getTimeInMillis());
        if(t1.getProgramStatus() == PROGRAM_STATUS_PREBOOKING)
        	t2.setProgramStatus(PROGRAM_STATUS_PREBOOKING);
        if(t1.getProgramStatus() == PROGRAM_STATUS_OUTSTANDING || t1.getProgramStatus() == PROGRAM_STATUS_UNDERWAY)
        	t2.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
        t2.setTransportPriority(t1.getTransportPriority());
        t2.getStatusMessages().clear();
        //date and time
        t2.setYear(Calendar.getInstance().get(Calendar.YEAR));
        t2.setDateOfTransport(t1.getDateOfTransport());
        t2.setAppointmentTimeAtDestination(t1.getAppointmentTimeAtDestination());
        t2.setPlannedStartOfTransport(t1.getPlannedStartOfTransport());
        t2.setPlannedTimeAtPatient(t1.getPlannedTimeAtPatient());
        //alarming
        t2.setHelicopterAlarming(t1.isHelicopterAlarming());
        t2.setPoliceAlarming(t1.isPoliceAlarming());
        t2.setAssistantPerson(t1.isAssistantPerson());
        t2.setBackTransport(t1.isBackTransport());
        t2.setBlueLightToGoal(t1.isBlueLightToGoal());
        t2.setBrkdtAlarming(t1.isBrkdtAlarming());
        t2.setFirebrigadeAlarming(t1.isFirebrigadeAlarming());
        t2.setDfAlarming(t1.isDfAlarming());
        t2.setEmergencyDoctorAlarming(t1.isEmergencyDoctorAlarming());
        t2.setEmergencyPhone(t1.isEmergencyPhone());
        t2.setLongDistanceTrip(t1.isLongDistanceTrip());
        t2.setMountainRescueServiceAlarming(t1.isMountainRescueServiceAlarming());
        //assert valid
        if(t1.getKindOfIllness() != null)
        	t2.setKindOfIllness(t1.getKindOfIllness());
        if(t1.getKindOfTransport()!= null)
        	t2.setKindOfTransport(t1.getKindOfTransport());
        if(t1.getCallerDetail() != null)
            t2.setCallerDetail(t1.getCallerDetail());
        if(t1.getFeedback() != null)
            t2.setFeedback(t1.getFeedback());
        //destination and target
        if(t1.getPlanedLocation() != null)
        	t2.setPlanedLocation(t1.getPlanedLocation());
        if(t1.getPatient() != null)
        {
        	t2.setPatient(t1.getPatient());
        }
        	
        t2.setDirection(t1.getDirection());
        t2.setFromCity(t1.getFromCity());
        t2.setFromStreet(t1.getFromStreet());
        t2.setToCity(t1.getToCity());
        t2.setToStreet(t1.getToStreet());
        
        //return the new transport
        return t2;
	}
}

package at.rc.tacos.client.controller;

import java.util.Calendar;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Transport;

/**
 * This action creates a transport for a dialysis patient and sends a add request to the server
 * @author Michael
 */
public class CreateTransportFromDialysis extends Action
{
	//properties
	private DialysisPatient patient;
	private Calendar dateOfTransport;
	
	/**
	 * Default class constructor defining the dialysis patient for the transport
	 * @param patient the dialysis patient
	 * @param dateOfTransport the date when the transport should be sheduled for
	 */
	public CreateTransportFromDialysis(DialysisPatient patient,Calendar dateOfTransport)
	{
		this.patient = patient;
		this.dateOfTransport = dateOfTransport;
	}
	
	/**
	 * Creates the transport and sends the add request
	 */
	@Override
	public void run()
	{
		//In the dialysis patients is only the time stored, so we have to add the current year,month and day
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(patient.getPlannedStartOfTransport());
		//now add the current year,month and day
		start.set(Calendar.YEAR, dateOfTransport.get(Calendar.YEAR));
		start.set(Calendar.MONTH, dateOfTransport.get(Calendar.MONTH));
		start.set(Calendar.DAY_OF_MONTH, dateOfTransport.get(Calendar.DAY_OF_MONTH));
		//time at patient
		Calendar atPatient = Calendar.getInstance();
		atPatient.setTimeInMillis(patient.getPlannedTimeAtPatient());
		atPatient.set(Calendar.YEAR, dateOfTransport.get(Calendar.YEAR));
		atPatient.set(Calendar.MONTH, dateOfTransport.get(Calendar.MONTH));
		atPatient.set(Calendar.DAY_OF_MONTH, dateOfTransport.get(Calendar.DAY_OF_MONTH));
		//time at the destination
		Calendar atDestination = Calendar.getInstance();
		atDestination.setTimeInMillis(patient.getAppointmentTimeAtDialysis());
		atDestination.set(Calendar.YEAR, dateOfTransport.get(Calendar.YEAR));
		atDestination.set(Calendar.MONTH, dateOfTransport.get(Calendar.MONTH));
		atDestination.set(Calendar.DAY_OF_MONTH, dateOfTransport.get(Calendar.DAY_OF_MONTH));
		
		//create a new transport
		Transport newTransport = new Transport();
		newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		newTransport.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
		//the date time of the transport is the planed start of the transport
		newTransport.setDateOfTransport(dateOfTransport.getTimeInMillis());
		newTransport.setTransportPriority("C");
		//set the known fields of the dialyis patient
		newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
		newTransport.setFromStreet(patient.getFromStreet());
		newTransport.setFromCity(patient.getFromCity());
		newTransport.setToCity(patient.getToCity());
		newTransport.setToStreet(patient.getToStreet());
		newTransport.setPlannedStartOfTransport(start.getTimeInMillis());
		newTransport.setPlannedTimeAtPatient(atPatient.getTimeInMillis());
		newTransport.setAppointmentTimeAtDestination(atDestination.getTimeInMillis());
		newTransport.setAssistantPerson(patient.isAssistantPerson());
		newTransport.setBackTransport(false);
		newTransport.setPatient(patient.getPatient());
		newTransport.setPlanedLocation(patient.getLocation());
		newTransport.setKindOfIllness("Dialyse");
		newTransport.setKindOfTransport(patient.getKindOfTransport());
		//add the transport to the database
		NetWrapper.getDefault().sendAddMessage(Transport.ID, newTransport);
		
		//log
		Activator.getDefault().log("Automatically generated the transport "+newTransport+" for the dialyse patient "+patient,IStatus.INFO);
	}
}

package at.rc.tacos.web.container;

import java.util.Date;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Patient;

/**
 * Container 
 * @author Birgit
 * @version 1.0
 */
public class TransportsToContainer {
    private String fromStreet;
    private String fromCity;
    private Patient patient;
    private String toStreet;
    private String toCity;
    private boolean assistantPerson;
    private String notes;
    private String kindOfTransport;
    private Location plannedLocation;
    private Date plannedStartOfTransport;
    private Date plannedTimeAtPatient;
    private Date appointmentTimeAtDestination;
    
	public Location getPlannedLocation() {
		return plannedLocation;
	}
	public void setPlannedLocation(Location plannedLocation) {
		this.plannedLocation = plannedLocation;
	}
	public String getFromStreet() {
		return fromStreet;
	}
	public void setFromStreet(String fromStreet) {
		this.fromStreet = fromStreet;
	}
	public String getFromCity() {
		return fromCity;
	}
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getToStreet() {
		return toStreet;
	}
	public void setToStreet(String toStreet) {
		this.toStreet = toStreet;
	}
	public String getToCity() {
		return toCity;
	}
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}
	public boolean isAssistantPerson() {
		return assistantPerson;
	}
	public void setAssistantPerson(boolean assistantPerson) {
		this.assistantPerson = assistantPerson;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getKindOfTransport() {
		return kindOfTransport;
	}
	public void setKindOfTransport(String kindOfTransport) {
		this.kindOfTransport = kindOfTransport;
	}
	public Date getPlannedStartOfTransport() {
		return plannedStartOfTransport;
	}
	public void setPlannedStartOfTransport(Date plannedStartOfTransport) {
		this.plannedStartOfTransport = plannedStartOfTransport;
	}
	public Date getPlannedTimeAtPatient() {
		return plannedTimeAtPatient;
	}
	public void setPlannedTimeAtPatient(Date plannedTimeAtPatient) {
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}
	public Date getAppointmentTimeAtDestination() {
		return appointmentTimeAtDestination;
	}
	public void setAppointmentTimeAtDestination(Date appointmentTimeAtDestination) {
		this.appointmentTimeAtDestination = appointmentTimeAtDestination;
	}

	
}

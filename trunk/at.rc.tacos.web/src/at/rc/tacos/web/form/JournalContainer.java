package at.rc.tacos.web.form;

import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;

/**
 * Container 
 * @author Birgit
 * @version 1.0
 */
public class JournalContainer 
{
	private Location realLocation; //for the drop down
	private int transportNumber;
	private VehicleContainer vehicleContainer;
    private String fromStreet;
    private String fromCity;
    private Patient patient;
    private String toStreet;
    private String toCity;
    private String notes;
    private String kindOfTransport;
    //TODO S1 - S6
    private String disposedByUser;
    private String feedback;
    private CallerDetail caller;
    private Disease kindOfIllness;
    
   
    
	public VehicleContainer getVehicleContainer() {
		return vehicleContainer;
	}
	public void setVehicleContainer(VehicleContainer vehicleContainer) {
		this.vehicleContainer = vehicleContainer;
	}
	public int getTransportNumber() {
		return transportNumber;
	}
	public void setTransportNumber(int transportNumber) {
		this.transportNumber = transportNumber;
	}
	public String getDisposedByUser() {
		return disposedByUser;
	}
	public void setDisposedByUser(String disposedByUser) {
		this.disposedByUser = disposedByUser;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public CallerDetail getCaller() {
		return caller;
	}
	public void setCaller(CallerDetail caller) {
		this.caller = caller;
	}
	public Disease getKindOfIllness() {
		return kindOfIllness;
	}
	public void setKindOfIllness(Disease kindOfIllness) {
		this.kindOfIllness = kindOfIllness;
	}
	public Location getRealLocation() {
		return realLocation;
	}
	public void setRealLocation(Location realLocation) {
		this.realLocation = realLocation;
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
	

	
}

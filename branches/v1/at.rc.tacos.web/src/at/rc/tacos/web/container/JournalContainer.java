package at.rc.tacos.web.container;

import java.util.Date;

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
    private Date s1;
    private Date s2;
    private Date s3;
    private Date s4;
    private Date s5;
    private Date s6;
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
	public Date getS1() {
		return s1;
	}
	public void setS1(Date s1) {
		this.s1 = s1;
	}
	public Date getS2() {
		return s2;
	}
	public void setS2(Date s2) {
		this.s2 = s2;
	}
	public Date getS3() {
		return s3;
	}
	public void setS3(Date s3) {
		this.s3 = s3;
	}
	public Date getS4() {
		return s4;
	}
	public void setS4(Date s4) {
		this.s4 = s4;
	}
	public Date getS5() {
		return s5;
	}
	public void setS5(Date s5) {
		this.s5 = s5;
	}
	public Date getS6() {
		return s6;
	}
	public void setS6(Date s6) {
		this.s6 = s6;
	}

	
}

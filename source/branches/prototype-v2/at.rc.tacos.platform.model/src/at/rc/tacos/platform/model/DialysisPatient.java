package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.util.MyUtils;

/**
 * Contains the data of the dialysis patient.
 * 
 * @author b.thek
 */
public class DialysisPatient extends Lockable {

	private int id;
	private Patient patient;
	private Location location;
	private long plannedStartOfTransport;
	private long plannedTimeAtPatient;
	private long appointmentTimeAtDialysis;
	private long plannedStartForBackTransport;
	private long readyTime;
	private String fromStreet;
	private String fromCity;
	private String toStreet;
	private String toCity;
	private String insurance;
	private boolean stationary;
	private String kindOfTransport;
	private boolean assistantPerson;
	// flags when the transport is repeated
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	// this values are needed to automatically generated transports for dialysis
	// patients they store the last automatically generated transport date so
	// that for one day only one transport is generated (per direction)
	private long lastTransportDate;
	private long lastBackTransportDate;

	/**
	 * Default class constructor
	 */
	public DialysisPatient() {
		id = -1;
		insurance = "Versicherung unbekannt";
	}

	/**
	 * Default class constructor for a dialysis patient object.
	 * 
	 * @param patient
	 *            the patient for this transport
	 * @param location
	 *            the location who is responsible the transport
	 * @param plannedStartOfTransport
	 *            the start time
	 * @param plannedTimeAtPatient
	 *            the meeting time with the patient
	 * @param appointmentTimeAtDialysis
	 *            the time at the target location
	 */
	public DialysisPatient(Patient patient, Location location, long plannedStartOfTransport, long plannedTimeAtPatient, long appointmentTimeAtDialysis) {
		this();
		this.patient = patient;
		this.location = location;
		this.plannedStartOfTransport = plannedStartOfTransport;
		this.plannedTimeAtPatient = plannedTimeAtPatient;
		this.appointmentTimeAtDialysis = appointmentTimeAtDialysis;
	}

	/**
	 * Returns the human readable string for this <code>DialysisPatient</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("patient", patient);
		builder.append("OS", location);
		builder.append("Abf", MyUtils.timestampToString(plannedStartOfTransport, MyUtils.timeFormat));
		builder.append("BeiPat: " + MyUtils.timestampToString(plannedTimeAtPatient, MyUtils.timeFormat));
		builder.append("Termin: " + MyUtils.timestampToString(appointmentTimeAtDialysis, MyUtils.timeFormat));
		builder.append("RT: " + MyUtils.timestampToString(plannedStartForBackTransport, MyUtils.timeFormat));
		builder.append("fertig" + MyUtils.timestampToString(readyTime, MyUtils.timeFormat));
		builder.append("von", fromStreet + " / " + fromCity);
		builder.append("nach", toStreet + "/" + toCity);
		builder.append("TA: " + kindOfTransport);
		builder.append("BeglPers", assistantPerson);
		builder.append("Mo", monday);
		builder.append("Di", tuesday);
		builder.append("MI", wednesday);
		builder.append("DO", thursday);
		builder.append("FR", friday);
		builder.append("SA", saturday);
		builder.append("SO,", sunday);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>DialysisPatient</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link DialysisPatient#getId()}.
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(25, 45);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>DialysisPatient</code> instance is equal
	 * to the compared object.
	 * <p>
	 * The compared fields are {@link DialysisPatient#getId()}.
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		DialysisPatient dialysisPatient = (DialysisPatient) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, dialysisPatient.id);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return id;
	}

	@Override
	public Class<?> getLockedClass() {
		return DialysisPatient.class;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the internal transport number for this dialyse transport
	 * 
	 * @return the unique id of the transport
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the patient informations about this transport
	 * 
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * Returns the responsible location for the transport.
	 * 
	 * @return the responsible location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Returns the planned start time of the dialysis transport
	 * 
	 * @return the plannedStartOfTransport
	 */
	public long getPlannedStartOfTransport() {
		return plannedStartOfTransport;
	}

	/**
	 * @return the plannedTimeAtPatient
	 */
	public long getPlannedTimeAtPatient() {
		return plannedTimeAtPatient;
	}

	/**
	 * @return the appointmentTimeAtDialysis
	 */
	public long getAppointmentTimeAtDialysis() {
		return appointmentTimeAtDialysis;
	}

	/**
	 * Returns the planed time for the back transport
	 * 
	 * @return the plannedStartForBackTransport
	 */
	public long getPlannedStartForBackTransport() {
		return plannedStartForBackTransport;
	}

	/**
	 * Returns the time when the dialyse is finished
	 * 
	 * @return the readyTime
	 */
	public long getReadyTime() {
		return readyTime;
	}

	/**
	 * Returns the starting street, in most cases the adress of the patient.
	 * 
	 * @return the fromStreet
	 */
	public String getFromStreet() {
		return fromStreet;
	}

	/**
	 * Returns the starting city, in most cases the home town of the patient.
	 * 
	 * @return the fromCity
	 */
	public String getFromCity() {
		return fromCity;
	}

	/**
	 * Returns the destination streeet
	 * 
	 * @return the destination street
	 */
	public String getToStreet() {
		return toStreet;
	}

	/**
	 * Returns the destination city
	 * 
	 * @return the city
	 */
	public String getToCity() {
		return toCity;
	}

	/**
	 * Returns the name of the insurance for the given patient.
	 * 
	 * @return the name of the insurance
	 */
	public String getInsurance() {
		return insurance;
	}

	/**
	 * Returns the transport type for the given patient. The different types are
	 * defined in the <code>IKindOfTransport</code> inteface.
	 * 
	 * @see IKindOfTransport
	 * @return the transport type
	 */
	public String getKindOfTransport() {
		return kindOfTransport;
	}

	/**
	 * Returns wheter or not this patient is staying in the hospital
	 * 
	 * @return the stationary
	 */
	public boolean isStationary() {
		return stationary;
	}

	/**
	 * Returns whether or not this transport has a assistant Person.
	 * 
	 * @return true if the transport has a additonal assistant person.
	 */
	public boolean isAssistantPerson() {
		return assistantPerson;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isMonday() {
		return monday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isTuesday() {
		return tuesday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isWednesday() {
		return wednesday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isThursday() {
		return thursday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isFriday() {
		return friday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isSaturday() {
		return saturday;
	}

	/**
	 * Returns whether this transport should be sheduled for monday
	 * 
	 * @return true if the transport should be sheduled
	 */
	public boolean isSunday() {
		return sunday;
	}

	/**
	 * Returns the date when the last transport was generated for this patient.
	 * 
	 * @return the date of the last transport
	 */
	public long getLastTransportDate() {
		return lastTransportDate;
	}

	/**
	 * Returns the date when the last backtransport was generated for this
	 * patient.
	 * 
	 * @return the date of the last backtransport
	 */
	public long getLastBackTransporDate() {
		return lastBackTransportDate;
	}

	/**
	 * Sets the internal unique number for the dialyse patient transport.<br>
	 * This value is out of the database and reflects the primary key of the
	 * table.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the patient information for the dialyse transport.
	 * 
	 * @param patient
	 *            the patient information
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * Sets the location who is responsible for the dialyse transport.
	 * 
	 * @param location
	 *            the responsible station
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Sets the planned start time for the dialysis transport
	 * 
	 * @param plannedStartOfTransport
	 *            the plannedStartOfTransport to set
	 */
	public void setPlannedStartOfTransport(long plannedStartOfTransport) {
		this.plannedStartOfTransport = plannedStartOfTransport;
	}

	/**
	 * @param plannedTimeAtPatient
	 *            the plannedTimeAtPatient to set
	 */
	public void setPlannedTimeAtPatient(long plannedTimeAtPatient) {
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}

	/**
	 * Sets the time when the patient should be picked up.
	 * 
	 * @param appointmentTimeAtDialysis
	 *            the pickup time
	 */
	public void setAppointmentTimeAtDialysis(long appointmentTimeAtDialysis) {
		this.appointmentTimeAtDialysis = appointmentTimeAtDialysis;
	}

	/**
	 * Sets the planned time when the patient should be transported back home.
	 * 
	 * @param plannedStartForBackTransport
	 *            the time for the backtransport
	 */
	public void setPlannedStartForBackTransport(long plannedStartForBackTransport) {
		this.plannedStartForBackTransport = plannedStartForBackTransport;
	}

	/**
	 * Sets the time when the patient is ready.
	 * 
	 * @param readyTime
	 *            the time when the patient is ready.
	 */
	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}

	/**
	 * Sets the name of the street where the patient should be picked up
	 * 
	 * @param fromStreet
	 *            name of the street
	 */
	public void setFromStreet(String fromStreet) {
		this.fromStreet = fromStreet;
	}

	/**
	 * Sets the name of the city where the patient should be picked up
	 * 
	 * @param fromCity
	 *            the name of the street
	 */
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	/**
	 * Sets the name of the street where tha patient should be carried to.
	 * 
	 * @param toStreet
	 *            the name of the street
	 */
	public void setToStreet(String toStreet) {
		this.toStreet = toStreet;
	}

	/**
	 * Sets the name of the city where the patient should be carried to.
	 * 
	 * @param toCity
	 *            the name of the city
	 */
	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	/**
	 * Sets the insurance of the given patient.
	 * 
	 * @param insurance
	 *            the name of the insurance
	 */
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	/**
	 * Sets the kind of the transport for the given patient
	 * 
	 * @param kindOfTransport
	 *            the kind of the transport type.
	 */
	public void setKindOfTransport(String kindOfTransport) {
		this.kindOfTransport = kindOfTransport;
	}

	/**
	 * Sets the flag to indicate whether the patient will stay longer at the
	 * hospital
	 * 
	 * @param stationary
	 *            the stationary to set
	 */
	public void setStationary(boolean stationary) {
		this.stationary = stationary;
	}

	/**
	 * Sets the flag to indicate that this transport has a assistant person
	 * 
	 * @param assistantPerson
	 *            true if the transport has one
	 */
	public void setAssistantPerson(boolean assistantPerson) {
		this.assistantPerson = assistantPerson;
	}

	/**
	 * Shedules the transport of the dialyse patient for monday
	 * 
	 * @param monday
	 *            true if the transport should be sheduled for the day
	 */
	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Tuesday
	 * 
	 * @param tuesday
	 *            true if the transport should be sheduled for the day
	 */
	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Wednesday
	 * 
	 * @param wednesday
	 *            true if the transport should be sheduled for the day
	 */
	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Thursday
	 * 
	 * @param thursday
	 *            true if the transport should be sheduled for the day
	 */
	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Friday
	 * 
	 * @param friday
	 *            true if the transport should be sheduled for the day
	 */
	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Saturday
	 * 
	 * @param saturday
	 *            true if the transport should be sheduled for the day
	 */
	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	/**
	 * Shedules the transport of the dialyse patient for Sunday
	 * 
	 * @param sunday
	 *            true if the transport should be sheduled for the day
	 */
	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	/**
	 * Sets the date when the last transport was generated for this dialyse
	 * patient.<br>
	 * This date is important so that only one transport per day is generated.
	 * 
	 * @param lastTransportDate
	 *            the date of the last transport
	 */
	public void setLastTransportDate(long lastTransportDate) {
		this.lastTransportDate = lastTransportDate;
	}

	/**
	 * Sets the date when the last backtransport was generated for this dialyse
	 * patient.<br>
	 * This date is important so that only one backtransport per day is
	 * generated.
	 * 
	 * @param lastBackTransportDate
	 *            the date of the last backtransport
	 */
	public void setLastBackTransportDate(long lastBackTransportDate) {
		this.lastBackTransportDate = lastBackTransportDate;
	}
}

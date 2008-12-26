package at.rc.tacos.platform.model;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import at.rc.tacos.platform.iface.IDirectness;
import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportPriority;
import at.rc.tacos.platform.iface.ITransportStatus;

/**
 * Specifies the transport details
 * 
 * @author b.thek
 */
public class Transport extends Lockable implements ITransportPriority, IDirectness, ITransportStatus {

	public final static int TRANSPORT_CANCLED = -1;
	public final static int TRANSPORT_FORWARD = -2;
	public final static int TRANSPORT_NEF = -4;

	// properties
	private int transportId;
	private int year;
	private int transportNumber;

	// Info about the source
	private CallerDetail callerDetail;

	// transport info
	private String fromStreet;
	private String fromCity;
	private Patient patient;
	private String toStreet;
	private String toCity;
	private String kindOfTransport;
	private String transportPriority;
	private boolean longDistanceTrip;
	private int direction;

	// patient info
	private Disease kindOfIllness;
	private boolean backTransport;
	private boolean assistantPerson;
	private boolean emergencyPhone;
	private String feedback;

	// schedule information
	private long creationTime;
	private long dateOfTransport;
	private long plannedStartOfTransport;
	private long plannedTimeAtPatient;
	private long appointmentTimeAtDestination;

	// general informations
	private Location planedLocation;
	private String notes;
	private int programStatus;
	private String createdByUser;
	private String disposedByUser;

	// notification info
	private boolean emergencyDoctorAlarming;
	private boolean helicopterAlarming;
	private boolean blueLightToGoal;
	private boolean blueLight1;
	private boolean dfAlarming;
	private boolean brkdtAlarming;
	private boolean firebrigadeAlarming;
	private boolean mountainRescueServiceAlarming;
	private boolean policeAlarming;
	private boolean KITAlarming;

	// notification time stamps
	private long timestampNA;
	private long timestampRTH;
	private long timestampDF;
	private long timestampBRKDT;
	private long timestampFW;
	private long timestampPolizei;
	private long timestampBergrettung;
	private long timestampKIT;

	// vehicle and staff assigned
	private VehicleDetail vehicleDetail;

	// status messages
	private Map<Integer, Long> statusMessages;

	/**
	 * Default class constructors for a transport<br>
	 * The needed values should be accessed by the getter and setter methods
	 */
	public Transport() {
		transportId = 0;
		statusMessages = new TreeMap<Integer, Long>();
	}

	/**
	 * Constructor for a minimal Transport object.
	 * 
	 * @param fromStreet
	 *            the street to get the transport
	 * @param fromCity
	 *            the city to get the transport
	 * @param dateOfTransport
	 *            the date for the transport
	 * @param plannedStartOfTransport
	 *            the starting time of the transport
	 * @param transportPriority
	 *            the priority of the transport
	 * @param direction
	 *            the direction of the transport
	 */
	public Transport(String fromStreet, String fromCity, Location planedLocation, long dateOfTransport, long plannedStartOfTransport, String transportPriority, int direction) {
		this();
		setFromStreet(fromStreet);
		setFromCity(fromCity);
		setPlanedLocation(planedLocation);
		setDateOfTransport(dateOfTransport);
		setPlannedStartOfTransport(plannedStartOfTransport);
		setTransportPriority(transportPriority);
		setDirection(direction);
	}

	/**
	 * Creates a new transport instance and initializes with the values from the
	 * specified source transport.
	 * 
	 * @param sourceTransport
	 *            the transport to copy
	 * @param createdBy
	 *            the name of the user who creates the copied transport
	 * @return a new transport instance with the values from the source
	 *         transport
	 */
	public static Transport createTransport(Transport sourceTransport, String createdBy) {
		// the new transport
		Transport newTransport = new Transport();
		// copy the transport
		newTransport.setCreatedByUsername(createdBy);
		newTransport.setTransportId(0);
		newTransport.setTransportNumber(0);
		newTransport.clearVehicleDetail();
		newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
		if (sourceTransport.getProgramStatus() == IProgramStatus.PROGRAM_STATUS_PREBOOKING)
			newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_PREBOOKING);
		if (sourceTransport.getProgramStatus() == IProgramStatus.PROGRAM_STATUS_OUTSTANDING
				|| sourceTransport.getProgramStatus() == IProgramStatus.PROGRAM_STATUS_UNDERWAY)
			newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		newTransport.setTransportPriority(sourceTransport.getTransportPriority());
		newTransport.getStatusMessages().clear();
		// date and time
		newTransport.setYear(Calendar.getInstance().get(Calendar.YEAR));
		newTransport.setDateOfTransport(sourceTransport.getDateOfTransport());
		newTransport.setAppointmentTimeAtDestination(sourceTransport.getAppointmentTimeAtDestination());
		newTransport.setPlannedStartOfTransport(sourceTransport.getPlannedStartOfTransport());
		newTransport.setPlannedTimeAtPatient(sourceTransport.getPlannedTimeAtPatient());
		// alarming
		newTransport.setHelicopterAlarming(sourceTransport.isHelicopterAlarming());
		newTransport.setPoliceAlarming(sourceTransport.isPoliceAlarming());
		newTransport.setAssistantPerson(sourceTransport.isAssistantPerson());
		newTransport.setBackTransport(sourceTransport.isBackTransport());
		newTransport.setBlueLightToGoal(sourceTransport.isBlueLightToGoal());
		newTransport.setBrkdtAlarming(sourceTransport.isBrkdtAlarming());
		newTransport.setFirebrigadeAlarming(sourceTransport.isFirebrigadeAlarming());
		newTransport.setDfAlarming(sourceTransport.isDfAlarming());
		newTransport.setEmergencyDoctorAlarming(sourceTransport.isEmergencyDoctorAlarming());
		newTransport.setEmergencyPhone(sourceTransport.isEmergencyPhone());
		newTransport.setLongDistanceTrip(sourceTransport.isLongDistanceTrip());
		newTransport.setMountainRescueServiceAlarming(sourceTransport.isMountainRescueServiceAlarming());
		newTransport.setKindOfIllness(sourceTransport.getKindOfIllness());
		newTransport.setKindOfTransport(sourceTransport.getKindOfTransport());
		newTransport.setCallerDetail(sourceTransport.getCallerDetail());
		newTransport.setFeedback(sourceTransport.getFeedback());

		// destination and target
		newTransport.setPlanedLocation(sourceTransport.getPlanedLocation());
		newTransport.setPatient(sourceTransport.getPatient());
		newTransport.setDirection(sourceTransport.getDirection());
		newTransport.setFromCity(sourceTransport.getFromCity());
		newTransport.setFromStreet(sourceTransport.getFromStreet());
		newTransport.setToCity(sourceTransport.getToCity());
		newTransport.setToStreet(sourceTransport.getToStreet());

		// return the new transport
		return newTransport;
	}

	/**
	 * Creates a new <code>Transport</code> instance from an existing
	 * <code>Transport</code> instance and switches the address fields, all
	 * other fields are not changed.
	 * 
	 * @param sourceTransport
	 *            the source transport
	 * @param createdBy
	 *            the name of the user who creates the backtransport
	 * @return the new transport instance with switched address fields
	 */
	public static Transport createBackTransport(Transport sourceTransport, String createdBy) {
		Transport newTransport = new Transport();

		// reset the values for the second transport
		newTransport.setCreatedByUsername(createdBy);
		newTransport.setTransportId(0);
		newTransport.setTransportNumber(0);
		newTransport.clearVehicleDetail();
		newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
		newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		newTransport.setTransportPriority("D");
		newTransport.getStatusMessages().clear();
		newTransport.setDirection(0);

		// date and time
		newTransport.setYear(Calendar.getInstance().get(Calendar.YEAR));
		newTransport.setDateOfTransport(Calendar.getInstance().getTimeInMillis());
		newTransport.setPlannedStartOfTransport(Calendar.getInstance().getTimeInMillis());

		// alarming
		newTransport.setAssistantPerson(sourceTransport.isAssistantPerson());
		newTransport.setBackTransport(false);
		newTransport.setEmergencyPhone(sourceTransport.isEmergencyPhone());
		newTransport.setLongDistanceTrip(sourceTransport.isLongDistanceTrip());

		// assert valid
		newTransport.setKindOfIllness(sourceTransport.getKindOfIllness());
		newTransport.setKindOfTransport(sourceTransport.getKindOfTransport());
		newTransport.setFeedback(sourceTransport.getFeedback());

		// destination and target
		newTransport.setPlanedLocation(sourceTransport.getPlanedLocation());
		newTransport.setPatient(sourceTransport.getPatient());

		// switch the address for the back transport
		if (sourceTransport.getToStreet() != null & !sourceTransport.getToStreet().trim().equalsIgnoreCase(""))
			newTransport.setFromStreet(sourceTransport.getToStreet());
		else
			newTransport.setFromStreet("kein Eintrag");

		if (sourceTransport.getToCity() != null & !sourceTransport.getToCity().trim().equalsIgnoreCase(""))
			newTransport.setFromCity(sourceTransport.getToCity());
		else
			newTransport.setFromCity("-");

		newTransport.setToStreet(sourceTransport.getFromStreet());
		newTransport.setToCity(sourceTransport.getFromCity());
		return newTransport;
	}

	/**
	 * Creates a new transport instance from an existing {@link DialysisPatient}
	 * 
	 * @param patient
	 *            the dialysis patient to get the needed information
	 * @param createdBy
	 *            the name of the user who creates the copied transport
	 * @return the created transport
	 */
	public static Transport createTransport(DialysisPatient patient, String createdBy) {
		// the current date
		Calendar now = Calendar.getInstance();

		// only the hours and minutes are valid
		// we have to set the current year,month and day
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(patient.getPlannedStartOfTransport());
		start.set(Calendar.YEAR, now.get(Calendar.YEAR));
		start.set(Calendar.MONTH, now.get(Calendar.MONTH));
		start.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

		// time at patient
		Calendar atPatient = Calendar.getInstance();
		atPatient.setTimeInMillis(patient.getPlannedTimeAtPatient());
		atPatient.set(Calendar.YEAR, now.get(Calendar.YEAR));
		atPatient.set(Calendar.MONTH, now.get(Calendar.MONTH));
		atPatient.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

		// time at the destination
		Calendar atDestination = Calendar.getInstance();
		atDestination.setTimeInMillis(patient.getAppointmentTimeAtDialysis());
		atDestination.set(Calendar.YEAR, now.get(Calendar.YEAR));
		atDestination.set(Calendar.MONTH, now.get(Calendar.MONTH));
		atDestination.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

		// create a new transport
		Transport newTransport = new Transport();
		newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		newTransport.setCreatedByUsername(createdBy);
		newTransport.setNotes("*AUTOMATISCH GENERIERT*");

		// the date time of the transport is the planed start of the transport
		newTransport.setDateOfTransport(now.getTimeInMillis());
		newTransport.setTransportPriority("C");

		// set the known fields of the dialyis patient
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
		newTransport.setKindOfIllness(new Disease("Dialyse"));
		newTransport.setKindOfTransport(patient.getKindOfTransport());

		// Return the new instance
		return newTransport;
	}

	/**
	 * Creates a backtransport from an existing {@link DialysisPatient}. The
	 * address fields are switched in the returned transport, all other field
	 * are not changed.
	 * 
	 * @param patient
	 *            the dialysis patient to get the needed information
	 * @param createdBy
	 *            the name of the user who creates the copied transport
	 * @return the created transport
	 */
	public static Transport createBackTransport(DialysisPatient patient, String createdBy) {
		// the current date
		Calendar now = Calendar.getInstance();

		// only the hours and minutes are valid
		// we have to set the current year,month and day
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(patient.getPlannedStartForBackTransport());
		start.set(Calendar.YEAR, now.get(Calendar.YEAR));
		start.set(Calendar.MONTH, now.get(Calendar.MONTH));
		start.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

		// time at patient
		Calendar ready = Calendar.getInstance();
		ready.setTimeInMillis(patient.getReadyTime());
		ready.set(Calendar.YEAR, now.get(Calendar.YEAR));
		ready.set(Calendar.MONTH, now.get(Calendar.MONTH));
		ready.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

		// create a new transport
		Transport newTransport = new Transport();
		newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		newTransport.setCreatedByUsername(createdBy);
		newTransport.setNotes("*AUTOMATISCH GENERIERT*");

		// the date time of the transport is the planed start of the transport
		newTransport.setDateOfTransport(now.getTimeInMillis());
		newTransport.setTransportPriority("D");

		// set the known fields of the dialyis patient
		newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
		newTransport.setFromStreet(patient.getToStreet());// !
		newTransport.setFromCity(patient.getToCity());
		newTransport.setToCity(patient.getFromCity());
		newTransport.setToStreet(patient.getFromStreet());
		newTransport.setPlannedStartOfTransport(start.getTimeInMillis());
		newTransport.setPlannedTimeAtPatient(ready.getTimeInMillis());
		newTransport.setAssistantPerson(patient.isAssistantPerson());
		newTransport.setBackTransport(false);
		newTransport.setPatient(patient.getPatient());
		newTransport.setPlanedLocation(patient.getLocation());
		newTransport.setKindOfIllness(new Disease("Dialyse RT"));
		newTransport.setKindOfTransport(patient.getKindOfTransport());

		return newTransport;
	}

	/**
	 * Helper method to set a status for a transport
	 * 
	 * @param statusId
	 *            the status identification
	 * @param timestamp
	 *            the time stamp of the status
	 */
	public void addStatus(int statusId, long timestamp) {
		statusMessages.put(statusId, timestamp);
	}

	/**
	 * Helper method to remove a status for a transport
	 * 
	 * @param statusId
	 *            the status identification
	 */
	public void removeStatus(int statusId) {
		statusMessages.remove(statusId);
	}

	/**
	 * clears the vehicle (detach car from the transport)
	 */
	public void clearVehicleDetail() {
		this.vehicleDetail = null;
	}

	/**
	 * Returns an integer with the highest transport status of a transport
	 * returns -1 if no condition matches (should not be possible for transports
	 * with the program status 'underway'
	 */
	public int getMostImportantStatusMessageOfOneTransport() {
		// S9
		if (statusMessages.containsKey(TRANSPORT_STATUS_OTHER))
			return TRANSPORT_STATUS_OTHER;
		// S8
		if (statusMessages.containsKey(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA)) {
			// S4
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_DESTINATION))
				return TRANSPORT_STATUS_AT_DESTINATION;
			// S3
			if (statusMessages.containsKey(TRANSPORT_STATUS_START_WITH_PATIENT))
				return TRANSPORT_STATUS_START_WITH_PATIENT;
			// S2
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_PATIENT))
				return TRANSPORT_STATUS_AT_PATIENT;
			// S1
			if (statusMessages.containsKey(TRANSPORT_STATUS_ON_THE_WAY))
				return TRANSPORT_STATUS_ON_THE_WAY;
			// S0
			if (statusMessages.containsKey(TRANSPORT_STATUS_ORDER_PLACED))
				return TRANSPORT_STATUS_ORDER_PLACED;
			return -2;
		}
		// S7
		if (statusMessages.containsKey(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
			return TRANSPORT_STATUS_OUT_OF_OPERATION_AREA;
		// S6
		if (statusMessages.containsKey(TRANSPORT_STATUS_CAR_IN_STATION))
			return TRANSPORT_STATUS_CAR_IN_STATION;
		// S5
		if (statusMessages.containsKey(TRANSPORT_STATUS_DESTINATION_FREE))
			return TRANSPORT_STATUS_DESTINATION_FREE;
		// S4
		if (statusMessages.containsKey(TRANSPORT_STATUS_AT_DESTINATION))
			return TRANSPORT_STATUS_AT_DESTINATION;
		// S3
		if (statusMessages.containsKey(TRANSPORT_STATUS_START_WITH_PATIENT))
			return TRANSPORT_STATUS_START_WITH_PATIENT;
		// S2
		if (statusMessages.containsKey(TRANSPORT_STATUS_AT_PATIENT))
			return TRANSPORT_STATUS_AT_PATIENT;
		// S1
		if (statusMessages.containsKey(TRANSPORT_STATUS_ON_THE_WAY))
			return TRANSPORT_STATUS_ON_THE_WAY;
		// S0
		if (statusMessages.containsKey(TRANSPORT_STATUS_ORDER_PLACED))
			return TRANSPORT_STATUS_ORDER_PLACED;

		return -1;
	}

	/**
	 * Returns the human readable string for this <code>Transport</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", transportId);
		builder.append("transportNr", transportNumber);
		builder.append("patient", patient);
		builder.append("from", fromStreet + "/" + fromCity);
		builder.append("to", toStreet + "/" + toCity);
		builder.append("vehicle", vehicleDetail);
		builder.append("created", createdByUser);
		builder.append("disposed", disposedByUser);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Transport</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Transport#getTransportId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(53, 63);
		builder.append(transportId);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Transport</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link Transport#getTransportId()}
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
		Transport transport = (Transport) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(transportId, transport.transportId);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return transportId;
	}

	@Override
	public Class<?> getLockedClass() {
		return Transport.class;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the identification string of this transport
	 * 
	 * @return the transportId
	 */
	public int getTransportId() {
		return transportId;
	}

	/**
	 * Returns the year when the transport is scheduled for. The year has four
	 * digits. e.g 2008
	 * 
	 * @return the year.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Returns the transport number of this transport
	 * 
	 * @return the transport number
	 */
	public int getTransportNumber() {
		return transportNumber;
	}

	/*
	 * ------------------------------------------ Info about the source
	 * --------------------------------------------
	 */
	/**
	 * @return the callerDetail
	 */
	public CallerDetail getCallerDetail() {
		return callerDetail;
	}

	/*
	 * ------------------------------------------ transport info
	 * --------------------------------------------
	 */
	/**
	 * @return the fromStreet
	 */
	public String getFromStreet() {
		return fromStreet;
	}

	/**
	 * @return the fromCity
	 */
	public String getFromCity() {
		return fromCity;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * @return the toStreet
	 */
	public String getToStreet() {
		return toStreet;
	}

	/**
	 * @return the toCity
	 */
	public String getToCity() {
		return toCity;
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
	 * @return the transportPriority the transport priorities are shown in the
	 *         ITransportPriority Interface
	 */
	public String getTransportPriority() {
		return transportPriority;
	}

	/**
	 * @return the longDistanceTrip
	 */
	public boolean isLongDistanceTrip() {
		return longDistanceTrip;
	}

	/**
	 * @return the directness
	 */
	public int getDirection() {
		return direction;
	}

	/*
	 * ------------------------------------------ patient info
	 * --------------------------------------------
	 */
	/**
	 * @return the kindOfIllness
	 */
	public Disease getKindOfIllness() {
		return kindOfIllness;
	}

	/**
	 * @return the backTransport
	 */
	public boolean isBackTransport() {
		return backTransport;
	}

	/**
	 * Returns whether or not this transport has a assistant Person.
	 * 
	 * @return true if the transport has an additonal assistant person.
	 */
	public boolean isAssistantPerson() {
		return assistantPerson;
	}

	/**
	 * the patient wear a chip on the body which is connected to a special phone
	 * to alert the red cross sometimes a key for the door is deposited at the
	 * red cross station or the police station
	 * 
	 * @return the emergencyPhone
	 */
	public boolean isEmergencyPhone() {
		return emergencyPhone;
	}

	/**
	 * Returns whether or not this transport has feedback information
	 * 
	 * @return true if there in feedback information
	 */
	public boolean hasFeedback() {
		if (feedback == null || feedback.trim().isEmpty())
			return false;
		// we have feedback :)
		return true;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}

	/*
	 * ------------------------------------------ schedule information
	 * --------------------------------------------
	 */
	/**
	 * Returns the time stamp when this transport was created.
	 * 
	 * @return the creation time
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * @return the dateOfTransport
	 */
	public long getDateOfTransport() {
		return dateOfTransport;
	}

	/**
	 * @return the plannedStartOfTransportTime
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
	 * @return the appointmentTimeAtDestination
	 */
	public long getAppointmentTimeAtDestination() {
		return appointmentTimeAtDestination;
	}

	/*
	 * ------------------------------------------ alarming time stamps
	 * --------------------------------------------
	 */
	/**
	 * Returns the timestamp when the NA button was selected
	 * 
	 * @return the selection timestamp
	 */
	public long getTimestampNA() {
		return timestampNA;
	}

	/**
	 * Returns the time stamp when the RTH button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampRTH() {
		return timestampRTH;
	}

	/**
	 * Returns the time stamp when the DF button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampDF() {
		return timestampDF;
	}

	/**
	 * Returns the time stamp when the BRKDT button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampBRKDT() {
		return timestampBRKDT;
	}

	/**
	 * Returns the time stamp when the FW button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampFW() {
		return timestampFW;
	}

	/**
	 * Returns the time stamp when the Polizei button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampPolizei() {
		return timestampPolizei;
	}

	/**
	 * Returns the time stamp when the Bergrettung button was selected
	 * 
	 * @return the selection time stamp
	 */
	public long getTimestampBergrettung() {
		return timestampBergrettung;
	}

	/**
	 * Returns the timestamp when the KIT button was selected
	 * 
	 * @return the selection timestamp
	 */
	public long getTimestampKIT() {
		return timestampKIT;
	}

	/*
	 * ------------------------------------------ general information
	 * --------------------------------------------
	 */
	/**
	 * Returns the planned responsible location who will execute the transport.
	 * 
	 * @return the planed location
	 */
	public Location getPlanedLocation() {
		return planedLocation;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Returns whether or not this transport has notes
	 * 
	 * @return true if the transport has notes
	 */
	public boolean hasNotes() {
		if (notes == null || notes.trim().isEmpty())
			return false;
		// we have notes :)
		return true;
	}

	/**
	 * program status to state the view which should show the transport the
	 * possible program stati are defined in an interface
	 * 
	 * @return the current status of the transport
	 * @see IProgramStatus
	 */
	public int getProgramStatus() {
		return programStatus;
	}

	/**
	 * Returns the name of the user who disposed the transport
	 * 
	 * @return the username of the disposer
	 */
	public String getDisposedByUsername() {
		return disposedByUser;
	}

	/**
	 * Returns the name of the user who created the roster entry.
	 * 
	 * @return the username of the creator.
	 */
	public String getCreatedByUsername() {
		return createdByUser;
	}

	/*
	 * ------------------------------------------ notification info
	 * --------------------------------------------
	 */
	/**
	 * @return the emergencyDoctorAlarming
	 */
	public boolean isEmergencyDoctorAlarming() {
		return emergencyDoctorAlarming;
	}

	/**
	 * @return the helicopterAlarming
	 */
	public boolean isHelicopterAlarming() {
		return helicopterAlarming;
	}

	/**
	 * @return the blueLightToGoal
	 */
	public boolean isBlueLightToGoal() {
		return blueLightToGoal;
	}

	/**
	 * @return the blueLight1
	 */
	public boolean isBlueLight1() {
		return blueLight1;
	}

	/**
	 * df = 'Dienstführender'
	 * 
	 * @return the dfAlarming
	 */
	public boolean isDfAlarming() {
		return dfAlarming;
	}

	/**
	 * brkdt = 'Bezirksrettungskommandant'
	 * 
	 * @return the brkdtAlarming
	 */
	public boolean isBrkdtAlarming() {
		return brkdtAlarming;
	}

	/**
	 * @return the firebrigadeAlarming
	 */
	public boolean isFirebrigadeAlarming() {
		return firebrigadeAlarming;
	}

	/**
	 * @return the mountainRescueServiceAlarming
	 */
	public boolean isMountainRescueServiceAlarming() {
		return mountainRescueServiceAlarming;
	}

	/**
	 * @return the policeAlarming
	 */
	public boolean isPoliceAlarming() {
		return policeAlarming;
	}

	/**
	 * @return the KITAlarming
	 */
	public boolean isKITAlarming() {
		return KITAlarming;
	}

	/*
	 * ------------------------------------------ vehicle and staff assigned
	 * --------------------------------------------
	 */
	/**
	 * @return the vehicleDetail
	 */
	public VehicleDetail getVehicleDetail() {
		return vehicleDetail;
	}

	/*
	 * ------------------------------------------ status messages
	 * --------------------------------------------
	 */
	/**
	 * Returns a list containing all transport status messages that are set.
	 * 
	 * @return the statusMessages
	 */
	public Map<Integer, Long> getStatusMessages() {
		return statusMessages;
	}

	/*
	 * ------------------------------------- PROPERTIES OF A TRANSPORT
	 * -------------------------------------
	 */
	/**
	 * Sets the identification string of this transport
	 * 
	 * @param transportId
	 *            the transportId to set
	 * @throws IllegalArgumentException
	 *             if the id is negative
	 */
	public void setTransportId(int transportId) {
		this.transportId = transportId;
	}

	/**
	 * Sets the transport year, when the transport is scheduled. The year must
	 * have four digits.
	 */
	public void setYear(int year) {
		// the validation is a problem for the transport numbers at the turn of
		this.year = year;
	}

	/**
	 * Sets the transport number for this transport. A transport number of -1
	 * indicates that the transport is cancled and the number is free to reuse
	 * again.
	 * 
	 * @param transportNumber
	 *            the number of the transport
	 */
	public void setTransportNumber(int transportNumber) {
		this.transportNumber = transportNumber;
	}

	/*
	 * ------------------------------------------ Info about the source
	 * --------------------------------------------
	 */
	/**
	 * @param callerDetail
	 *            the callerDetail to set
	 */
	public void setCallerDetail(CallerDetail callerDetail) {
		this.callerDetail = callerDetail;
	}

	/*
	 * ------------------------------------------ transport info
	 * --------------------------------------------
	 */
	/**
	 * @param fromStreet
	 *            the fromStreet to set
	 */
	public void setFromStreet(String fromStreet) {
		this.fromStreet = fromStreet;
	}

	/**
	 * @param fromCity
	 *            the fromCity to set
	 */
	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	/**
	 * @param patient
	 *            the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * @param toStreet
	 *            the toStreet to set
	 */
	public void setToStreet(String toStreet) {
		this.toStreet = toStreet;
	}

	/**
	 * @param toCity
	 *            the toCity to set
	 */
	public void setToCity(String toCity) {
		this.toCity = toCity;
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
	 * @param transportPriority
	 *            the transportPriority to set
	 */
	public void setTransportPriority(String transportPriority) {
		this.transportPriority = transportPriority;
	}

	/**
	 * @param longDistanceTrip
	 *            the longDistanceTrip to set
	 */
	public void setLongDistanceTrip(boolean longDistanceTrip) {
		this.longDistanceTrip = longDistanceTrip;
	}

	/**
	 * @param direction
	 *            the directness to set
	 * @see IDirectness
	 */
	public void setDirection(int direction) {
		if (direction < 0 || direction > 6)
			throw new IllegalArgumentException("Invalid value for direction. Vaild values: 1,2,3,4,5,6");
		this.direction = direction;
	}

	/*
	 * ------------------------------------------ patient info
	 * --------------------------------------------
	 */
	/**
	 * @param kindOfIllness
	 *            the kindOfIllness to set
	 */
	public void setKindOfIllness(Disease kindOfIllness) {
		this.kindOfIllness = kindOfIllness;
	}

	/**
	 * @param backTransport
	 *            the backTransport to set
	 */
	public void setBackTransport(boolean backTransport) {
		this.backTransport = backTransport;
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
	 * @param emergencyPhone
	 *            the emergencyPhone to set
	 */
	public void setEmergencyPhone(boolean emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	/**
	 * @param feedback
	 *            the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	/*
	 * ------------------------------------------ schedule information
	 * --------------------------------------------
	 */
	/**
	 * Sets the time stamp when this transport was created.
	 * 
	 * @param creationTime
	 *            the creation time
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @param dateOfTransport
	 *            the dateOfTransport to set
	 */
	public void setDateOfTransport(long dateOfTransport) {
		this.dateOfTransport = dateOfTransport;
	}

	/**
	 * @param plannedStartOfTransport
	 *            the plannedStartOfTransport to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setPlannedStartOfTransport(long plannedStartOfTransport) {
		this.plannedStartOfTransport = plannedStartOfTransport;
	}

	/**
	 * @param plannedTimeAtPatient
	 *            the plannedTimeAtPatient to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setPlannedTimeAtPatient(long plannedTimeAtPatient) {
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}

	/**
	 * @param appointmentTimeAtDestination
	 *            the appointmentTimeAtDestination to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setAppointmentTimeAtDestination(long appointmentTimeAtDestination) {
		this.appointmentTimeAtDestination = appointmentTimeAtDestination;
	}

	/*
	 * ------------------------------------------ alarming time tamps
	 * --------------------------------------------
	 */
	/**
	 * Sets the time stamp when the NA button was selected
	 * 
	 * @param timestampNA
	 *            the selection time
	 */
	public void settimestampNA(long timestampNA) {
		this.timestampNA = timestampNA;
	}

	/**
	 * Sets the time tamp when the RTH button was selected
	 * 
	 * @param timestampRTH
	 *            the selection time
	 */
	public void settimestampRTH(long timestampRTH) {
		this.timestampRTH = timestampRTH;
	}

	/**
	 * Sets the time stamp when the DF button was selected
	 * 
	 * @param timestampDF
	 *            the selection time
	 */
	public void settimestampDF(long timestampDF) {
		this.timestampDF = timestampDF;
	}

	/**
	 * Sets the time stamp when the BRKDT button was selected
	 * 
	 * @param timestampBRKDT
	 *            the selection time
	 */
	public void settimestampBRKDT(long timestampBRKDT) {
		this.timestampBRKDT = timestampBRKDT;
	}

	/**
	 * Sets the time stamp when the FW button was selected
	 * 
	 * @param timestampFW
	 *            the selection time
	 */
	public void settimestampFW(long timestampFW) {
		this.timestampFW = timestampFW;
	}

	/**
	 * Sets the time stamp when the Polizei button was selected
	 * 
	 * @param timestampPolizei
	 *            the selection time
	 */
	public void settimestampPolizei(long timestampPolizei) {
		this.timestampPolizei = timestampPolizei;
	}

	/**
	 * Sets the time stamp when the Bergrettung button was selected
	 * 
	 * @param timestampBergrettung
	 *            the selection time
	 */
	public void settimestampBergrettung(long timestampBergrettung) {
		this.timestampBergrettung = timestampBergrettung;
	}

	/**
	 * Sets the time stamp when the KIT button was selected
	 * 
	 * @param timestampKIT
	 *            the selection time
	 */
	public void settimestampKIT(long timestampKIT) {
		this.timestampKIT = timestampKIT;
	}

	/*
	 * ------------------------------------------ general informations
	 * --------------------------------------------
	 */
	/**
	 * Sets the planned location for the transport.<br>
	 * The planned location must not do not have to be the real location
	 * 
	 * @param planedLocation
	 *            the responsibleStation to set
	 */
	public void setPlanedLocation(Location planedLocation) {
		this.planedLocation = planedLocation;
	}

	/**
	 * @param notes
	 *            the notes for the transport
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Sets the internal status of this transport.
	 * 
	 * @param programStatus
	 */
	public void setProgramStatus(int programStatus) {
		this.programStatus = programStatus;
	}

	/**
	 * Sets the name of the staff member who created the entry.
	 * 
	 * @param createdByUser
	 *            the user name of the creator
	 */
	public void setCreatedByUsername(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	/**
	 * Sets the name of the staff member who disposed the transport
	 * 
	 * @param disposedByUser
	 *            the user name of the disposer
	 */
	public void setDisposedByUsername(String disposedByUser) {
		this.disposedByUser = disposedByUser;
	}

	/*
	 * ------------------------------------------ notification infos
	 * --------------------------------------------
	 */
	/**
	 * @param emergencyDoctorAlarming
	 *            the emergencyDoctorAlarming to set
	 */
	public void setEmergencyDoctorAlarming(boolean emergencyDoctorAlarming) {
		this.emergencyDoctorAlarming = emergencyDoctorAlarming;
	}

	/**
	 * @param helicopterAlarming
	 *            the helicopterAlarming to set
	 */
	public void setHelicopterAlarming(boolean helicopterAlarming) {
		this.helicopterAlarming = helicopterAlarming;
	}

	/**
	 * @param blueLightToGoal
	 *            the blueLightToGoal to set
	 */
	public void setBlueLightToGoal(boolean blueLightToGoal) {
		this.blueLightToGoal = blueLightToGoal;
	}

	/**
	 * @param blueLight1
	 *            the blueLight1 to set
	 */
	public void setBlueLight1(boolean blueLight1) {
		this.blueLight1 = blueLight1;
	}

	/**
	 * @param dfAlarming
	 *            the dfAlarming to set
	 */
	public void setDfAlarming(boolean dfAlarming) {
		this.dfAlarming = dfAlarming;
	}

	/**
	 * @param brkdtAlarming
	 *            the brkdtAlarming to set
	 */
	public void setBrkdtAlarming(boolean brkdtAlarming) {
		this.brkdtAlarming = brkdtAlarming;
	}

	/**
	 * @param firebrigadeAlarming
	 *            the firebrigadeAlarming to set
	 */
	public void setFirebrigadeAlarming(boolean firebrigadeAlarming) {
		this.firebrigadeAlarming = firebrigadeAlarming;
	}

	/**
	 * @param mountainRescueServiceAlarming
	 *            the mountainRescueServiceAlarming to set
	 */
	public void setMountainRescueServiceAlarming(boolean mountainRescueServiceAlarming) {
		this.mountainRescueServiceAlarming = mountainRescueServiceAlarming;
	}

	/**
	 * @param policeAlarming
	 *            the policeAlarming to set
	 */
	public void setPoliceAlarming(boolean policeAlarming) {
		this.policeAlarming = policeAlarming;
	}

	/**
	 * @param KITAlarming
	 *            the KITAlarming to set
	 */
	public void setKITAlarming(boolean KITAlarming) {
		this.KITAlarming = KITAlarming;
	}

	/*
	 * ------------------------------------------ vehicle and staff assigned
	 * --------------------------------------------
	 */
	/**
	 * @param vehicleDetail
	 *            the vehicleDetail to set
	 */
	public void setVehicleDetail(VehicleDetail vehicleDetail) {
		this.vehicleDetail = vehicleDetail;
	}
}

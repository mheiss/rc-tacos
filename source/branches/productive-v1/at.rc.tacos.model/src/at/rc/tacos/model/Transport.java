/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.model;

import java.util.Map;
import java.util.TreeMap;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportPriority;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.util.MyUtils;

/**
 * Specifies the transport details
 * 
 * @author b.thek
 */
public class Transport extends AbstractMessage implements ITransportPriority, IDirectness, ITransportStatus {

	// unique identification string
	public final static String ID = "transport";

	// Transport numbers
	public final static int TRANSPORT_CANCLED = -1;
	public final static int TRANSPORT_FORWARD = -2;
	public final static int TRANSPORT_NEF = -4;

	/**
	 * Indicates an error while operation with the transport DAO
	 */
	public final static int TRANSPORT_ERROR = -3;

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
		super(ID);
		// set default values
		transportId = 0;
		statusMessages = new TreeMap<Integer, Long>();
	}

	/**
	 * Constructor for a minimal Transport object.
	 * 
	 * @param fromStreet
	 *            the street to get the transport
	 * @param fromNumber
	 *            the house number
	 * @param fromCity
	 *            the city to get the transport
	 * @param responsibleStation
	 *            the station that is responsible
	 * @param dateOfTransport
	 *            the date for the transport
	 * @param plannedStartOfTransport
	 *            the starting time of the transport
	 * @param transportPriority
	 *            the priority of the transport
	 * @param directness
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
	 * Helper method to set a status for a transport
	 * 
	 * @param statusId
	 *            the status identification
	 * @param timestamp
	 *            the time stamp of the status
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void addStatus(int statusId, long timestamp) {
		// check the date
		if (!MyUtils.isValidDate(timestamp))
			throw new IllegalArgumentException("The given date for the transport status is not valid");
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
	 * Returns a string based description of the object
	 */
	@Override
	public String toString() {
		String transport;
		// common transport details
		transport = "von" + fromStreet + "/" + fromCity + ";";
		if (patient != null)
			transport = transport + "Patient: " + patient.getLastname() + " " + patient.getFirstname() + ";";
		transport = transport + "nach" + toStreet + "/" + toCity + ";";
		if (kindOfTransport != null)
			transport = transport + kindOfTransport + ";";
		transport = transport + ";" + "Pr: " + transportPriority + ";";
		if (longDistanceTrip)
			transport = transport + "FF" + ";";
		transport = transport + "Ri: " + direction + ";";
		if (kindOfIllness != null)
			transport = transport + "Erkr/Verl: " + kindOfIllness.getDiseaseName() + ";";
		if (backTransport)
			transport = transport + "RT möglich" + ";";
		if (assistantPerson)
			transport = transport + "BeglPers" + ";";
		if (emergencyPhone)
			transport = transport + "Rufhilfe" + ";";
		if (feedback != null && !feedback.isEmpty())
			transport = transport + "RM: " + feedback + ";";
		transport = transport + "Erstellt: " + MyUtils.timestampToString(creationTime, MyUtils.timeAndDateFormat) + ";" + "TrDatum: "
				+ MyUtils.timestampToString(dateOfTransport, MyUtils.dateFormat) + ";";
		if (plannedStartOfTransport != 0)
			transport = transport + "Abf: " + MyUtils.timestampToString(plannedStartOfTransport, MyUtils.timeFormat) + ";";
		if (plannedTimeAtPatient != 0)
			transport = transport + "Pat: " + MyUtils.timestampToString(plannedTimeAtPatient, MyUtils.timeFormat) + ";";
		if (appointmentTimeAtDestination != 0)
			transport = transport + "Term: " + MyUtils.timestampToString(appointmentTimeAtDestination, MyUtils.timeFormat) + ";";
		transport = transport + "OS: " + planedLocation.getLocationName() + ";";
		if (notes != null)
			transport = transport + "Anm: " + notes + ";";
		transport = transport + "LSD1: " + createdByUser + ";";
		if (disposedByUser != null)
			transport = transport + "LSD2: " + disposedByUser + ";";
		if (blueLight1)
			transport = transport + "BD1" + ";";
		if (blueLightToGoal)
			transport = transport + "BD2" + ";";
		// caller
		if (callerDetail != null)
			transport = transport + "Melder: " + callerDetail.getCallerName() + ";" + callerDetail.getCallerTelephoneNumber() + ";";
		// alarming
		if (emergencyDoctorAlarming)
			transport = transport + "NA: " + MyUtils.timestampToString(timestampNA, MyUtils.timeAndDateFormat) + ";";
		if (helicopterAlarming)
			transport = transport + "RTH: " + MyUtils.timestampToString(timestampRTH, MyUtils.timeAndDateFormat) + ";";
		if (dfAlarming)
			transport = transport + "DF: " + MyUtils.timestampToString(timestampDF, MyUtils.timeAndDateFormat) + ";";
		if (brkdtAlarming)
			transport = transport + "BRKDT: " + MyUtils.timestampToString(timestampBRKDT, MyUtils.timeAndDateFormat) + ";";
		if (firebrigadeAlarming)
			transport = transport + "FW: " + MyUtils.timestampToString(timestampFW, MyUtils.timeAndDateFormat) + ";";
		if (mountainRescueServiceAlarming)
			transport = transport + "BR: " + MyUtils.timestampToString(timestampBergrettung, MyUtils.timeAndDateFormat) + ";";
		if (policeAlarming)
			transport = transport + "Pol: " + MyUtils.timestampToString(timestampPolizei, MyUtils.timeAndDateFormat) + ";";
		if (KITAlarming)
			transport = transport + "KIT: " + MyUtils.timestampToString(timestampKIT, MyUtils.timeAndDateFormat) + ";";
		// vehicleDetail
		if (vehicleDetail != null) {
			transport = transport + "Fzg: " + vehicleDetail.getVehicleName() + ";";
			if (vehicleDetail.getDriver() != null)
				transport = transport + "F: " + vehicleDetail.getDriver().getUserName() + ";";
			if (vehicleDetail.getFirstParamedic() != null)
				transport = transport + "SaniI: " + vehicleDetail.getFirstParamedic().getUserName() + ";";
			if (vehicleDetail.getSecondParamedic() != null)
				transport = transport + "SaniII: " + vehicleDetail.getSecondParamedic().getUserName() + ";";
			if (vehicleDetail.getMobilePhone() != null)
				transport = transport + "H: " + vehicleDetail.getMobilePhone() + ";";
		}
		// status messages
		if (statusMessages != null) {
			// SO
			if (statusMessages.containsKey(TRANSPORT_STATUS_ORDER_PLACED))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_ORDER_PLACED), MyUtils.timeFormat);
			// S1
			if (statusMessages.containsKey(TRANSPORT_STATUS_ON_THE_WAY))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_ON_THE_WAY), MyUtils.timeFormat);
			// S2
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_PATIENT))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_AT_PATIENT), MyUtils.timeFormat);
			// S3
			if (statusMessages.containsKey(TRANSPORT_STATUS_START_WITH_PATIENT))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_START_WITH_PATIENT), MyUtils.timeFormat);
			// S4
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_DESTINATION))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_AT_DESTINATION), MyUtils.timeFormat);
			// S5
			if (statusMessages.containsKey(TRANSPORT_STATUS_DESTINATION_FREE))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_DESTINATION_FREE), MyUtils.timeFormat);
			// S6
			if (statusMessages.containsKey(TRANSPORT_STATUS_CAR_IN_STATION))
				transport = transport + MyUtils.timestampToString(statusMessages.get(TRANSPORT_STATUS_CAR_IN_STATION), MyUtils.timeFormat);
		}
		return transport;
	}

	/**
	 * Returns whether or not the given transports are equal.<br>
	 * Two <code>Transport</code> objects are equal if they have the same
	 * transport id.
	 * 
	 * @param true if the transports are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Transport other = (Transport) obj;
		if (transportId != other.transportId)
			return false;
		return true;
	}

	/**
	 * Returns the calculated hash code based on the transport id.<br>
	 * Two transports have the same hash code if the id is the same.
	 * 
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode() {
		return 31 + (transportId ^ (transportId >>> 32));
	}

	// GETTERS AND SETTERS
	/*
	 * ------------------------------------- PROPERTIES OF A TRANSPORT
	 * -------------------------------------
	 */

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
		// the year
		// if(year < Calendar.getInstance().get(Calendar.YEAR))
		// throw new IllegalArgumentException("Cannot set a date in the past");
		this.year = year;
	}

	/**
	 * Sets the transport number for this transport. A transport number of -1
	 * indicates that the transport is cancled and the number is free to reuse
	 * again.
	 * 
	 * @param number
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
		if (callerDetail == null)
			throw new IllegalArgumentException("The callerDetail cannot be null");
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
		if (fromStreet == null || fromStreet.trim().isEmpty())
			throw new IllegalArgumentException("The fromStreet cannot be null or empty");
		this.fromStreet = fromStreet;
	}

	/**
	 * @param fromCity
	 *            the fromCity to set
	 */
	public void setFromCity(String fromCity) {
		if (fromCity == null)
			throw new IllegalArgumentException("fromCity cannot be null");
		this.fromCity = fromCity;
	}

	/**
	 * @param patient
	 *            the patient to set
	 */
	public void setPatient(Patient patient) {
		if (patient == null)
			throw new IllegalArgumentException("The patient cannot be null");
		this.patient = patient;
	}

	/**
	 * @param toStreet
	 *            the toStreet to set
	 */
	public void setToStreet(String toStreet) {
		if (toStreet == null)
			throw new IllegalArgumentException("The toStreet cannot be null");
		this.toStreet = toStreet;
	}

	/**
	 * @param toCity
	 *            the toCity to set
	 */
	public void setToCity(String toCity) {
		if (toCity == null)
			throw new IllegalArgumentException("toCity cannot be null");
		this.toCity = toCity;
	}

	/**
	 * Sets the kind of the transport for the given patient
	 * 
	 * @param kindOfTransport
	 *            the kind of the transport type.
	 */
	public void setKindOfTransport(String kindOfTransport) {
		if (kindOfTransport == null)
			throw new IllegalArgumentException("The kind of transport cannot be null");
		this.kindOfTransport = kindOfTransport;
	}

	/**
	 * @param transportPriority
	 *            the transportPriority to set
	 */
	public void setTransportPriority(String transportPriority) {
		if (transportPriority == null || transportPriority.trim().isEmpty())
			throw new IllegalArgumentException("The transport priority cannot be null");
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
	 * @see IDirection
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
		if (kindOfIllness == null)
			throw new IllegalArgumentException("The kind of illness cannot be null");
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
	 * @param assistanPerson
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
		if (feedback == null)
			throw new IllegalArgumentException("The feedback cannot be null");
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
		if (!MyUtils.isValidDate(dateOfTransport))
			throw new IllegalArgumentException("The given date of Transport is not a valid date:");
		this.dateOfTransport = dateOfTransport;
	}

	/**
	 * @param plannedStartOfTransport
	 *            the plannedStartOfTransport to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setPlannedStartOfTransport(long plannedStartOfTransport) {
		if (!MyUtils.isValidDate(plannedStartOfTransport))
			throw new IllegalArgumentException("The given plannedStartOfTrnsport is not a valid date");
		this.plannedStartOfTransport = plannedStartOfTransport;
	}

	/**
	 * @param plannedTimeAtPatient
	 *            the plannedTimeAtPatient to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setPlannedTimeAtPatient(long plannedTimeAtPatient) {
		if (!MyUtils.isValidDate(plannedTimeAtPatient))
			throw new IllegalArgumentException("The given plannedTimeAtPatient is not a valid date");
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}

	/**
	 * @param appointmentTimeAtDestination
	 *            the appointmentTimeAtDestination to set
	 * @throws IllegalArgumentException
	 *             if the date is not valid
	 */
	public void setAppointmentTimeAtDestination(long appointmentTimeAtDestination) {
		if (!MyUtils.isValidDate(appointmentTimeAtDestination))
			throw new IllegalArgumentException("The given appointmentTimeAtDestination is not a valid date");
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
	 * @param responsibleStation
	 *            the responsibleStation to set
	 */
	public void setPlanedLocation(Location planedLocation) {
		if (planedLocation == null)
			throw new IllegalArgumentException("The planned location cannot be null");
		this.planedLocation = planedLocation;
	}

	/**
	 * @param notes
	 *            the notes for the transport
	 */
	public void setNotes(String notes) {
		if (notes == null)
			throw new IllegalArgumentException("The notes cannot be null");
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
	 * @param username
	 *            the user name of the creator
	 */
	public void setCreatedByUsername(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	/**
	 * Sets the name of the staff member who disposed the transport
	 * 
	 * @param username
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
		if (vehicleDetail == null)
			throw new IllegalArgumentException("The vehicle detail cannot be null");
		this.vehicleDetail = vehicleDetail;
	}
}

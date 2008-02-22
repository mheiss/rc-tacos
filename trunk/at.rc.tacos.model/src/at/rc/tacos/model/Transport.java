package at.rc.tacos.model;

import java.util.Calendar;
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
 * @author b.thek
 */
public class Transport extends AbstractMessage implements ITransportPriority,IDirectness, ITransportStatus
{
	//unique identification string
	public final static String ID = "transport";

	//Transport stati
	public final static int TRANSPORT_CANCLED = -1;
	public final static int TRANSPORT_FORWARD = -2;
	
	/**
	 * Indicates an error while operation with the transport DAO
	 */
	public final static int TRANSPORT_ERROR = -3;

	// properties
	private int transportId;
	private int year;
	private int transportNumber;

	// Infos about the source
	private CallerDetail callerDetail;

	// transport infos
	private String fromStreet;
	private String fromCity;
	private Patient patient;
	private String toStreet;
	private String toCity; 
	private String kindOfTransport;
	private String transportPriority;
	private boolean longDistanceTrip;
	private int direction;

	// patient infos
	private String kindOfIllness;
	private boolean backTransport;
	private boolean assistantPerson;
	private boolean emergencyPhone;
	private String feedback;

	//shedule information
	private long creationTime;
	private long dateOfTransport;
	private long plannedStartOfTransport;
	private long plannedTimeAtPatient;
	private long appointmentTimeAtDestination;

	//general informations
	private Location planedLocation;  
	private String notes;
	private int programStatus;
	private String createdByUser;

	//notification infos
	private boolean emergencyDoctorAlarming;
	private boolean helicopterAlarming;
	private boolean blueLightToGoal;
	private boolean dfAlarming;
	private boolean brkdtAlarming;
	private boolean firebrigadeAlarming;
	private boolean mountainRescueServiceAlarming;
	private boolean policeAlarming;

	//vehicle and staff assigned 
	private VehicleDetail vehicleDetail;

	//status messages
	private Map<Integer,Long> statusMessages;
	/**
	 * Default class constructors for a transport<br>
	 * The needed values should be accessed by the getter and setter methods
	 */
	public Transport()
	{
		super(ID);
		//set default values
		transportId = 0;
		statusMessages = new TreeMap<Integer, Long>();
	}

	/**
	 *  Constructor for a minimal Transport object.
	 *  @param fromStreet the street to get the transport
	 *  @param fromNumber the housenumber
	 *  @param fromCity the city to get the transport
	 *  @param responsibleStation the station that is responsible
	 *  @param dateOfTransport the date for the transport
	 *  @param plannedStartOfTransport the starting time of the transport
	 *  @param transportPriority the priority of the transport
	 *  @param directness the direction of the transport
	 */
	public Transport(String fromStreet,String fromCity,
			Location planedLocation,long dateOfTransport, long plannedStartOfTransport,
			String transportPriority,int direction)
	{
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
	 * @param statusId the status identification 
	 * @param timestamp the timestamp of the statu
	 * @throws IllegalArgumentException if the date is not valid
	 */
	public void addStatus(int statusId, long timestamp)
	{
		//check the date
		if(!MyUtils.isValidDate(timestamp))
			throw new IllegalArgumentException("The given date for the transport status is not valid");
		statusMessages.put(statusId, timestamp);
	}  

	/**
	 * clears the vehicle (detach car from the transport)
	 */
	public void clearVehicleDetail()
	{
		this.vehicleDetail = null;
	}

	/**
	 * Returns an integer with the highest transport status
	 * of a transport
	 * returns -1 if no condition matches (should not be possible for transports with the program status 'underway'
	 */
	public int getMostImportantStatusMessageOfOneTransport()
	{
		//S9
		if (statusMessages.containsKey(TRANSPORT_STATUS_OTHER))
			return TRANSPORT_STATUS_OTHER;
		//S8
		if (statusMessages.containsKey(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
		{
			//S4
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_DESTINATION))
				return TRANSPORT_STATUS_AT_DESTINATION;
			//S3
			if (statusMessages.containsKey(TRANSPORT_STATUS_START_WITH_PATIENT))
				return TRANSPORT_STATUS_START_WITH_PATIENT;
			//S2
			if (statusMessages.containsKey(TRANSPORT_STATUS_AT_PATIENT))
				return TRANSPORT_STATUS_AT_PATIENT;
			//S1
			if (statusMessages.containsKey(TRANSPORT_STATUS_ON_THE_WAY))
				return TRANSPORT_STATUS_ON_THE_WAY;
			//S0
			if (statusMessages.containsKey(TRANSPORT_STATUS_ORDER_PLACED))
				return TRANSPORT_STATUS_ORDER_PLACED;
			return -2;
		}
		//S7
		if (statusMessages.containsKey(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
			return TRANSPORT_STATUS_OUT_OF_OPERATION_AREA;
		//S6
		if (statusMessages.containsKey(TRANSPORT_STATUS_CAR_IN_STATION))
			return TRANSPORT_STATUS_CAR_IN_STATION;
		//S5
		if(statusMessages.containsKey(TRANSPORT_STATUS_DESTINATION_FREE))
			return TRANSPORT_STATUS_DESTINATION_FREE;
		//S4
		if (statusMessages.containsKey(TRANSPORT_STATUS_AT_DESTINATION))
			return TRANSPORT_STATUS_AT_DESTINATION;
		//S3
		if (statusMessages.containsKey(TRANSPORT_STATUS_START_WITH_PATIENT))
			return TRANSPORT_STATUS_START_WITH_PATIENT;
		//S2
		if (statusMessages.containsKey(TRANSPORT_STATUS_AT_PATIENT))
			return TRANSPORT_STATUS_AT_PATIENT;
		//S1
		if (statusMessages.containsKey(TRANSPORT_STATUS_ON_THE_WAY))
			return TRANSPORT_STATUS_ON_THE_WAY;
		//S0
		if (statusMessages.containsKey(TRANSPORT_STATUS_ORDER_PLACED))
			return TRANSPORT_STATUS_ORDER_PLACED;

		return -1;
	}

	/**
	 * Returns a string based description of the object
	 */
	@Override
	public String toString()
	{
		return transportId+","+fromStreet+","+fromCity+","+patient+","+toStreet+","+toCity;
	}

	/**
	 * Returns whether or not the given transports are equal.<br>
	 * Two <code>Transport</code> objects are equal if they have the 
	 * same transport id.
	 * @param true if the transports  are equal.
	 */
	@Override
	public boolean equals(Object obj)
	{
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
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode()
	{
		return 31  + (int) (transportId ^ (transportId >>> 32));
	} 

	//GETTERS AND SETTERS
	/* -------------------------------------
	 * PROPERTIES OF A TRANSPORT
	 * -------------------------------------*/

	/**
	 * Returns the identification string of this transport
	 * @return the transportId
	 */
	public int getTransportId() 
	{
		return transportId;
	}

	/**
	 * Returns the year when the transport is sheduled for.
	 * The year has four digits. e.g 2008
	 * @return the year.
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * Returns the transport number of this transport
	 * @return the transport number
	 */
	public int getTransportNumber()
	{
		return transportNumber;
	}

	/* ------------------------------------------
	 * Infos about the source
	 * --------------------------------------------*/
	/**
	 * @return the callerDetail
	 */
	public CallerDetail getCallerDetail() 
	{
		return callerDetail;
	}

	/* ------------------------------------------
	 * transport infos
	 * --------------------------------------------*/
	/**
	 * @return the fromStreet
	 */
	public String getFromStreet() 
	{
		return fromStreet;
	}

	/**
	 * @return the fromCity
	 */
	public String getFromCity() 
	{
		return fromCity;
	}

	/**
	 * @return the patient
	 */
	public Patient getPatient() 
	{
		return patient;
	}

	/**
	 * @return the toStreet
	 */
	public String getToStreet() 
	{
		return toStreet;
	}

	/**
	 * @return the toCity
	 */
	public String getToCity() 
	{
		return toCity;
	}

	/**
	 * Returns the transport type for the given patient.
	 * The different types are defined in the <code>IKindOfTransport</code> inteface.
	 * @see IKindOfTransport
	 * @return the transport type
	 */
	public String getKindOfTransport() 
	{
		return kindOfTransport;
	}

	/**
	 * @return the transportPriority
	 * the transport priorities are shown in the ITransportPriority Interface
	 */
	public String getTransportPriority() 
	{
		return transportPriority;
	}

	/**
	 * @return the longDistanceTrip
	 */
	public boolean isLongDistanceTrip() 
	{
		return longDistanceTrip;
	}

	/**
	 * @return the directness
	 */
	public int getDirection() 
	{
		return direction;
	}

	/* ------------------------------------------
	 * patient infos
	 * --------------------------------------------*/
	/**
	 * @return the kindOfIllness
	 */
	public String getKindOfIllness() 
	{
		return kindOfIllness;
	}

	/**
	 * @return the backTransport
	 */
	public boolean isBackTransport() 
	{
		return backTransport;
	}

	/**
	 * Returns whether or not this transport has a assistant Person.
	 * @return true if the transport has a additonal assistant person.
	 */
	public boolean isAssistantPerson()
	{
		return assistantPerson;
	}

	/**
	 * the patient wear a chip on the body which is connected to a special phone to alert the red cross
	 * sometimes a key for the door is deposited at the red cross station or the police station
	 * @return the emergencyPhone
	 */
	public boolean isEmergencyPhone() 
	{
		return emergencyPhone;
	}

	/**
	 * Returns whether or not this transport has feedback information
	 * @return true if there in feedback information
	 */
	public boolean hasFeedback()
	{
		if (feedback == null || feedback.trim().isEmpty())
			return false;
		//we have feedback :)
		return true;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() 
	{
		return feedback;
	}

	/* ------------------------------------------
	 * shedule information
	 * --------------------------------------------*/
	/**
	 * Returns the timestamp when this transport was created.
	 * @return the creation time
	 */
	public long getCreationTime() 
	{
		return creationTime;
	}

	/**
	 * @return the dateOfTransport
	 */
	public long getDateOfTransport() 
	{
		return dateOfTransport;
	}

	/**
	 * @return the plannedStartOfTransportTime
	 */
	public long getPlannedStartOfTransport() 
	{
		return plannedStartOfTransport;
	}

	/**
	 * @return the plannedTimeAtPatient
	 */
	public long getPlannedTimeAtPatient() 
	{
		return plannedTimeAtPatient;
	}

	/**
	 * @return the appointmentTimeAtDestination
	 */
	public long getAppointmentTimeAtDestination() 
	{
		return appointmentTimeAtDestination;
	}

	/* ------------------------------------------
	 * general informations
	 * --------------------------------------------*/
	/**
	 * Returns the planned responsible location who will execute the transport.
	 * @return the planed location
	 */
	public Location getPlanedLocation() 
	{
		return planedLocation;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() 
	{
		return notes;
	}

	/**
	 * Returns whether or not this transport has notes
	 * @return true if the transport has notes
	 */
	public boolean hasNotes()
	{
		if (notes == null || notes.trim().isEmpty())
			return false;
		//we have notes :)
		return true;
	}

	/**
	 * program status to state the view which should show the transport
	 * the possible program stati are defined in an interface
	 * @return the current status of the transport
	 * @see IProgramStatus
	 */
	public int getProgramStatus() 
	{
		return programStatus;
	}

	/**
	 * Returns the name of the user who created the roster entry.
	 * @return the username of the creator.
	 */
	public String getCreatedByUsername()
	{
		return createdByUser;
	}

	/* ------------------------------------------
	 * notification infos
	 * --------------------------------------------*/
	/**
	 * @return the emergencyDoctorAlarming
	 */
	public boolean isEmergencyDoctorAlarming() 
	{
		return emergencyDoctorAlarming;
	}

	/**
	 * @return the helicopterAlarming
	 */
	public boolean isHelicopterAlarming() 
	{
		return helicopterAlarming;
	}

	/**
	 * @return the blueLightToGoal
	 */
	public boolean isBlueLightToGoal() 
	{
		return blueLightToGoal;
	}

	/**
	 * df = 'Dienstführender'
	 * @return the dfAlarming
	 */
	public boolean isDfAlarming() 
	{
		return dfAlarming;
	}

	/**
	 * brkdt = 'Bezirksrettungskommandant'
	 * @return the brkdtAlarming
	 */
	public boolean isBrkdtAlarming() 
	{
		return brkdtAlarming;
	}

	/**
	 * @return the firebrigadeAlarming
	 */
	public boolean isFirebrigadeAlarming() 
	{
		return firebrigadeAlarming;
	}

	/**
	 * @return the mountainRescueServiceAlarming
	 */
	public boolean isMountainRescueServiceAlarming() 
	{
		return mountainRescueServiceAlarming;
	}

	/**
	 * @return the policeAlarming
	 */
	public boolean isPoliceAlarming() 
	{
		return policeAlarming;
	}

	/* ------------------------------------------
	 * vehicle and staff assigned
	 * --------------------------------------------*/ 
	/**
	 * @return the vehicleDetail
	 */
	public VehicleDetail getVehicleDetail() 
	{
		return vehicleDetail;
	}

	/* ------------------------------------------
	 * status messages
	 * --------------------------------------------*/ 
	/**
	 * Returns a list containing all transport status messages that are set. 
	 * @return the statusMessages
	 */
	public Map<Integer,Long> getStatusMessages() 
	{
		return statusMessages;
	}

	/* -------------------------------------
	 * PROPERTIES OF A TRANSPORT
	 * -------------------------------------*/
	/**
	 * Sets the identification string of this transport
	 * @param transportId the transportId to set
	 * @throws IllegalArgumentException if the id is negative
	 */
	public void setTransportId(int transportId) 
	{
		this.transportId = transportId;
	}

	/**
	 * Sets the transport year, when the transport is sheduled.
	 * The year must have four digits.
	 */
	public void setYear(int year)
	{
		if(year < Calendar.getInstance().get(Calendar.YEAR))
			throw new IllegalArgumentException("Cannot set a date in the past");
		this.year = year;
	}

	/**
	 * Sets the transport number for this transport.
	 * A transport number of -1 indicates that the transport is cancled
	 * and the number is free to reuse again.
	 * @param number the number of the transport
	 */
	public void setTransportNumber(int transportNumber)
	{
		this.transportNumber = transportNumber;
	}

	/* ------------------------------------------
	 * Infos about the source
	 * --------------------------------------------*/
	/**
	 * @param callerDetail the callerDetail to set
	 */
	public void setCallerDetail(CallerDetail callerDetail) 
	{
		if(callerDetail == null)
			throw new IllegalArgumentException("The callerDetail cannot be null");
		this.callerDetail = callerDetail;
	}

	/* ------------------------------------------
	 * transport infos
	 * --------------------------------------------*/
	/**
	 * @param fromStreet the fromStreet to set
	 */
	public void setFromStreet(String fromStreet) 
	{
		if(fromStreet == null || fromStreet.trim().isEmpty())
			throw new IllegalArgumentException("The fromStreet cannot be null or empty");
		this.fromStreet = fromStreet;
	}

	/**
	 * @param fromCity the fromCity to set
	 */
	public void setFromCity(String fromCity) 
	{
		if(fromCity == null)
			throw new IllegalArgumentException("fromCity cannot be null");
		this.fromCity = fromCity;
	}

	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient)
	{
		if(patient == null)
			throw new IllegalArgumentException("The patient cannot be null");
		this.patient = patient;
	}

	/**
	 * @param toStreet the toStreet to set
	 */
	public void setToStreet(String toStreet) 
	{
		if(toStreet == null)
			throw new IllegalArgumentException("The toStreet cannot be null");
		this.toStreet = toStreet;
	}

	/**
	 * @param toCity the toCity to set
	 */
	public void setToCity(String toCity) 
	{
		if(toCity == null)
			throw new IllegalArgumentException("toCity cannot be null");
		this.toCity = toCity;
	}

	/**
	 * Sets the kind of the transport for the given patient
	 * @param kindOfTransport the kind of the transport type.
	 */
	public void setKindOfTransport(String kindOfTransport) 
	{
		if(kindOfTransport == null)
			throw new IllegalArgumentException("The kind of transport cannot be null");
		this.kindOfTransport = kindOfTransport;
	}

	/**
	 * @param transportPriority the transportPriority to set
	 */
	public void setTransportPriority(String transportPriority) 
	{
		if(transportPriority == null || transportPriority.trim().isEmpty())
			throw new IllegalArgumentException("The transport priority cannot be null");
		this.transportPriority = transportPriority;
	}

	/**
	 * @param longDistanceTrip the longDistanceTrip to set
	 */
	public void setLongDistanceTrip(boolean longDistanceTrip) 
	{
		this.longDistanceTrip = longDistanceTrip;
	}

	/**
	 * @param direction the directness to set
	 * @see IDirection
	 */
	public void setDirection(int direction) 
	{
		if(direction < 0 || direction > 6)
			throw new IllegalArgumentException("Invalid value for direction. Vaild values: 1,2,3,4,5,6");
		this.direction = direction;
	}

	/* ------------------------------------------
	 * patient infos
	 * --------------------------------------------*/
	/**
	 * @param kindOfIllness the kindOfIllness to set
	 */
	public void setKindOfIllness(String kindOfIllness) 
	{
		if(kindOfIllness == null)
			throw new IllegalArgumentException("The kind of illness cannot be null");
		this.kindOfIllness = kindOfIllness;
	}

	/**
	 * @param backTransport the backTransport to set
	 */
	public void setBackTransport(boolean backTransport) 
	{
		this.backTransport = backTransport;
	}

	/**
	 * Sets the flag to indicate that this transport has a assistant person
	 * @param assistanPerson true if the transport has one
	 */
	public void setAssistantPerson(boolean assistantPerson)
	{
		this.assistantPerson = assistantPerson;
	}

	/**
	 * @param emergencyPhone the emergencyPhone to set
	 */
	public void setEmergencyPhone(boolean emergencyPhone) 
	{
		this.emergencyPhone = emergencyPhone;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) 
	{
		if(feedback == null)
			throw new IllegalArgumentException("The feedback cannot be null");
		this.feedback = feedback;
	}

	/* ------------------------------------------
	 * shedule information
	 * --------------------------------------------*/
	/**
	 * Sets the timestamp when this transport was created.
	 * @param creationTime the creation time
	 */
	public void setCreationTime(long creationTime) 
	{
		this.creationTime = creationTime;
	}

	/**
	 * @param dateOfTransport the dateOfTransport to set
	 */
	public void setDateOfTransport(long dateOfTransport) 
	{
		if(!MyUtils.isValidDate(dateOfTransport))
			throw new IllegalArgumentException("The given date of Transport is not a valid date:");
		this.dateOfTransport = dateOfTransport;
	}

	/**
	 * @param plannedStartOfTransport the plannedStartOfTransport to set
	 * @throws IllegalArgumentException if the date is not valid
	 */
	public void setPlannedStartOfTransport(long plannedStartOfTransport) 
	{
		if(!MyUtils.isValidDate(plannedStartOfTransport))
			throw new IllegalArgumentException("The given plannedStartOfTrnsport is not a valid date");
		this.plannedStartOfTransport = plannedStartOfTransport;
	}

	/**
	 * @param plannedTimeAtPatient the plannedTimeAtPatient to set
	 * @throws IllegalArgumentException if the date is not valid
	 */
	public void setPlannedTimeAtPatient(long plannedTimeAtPatient) 
	{
		if(!MyUtils.isValidDate(plannedTimeAtPatient))
			throw new IllegalArgumentException("The given plannedTimeAtPatient is not a valid date");
		this.plannedTimeAtPatient = plannedTimeAtPatient;
	}

	/**
	 * @param appointmentTimeAtDestination the appointmentTimeAtDestination to set
	 * @throws IllegalArgumentException if the date is not valid
	 */
	public void setAppointmentTimeAtDestination(long appointmentTimeAtDestination) 
	{
		if(!MyUtils.isValidDate(appointmentTimeAtDestination))
			throw new IllegalArgumentException("The given appointmentTimeAtDestination is not a valid date");
		this.appointmentTimeAtDestination = appointmentTimeAtDestination;
	}

	/* ------------------------------------------
	 * general informations
	 * --------------------------------------------*/
	/**
	 * Sets the planned location for the transport.<br>
	 * The planned location must not do not have to be the real location
	 * @param responsibleStation the responsibleStation to set
	 */
	public void setPlanedLocation(Location planedLocation)
	{
		if(planedLocation == null)
			throw new IllegalArgumentException("The planned location cannot be null");
		this.planedLocation = planedLocation;
	}

	/**
	 * @param notes the notes for the transport
	 */
	public void setNotes(String notes) 
	{
		if(notes == null)
			throw new IllegalArgumentException("The notes cannot be null");
		this.notes = notes;
	}

	/**
	 * Sets the internal status of this transport.
	 * @param programStatus
	 */
	public void setProgramStatus(int programStatus) 
	{
		this.programStatus = programStatus;
	}

	/**
	 * Sets the name of the staff member who created the entry.
	 * @param username the username of the creator
	 */
	public void setCreatedByUsername(String createdByUser) 
	{
		this.createdByUser = createdByUser;
	}

	/* ------------------------------------------
	 * notification infos
	 * --------------------------------------------*/
	/**
	 * @param emergencyDoctorAlarming the emergencyDoctorAlarming to set
	 */
	public void setEmergencyDoctorAlarming(boolean emergencyDoctorAlarming) 
	{
		this.emergencyDoctorAlarming = emergencyDoctorAlarming;
	}

	/**
	 * @param helicopterAlarming the helicopterAlarming to set
	 */
	public void setHelicopterAlarming(boolean helicopterAlarming) 
	{
		this.helicopterAlarming = helicopterAlarming;
	}  

	/**
	 * @param blueLightToGoal the blueLightToGoal to set
	 */
	public void setBlueLightToGoal(boolean blueLightToGoal) 
	{
		this.blueLightToGoal = blueLightToGoal;
	}

	/**
	 * @param dfAlarming the dfAlarming to set
	 */
	public void setDfAlarming(boolean dfAlarming) 
	{
		this.dfAlarming = dfAlarming;
	}

	/**
	 * @param brkdtAlarming the brkdtAlarming to set
	 */
	public void setBrkdtAlarming(boolean brkdtAlarming) 
	{
		this.brkdtAlarming = brkdtAlarming;
	}

	/**
	 * @param firebrigadeAlarming the firebrigadeAlarming to set
	 */
	public void setFirebrigadeAlarming(boolean firebrigadeAlarming) 
	{
		this.firebrigadeAlarming = firebrigadeAlarming;
	}

	/**
	 * @param mountainRescueServiceAlarming the mountainRescueServiceAlarming to set
	 */
	public void setMountainRescueServiceAlarming(boolean mountainRescueServiceAlarming) 
	{
		this.mountainRescueServiceAlarming = mountainRescueServiceAlarming;
	}

	/**
	 * @param policeAlarming the policeAlarming to set
	 */
	public void setPoliceAlarming(boolean policeAlarming) 
	{
		this.policeAlarming = policeAlarming;
	}

	/* ------------------------------------------
	 * vehicle and staff assigned
	 * --------------------------------------------*/ 
	/**
	 * @param vehicleDetail the vehicleDetail to set
	 */
	public void setVehicleDetail(VehicleDetail vehicleDetail) 
	{
		if(vehicleDetail == null)
			throw new IllegalArgumentException("The vehicle detail cannot be null");
		this.vehicleDetail = vehicleDetail;
	}

}
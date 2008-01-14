package at.rc.tacos.model;


import java.util.Calendar;

import at.rc.tacos.common.AbstractMessage;


/**
 * Contains the data of the dialysis patient.
 * @author b.thek
 */
//TODO set days of the dialysis transport
public class DialysisPatient extends AbstractMessage
{
    //unique identification string
    public final static String ID = "patient";

    private long patientId;
    private Patient patient;
    private String station;
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
    private boolean accompanyingPerson;
    
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    

    /**
     * Default class constructor
     */
    public DialysisPatient()
    {
        super(ID);
    }

    /**
     * Default class constructor for a dialysis patient object.
	 * @param id
	 * @param patientId
	 * @param firstname
	 * @param lastname
	 * @param station
	 * @param plannedStartOfTransport
	 * @param plannedTimeAtPatient
	 * @param appointmentTimeAtDialysis
	 */
	public DialysisPatient(String id, long patientId,
			String station, long plannedStartOfTransport,
			long plannedTimeAtPatient, long appointmentTimeAtDialysis) {
		super(id);
		this.patientId = patientId;
		this.station = station;
		this.plannedStartOfTransport = plannedStartOfTransport;
		this.plannedTimeAtPatient = plannedTimeAtPatient;
		this.appointmentTimeAtDialysis = appointmentTimeAtDialysis;
	}

    
   

    //METHODS
	
	/**
     * Convinience helper method to ensure a long value
     * is a valid date.
     * @param timestamp the value to check
     * @return true if the value is a date, otherwise false
     */
    private boolean isValidDate(long timestamp)
    {
        //create a calendar entry
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        //check the year
        if(cal.get(Calendar.YEAR) > 1960 && cal.get(Calendar.YEAR) < 2100)
            return true;
        //date out of range
        return false;
    }
    
    
    
    /**
     * Returns a string based description of the object.<br>
     * The returned values are patientId,firstname,lastname.
     * @return the description of the object
     */
    @Override
    public String toString()
    {
        return patientId+","+patient;
    }
    
    /**
     * Returns the calculated hash code based on the patient id.<br>
     * Two patients have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31  + (int) (patientId ^ (patientId >>> 32));
    }   
    
    /**
     * Returns whether the objects are equal or not.<br>
     * Two patients are equal if, and only if, the patient id is the same.
     * @return true if the id is the same otherwise false.
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
        final DialysisPatient other = (DialysisPatient) obj;
        if (patientId != other.patientId)
            return false;
        return true;
    }

    
    //GETTERS AND SETTERS
     /**
     * Returns the identification number of the patient
     * @return the patientId
     */
	public long getPatientId() {
		return patientId;
	}


	/**
     * @return the patient
     */
    public Patient getPatient() 
    {
        return patient;
    }

    /**
     * @param patient the patient to set
     */
    public void setPatient(Patient patient)
    {
//        if(patient == null)
//            throw new IllegalArgumentException("The patient cannot be null");
        this.patient = patient;
    }

	/**
	 * Returns the responsible station of the DialysisPatient
	 * @return the station
	 */
	public String getStation() 
	{
		return station;
	}

	/**
	 * Sets the station for the dialysisPatient
	 * @param station the station to set
	 * @throws IllegalArgumentException if the station is null or empty
	 */
	public void setStation(String station) 
	{
		if(station == null || station.trim().isEmpty())
            throw new IllegalArgumentException("Invalid station");
		this.station = station.trim();
	}

	/**
	 * Returns the planned start time of the dialysis transport
	 * @return the plannedStartOfTransport
	 */
	public long getPlannedStartOfTransport() 
	{
		return plannedStartOfTransport;
	}

	/**
	 * Sets the planned start time for the dialysis transport
	 * @param plannedStartOfTransport the plannedStartOfTransport to set
	 * @throws IllegalArgumentException if the planned start time is null or empty
	 */
	public void setPlannedStartOfTransport(long plannedStartOfTransport) 
	{
		 if(plannedStartOfTransport < 0)
	            throw new IllegalArgumentException("Date cannot be negative");
	        if(!isValidDate(plannedStartOfTransport))
	            throw new IllegalArgumentException("Date is out of range");
	        this.plannedStartOfTransport = plannedStartOfTransport;
	}

	 /**
     * @return the plannedTimeAtPatient
     */
    public long getPlannedTimeAtPatient() 
    {
        return plannedTimeAtPatient;
    }

    /**
     * @param plannedTimeAtPatient the plannedTimeAtPatient to set
     */
    public void setPlannedTimeAtPatient(long plannedTimeAtPatient) 
    {
        if(plannedTimeAtPatient < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(plannedTimeAtPatient))
            throw new IllegalArgumentException("Date is out of range");
        this.plannedTimeAtPatient = plannedTimeAtPatient;
    }

    /**
     * @return the appointmentTimeAtDialysis
     */
    public long getAppointmentTimeAtDialysis() 
    {
        return appointmentTimeAtDialysis;
    }

    /**
     * @param appointmentTimeAtDialysis the appointmentTimeAtDialysis to set
     */
    public void setAppointmentTimeAtDialysis(long appointmentTimeAtDialysis) 
    {
        if(appointmentTimeAtDialysis < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(appointmentTimeAtDialysis))
            throw new IllegalArgumentException("Date is out of range");
        this.appointmentTimeAtDialysis = appointmentTimeAtDialysis;
    }
    
    
    
    /**
     * @return the accompanyingPerson
     */
    public boolean isAccompanyingPerson() 
    {
        return accompanyingPerson;
    }

    /**
     * @param accompanyingPerson the accompanyingPerson to set
     */
    public void setAccompanyingPerson(boolean accompanyingPerson) 
    {
        this.accompanyingPerson = accompanyingPerson;
    }
    
    
    

    
    /**
     * @return the plannedStartForBackTransport
     */
    public long getplannedStartForBackTransport() 
    {
        return plannedStartForBackTransport;
    }

    /**
     * @param plannedStartForBackTransport the plannedStartForBackTransport to set
     */
    public void setplannedStartForBackTransport(long plannedStartForBackTransport) 
    {
        if(plannedStartForBackTransport < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(plannedStartForBackTransport))
            throw new IllegalArgumentException("Date is out of range");
        this.plannedStartForBackTransport = plannedStartForBackTransport;
    }
    
    
    /**
     * @return the readyTime
     */
    public long getreadyTime() 
    {
        return readyTime;
    }

    /**
     * @param readyTime the readyTime to set
     */
    public void setreadyTime(long readyTime) 
    {
        if(readyTime < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(readyTime))
            throw new IllegalArgumentException("Date is out of range");
        this.readyTime = readyTime;
    }
    
    
    /**
     * @return the fromStreet
     */
    public String getFromStreet() 
    {
        return fromStreet;
    }

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
     * @return the fromCity
     */
    public String getFromCity() 
    {
        return fromCity;
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
     * @return the insurance
     */
    public String getInsurance() 
    {
        return insurance;
    }

    /**
     * @param fromCity the insurance to set
     */
    public void setInsurance(String insurance) 
    {
        if(insurance == null)
            throw new IllegalArgumentException("insurance cannot be null");
        this.insurance = insurance;

    }
    
    
    /**
     * Possible: 'gehend', 'sitzend', 'liegend', Rollstuhl'
     * @return the kindOfTransport
     */
    public String getKindOfTransport() 
    {
        return kindOfTransport;
    }

    /**
     * @param kindOfTransport the kindOfTransport to set
     */
    public void setKindOfTransport(String kindOfTransport) 
    {
        if(kindOfTransport == null)
            throw new IllegalArgumentException("The kind of transport cannot be null");
        this.kindOfTransport = kindOfTransport;
    }
    
    
    /**
     * @return the stationary
     */
    public boolean isStationary() 
    {
        return stationary;
    }

    /**
     * @param stationary the stationary to set
     */
    public void setStationary(boolean stationary) 
    {
        this.stationary = stationary;

    }

    
    
    
	/**
	 * @return the iD
	 */
	public static String getID() 
	{
		return ID;
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

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}
}

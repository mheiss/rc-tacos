package at.rc.tacos.model;

import java.util.Calendar;
import at.rc.tacos.common.AbstractMessage;

/**
 * Represents one roster entry
 * @author b.thek
 */
public class RosterEntry extends AbstractMessage
{
    //unique identification string
    public final static String ID = "rosterEntry";
    
	private long rosterId;
	private StaffMember staffMember;
	private long plannedStartOfWork;
	private long plannedEndOfWork;
	private long realStartOfWork;
	private long realEndOfWork;
	private String station;
	private String competence;
	private String servicetype;
	private String rosterNotes;
	private boolean standby;	
	private boolean splitEntry;
	
	/**
	 * Default class construtor
	 */
	public RosterEntry()
	{
	    super(ID);
	}
	
	/**
	 * Constructor for a minimal roster entry object
	 * @param staffMember the person for this service
	 * @param servicetype the employee status
	 * @param competence the function of this person
	 * @param station the place to work
	 * @param plannedStartOfWork the planned time to start the service
	 * @param plannedEndOfWork the planned end of the service
	 */
	public RosterEntry(StaffMember staffMember,String servicetype,String competence,
	        String station,long plannedStartOfWork, long plannedEndOfWork)
	{
	    super(ID);
        setStaffMember(staffMember);
        setPlannedEndOfWork(plannedEndOfWork);
        setPlannedStartOfWork(plannedStartOfWork);
        setStation(station);
        setCompetence(competence);
        setServicetype(servicetype);
	}

	/**
	 * @param rosterId the identification of the entry
	 * @param staffMember the staff member for this entry
	 * @param plannedStartOfWork the planned star of work
	 * @param plannedEndOfWork the planned end of work
	 * @param realStartOfWork the real start of work
	 * @param realEndOfWork the real end of work
	 * @param station the roster station of
	 * @param competence the competence of this entry
	 * @param servicetype the service type of this entry
	 * @param rosterNotes the notes for this roster
	 * @param standby flag to show that the staff member is at home
	 */
	public RosterEntry(StaffMember staffMember,
			long plannedStartOfWork, long plannedEndOfWork,
			long realStartOfWork, long realEndOfWork, String station,
			String competence, String servicetype, String rosterNotes,
			boolean standby) 
	{
		super(ID);
		setStaffMember(staffMember);
		setPlannedEndOfWork(plannedEndOfWork);
		setPlannedStartOfWork(plannedStartOfWork);
		setRealStartOfWork(realStartOfWork);
		setRealEndOfWork(realEndOfWork);
		setStation(station);
		setCompetence(competence);
		setServicetype(servicetype);
		setRosterNotes(rosterNotes);
		setStandby(standby);
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
	 * Returns a string based description of the object
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
	    return rosterId+","+staffMember+","+station;
	}
	
	/**
	 * Returns wheter or not the given roster entries are equal.<br>
	 * Two <code>RosterEntry</code> objects are equal if the have the 
	 * same roster entry id.
	 * @param true if the roster entries are equal.
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
        final RosterEntry other = (RosterEntry) obj;
        if (rosterId != other.rosterId)
            return false;
        return true;
	}
	
    /**
     * Returns the calculated hash code based on the roster id.<br>
     * Two roster entries have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        return 31  + (int) (rosterId ^ (rosterId >>> 32));
    } 

	//GETTERS AND SETTERS
	/**
	 * Returns the identification string of this member
	 * @return the rosterId
	 */
	public long getRosterId() 
	{
		return rosterId;
	}

	/**
	 * Sets the identification string of this member
	 * @param rosterId the rosterId to set
	 * @throws IllegalArgumentException if the id is negative
	 */
	public void setRosterId(long rosterId) 
	{
	    if(rosterId < 0)
	        throw new IllegalArgumentException("The id cannot be negative");
		this.rosterId = rosterId;
	}


	/**
	 * Returns the member of this entry
	 * @return the staff member
	 */
	public StaffMember getStaffMember() 
	{
		return staffMember;
	}

	/**
	 * Sets the member of this entry
	 * @param staffMember the staff member to set
	 * @throws IllegalArgumentException if the staff member is null
	 */
	public void setStaffMember(StaffMember staffMember) 
	{
	    if(staffMember == null)
	        throw new IllegalArgumentException("The staff member cannot be null");
		this.staffMember = staffMember;
	}

	/**
	 * Returns the planned end of work for this roster entry.<br>
	 * @return the plannedEndOfWork
	 */
	public long getPlannedEndOfWork() 
	{
		return plannedEndOfWork;
	}

	/**
	 * Sets the planned end of work for this entry.<br>
	 * @param plannedEndOfWork the time and date of the planned end of work
	 * @throws IllegalArgumentException if the given value is not a valid date
	 */
	public void setPlannedEndOfWork(long plannedEndOfWork) 
	{
	    if(plannedEndOfWork < 0)
	        throw new IllegalArgumentException("Date cannot be negative");
	    if(!isValidDate(plannedEndOfWork))
	        throw new IllegalArgumentException("Date is out of range");
		this.plannedEndOfWork = plannedEndOfWork;
	}

	/**
	 * Returns the planned start of work in milliseconds.
	 * @return the planned start of work.
	 */
	public long getPlannedStartOfWork() 
	{
		return plannedStartOfWork;
	}

	/**
	 * Sets the planned start of work for this roster entry.
	 * @param plannedStartOfWork the plannedStartOfWork to set
	 * @throws IllegalArgumentException if the given value is not a valid date
	 */
	public void setPlannedStartOfWork(long plannedStartOfWork) 
	{
	       if(plannedStartOfWork < 0)
	            throw new IllegalArgumentException("Date cannot be negative");
	        if(!isValidDate(plannedStartOfWork))
	            throw new IllegalArgumentException("Date is out of range");
		this.plannedStartOfWork = plannedStartOfWork;
	}

	/**
	 * Returns the check in time of this roster entry.
	 * This time represents the real start time of the roster entry.
	 * @return the realStartOfWork
	 */
	public long getRealStartOfWork() 
	{
		return realStartOfWork;
	}

	/**
	 * Sets the check-in time for this roster entry
	 * @param realStartOfWork the realStartOfWork to set
	 * @throws IllegalArgumentException if the given value is not a valid date
	 */
	public void setRealStartOfWork(long realStartOfWork) 
	{
        if(realStartOfWork < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(realStartOfWork))
            throw new IllegalArgumentException("Date is out of range");
		this.realStartOfWork = realStartOfWork;
	}

	/**
	 * Returns the check-out time for this member.
	 * This time represents the real start time of the meber.
	 * @return the realEndOfWork
	 */
	public long getRealEndOfWork()
	{
		return realEndOfWork;
	}

	/**
	 * Sets the check-out time for this roster entry.
	 * @param realEndOfWork the realEndOfWork to set
	 * @throws IllegalArgumentException if the given value is not a valid date
	 */
	public void setRealEndOfWork(long realEndOfWork) 
	{
        if(realEndOfWork < 0)
            throw new IllegalArgumentException("Date cannot be negative");
        if(!isValidDate(realEndOfWork))
            throw new IllegalArgumentException("Date is out of range");
		this.realEndOfWork = realEndOfWork;
	}

	/**
	 * Returns the station where the staff member is working for this roster entry. <br>
	 * The possible station are:<br>
	 * <ul>
	 * <li>Bruck-Kapfenberg</li>
	 * <li>Bruck an der Mur</li>
	 * <li>Kapfenberg</li>
	 * <li>St. Marein</li>
	 * <li>Breitenau</li>
	 * <li>Thörl</li>
	 * <li>Turnau</li>
	 * <li>NEF</li>
	 * </ul>
	 * @return the station of th
	 */
	public String getStation() 
	{
		return station;
	}

	/**
	 * Sets the station for this staff member
	 * @param station the station to set
	 * @throws IllegalArgumentException if the station is null or empty
	 */
	public void setStation(String station) 
	{
	    if(station == null || station.trim().isEmpty())
	        throw new IllegalArgumentException("The station canno be null or empty");
		this.station = station;
	}

	/**
	 * Returns the competence of this staff member.<br>
	 * The possible competences are as follwed<br>
	 * <ul>
	 * <li>Fahrer</li>
	 * <li>Sanitäter</li>
	 * <li>Zweithelfer</li>
	 * <li>Notfallsanitäter</li>
	 * <li>Leitstellendisponent</li>
	 * <li>Dienstführender</li>
	 * <li>Inspektionsdienst</li>
	 * <li>Sonstiges</li>
	 * </ul>
	 * @return the competence
	 */
	public String getCompetence() 
	{
		return competence;
	}

	/**
	 * Sets the competences for this staff member.
	 * @param competence the competence to set
	 * @throws IllegalArgumentException if the competence is null or empty
	 */
	public void setCompetence(String competence) 
	{
	    if(competence == null || competence.trim().isEmpty())
	        throw new IllegalArgumentException("The competence canno be null or empty");
		this.competence = competence;
	}

	/**
	 * Returns the possible service type of this staff member.<br>
	 * The possible service types are:
	 * <ul>
	 * <li>Hauptamtlich</li>
     * <li>Freiwillig</li>
     * <li>Zivildienstleistender</li>
     * <li>Sonstiges</li>
	 * </ul>
	 * @return the service type
	 */
	public String getServicetype() 
	{
		return servicetype;
	}

	/**
	 * Sets the service type for this staff member
	 * @param servicetype the service type to set
     * @throws IllegalArgumentException if the competence is null or empty
	 */
	public void setServicetype(String servicetype) 
	{
	    if(servicetype == null || servicetype.trim().isEmpty())
	        throw new IllegalArgumentException("The service type cannot be null or empty");
		this.servicetype = servicetype;
	}

	/**
	 * Returns the notes for this roster entry.
	 * @return the rosterNotes
	 */
	public String getRosterNotes() 
	{
		return rosterNotes;
	}

	/**
	 * Sets the notes for this roster entry.
	 * @param rosterNotes the rosterNotes to set
	 * @throws IllegalArgumentException if the notes are null
	 */
	public void setRosterNotes(String rosterNotes) 
	{
	    if(rosterNotes == null)
	        throw new IllegalArgumentException("The notes canno be null");
	    this.rosterNotes = rosterNotes;
	}

	/**
	 * Returns wheter or not the member is currently at home
	 * and in standby. <br>
	 * The staff member is at home and must be called.
	 * @return the status of the staff member
	 */
	public boolean getStandby() 
	{
		return standby;
	}

	/**
	 * Sets whether or not this staff member is in standby
	 * @param standby the standby to set
	 */
	public void setStandby(boolean standby) 
	{ 
		this.standby = standby;
	}

    /**
     * Returns whether or not the roster entry is split up onto
     * more or one day.<br>
     * The start day and the end day is not the same.
     * @return true if the start and end day is not the same
     */
    public boolean isSplitEntry()
    {
        return splitEntry;
    }

    /**
     * Sets wheter this entry is split up over one or more days.
     * @param splitEntry the splitEntry to set
     */
    public void setSplitEntry(boolean splitEntry)
    {
        this.splitEntry = splitEntry;
    }	
}


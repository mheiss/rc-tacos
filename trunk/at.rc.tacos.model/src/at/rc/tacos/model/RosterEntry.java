package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.util.MyUtils;

/**
 * Represents one roster entry
 * @author b.thek
 */
public class RosterEntry extends AbstractMessage
{
	//unique identification string
	public final static String ID = "rosterEntry";

	//Properties    
	private int rosterId;
	private Location station;
	private StaffMember staffMember;
	private long plannedStartOfWork;
	private long plannedEndOfWork;
	private long realStartOfWork;
	private long realEndOfWork;

	private ServiceType serviceType;
	private Job job;

	private String createdByUser;
	private String rosterNotes;
	private boolean standby;	

	/**
	 * Default class construtor
	 */
	public RosterEntry()
	{
		super(ID);
		//set default values
		rosterId = -1;
	}

	/**
	 * Constructor for a minimal roster entry object
	 * @param staffMember the person for this service
	 * @param servicetype the employee status
	 * @param job of this person only for this roster entry
	 * @param station the place to work
	 * @param plannedStartOfWork the planned time to start the service
	 * @param plannedEndOfWork the planned end of the service
	 */
	public RosterEntry(StaffMember staffMember,
			ServiceType serviceType,Job job,Location station,
			long plannedStartOfWork, long plannedEndOfWork)
	{
		super(ID);
		setStaffMember(staffMember);
		setPlannedEndOfWork(plannedEndOfWork);
		setPlannedStartOfWork(plannedStartOfWork);
		setStation(station);
		setJob(job);
		setServicetype(serviceType);
	}

	/**
	 * @param rosterId the identification of the entry
	 * @param staffMember the staff member for this entry
	 * @param plannedStartOfWork the planned star of work
	 * @param plannedEndOfWork the planned end of work
	 * @param realStartOfWork the real start of work
	 * @param realEndOfWork the real end of work
	 * @param station the roster station of
	 * @param job of this entry
	 * @param servicetype the service type of this entry
	 * @param rosterNotes the notes for this roster
	 * @param standby flag to show that the staff member is at home
	 */
	public RosterEntry(StaffMember staffMember,
			long plannedStartOfWork, long plannedEndOfWork,
			long realStartOfWork, long realEndOfWork, 
			ServiceType serviceType,Job job,Location station,
			String rosterNotes, boolean standby) 
	{
		super(ID);
		setStaffMember(staffMember);
		setPlannedEndOfWork(plannedEndOfWork);
		setPlannedStartOfWork(plannedStartOfWork);
		setRealStartOfWork(realStartOfWork);
		setRealEndOfWork(realEndOfWork);
		setStation(station);
		setJob(job);
		setServicetype(serviceType);
		setRosterNotes(rosterNotes);
		setStandby(standby);
	}

	//METHODS	
	/**
	 * Returns a string based description of the object
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
		return rosterId+","+staffMember+","+station+","+MyUtils.timestampToString(plannedStartOfWork,MyUtils.dateFormat)+"-"+MyUtils.timestampToString(plannedEndOfWork,MyUtils.dateFormat);
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

	/**
	 * Returns wheter or not this entry has notes
	 * @return true if there are notes
	 */
	public boolean hasNotes()
	{
		if (rosterNotes == null)
			return false;
		if (rosterNotes.trim().isEmpty())
		{
			return false;
		}
		//we have notes :)
		return true;
	}

	//GETTERS AND SETTERS
	/**
	 * Returns the identification string of this member
	 * @return the rosterId
	 */
	public int getRosterId() 
	{
		return rosterId;
	}

	/**
	 * Sets the identification string of this member
	 * @param rosterId the rosterId to set
	 * @throws IllegalArgumentException if the id is negative
	 */
	public void setRosterId(int rosterId) 
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
		if(!MyUtils.isValidDate(plannedEndOfWork))
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
		if(!MyUtils.isValidDate(plannedStartOfWork))
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
		if(!MyUtils.isValidDate(realStartOfWork))
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
		if(!MyUtils.isValidDate(realEndOfWork))
			throw new IllegalArgumentException("Date is out of range");
		this.realEndOfWork = realEndOfWork;
	}

	/**
	 * Returns the station where the staff member is working for this roster entry. 
	 * @return the station of the duty
	 */
	public Location getStation() 
	{
		return station;
	}

	/**
	 * Sets the station for this staff member
	 * @param station the station to set
	 * @throws IllegalArgumentException if the station is null or empty
	 */
	public void setStation(Location station) 
	{
		if(station == null )
			throw new IllegalArgumentException("The station canno be null or empty");
		this.station = station;
	}

	/**
	 * Returns the job of this staff member.<br>
	 * @return the job of the roster entry.
	 */
	public Job getJob() 
	{
		return job;
	}

	/**
	 * Sets the job for this staff member.
	 * @param job the job to set
	 * @throws IllegalArgumentException if the job is null or empty
	 */
	public void setJob(Job job) 
	{
		if(job == null)
			throw new IllegalArgumentException("The job can not be null or empty");
		this.job = job;
	}

	/**
	 * Returns the service type of this staff member.<br>
	 * @return the service type
	 */
	public ServiceType getServicetype() 
	{
		return serviceType;
	}

	/**
	 * Sets the service type for this staff member
	 * @param servicetype the service type to set
	 * @throws IllegalArgumentException if the service type is null or empty
	 */
	public void setServicetype(ServiceType serviceType) 
	{
		if(serviceType == null)
			throw new IllegalArgumentException("The service type cannot be null or empty");
		this.serviceType = serviceType;
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
	 * Returns whether or not the member is currently at home
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
		return !MyUtils.isEqualDate(plannedStartOfWork, plannedEndOfWork);
	}

	/**
	 * Returns the name of the user who created the roster entry.
	 * @return the username of the creator.
	 */
	public String getCreatedByUsername() {
		return createdByUser;
	}

	/**
	 * Sets the name of the staff member who created the entry.
	 * @param username the username of the creator
	 */
	public void setCreatedByUsername(String createdByUser) 
	{
		this.createdByUser = createdByUser;
	}
}


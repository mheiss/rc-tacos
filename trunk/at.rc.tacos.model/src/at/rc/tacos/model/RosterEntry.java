package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

//TODO mark roster entries which hold on for more than one day

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
	private long plannedEndOfWork;
	private long plannedStartOfWork;
	private long realStartOfWork;
	private long realEndOfWork;
	private String station;
	private String competence;
	private String servicetype;
	private String rosterNotes;
	private boolean standby;	
	
	/**
	 * Default class construtor
	 */
	public RosterEntry()
	{
	    super(ID);
	}
	

	/**
	 * @param id
	 * @param rosterId
	 * @param staffMember
	 * @param plannedEndOfWork
	 * @param plannedStartOfWork
	 * @param realStartOfWork
	 * @param realEndOfWork
	 * @param station
	 * @param competence
	 * @param servicetype
	 * @param rosterNotes
	 * @param standby
	 */
	public RosterEntry(String id, long rosterId, StaffMember staffMember,
			long plannedStartOfWork, long plannedEndOfWork,
			long realStartOfWork, long realEndOfWork, String station,
			String competence, String servicetype, String rosterNotes,
			boolean standby) {
		super(id);
		this.rosterId = rosterId;
		this.staffMember = staffMember;
		this.plannedEndOfWork = plannedEndOfWork;
		this.plannedStartOfWork = plannedStartOfWork;
		this.realStartOfWork = realStartOfWork;
		this.realEndOfWork = realEndOfWork;
		this.station = station;
		this.competence = competence;
		this.servicetype = servicetype;
		this.rosterNotes = rosterNotes;
		this.standby = standby;
	}





	//METHODS
	/**
	 * Returns a string based description of the object
	 * @return the description of the object
	 */
	@Override
	public String toString()
	{
	    return ID;
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
	 */
	public void setRosterId(long rosterId) 
	{
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
	 * @param staff member the staff member to set
	 */
	public void setStaffMember(StaffMember staffMember) 
	{
		this.staffMember = staffMember;
	}

	

	/**
	 * @return the plannedEndOfWork
	 */
	public long getPlannedEndOfWork() {
		return plannedEndOfWork;
	}





	/**
	 * @param plannedEndOfWork the plannedEndOfWork to set
	 */
	public void setPlannedEndOfWork(long plannedEndOfWork) {
		this.plannedEndOfWork = plannedEndOfWork;
	}





	/**
	 * @return the plannedStartOfWork
	 */
	public long getPlannedStartOfWork() {
		return plannedStartOfWork;
	}





	/**
	 * @param plannedStartOfWork the plannedStartOfWork to set
	 */
	public void setPlannedStartOfWork(long plannedStartOfWork) {
		this.plannedStartOfWork = plannedStartOfWork;
	}





	/**
	 * Returns the check in time of this member.
	 * This time represents the real start time of the meber.
	 * @return the realStartOfWork
	 */
	public long getRealStartOfWork() 
	{
		return realStartOfWork;
	}

	/**
	 * Sets the check-in time for this member
	 * @param realStartOfWork the realStartOfWork to set
	 */
	public void setRealStartOfWork(long realStartOfWork) 
	{
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
	 * Sets the check-out time for this member
	 * @param realEndOfWork the realEndOfWork to set
	 */
	public void setRealEndOfWork(long realEndOfWork) 
	{
		this.realEndOfWork = realEndOfWork;
	}

	/**
	 * Returns the station that this staff is a member from.<br>
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
	 * @return the station
	 */
	public String getStation() 
	{
		return station;
	}

	/**
	 * Sets the station for this staff member<br>
	 * @param station the station to set
	 */
	public void setStation(String station) {
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
	 * Sets the competences for this staff member
	 * @param competence the competence to set
	 */
	public void setCompetence(String competence) 
	{
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
	 */
	public void setServicetype(String servicetype) 
	{
		this.servicetype = servicetype;
	}

	/**
	 * Returns the notes for this roster entry
	 * @return the rosterNotes
	 */
	public String getRosterNotes() 
	{
		return rosterNotes;
	}

	/**
	 * Sets the notes for this roster entry
	 * @param rosterNotes the rosterNotes to set
	 */
	public void setRosterNotes(String rosterNotes) 
	{
		this.rosterNotes = rosterNotes;
	}

	/**
	 * Returns wheter or not the member is currenlty at home
	 * and in standby
	 * @return the standby
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
}


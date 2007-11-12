package at.rc.tacos.model;


import java.util.Date;

/**
 * Represents one roster entry
 * @author b.thek
 */
//TODO mark roster entries which hold on for more than one day

public class RosterEntry 
{
	private long rosterId;
	private StaffMember staffMember;
	private Date dateOfRosterEntry;
	private long plannedStartofWork;
	private long plannedEndOfWork;
	private long realStartOfWork;
	private long realEndOfWork;
	private String station;
	private String competence;
	private String servicetype;
	private String rosterNotes;
	private boolean standby;
	
	
	
	/**
	 * Constructors
	 */
	public RosterEntry(){}
	
	
	public RosterEntry(long rosterId, StaffMember staffMember, Date dateOfRosterEntry,
			long plannedStartofWork, long plannedEndOfWork,
			long realStartOfWork, long realEndOfWork, String station,
			String competence, String servicetype, String rosterNotes,
			boolean standby) 
	{
		
		this.rosterId = rosterId;
		this.staffMember = staffMember;
		this.dateOfRosterEntry = dateOfRosterEntry;
		this.plannedStartofWork = plannedStartofWork;
		this.plannedEndOfWork = plannedEndOfWork;
		this.realStartOfWork = realStartOfWork;
		this.realEndOfWork = realEndOfWork;
		this.station = station;
		this.competence = competence;
		this.servicetype = servicetype;
		this.rosterNotes = rosterNotes;
		this.standby = standby;
	}

	
	/**
	 * Setter&Getter
	 */

	/**
	 * @return the rosterId
	 */
	public long getRosterId() {
		return rosterId;
	}


	/**
	 * @param rosterId the rosterId to set
	 */
	public void setRosterId(long rosterId) {
		this.rosterId = rosterId;
	}


	/**
	 * @return the staff member
	 */
	public StaffMember getStaffMember() {
		return staffMember;
	}


	/**
	 * @param staff member the staff member to set
	 */
	public void setStaffMember(StaffMember staffMember) {
		this.staffMember = staffMember;
	}


	/**
	 * @return the dateOfRosterEntry
	 */
	public Date getDateOfRosterEntry() {
		return dateOfRosterEntry;
	}


	/**
	 * @param dateOfRosterEntry the dateOfRosterEntry to set
	 */
	public void setDateOfRosterEntry(Date dateOfRosterEntry) {
		this.dateOfRosterEntry = dateOfRosterEntry;
	}


	/**
	 * @return the plannedStartofWork
	 */
	public long getPlannedStartofWork() {
		return plannedStartofWork;
	}


	/**
	 * @param plannedStartofWork the plannedStartofWork to set
	 */
	public void setPlannedStartofWork(long plannedStartofWork) {
		this.plannedStartofWork = plannedStartofWork;
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
	 * @return the realStartOfWork
	 * check-in
	 */
	public long getRealStartOfWork() {
		return realStartOfWork;
	}


	/**
	 * @param realStartOfWork the realStartOfWork to set
	 * check-in
	 */
	public void setRealStartOfWork(long realStartOfWork) {
		this.realStartOfWork = realStartOfWork;
	}


	/**
	 * @return the realEndOfWork
	 * check-out
	 */
	public long getRealEndOfWork() {
		return realEndOfWork;
	}


	/**
	 * @param realEndOfWork the realEndOfWork to set
	 * check-out
	 */
	public void setRealEndOfWork(long realEndOfWork) {
		this.realEndOfWork = realEndOfWork;
	}


	/**
	 * @return the station
	 * Possible stations: 'Bruck-Kapfenberg', 'Bruck an der Mur', 'Kapfenberg', 'St. Marein', 'Breitenau', 'Thörl', 'Turnau', 'NEF'
	 */
	public String getStation() {
		return station;
	}


	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}


	/**
	 * @return the competence
	 * Possible competences: 'Fahrer', 'Sanitäter', 'Zweithelfer', 'Notfallsanitäter', 'Leitstellendisponent', 'Dienstführender'
	 * 'Inspektionsdienst', 'Sonstiges'
	 */
	public String getCompetence() {
		return competence;
	}


	/**
	 * @param competence the competence to set
	 */
	public void setCompetence(String competence) {
		this.competence = competence;
	}


	/**
	 * @return the service type
	 * Possible service types: 'Hauptamtlich', 'Freiwillig', 'Zivildienstleistender', 'Sonstiges'
	 */
	public String getServicetype() {
		return servicetype;
	}


	/**
	 * @param servicetype the service type to set
	 */
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}


	/**
	 * @return the rosterNotes
	 */
	public String getRosterNotes() {
		return rosterNotes;
	}


	/**
	 * @param rosterNotes the rosterNotes to set
	 */
	public void setRosterNotes(String rosterNotes) {
		this.rosterNotes = rosterNotes;
	}


	/**
	 * @return the standby
	 */
	public boolean getStandby() {
		return standby;
	}


	/**
	 * @param standby the standby to set
	 */
	public void setStandby(boolean standby) {
		this.standby = standby;
	}	
}


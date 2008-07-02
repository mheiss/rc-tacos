package at.rc.tacos.server.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.*;

public interface RosterDAO 
{
    public static final String TABLE_NAME = "roster";
    
	/**
	 * Adds a new entry to the list of roster entries.
	 * @param entry the entry to add.
	 * @return the generated id to identify the roster entry
	 */
    public int addRosterEntry(RosterEntry entry) throws SQLException;
    
    /**
     * Updates the roster entry in the database
     * @param entry the roster entry to update
     * @return true if the update was successfull
     */
    public boolean updateRosterEntry(RosterEntry entry) throws SQLException;
    
    /**
     * Removes the roster entry from the database.
     * @param id the id of the roster entry to remove
     * @return true if the remove was successfull
     */
    public boolean removeRosterEntry(int id) throws SQLException;
    
    /**
     * Returns a specifiy roster entry identfied by the id
     * @param rosterEntryId the id of the entry to get
     * @return the accociated entry or null if the entry does not exist
     */
    public RosterEntry getRosterEntryById(int rosterEntryId) throws SQLException;
    
    /**
     * Returns a list of all roster entries accociated with a given staff member
     * @param staffMemberId the id of the staff member to get the roster entries
     * @return the list of roster entries of the staff member
     */
	public List<RosterEntry> listRosterEntryByStaffMember(int staffMemberId) throws SQLException;
	
	/**
	 * List all roster entries in the given time intervall.
	 * @param startTime the start time of the entries
	 * @param endTime the end time of the entires
	 * @return the list of roster entries
	 */
	public List<RosterEntry> listRosterEntryByDate(long startTime, long endTime) throws SQLException;
	
	public List<RosterEntry> listRosterEntriesByDateAndLocation(long startTime, long endTime, int filterLocationId) throws SQLException;
	
	public List<RosterEntry> listRosterEntriesForRosterMonth(int locationFilter, int monthFilter, int yearFilter, int locationStaffMemberFilter, String functionJobServiceTypeFilter, String functionStaffMemberCompetenceFilter, int staffMemberFilter) throws SQLException;
	
}
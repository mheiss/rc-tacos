package at.rc.tacos.core.db;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.*;

public interface RosterLayer {

	//Roster
	List<RosterEntry> listRosterEntrys(String order, String orderSequence) throws SQLException;
	List<RosterEntry> listRosterEntryByEmployee(int emplyeeID, String order, String orderSequence) throws SQLException;
	List<RosterEntry> listRosterEntryByTime(String startTime, String endTime, String order, String orderSequence) throws SQLException;
	Integer addRosterEntry(int location_ID, int employee_ID, int servicetype_ID, int job_ID, String starttime, String endtime, String checkIn, String checkOut, String note) throws SQLException;
	boolean updateRosterEntry(String columnName, String newValue, String rosterID) throws SQLException;
	
	
}

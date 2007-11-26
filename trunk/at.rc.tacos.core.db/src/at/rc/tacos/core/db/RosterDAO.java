package at.rc.tacos.core.db;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.RosterEntry;

public class RosterDAO implements RosterLayer
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	public RosterDAO(){}

	@Override
	public Integer addRosterEntry(int location_ID, int employee_ID,
			int servicetype_ID, int job_ID, String starttime, String endtime,
			String checkIn, String checkOut, String note) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RosterEntry> listRosterEntryByEmployee(int emplyeeID,
			String order, String orderSequence) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RosterEntry> listRosterEntryByTime(String startTime,
			String endTime, String order, String orderSequence)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RosterEntry> listRosterEntrys(String order, String orderSequence)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateRosterEntry(String columnName, String newValue,
			String rosterID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


}

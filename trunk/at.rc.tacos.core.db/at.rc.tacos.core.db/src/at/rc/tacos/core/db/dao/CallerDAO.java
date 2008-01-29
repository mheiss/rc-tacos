package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.CallerDetail;

public interface CallerDAO 
{
	public int addCaller(CallerDetail notifierDetail) throws SQLException;
	public boolean updateCaller(CallerDetail notifierDetail, int id) throws SQLException;
	public boolean removeCaller(int id) throws SQLException;
	
	public CallerDetail getCallerByID(int callerID) throws SQLException;
	public int getCallerId(CallerDetail notifierDetail) throws SQLException;
	public List<CallerDetail> listCallers() throws SQLException;
}

package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;

public class CallerDAOMySQL implements CallerDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addCaller(CallerDetail notifierDetail) throws SQLException
	{
		int callerId = 0;
		try
		{	
			// callername, caller_phonenumber
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.caller"));
			query.setString(1, notifierDetail.getCallerName());
			query.setString(2, notifierDetail.getCallerTelephoneNumber());
			query.executeUpdate();
			
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.callerID"));
			query1.setString(1, notifierDetail.getCallerName());
			query1.setString(2, notifierDetail.getCallerTelephoneNumber());
			final ResultSet rsCallerId = query1.executeQuery();
			
			if(rsCallerId.first())
				callerId = rsCallerId.getInt("caller_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
		return callerId;
	}

	@Override
	public int getCallerId(CallerDetail notifierDetail) throws SQLException
	{
		int callerId = 0;
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.callerID"));
			query1.setString(1, notifierDetail.getCallerName());
			query1.setString(2, notifierDetail.getCallerTelephoneNumber());
			final ResultSet rsCallerId = query1.executeQuery();
			
			if(rsCallerId.first())
				callerId = rsCallerId.getInt("caller_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
		return callerId;
	}
	
	@Override
	public CallerDetail getCallerByID(int callerID) throws SQLException
	{
		CallerDetail caller = new CallerDetail();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.callerByID"));
			query1.setInt(1, callerID);
			final ResultSet rs = query1.executeQuery();
			
			rs.first();
			caller.setCallerName(rs.getString("callername"));
			caller.setCallerName(rs.getString("caller_phonenumber"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return caller;
	}

	@Override
	public List<CallerDetail> listCallers() throws SQLException
	{
		List<CallerDetail> callerList = new ArrayList<CallerDetail>();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.callers"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				CallerDetail caller = null;
				caller.setCallerName(rs.getString("callername"));
				caller.setCallerTelephoneNumber(rs.getString("caller_phonenumber"));
				callerList.add(caller);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return callerList;
	}

	@Override
	public boolean removeCaller(int id) throws SQLException
	{
    	try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("remove.caller"));
    		query.setInt(1, id);

    		query.executeUpdate();
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return false;
    	}
    	return true;
	}

	@Override
	public boolean updateCaller(CallerDetail notifierDetail, int id) throws SQLException
	{
    	try
		{
    	// callername, caller_phonenumber, caller_ID
    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.caller"));
		query.setString(1, notifierDetail.getCallerName());
		query.setString(2, notifierDetail.getCallerTelephoneNumber());
		query.setInt(3, id);
		
		query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
		
	}


}

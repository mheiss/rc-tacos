package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;

public class CallerDAOMySQL implements CallerDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addCaller(CallerDetail notifierDetail)
	{
		int callerId = -1;
		try
		{	
			// callername, caller_phonenumber
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.caller"));
			query.setString(1, notifierDetail.getCallerName());
			query.setString(2, notifierDetail.getCallerTelephoneNumber());
			query.executeUpdate();

			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
		    if (rs.next()) 
		        callerId = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return callerId;
	}

	@Override
	public CallerDetail getCallerByID(int callerID)
	{
		CallerDetail caller = new CallerDetail();
		try
		{
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.callerByID"));
			query1.setInt(1, callerID);
			final ResultSet rs = query1.executeQuery();

			if(rs.first())
			{
				caller.setCallerName(rs.getString("callername"));
				caller.setCallerName(rs.getString("caller_phonenumber"));
			}
			else
				return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return caller;
	}

	@Override
	public boolean updateCaller(CallerDetail notifierDetail)
	{
		try
		{
			// callername, caller_phonenumber, caller_ID
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.caller"));
			query.setString(1, notifierDetail.getCallerName());
			query.setString(2, notifierDetail.getCallerTelephoneNumber());
			query.setInt(3, notifierDetail.getCallerId());

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

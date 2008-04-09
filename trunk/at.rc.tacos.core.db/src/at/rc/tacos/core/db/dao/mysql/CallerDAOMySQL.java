package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;

public class CallerDAOMySQL implements CallerDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addCaller(CallerDetail notifierDetail) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// callername, caller_phonenumber
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("insert.caller"));
			stmt.setString(1, notifierDetail.getCallerName());
			stmt.setString(2, notifierDetail.getCallerTelephoneNumber());
			stmt.executeUpdate();

			//get the last inserted id
			final ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) 
				return rs.getInt(1);
			//no auto value
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public CallerDetail getCallerByID(int callerID) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.callerByID"));
			stmt.setInt(1, callerID);
			final ResultSet rs = stmt.executeQuery();
			//assert we have a result set
			if(rs.first())
			{
				CallerDetail caller = new CallerDetail();
				caller.setCallerName(rs.getString("callername"));
				caller.setCallerName(rs.getString("caller_phonenumber"));
				return caller;
			}
			//no result 
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateCaller(CallerDetail notifierDetail) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// callername, caller_phonenumber where caller_ID
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.caller"));
			query.setString(1, notifierDetail.getCallerName());
			query.setString(2, notifierDetail.getCallerTelephoneNumber());
			query.setInt(3, notifierDetail.getCallerId());
			//assert the update was successfully
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}
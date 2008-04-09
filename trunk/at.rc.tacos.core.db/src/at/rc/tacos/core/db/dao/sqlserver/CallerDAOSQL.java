package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;

public class CallerDAOSQL implements CallerDAO
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
			int id = 0;
			//get the next id
			final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextCallerID"));
			final ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return -1;
			
			id = rs.getInt(1);

			// callername, caller_phonenumber
			final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.caller"));
			insertstmt.setString(1, notifierDetail.getCallerName());
			insertstmt.setString(2, notifierDetail.getCallerTelephoneNumber());
			insertstmt.executeUpdate();
//
			if(insertstmt.executeUpdate() == 0)
				return -1;

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
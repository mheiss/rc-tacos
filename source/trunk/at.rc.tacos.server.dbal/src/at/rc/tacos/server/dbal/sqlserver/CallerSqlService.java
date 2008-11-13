package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.services.dbal.CallerService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for caller details.
 * 
 * @author Michael
 */
public class CallerSqlService implements CallerService {
	
	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public int addCaller(CallerDetail notifierDetail) throws SQLException {
		int id = 0;
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextCallerID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		id = rs.getInt(1);

		// callername, caller_phonenumber
		final PreparedStatement insertstmt = connection.prepareStatement(queries.getStatment("insert.caller"));
		insertstmt.setInt(1, id);
		insertstmt.setString(2, notifierDetail.getCallerName());
		insertstmt.setString(3, notifierDetail.getCallerTelephoneNumber());

		if (insertstmt.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public CallerDetail getCallerByID(int callerID) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.callerByID"));
		stmt.setInt(1, callerID);
		final ResultSet rs = stmt.executeQuery();
		// assert we have a result set
		if (rs.first()) {
			CallerDetail caller = new CallerDetail();
			caller.setCallerName(rs.getString("callername"));
			caller.setCallerName(rs.getString("caller_phonenumber"));
			return caller;
		}
		// no result
		return null;
	}

	@Override
	public boolean updateCaller(CallerDetail notifierDetail) throws SQLException {
		// callername, caller_phonenumber where caller_ID
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.caller"));
		query.setString(1, notifierDetail.getCallerName());
		query.setString(2, notifierDetail.getCallerTelephoneNumber());
		query.setInt(3, notifierDetail.getCallerId());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}
}

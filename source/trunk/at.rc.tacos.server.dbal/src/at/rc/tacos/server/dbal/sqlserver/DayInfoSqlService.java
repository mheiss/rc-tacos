package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.services.dbal.DayInfoService;
import at.rc.tacos.platform.util.MyUtils;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for caller details.
 * 
 * @author Michael
 */
public class DayInfoSqlService implements DayInfoService {
	
	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public DayInfoMessage getDayInfoByDate(long date) throws SQLException {
		// dayinfo_ID, username, date, message
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.dayInfoByDate"));
		query.setString(1, MyUtils.timestampToString(date, MyUtils.timeAndDateFormat));
		final ResultSet rs = query.executeQuery();
		// assert we have a result set
		if (rs.next()) {
			DayInfoMessage dayInfo = new DayInfoMessage();
			dayInfo.setLastChangedBy(rs.getString("username"));
			dayInfo.setTimestamp(MyUtils.stringToTimestamp(rs.getString("date"), MyUtils.sqlDate));
			dayInfo.setMessage(rs.getString("message"));
			return dayInfo;
		}
		return null;
	}

	@Override
	public boolean updateDayInfoMessage(DayInfoMessage message) throws SQLException {
		// check if the day info message already exists -> update
		// if we have no day info message for this day -> add new
		DayInfoMessage dayInfo = getDayInfoByDate(message.getTimestamp());
		if (dayInfo != null) {
			// update.dayInfo = UPDATE dayinfo SET username = ?, message = ?
			// WHERE date = ?;
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.dayInfo"));
			query.setString(1, message.getLastChangedBy());
			query.setString(2, message.getMessage());
			query.setString(3, MyUtils.timestampToString(message.getTimestamp(), MyUtils.timeAndDateFormat));
			// check if the day info message was updated
			if (query.executeUpdate() == 0)
				return false;
			return true;
		}
		else {
			// username, date, message
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.dayInfo"));
			query.setString(1, message.getLastChangedBy());
			query.setString(2, MyUtils.timestampToString(message.getTimestamp(), MyUtils.timeAndDateFormat));
			query.setString(3, message.getMessage());
			// assert the message was inserted
			if (query.executeUpdate() == 0)
				return false;
			return true;
		}
	}
}

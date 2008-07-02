package at.rc.tacos.server.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.server.db.DbWrapper;
import at.rc.tacos.server.db.SQLQueries;
import at.rc.tacos.server.db.dao.DayInfoDAO;
import at.rc.tacos.util.MyUtils;

public class DayInfoDAOSQL implements DayInfoDAO
{
	//The data source to get the connection and the queries file
	private final DbWrapper source = DbWrapper.getDefault();
	private final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public DayInfoMessage getDayInfoByDate(long date) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//dayinfo_ID, username, date, message
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.dayInfoByDate"));
			query.setString(1,MyUtils.timestampToString(date, MyUtils.timeAndDateFormat));
			final ResultSet rs = query.executeQuery();
			//assert we have a result set
			if(rs.next())
			{
				DayInfoMessage dayInfo = new DayInfoMessage();
				dayInfo.setLastChangedBy(rs.getString("username"));
				dayInfo.setTimestamp(MyUtils.stringToTimestamp(rs.getString("date"), MyUtils.sqlDate));
				dayInfo.setMessage(rs.getString("message"));
				return dayInfo;
			}
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateDayInfoMessage(DayInfoMessage message) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//check if the day info message already exists -> update
			//if we have no day info message for this day -> add new
			DayInfoMessage dayInfo = getDayInfoByDate(message.getTimestamp());
			if(dayInfo != null)
			{
				//update.dayInfo = UPDATE dayinfo SET username = ?, message = ? WHERE date = ?;
				final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.dayInfo"));
				query.setString(1,message.getLastChangedBy());
				query.setString(2,message.getMessage());
				query.setString(3, MyUtils.timestampToString(message.getTimestamp(), MyUtils.timeAndDateFormat));
				//check if the day info message was updated
				if(query.executeUpdate() == 0)
					return false;
				return true;
			}
			else
			{
				//username, date, message
				final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.dayInfo"));
				query.setString(1, message.getLastChangedBy());
				query.setString(2, MyUtils.timestampToString(message.getTimestamp(), MyUtils.timeAndDateFormat));
				query.setString(3, message.getMessage());
				//assert the message was inserted
				if(query.executeUpdate() == 0)
					return false;
				return true;
			}
		}
		finally
		{
			connection.close();
		}
	}
}

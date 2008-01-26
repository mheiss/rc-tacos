package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.util.MyUtils;

public class DayInfoDAOMySQL implements DayInfoDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public DayInfoMessage getDayInfoByDate(long date) 
	{
		DayInfoMessage dayInfo = new DayInfoMessage();

		try
		{
			//dayinfo_ID, username, date, message
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DayInfoDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.dayInfoByDate"));
			query.setString(1,MyUtils.formatDate(date));

			final ResultSet rs = query.executeQuery();

			if(rs.first())
			{
				dayInfo.setLastChangedBy(rs.getString("username"));

				dayInfo.setTimestamp(MyUtils.getTimestampFromDate(rs.getString("date")));
				dayInfo.setMessage(rs.getString("message"));
			}
			else return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return dayInfo;
	}

	@Override
	public boolean updateDayInfoMessage(DayInfoMessage message) 
	{
		try
		{
			DayInfoMessage dayInfo = getDayInfoByDate(message.getTimestamp());
			if(dayInfo != null)
			{
				//update.dayInfo = UPDATE dayinfo SET username = ?, message = ? WHERE date = ?;
				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DayInfoDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.dayInfo"));
				query.setString(1,message.getLastChangedBy());
				query.setString(2,message.getMessage());
				query.setString(3, MyUtils.formatDate(message.getTimestamp()));
				query.executeUpdate();
				return true;
			}
			else if (dayInfo == null)
			{
				System.out.println(MyUtils.formatDate(message.getTimestamp()));
				//username, date, message
				final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.dayInfo"));
				query.setString(1, message.getLastChangedBy());
				query.setString(2, MyUtils.formatDate(message.getTimestamp()));
				query.setString(3, message.getMessage());
				query.executeUpdate();
				return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

}

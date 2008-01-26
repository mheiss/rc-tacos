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

    		rs.first();
    		
    		dayInfo.setId(rs.getInt("dayinfo_ID"));
    		dayInfo.setLastChangedBy(rs.getString("username"));
    		dayInfo.setTimestamp(MyUtils.getTimestampFromDate(rs.getString("date")));
    		dayInfo.setMessage(rs.getString("message"));
    		
    	}
    	catch (SQLException e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    	return dayInfo;
	}

	@Override
	public int updateDayInfoMessage(DayInfoMessage message) 
	{
		try
		{
			//update.dayinfo = UPDATE dayinfo SET username = ?, date = ?, message = ? WHERE dayinfo_ID = ?;
	    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(DayInfoDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.dayInfo"));
	    	query.setString(1,message.getLastChangedBy());
	    	query.setString(2, MyUtils.formatDate(message.getTimestamp()));
	    	query.setString(3,message.getMessage());
	    	query.setInt(4,message.getId());
					
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;//TODO correct?
		}
		return message.getId();	
	}

}

package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.util.MyUtils;

public class DayInfoDAOMemory implements DayInfoDAO
{
	//the shared instance
	private static DayInfoDAOMemory instance;

	//the data list
	private ArrayList<DayInfoMessage> dayInfoList; 

	/**
	 * Default private class constructor
	 */
	public DayInfoDAOMemory()
	{
		dayInfoList = new ArrayList<DayInfoMessage>();
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static DayInfoDAOMemory getInstance()
	{
		//create new or return
		if(instance == null)
			instance = new DayInfoDAOMemory();
		return instance;
	}

	@Override
	public DayInfoMessage getDayInfoByDate(long date) 
	{     
		//loop and compare
		for(DayInfoMessage dayInfo:dayInfoList)
		{
			if(MyUtils.isEqualDate(date, dayInfo.getTimestamp()))
				return dayInfo;
		}
		//nothing found
		return null;
	}

	@Override
	public int updateDayInfoMessage(DayInfoMessage message)
	{
		//check if we have a day info for this date
		if(getDayInfoByDate(message.getTimestamp()) != null)
		{
			//get the message and replace it by the new
			DayInfoMessage dayInfo = getDayInfoByDate(message.getTimestamp());
			int index = dayInfoList.indexOf(dayInfo);
			dayInfoList.set(index, message);
			return index;
		}	
		//nothing found so add to the server list
		dayInfoList.add(message);
		return dayInfoList.size();
	}
}

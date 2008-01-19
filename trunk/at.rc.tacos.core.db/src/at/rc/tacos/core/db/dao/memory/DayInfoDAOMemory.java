package at.rc.tacos.core.db.dao.memory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.model.DayInfoMessage;

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
		//the requested date
        String filterDate = formatDate(date);
        
        System.out.println("Request day info for: "+filterDate);
                
        //loop and compare
        for(DayInfoMessage dayInfo:dayInfoList)
        {
        	System.out.println("Checking: "+formatDate(dayInfo.getTimestamp()));
        	if(filterDate.equalsIgnoreCase(formatDate(dayInfo.getTimestamp())))
        	{
        		System.out.println("Found day info: "+dayInfo.getMessage());
        			return dayInfo;
        	}
        }
        //nothing found
        return null;
	}

	@Override
	public void setDayInfoMessage(DayInfoMessage message) 
	{
		//the date to update
		String updateDate = formatDate(message.getTimestamp());
		
		System.out.println("Updating the day info for: "+updateDate);
		
		//check if we have a day info for this date
		if(getDayInfoByDate(message.getTimestamp()) != null)
		{
			//get the message
			DayInfoMessage dayInfo = getDayInfoByDate(message.getTimestamp());
			int index = dayInfoList.indexOf(dayInfo);
			//replace
			dayInfoList.set(index, message);
		}	
        //nothing found so add
        dayInfoList.add(message);
	}
	
    /** 
     * Helper method to format the date and return a string 
     * @param date the date to format
     * @return the formatted string
     */
    private String formatDate(long date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return sdf.format(cal.getTime());
    }
}
	
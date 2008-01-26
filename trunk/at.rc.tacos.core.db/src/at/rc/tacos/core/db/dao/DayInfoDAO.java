package at.rc.tacos.core.db.dao;

import at.rc.tacos.model.DayInfoMessage;

public interface DayInfoDAO 
{
	/**
	 * Creates a new day info message or updates a existing message in the database.
	 * @param message the message to update
	 * @return the a boolean value if update/insert was sucessful
	 */
	public boolean updateDayInfoMessage(DayInfoMessage message);
	
	/**
	 * Returns the day info message for a given date.
	 * This method will return <code>null</code> when no day info message is stored for the day.
	 * @param date the date to get the message
	 * @return the day info message for the day
	 */
	public DayInfoMessage getDayInfoByDate(long date);
}

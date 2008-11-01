package at.rc.tacos.platform.services.dbal;

import java.sql.SQLException;

import at.rc.tacos.platform.model.DayInfoMessage;

public interface DayInfoService {

	public static final String TABLE_NAME = "dayinfo";

	/**
	 * Creates a new day info message or updates a existing message in the
	 * database.
	 * 
	 * @param message
	 *            the message to update
	 * @return the a boolean value if update/insert was sucessful
	 */
	public boolean updateDayInfoMessage(DayInfoMessage message) throws SQLException;

	/**
	 * Returns the day info message for a given date. This method will return
	 * <code>null</code> when no day info message is stored for the day.
	 * 
	 * @param date
	 *            the date to get the message
	 * @return the day info message for the day
	 */
	public DayInfoMessage getDayInfoByDate(long date) throws SQLException;
}

package at.rc.tacos.core.db.dao;

import at.rc.tacos.model.DayInfoMessage;

public interface DayInfoDAO 
{
	public void setDayInfoMessage(DayInfoMessage message);
	public DayInfoMessage getDayInfoByDate(long date);
}

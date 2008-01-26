package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.util.MyUtils;

public class DayInfoListener extends ServerListenerAdapter
{
	//The DAO classes
	private DayInfoDAO dayInfoDao = DaoFactory.MYSQL.createDayInfoDAO();

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) 
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//listing by date
		if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
			//get the query filter
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			//get the timestamp
			long date = MyUtils.getTimestampFromDate(dateFilter);
			//the day info
			DayInfoMessage message = dayInfoDao.getDayInfoByDate(date);
			//assert valid
			if(message == null)
			{
				message = new DayInfoMessage();
				message.setTimestamp(date);
				message.setMessage("");
				message.setDirty(false);
				message.setLastChangedBy("");
			}
			list.add(message);
		}
		return list;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) 
	{
		DayInfoMessage dayInfo = (DayInfoMessage)updateObject;
		//update the message on the server
		dayInfoDao.updateDayInfoMessage(dayInfo);
		//reset the dirty flag
		dayInfo.setDirty(false);
		return dayInfo;
	}
}
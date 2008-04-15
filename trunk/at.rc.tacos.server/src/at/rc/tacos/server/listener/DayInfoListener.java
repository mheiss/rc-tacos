package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.util.MyUtils;

public class DayInfoListener extends ServerListenerAdapter
{
	//The DAO classes
	private DayInfoDAO dayInfoDao = DaoFactory.SQL.createDayInfoDAO();

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//listing by date
		if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
			//get the query filter
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			//get the timestamp
			long date = MyUtils.stringToTimestamp(dateFilter, MyUtils.dateFormat);
			//the day info
			DayInfoMessage message = dayInfoDao.getDayInfoByDate(date);
			//assert valid
			if(message == null)
			{
				message = new DayInfoMessage();
				message.setTimestamp(date);
				message.setMessage("");
				message.setDirty(false);
				message.setLastChangedBy("<keine Änderung>");
			}
			list.add(message);
		}
		return list;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
	{
		DayInfoMessage dayInfo = (DayInfoMessage)updateObject;
		//update the message on the server
		if(!dayInfoDao.updateDayInfoMessage(dayInfo))
			throw new DAOException("DayInfoListener","Failed to update the day info message: "+dayInfo);
		//reset the dirty flag
		dayInfo.setDirty(false);
		return dayInfo;
	}
}
package at.rc.tacos.server.listener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.QueryFilter;

public class DayInfoListener extends ServerListenerAdapter
{
	//The DAO classes
    private DayInfoDAO dayInfoDao = DaoFactory.TEST.createDayInfoDAO();

	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) 
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//listing by date
		if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
        {
            //get the query filter
            final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
            //format the time
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Calendar filterTime = Calendar.getInstance();
            try
            {
                filterTime.setTime(df.parse(dateFilter));
                long date = filterTime.getTimeInMillis();
                //the day info
                DayInfoMessage message = dayInfoDao.getDayInfoByDate(date);
                //assert valid
                if(message == null)
                	message = new DayInfoMessage();
                list.add(message);
            }
            catch(ParseException pe)
            {
                throw new IllegalArgumentException("cannot parse the date");
            }
        }
		return list;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) 
	{
		DayInfoMessage dayInfo = (DayInfoMessage)updateObject;
		dayInfoDao.setDayInfoMessage(dayInfo);
		return dayInfo;
	}
}

package at.rc.tacos.server.listener;

import java.util.ArrayList;
import java.util.Calendar;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * This class will be notified uppon transport changes
 * @author Michael
 */
public class TransportListener extends ServerListenerAdapter
{
    private TransportDAO transportDao = DaoFactory.MYSQL.createTransportDAO();
    
    /**
     * Add a transport
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Transport transport = (Transport)addObject;
        int id = transportDao.addTransport(transport);
        transport.setTransportId(id);
        return transport;
    }

    /**
     * Transport listing
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all transport entries is denied.");
			return list;
		}
		else if(queryFilter.containsFilterType(IFilterTypes.TYPE_FILTER))
		{
			//get the query filter and parse it
			final String type = queryFilter.getFilterValue(IFilterTypes.TYPE_FILTER);
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			//show current transports that are in progress
			if(Transport.TRANSPORT_PROGRESS.equalsIgnoreCase(type))
				list.addAll(transportDao.listTransportsByDateOfTransport(dateStart, dateEnd));
			//show the transports in the journal
			if(Transport.TRANSPORT_JOURNAL.equalsIgnoreCase(type))
				list.addAll(transportDao.listArchivedTransports(dateStart, dateEnd));
		}
		return list;
    }

    /**
     * Remove a transport
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
    	System.out.println("WARNING: Removing of transports is not allowed.");
        return removeObject;
    }

    /**
     *  Update a transport
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        Transport transport = (Transport)updateObject;
        //update a transport
        if(transport.getVehicleDetail() != null && transport.getTransportNumber() == 0)
        {
        	System.out.println("Assign car to transport, generating transport number");
        	transport.setYear(Calendar.getInstance().get(Calendar.YEAR));
        	int transportNr = transportDao.assignVehicleToTransport(transport);
        	transport.setTransportNumber(transportNr);
        }
        else
        {
        	//send a simple update request to the dao
        	transportDao.updateTransport(transport);
        }
        return transport;
    }
}

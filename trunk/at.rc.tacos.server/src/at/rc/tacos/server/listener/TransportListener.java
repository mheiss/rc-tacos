package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * This class will be notified uppon transport changes
 * @author Michael
 */
public class TransportListener extends ServerListenerAdapter
{
	private TransportDAO transportDao = DaoFactory.SQL.createTransportDAO();
	//the logger
	private static Logger logger = Logger.getLogger(TransportListener.class);

	/**
	 * Add a transport
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject, String username) throws DAOException,SQLException
	{
		Transport transport = (Transport)addObject;
		int id = transportDao.addTransport(transport);
		if(id == Transport.TRANSPORT_ERROR)
			throw new DAOException("TransportListener","Failed to add the transport:"+transport);
		transport.setTransportId(id);
		
		//for the direct car assign to a transport (in the transport form)
		if(transport.getVehicleDetail() != null && transport.getTransportNumber() == 0)
		{
			System.out.println("Assign car to transport, generating transport number");
			//set the current year to generate a valid transport numer
			transport.setYear(Calendar.getInstance().get(Calendar.YEAR));
			int transportNr = transportDao.generateTransportNumber(transport);
			if (transportNr == Transport.TRANSPORT_ERROR)
				throw new DAOException("TransportListener","Failed to generate a valid transport number for transport "+transport);
			transport.setTransportNumber(transportNr);
			transportDao.updateTransport(transport);//to set the AE- status
		}
		logger.info("added by:" +username +";" +transport);
		return transport;
	}

	/**
	 * Transport listing
	 */
	@Override
	public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)  throws DAOException,SQLException
	{
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		//if there is no filter -> request all
		if(queryFilter == null || queryFilter.getFilterList().isEmpty())
		{
			System.out.println("WARNING: Listing of all transport entries is denied.");
			throw new DAOException("TransportListener","Listing of all transport entries is denied");
		}
		
		//online transport listing *** transports todo (prebooking and outstanding) ***
		else if(queryFilter.containsFilterType(IFilterTypes.TRANSPORT_TODO_FILTER))
		{
			//for online transport listing
			//list only the prebooked transports
			//show the prebooked transports
			List<Transport> prebookedList = transportDao.listTransportsTodo();
			//check the prebooked
			if(prebookedList == null)
			{
				throw new DAOException("TransportListener","Failed to list the transports todo (prebooked and outstanding)");
			}
			list.addAll(prebookedList);
		}
		//online transport listing *** only running transports (outstanding and underway) ***
		else if(queryFilter.containsFilterType(IFilterTypes.TRANSPORT_UNDERWAY_FILTER))
		{
			//for online transport listing
			//list only the running transports (outstanding and underway)
			//show the running transports
			//show current transports that are in progress
			List<Transport> transportList = transportDao.listUnderwayTransports();
			if(transportList == null)
			{
				throw new DAOException("TransportListener","Failed to list the underway transports");
			}
			list.addAll(transportList);
			
		}
		//online transport listing *** archived by location ***
		else if(queryFilter.containsFilterType(IFilterTypes.TRANSPORT_ARCHIVED_FILTER) && queryFilter.containsFilterType(IFilterTypes.TRANSPORT_LOCATION_FILTER))
		{
			//for online transport listing
			//list only the archived transports (journal)
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			final String locationFilter = queryFilter.getFilterValue(IFilterTypes.TRANSPORT_LOCATION_FILTER);
			final int locationId = Integer.valueOf(locationFilter).intValue();
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			
			//show the transports in the journal which have a assigned vehicle
			List<Transport> journalListVehicleLocation = transportDao.listArchivedTransportsByVehicleLocationAndDate(dateStart, dateEnd, locationId);
			//check archived
			if(journalListVehicleLocation == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("TransportListener","Failed to list the archived transports by date from "+time);
			}
			
			//show the transports in the journal which have no assigned vehicle - so the location is the planned location
			
			List<Transport> journalListTransportLocation = transportDao.listArchivedTransportsByTransportLocationAndDate(dateStart, dateEnd, locationId);
			//check archived
			if(journalListTransportLocation == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("TransportListener","Failed to list the archived transports by date from "+time);
			}
			
			//immediate list to pick the needed transports
			List<Transport> journalList = new ArrayList<Transport>();
			
			//transports without a vehicle
			for(Transport transport : journalListTransportLocation)
			{
				//
				if(transport.getVehicleDetail() == null)
				{
					journalList.add(transport);
				}	
			}
			
			//transports with vehicle
			for(Transport transport : journalListVehicleLocation)
			{
				journalList.add(transport);
			}		
			list.addAll(journalList);	
		}
		//online transport listing *** only archived transports ***
		else if(queryFilter.containsFilterType(IFilterTypes.TRANSPORT_ARCHIVED_FILTER))
		{
			//for online transport listing
			//list only the archived transports (journal)
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.TRANSPORT_ARCHIVED_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			
			//show the transports in the journal
			List<Transport> journalList = transportDao.listArchivedTransports(dateStart, dateEnd);
			
			//check archived
			if(journalList == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("TransportListener","Failed to list the archived transports by date from "+time);
			}
			list.addAll(journalList);
		}
		
		else if(queryFilter.containsFilterType(IFilterTypes.DATE_FILTER))
		{
			//get the query filter and parse it to a date time
			final String dateFilter = queryFilter.getFilterValue(IFilterTypes.DATE_FILTER);
			long dateStart = MyUtils.stringToTimestamp(dateFilter,MyUtils.dateFormat);
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTimeInMillis(dateStart);
			calEnd.add(Calendar.DAY_OF_MONTH, 1);
			long dateEnd = calEnd.getTimeInMillis();
			//show the prebooked transports
			List<Transport> prebookedList = transportDao.listPrebookedTransports();
			//show current transports that are in progress
			List<Transport> transportList = transportDao.listRunningTransports();
			//show the transports in the journal
			List<Transport> journalList = transportDao.listArchivedTransports(dateStart, dateEnd);
			//check the prebooked
			if(prebookedList == null)
			{
				throw new DAOException("TransportListener","Failed to list the prebooked transports");
			}
			list.addAll(prebookedList);
			//check the running
			if(transportList == null)
			{
				throw new DAOException("TransportListener","Failed to list the running and open transports");
			}
			list.addAll(transportList);
			//check archived
			if(journalList == null)
			{
				String time = MyUtils.timestampToString(dateStart, MyUtils.dateFormat) +" bis " +MyUtils.timestampToString(dateEnd, MyUtils.dateFormat);
				throw new DAOException("TransportListener","Failed to list the archived transports by date from "+time);
			}
			list.addAll(journalList);
		}
		return list;
	}

	/**
	 * Remove a transport
	 */
	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)  throws DAOException,SQLException
	{
		System.out.println("WARNING: Removing of transports is not allowed.");
		throw new DAOException("TransportListener","Removing of transports is not allowed.");
	}

	/**
	 *  Update a transport
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject, String username)  throws DAOException,SQLException
	{
		Transport transport = (Transport)updateObject;
		//generate a transport id if we do not have one
		if(transport.getVehicleDetail() != null && transport.getTransportNumber() == 0)
		{
			System.out.println("Assign car to transport, generating transport number");
			//set the current year to generate a valid transport numer
			transport.setYear(Calendar.getInstance().get(Calendar.YEAR));
			int transportNr = transportDao.generateTransportNumber(transport);
			if (transportNr == Transport.TRANSPORT_ERROR)
				throw new DAOException("TransportListener","Failed to generate a valid transport number for transport "+transport);
			transport.setTransportNumber(transportNr);
		}

		//Vehicle is removed but we have a transport number -> cancel
		if(transport.getVehicleDetail() == null && transport.getTransportNumber() > 0)
		{
			System.out.println("vehicle removed, but transport number-> removeVehicle");
			if(!transportDao.removeVehicleFromTransport(transport)) //remove assigned vehicle, reset the transport number (to 0 and restore the given number), set program status to outstanding 
				throw new DAOException("TransportListener","Failed to remove the transport from the vehicle");
		}
		
		//STORNO OR FORWARD
		if(transport.getTransportNumber() == Transport.TRANSPORT_CANCLED 
				|| transport.getTransportNumber() == Transport.TRANSPORT_FORWARD)
		{
			if(transport.getVehicleDetail() != null)
			{
				if(!transportDao.removeVehicleFromTransport(transport))
					throw new DAOException("TransportListener", "Failed to remove the transport from the vehicle");
				transport.clearVehicleDetail();
				transport.setTransportNumber(Transport.TRANSPORT_CANCLED);//reset the transportNumber to CANCELED
			}
			System.out.println("CANCEL OR FORWARD TRANSPORT");
			if(!transportDao.cancelTransport(transport)) //set the transportNumber to -1 or -2 (to the value in the transport) and set program status journal
				throw new DAOException("TransportListner","Failed to cancle the transport "+transport);
		}

		//send a simple update request to the dao
		if(!transportDao.updateTransport(transport))
			throw new DAOException("TransportListener","Failed to update the transport: "+transport);
		logger.info("updated by: " +username +";" +transport);
		return transport;
	}
}
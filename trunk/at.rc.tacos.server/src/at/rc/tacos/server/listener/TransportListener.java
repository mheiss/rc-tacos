package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	private TransportDAO transportDao = DaoFactory.MYSQL.createTransportDAO();

	/**
	 * Add a transport
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
	{
		Transport transport = (Transport)addObject;
		int id = transportDao.addTransport(transport);
		if(id == Transport.TRANSPORT_ERROR)
			throw new DAOException("TransportListener","Failed to add the transport:"+transport);
		transport.setTransportId(id);
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
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)  throws DAOException,SQLException
	{
		Transport transport = (Transport)updateObject;
		//
		System.out.println("Update request for the transport");
		System.out.println("car: "+transport.getVehicleDetail());
		System.out.println("id: "+transport.getTransportId());
		System.out.println("nummer: "+transport.getTransportNumber());
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

		//STORNO OR FORWARD
		if(transport.getTransportNumber() == Transport.TRANSPORT_CANCLED 
				|| transport.getTransportNumber() == Transport.TRANSPORT_FORWARD)
		{
			System.out.println("CANCEL OR FORWARD TRANSPORT");
			if(!transportDao.cancelTransport(transport))
				throw new DAOException("TransportListner","Failed to cancle the transport "+transport);
		}

		//Vehicle is removed but we have a transport number -> cancle
		if(transport.getVehicleDetail() == null && transport.getTransportNumber() > 0)
		{
			System.out.println("vehicle removed, but transport number-> removeVehicle");
			if(!transportDao.removeVehicleFromTransport(transport))
				throw new DAOException("TransportListener","Failed to remove the transport from the vehicle");
		}

		//send a simple update request to the dao
		if(!transportDao.updateTransport(transport))
			throw new DAOException("TransportListener","Failed to update the transport: "+transport);

		return transport;
	}
}
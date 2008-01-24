package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Transport;

public interface TransportDAO 
{
	/**
	 * Adds a new transport to the table and returns the transport id.<br>
	 * The transport id is the internal primary key in the database and is unique for every transport.
	 * @param transport the transport to add
	 * @return the unique transport id 
	 */
    public int addTransport(Transport transport);
    
    /**
     * Assigns a transport to a vehicle and returns the transport number.
     * The returned transport number is not unique and individual for each location.
     * The transport number is calculated by the current year and the highest not cancled transport number of a station.
     * @param transport the transport to assign
     * @return the transport number.
     */
    public int assignVehicleToTransport(Transport transport);
    
    /**
     * Stores the actual transport in the log table and prevents modification of the transport in the future.
     * This method deletes the current transportId out of the transport table and moves the transport to the log.
     * @param transport the transport to archive
     * @return true if the archive was successfully
     */
    public boolean archiveTransport(Transport transport);
    
    /**    
     * Updates a given transport.<br>
     * This method updates every column of the transport table except the transport number.
     * @param transport the transport to update
     * @return true if the update was successfull.
     */
    public boolean updateTransport(Transport transport);
    
    /**
     * Cancels the transport and stores the used transport number in the dummy table so that the transport number can be reused.
     * @param transportId the transport to cancel.
     * @return true if the cancel was successfull.
     */
    public boolean cancelTransport(int transportId);
    
    /**
     * Returns all transport in the given time interval.
     * @param startdate the start date
     * @param enddate the end date
     * @return the list of transports in the given interval.
     */
    public List<Transport> listTransports(long startdate, long enddate);
}

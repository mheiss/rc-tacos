package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Transport;

public interface TransportDAO 
{
    public static final String TABLE_NAME = "transports";
    public static final String TABLE_DEPENDENT_STATE = "transportstate";
    public static final String TABLE_DEPENDENT_SELECTED = "transport_selected";
    public static final String TABLE_DEPENDENT_ASSIGNED_VEHICLES = "assigned_vehicle";
    
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
     * The transport number is calculated by the current year and the highest not canceled transport number of a station.
     * @param transport the transport to assign
     * @return the transport number.
     */
    public int assignVehicleToTransportAndGenerateTransportNumber(Transport transport);
    
    /**
     * removes an assigned vehicle from a transport
     * @param transport
     * @return true if everything was successful
     */
    public boolean removeVehicleFromTransport(Transport transport);
    
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
     * @return true if the update was successful.
     */
    public boolean updateTransport(Transport transport);
    
    /**
     * Cancels the transport and stores the used transport number in the dummy table so that the transport number can be reused.
     * @param transportId the transport to cancel.
     * @return true if the cancel was successful.
     */
    public boolean cancelTransport(int transportId);
    
    /**
     * Returns all transport in the given time interval.
     * @param startdate the start date
     * @param enddate the end date
     * @return the list of transports in the given interval.
     */
    public List<Transport> listTransportsByDateOfTransport(long startdate, long enddate);
    
    /**
     * Returns all archived transports in the given interval.
     * @param startdate the start date
     * @param enddate the end date
     * @return the list of archived transports in the given interval.
     */
    public List<Transport> listArchivedTransports(long startdate, long enddate);
    
    /**
     * Returns a list of all transports associated with the given vehicle
     * and with the program status <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>
     * @param vehicleName the name of the vehicle to get the transports from
     * @return the list of transports
     */
    public List<Transport> getTransportsFromVehicle(String vehicleName);
    
    /**
     * Assigns a new transport state to an existing transport
     * @param transport
     * @return boolean if insert was successful or not
     */
    public boolean assignTransportstate(Transport transport);
    
    /**
     * updates the time of an existing transport state
     * @param transport
     * @return boolean if insert was successful or not
     */
    public boolean updateTransportstate(Transport transport);
    
    /**
     * removes an inserted transport state
     * @param transport
     * @return boolean if remove was successful or not
     */
    public boolean removeTransportstate(Transport transport);
    /**
     * Returns a transport searched by id
     * @param id the id of the transport
     * @return transport
     */
    public Transport getTransportById(int transportId);
    /**
     * Returns the new transport number
     * @param locationname
     * @return transport number
     */
    public int getNewTransportNrForLocation(String locationname);
}

package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Transport;

public interface TransportDAO 
{
    public static final String TABLE_NAME = "transports";
    public static final String TABLE_DEPENDENT_STATE = "transportstate";
    public static final String TABLE_DEPENDENT_SELECTED = "transport_selected";
    public static final String TABLE_DEPENDENT_ASSIGNED_VEHICLES = "assigned_vehicle";
    public static final String TABLE_DEPENDENT_TMP = "tmptransports";
    
	/**
	 * Adds a new transport to the table and returns the transport id.<br>
	 * The transport id is the internal primary key in the database and is unique for every transport.
	 * @param transport the transport to add
	 * @return the unique transport id 
	 */
    public int addTransport(Transport transport) throws SQLException;
    
    /**
     * Assigns a vehicle to a transport and calculates the transport number.<br>
     * The returned transport number is not unique and individual for each location.<br>
     * Please see the <b>sql/calculateTransportNr.txt</b> for more details.
     * @param transport the transport to assign
     * @return the transport number.
     */
    public int generateTransportNumber(Transport transport) throws SQLException;
    
    /**
     * removes an assigned vehicle from a transport
     * @param transport
     * @return true if everything was successful
     */
    public boolean removeVehicleFromTransport(Transport transport) throws SQLException;
    
    /**    
     * Updates a given transport.<br>
     * This method updates every column of the transport table except the transport number.
     * @param transport the transport to update
     * @return true if the update was successful.
     */
    public boolean updateTransport(Transport transport) throws SQLException;
    
    /**
     * Cancels the transport and stores the used transport number in the dummy table.<br>
     * This method will either write <code>Transport.TRANSPORT_CANCLED</code> oder <code>Transport.TRANSPORT_FORWARD</code> into the databse.
     * This is dependen onwich value is in the transportNr given to the method.
     * @param transport the transport to cancel.
     * @return true if the cancel was successful.
     */
    public boolean cancelTransport(Transport transport) throws SQLException;
    
    /**
     * Returns all running transports that are currently in progress. Running transports are identified by the
     * status <code>IProgramStatus.PROGRAM_STATUS_OUTSTANDING</code> or <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>.
     * The transports are independend from the current date.
     * @return the list of all running transports in the database
     */
    public List<Transport> listRunningTransports() throws SQLException;
    
    /**
     * Returns all predbooked transports with the status <code>IProgramStatus.PROGRAM_STATUS_PREBOOKING</code>.
     * @return the list of all prebooked transports in the database
     */
    public List<Transport> listPrebookedTransports() throws SQLException;
        
    /**
     * Returns all transports in the given intervall with the status <code>IProgramStatus.PROGRAM_STATUS_JOURNAL</code>.
     * @param startdate the start date
     * @param enddate the end date
     * @return the list of archived transports
     */
    public List<Transport> listArchivedTransports(long startdate,long enddate) throws SQLException;
 
    /**
     * Returns a transport searched by id
     * @param id the id of the transport
     * @return transport
     */
    public Transport getTransportById(int transportId) throws SQLException;
    
    /**
     * Returns alle transports in the given interval by the given location (location_ID of the assignedVehicle (based on the vehicle current location))
     * @param startdate the start date
     * @param enddate the end date
     * @param locationId the locationId (real location of transports with a vehicle, planned location of transports without location (Storno, Leerfahrt, Weiterleitung)
     */
    public List<Transport> listArchivedTransportsByVehicleLocationAndDate(long startdate, long enddate, int locationId) throws SQLException;
    
    /**
     * Returns alle transports in the given interval by the given location (planned location of the transport)
     * @param startdate the start date
     * @param enddate the end date
     * @param locationId the locationId (real location of transports with a vehicle, planned location of transports without location (Storno, Leerfahrt, Weiterleitung)
     */
    public List<Transport> listArchivedTransportsByTransportLocationAndDate(long startdate, long enddate, int locationId) throws SQLException;
}

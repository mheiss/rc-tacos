package at.rc.tacos.platform.services.dbal;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Transport;

public interface TransportService {

	public static final String TABLE_NAME = "transports";
	public static final String TABLE_DEPENDENT_STATE = "transportstate";
	public static final String TABLE_DEPENDENT_SELECTED = "transport_selected";
	public static final String TABLE_DEPENDENT_ASSIGNED_VEHICLES = "assigned_vehicle";
	public static final String TABLE_DEPENDENT_TMP = "tmptransports";

	/**
	 * Indicates an database error
	 */
	public final static int TRANSPORT_ERROR = -3;

	/**
	 * Adds a new transport to the table and returns the transport id.<br>
	 * The transport id is the internal primary key in the database and is
	 * unique for every transport.
	 * 
	 * @param transport
	 *            the transport to add
	 * @return the unique transport id
	 */
	public int addTransport(Transport transport) throws SQLException;

	/**
	 * Assigns a vehicle to a transport and calculates the transport number.<br>
	 * The returned transport number is not unique and individual for each
	 * location.<br>
	 * Please see the <b>sql/calculateTransportNr.txt</b> for more details.
	 * 
	 * @param transport
	 *            the transport to assign
	 * @return the transport number.
	 */
	public int generateTransportNumber(Transport transport) throws SQLException;

	/**
	 * removes an assigned vehicle from a transport
	 * 
	 * @param transport
	 * @return true if everything was successful
	 */
	public boolean removeVehicleFromTransport(Transport transport) throws SQLException;

	/**
	 * Updates a given transport.<br>
	 * This method updates every column of the transport table except the
	 * transport number.
	 * 
	 * @param transport
	 *            the transport to update
	 * @return true if the update was successful.
	 */
	public boolean updateTransport(Transport transport) throws SQLException;

	/**
	 * Cancels the transport and stores the used transport number in the dummy
	 * table.<br>
	 * This method will either write <code>Transport.TRANSPORT_CANCLED</code>
	 * oder <code>Transport.TRANSPORT_FORWARD</code> into the databse. This is
	 * dependen onwich value is in the transportNr given to the method.
	 * 
	 * @param transport
	 *            the transport to cancel.
	 * @return true if the cancel was successful.
	 */
	public boolean cancelTransport(Transport transport) throws SQLException;

	/**
	 * Returns all running transports that are currently in progress. Running
	 * transports are identified by the status
	 * <code>IProgramStatus.PROGRAM_STATUS_OUTSTANDING</code> or
	 * <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>. The transports are
	 * independend from the current date.
	 * 
	 * @return the list of all running transports in the database
	 */
	public List<Transport> listRunningTransports() throws SQLException;

	/**
	 * Returns all predbooked transports with the status
	 * <code>IProgramStatus.PROGRAM_STATUS_PREBOOKING</code>.
	 * 
	 * @return the list of all prebooked transports in the database
	 */
	public List<Transport> listPrebookedTransports() throws SQLException;

	/**
	 * Returns all underway transports:
	 * <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>.
	 * 
	 * @return the list of all underway transports in the database
	 */
	public List<Transport> listUnderwayTransports() throws SQLException;

	/**
	 * Returns a list of all <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>
	 * transports that are assigned to this vehicle.
	 * 
	 * @param vehicleID
	 *            the identifier of the vehicle
	 * @return the list of all underway transports in the database
	 */
	public List<Transport> listUnderwayTransportsByVehicle(String vehicleId) throws SQLException;

	/**
	 * Returns all transports in the given intervall with the status
	 * <code>IProgramStatus.PROGRAM_STATUS_JOURNAL</code>.
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @return the list of archived transports
	 */
	public List<Transport> listArchivedTransports(long startdate, long enddate) throws SQLException;

	/**
	 * Returns a transport searched by id
	 * 
	 * @param transportId
	 *            the id of the transport
	 * @return transport
	 */
	public Transport getTransportById(int transportId) throws SQLException;

	/**
	 * Returns alle transports in the given interval by the given location
	 * (location_ID of the assignedVehicle (based on the vehicle current
	 * location))
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @param locationId
	 *            the locationId (real location of transports with a vehicle,
	 *            planned location of transports without location (Storno,
	 *            Leerfahrt, Weiterleitung)
	 */
	public List<Transport> listArchivedTransportsByVehicleLocationAndDate(long startdate, long enddate, int locationId) throws SQLException;

	/**
	 * Returns alle transports in the given interval by the given location
	 * (location_ID of the assignedVehicle (based on the vehicle current
	 * location)) and vehicle
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @param vehicleName
	 *            the vehicleName
	 * @param locationId
	 *            the locationId (real location of transports with a vehicle,
	 *            planned location of transports without location (Storno,
	 *            Leerfahrt, Weiterleitung)
	 */
	public List<Transport> listArchivedTransportsByVehicleLocationAndDateAndVehicle(long startdate, long enddate, int locationId, String vehicleName) throws SQLException;

	/**
	 * Returns alle transports in the given interval by the given location
	 * (planned location of the transport)
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @param locationId
	 *            the locationId (real location of transports with a vehicle,
	 *            planned location of transports without location (Storno,
	 *            Leerfahrt, Weiterleitung)
	 */
	public List<Transport> listArchivedTransportsByTransportLocationAndDate(long startdate, long enddate, int locationId) throws SQLException;

	/**
	 * Returns alle transports in the given interval by the given location
	 * (planned location of the transport)and vehicle
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @param locationId
	 *            the locationId (real location of transports with a vehicle,
	 *            planned location of transports without location (Storno,
	 *            Leerfahrt, Weiterleitung)
	 * @param vehicleName
	 *            the vehicleName
	 */
	public List<Transport> listArchivedTransportsByTransportLocationAndDateAndVehicle(long startdate, long enddate, int locationId, String vehicleName) throws SQLException;

	/**
	 * Returns all transports todo (prebooked and outstanding). Transports Todo
	 * are identified by the status
	 * <code>IProgramStatus.PROGRAM_STATUS_OUTSTANDING</code> or
	 * <code>IProgramStatus.PROGRAM_STATUS_PREBOOKED</code>. The transports are
	 * independend from the current date.
	 * 
	 * @return the list of all transports todo in the database
	 */
	public List<Transport> listTransportsTodo() throws SQLException;

	/**
	 * Returns all transports of the given vehicle in the given intervall with
	 * the status <code>IProgramStatus.PROGRAM_STATUS_JOURNAL</code>.
	 * 
	 * @param startdate
	 *            the start date
	 * @param enddate
	 *            the end date
	 * @param vehicleName
	 *            the vehicleName
	 * @return the list of archived transports of the given vehicle
	 */
	public List<Transport> listArchivedTransportsByVehicle(long startdate, long enddate, String vehicleName) throws SQLException;

	/**
	 * Returns a list of the transports with the program status 'journal' which
	 * are assigned to this vehicle and have no set transport status S6 yet.
	 * 
	 * @param vehicleName
	 *            the name of the vehicle to list the transports
	 * @return transports filtered by vehicle, program status 'journal' and
	 *         without transport status S6
	 */
	public List<Transport> listArchivedWithoutStatusSix(String vehicleName) throws SQLException;
}

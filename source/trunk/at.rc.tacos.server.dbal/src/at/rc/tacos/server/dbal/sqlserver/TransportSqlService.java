package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Patient;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CallerService;
import at.rc.tacos.platform.services.dbal.TransportService;
import at.rc.tacos.platform.util.MyUtils;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for transport.
 * 
 * @author Michael
 */
public class TransportSqlService implements TransportService, IProgramStatus {

	private Logger log = LoggerFactory.getLogger(TransportSqlService.class);

	@Resource(name = "sqlConnection")
	protected Connection connection;

	@Service(clazz = CallerService.class)
	private CallerService callerDAO;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public Transport getTransportById(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.transportById"));
		query.setInt(1, id);
		final ResultSet rs = query.executeQuery();
		if (rs.next()) {
			return setupTransport(rs);
		}
		// nothing in the result set
		return null;
	}

	@Override
	public int addTransport(Transport transport) throws SQLException {
		if (transport.getCallerDetail() != null) {
			int callerId = callerDAO.addCaller(transport.getCallerDetail());
			// assert the add was successfully
			if (callerId == -1)
				return TransportService.TRANSPORT_ERROR;
			transport.getCallerDetail().setId(callerId);
		}
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextTransportID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.transport"));
		query.setInt(1, id); // transport.getTransportId()
		query.setInt(2, transport.getTransportNumber());
		query.setInt(3, transport.getDirection());
		if (transport.getCallerDetail() == null)
			query.setString(4, null);
		else
			query.setInt(4, transport.getCallerDetail().getId());
		query.setString(5, transport.getNotes());
		query.setString(6, transport.getCreatedByUsername());
		query.setString(7, transport.getTransportPriority());
		query.setString(8, transport.getFeedback());
		query.setString(9, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.timeAndDateFormat));
		query.setString(10, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.timeAndDateFormat));
		query.setString(11, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.timeAndDateFormat));
		query.setString(12, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.timeAndDateFormat));
		if (transport.getKindOfTransport() == null)
			query.setString(13, "");
		else
			query.setString(13, transport.getKindOfTransport());
		if (transport.getKindOfIllness() == null)
			query.setString(14, null);
		else
			query.setString(14, transport.getKindOfIllness().getDiseaseName());
		if (transport.getPatient() == null) {
			query.setString(15, null);
			query.setString(16, null);
		}
		else {
			query.setString(15, transport.getPatient().getFirstname());
			query.setString(16, transport.getPatient().getLastname());
		}
		if (transport.getPlanedLocation() == null)
			query.setString(17, null);
		else
			query.setInt(17, transport.getPlanedLocation().getId());
		query.setString(18, transport.getFromStreet());
		query.setString(19, transport.getFromCity());
		query.setString(20, transport.getToStreet());
		query.setString(21, transport.getToCity());
		query.setInt(22, transport.getProgramStatus());
		query.setString(23, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.timeAndDateFormat));
		query.setString(24, transport.getDisposedByUsername());
		query.executeUpdate();

		transport.setTransportId(id);

		// assign the transport items!
		if (!assignTransportItems(transport))
			return TransportService.TRANSPORT_ERROR;
		// if the new transport is a nef transort (from
		// DuplicatePriorityATransportAction) the
		// vehicle and the status messages (S0 = AE) must be set
		if (transport.getTransportNumber() == Transport.TRANSPORT_NEF && transport.getVehicleDetail() != null) {
			// assign vehicle, assign transport items, assign transportstate
			if (!assignVehicle(transport)) {
				log.error("Assigning vehicle (NEF) failed");
				return TransportService.TRANSPORT_ERROR;
			}
			// assign the transport states S1,S2,....
			if (!assignTransportstate(transport)) {
				log.error("Assigning transport state failed");
				return TransportService.TRANSPORT_ERROR;
			}
		}
		return transport.getTransportId();
	}

	@Override
	public List<Transport> listRunningTransports() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.runningTransports"));
		query.setInt(1, IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		query.setInt(2, IProgramStatus.PROGRAM_STATUS_UNDERWAY);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listTransportsTodo() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.transportsToDo"));
		query.setInt(1, IProgramStatus.PROGRAM_STATUS_PREBOOKING);
		query.setInt(2, IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listPrebookedTransports() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.prebookedTransports"));
		query.setInt(1, IProgramStatus.PROGRAM_STATUS_PREBOOKING);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransports(long startdate, long enddate) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransports"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransportsByVehicle(long startdate, long enddate, String vehicleName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransportsByVehicle"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		query.setString(4, vehicleName);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransportsByVehicleLocationAndDate(long startdate, long enddate, int locationId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransportsByDateAndVehicleLocation"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		query.setInt(4, locationId);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransportsByVehicleLocationAndDateAndVehicle(long startdate, long enddate, int locationId, String vehicleName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransportsByDateAndVehicleLocationAndVehicle"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		query.setInt(4, locationId);
		query.setString(5, vehicleName);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransportsByTransportLocationAndDate(long startdate, long enddate, int locationId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.archivedTransportsByDateAndTransportLocation"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		query.setInt(4, locationId);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listArchivedTransportsByTransportLocationAndDateAndVehicle(long startdate, long enddate, int locationId, String vehicleName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries
				.getStatment("list.archivedTransportsByDateAndTransportLocationAndVehicle"));
		query.setString(1, MyUtils.timestampToString(startdate, MyUtils.timeAndDateFormat));
		query.setString(2, MyUtils.timestampToString(enddate, MyUtils.timeAndDateFormat));
		query.setInt(3, PROGRAM_STATUS_JOURNAL);
		query.setInt(4, locationId);
		query.setString(5, vehicleName);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listUnderwayTransports() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.underwayTransports"));
		query.setInt(1, IProgramStatus.PROGRAM_STATUS_UNDERWAY);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	@Override
	public List<Transport> listUnderwayTransportsByVehicle(String vehicleName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.underwayTransportsByVehicle"));
		query.setInt(1, IProgramStatus.PROGRAM_STATUS_UNDERWAY);
		query.setString(2, vehicleName);
		final ResultSet rs = query.executeQuery();
		return setupTransportList(rs);
	}

	/**
	 * Updates the transport record in the database with the current values form
	 * the specified transport
	 * 
	 * @param transport
	 *            the transport to update
	 * @return true if the update was successfully otherwise false
	 */
	@Override
	public boolean updateTransport(Transport transport) throws SQLException {
		/** add or update the caller of the transport */
		if (transport.getCallerDetail() != null && transport.getCallerDetail().getId() == 0) {
			int callerId = callerDAO.addCaller(transport.getCallerDetail());
			// assert the add was successfully
			if (callerId == -1) {
				log.error("Failed to add the caller '" + transport.getCallerDetail() + "' to the transport '" + transport + "'");
				return false;
			}
			transport.getCallerDetail().setId(callerId);
		}
		else if (transport.getCallerDetail() != null && transport.getCallerDetail().getId() != 0) {
			if (!callerDAO.updateCaller(transport.getCallerDetail())) {
				log.error("Failed to update the caller '" + transport.getCallerDetail() + "' from transport '" + transport + "'");
				return false;
			}
		}

		/** update the vehicle of the transport */
		if (transport.getVehicleDetail() != null) {
			if (!updateAssignedVehicleOfTransport(transport.getVehicleDetail(), transport.getTransportId())) {
				log.error("Failed to update the assigned vehicle '" + transport.getVehicleDetail() + "''" + transport + "'");
				return false;
			}
		}

		// update the transport
		if (!doUpdateTransport(transport)) {
			log.error("Transport update failed");
			return false;
		}

		// assign the boolean values police,fire alarming, ...
		if (!assignTransportItems(transport)) {
			log.error("Assigning transport items failed");
			return false;
		}

		// assign the transport states S1,S2,....
		if (!assignTransportstate(transport)) {
			log.error("Assigning transport state failed");
			return false;
		}

		// everything is ok
		return true;
	}

	@Override
	public int generateTransportNumber(Transport transport) throws SQLException {
		// assert that we have a current station and a vehicle
		if (transport.getVehicleDetail() == null || transport.getVehicleDetail().getCurrentStation() == null)
			return TransportService.TRANSPORT_ERROR;

		int locationId = transport.getVehicleDetail().getCurrentStation().getId();

		// STEP1: INSERT the vehicle in the assigned_vehicle table
		if (!assignVehicle(transport))
			return TransportService.TRANSPORT_ERROR;

		// STEP2: Query the tmptransport
		int tmpNr = getTransportNrFromTemp(locationId, Calendar.getInstance().get(Calendar.YEAR));
		// transport number valid, so return it
		if (tmpNr > 0) {
			if (!updateTransportNr(transport.getTransportId(), tmpNr, Calendar.getInstance().get(Calendar.YEAR)))
				return TransportService.TRANSPORT_ERROR;
			return tmpNr;
		}

		// check for errors
		if (tmpNr == TransportService.TRANSPORT_ERROR)
			return TransportService.TRANSPORT_ERROR;

		// get the highest number from the table and update the transport
		tmpNr = getHighestTransportNumber(locationId, Calendar.getInstance().get(Calendar.YEAR));
		tmpNr++;
		if (!updateTransportNr(transport.getTransportId(), tmpNr, Calendar.getInstance().get(Calendar.YEAR)))
			return TransportService.TRANSPORT_ERROR;
		return tmpNr;
	}

	@Override
	public boolean removeVehicleFromTransport(Transport transport) throws SQLException {
		int oldNumber = getTransportNrById(transport.getTransportId());
		int transportId = transport.getTransportId();
		int locationId = getLocationOfTransport(transport.getTransportId());
		int actualYear = Calendar.getInstance().get(Calendar.YEAR);

		// remove the vehicle from the transport
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("remove.assignedVehicle"));
		query.setInt(1, transportId);
		// assert the remove was successfully
		if (query.executeUpdate() == 0)
			return false;

		// reset the transport number
		transport.setTransportNumber(0);
		if (!updateTransportNr(transport.getTransportId(), transport.getTransportNumber(), Calendar.getInstance().get(Calendar.YEAR)))
			return false;

		// store the number in the temp table, but don't store the nef
		// transport number (-4)
		if (oldNumber != Transport.TRANSPORT_NEF) {
			if (!archiveTransportNumber(oldNumber, locationId, actualYear)) {
				return false;
			}
		}

		// set the transport status to PROGRAM_STATUS_OUTSTANDING
		transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
		if (!doUpdateTransport(transport))
			return false;

		return true;
	}

	@Override
	public boolean cancelTransport(Transport transport) throws SQLException {
		int locationId = 0;
		int transportId = transport.getTransportId();
		int oldNumber = getTransportNrById(transport.getTransportId());
		int actualYear = Calendar.getInstance().get(Calendar.YEAR);
		if (transport.getVehicleDetail() != null) {
			locationId = transport.getVehicleDetail().getCurrentStation().getId();
		}
		// archive the transport number if we have one
		if (oldNumber > 0) {
			if (!archiveTransportNumber(oldNumber, locationId, actualYear)) {
				return false;
			}
		}
		// set the transport number to TRANSPORT_STORNO or TRANSPORT_FORWARD
		if (!updateTransportNr(transportId, transport.getTransportNumber(), Calendar.getInstance().get(Calendar.YEAR))) {
			return false;
		}
		// set the program status to journal
		transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
		return true;
	}

	/***********************************************
	 * HELPER METHODS FOR THE TRANSPORT DAO *
	 *********************************************/
	/**
	 * Assigns the vehicle to the transport.
	 * 
	 * @param transport
	 *            the transport to assign the vehicle to
	 * @return true if the insert was successfully
	 */
	private boolean assignVehicle(Transport transport) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.assignedVehicle"));
		query.setInt(1, transport.getTransportId());
		query.setString(2, transport.getVehicleDetail().getVehicleName());
		query.setString(3, transport.getVehicleDetail().getVehicleType());
		// the driver
		if (transport.getVehicleDetail().getDriver() != null) {
			query.setInt(4, transport.getVehicleDetail().getDriver().getStaffMemberId());
			query.setString(5, transport.getVehicleDetail().getDriver().getLastName());
			query.setString(6, transport.getVehicleDetail().getDriver().getFirstName());
		}
		else {
			query.setString(4, null);
			query.setString(5, null);
			query.setString(6, null);
		}
		// first medic
		if (transport.getVehicleDetail().getFirstParamedic() != null) {
			query.setInt(7, transport.getVehicleDetail().getFirstParamedic().getStaffMemberId());
			query.setString(8, transport.getVehicleDetail().getFirstParamedic().getLastName());
			query.setString(9, transport.getVehicleDetail().getFirstParamedic().getFirstName());
		}
		else {
			query.setString(7, null);
			query.setString(8, null);
			query.setString(9, null);
		}
		// second medic
		if (transport.getVehicleDetail().getSecondParamedic() != null) {
			query.setInt(10, transport.getVehicleDetail().getSecondParamedic().getStaffMemberId());
			query.setString(11, transport.getVehicleDetail().getSecondParamedic().getLastName());
			query.setString(12, transport.getVehicleDetail().getSecondParamedic().getFirstName());
		}
		else {
			query.setString(10, null);
			query.setString(11, null);
			query.setString(12, null);
		}
		// the location
		if (transport.getVehicleDetail().getCurrentStation() != null) {
			query.setInt(13, transport.getVehicleDetail().getCurrentStation().getId());
			query.setString(14, transport.getVehicleDetail().getCurrentStation().getLocationName());
		}
		else {
			query.setString(13, null);
			query.setString(14, null);
		}
		query.setString(15, transport.getVehicleDetail().getVehicleNotes());
		// assert the vehicle is inserted
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	private boolean updateAssignedVehicleOfTransport(VehicleDetail vehicle, int transportId) throws SQLException {
		// UPDATE assigned_vehicle SET vehicle_ID = ?, vehicletype = ?,
		// driver_ID = ?, driver_lastname = ?, driver_firstname = ?,
		// medic1_ID = ?, medic1_lastname = ?,medic1_firstname = ?,
		// medic2_ID = ? , medic2_lastname = ?, medic2_firstname = ?,
		// location_ID = ?, location_name = ?, note = ? WHERE transport_ID =
		// ?;
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.assignedVehicle"));
		query.setString(1, vehicle.getVehicleName());
		query.setString(2, vehicle.getVehicleType());
		// assert we have a driver
		if (vehicle.getDriver() == null) {
			query.setString(3, null);
			query.setString(4, null);
			query.setString(5, null);
		}
		else {
			query.setInt(3, vehicle.getDriver().getStaffMemberId());
			query.setString(4, vehicle.getDriver().getLastName());
			query.setString(5, vehicle.getDriver().getFirstName());
		}
		// assert we have a first medic
		if (vehicle.getFirstParamedic() == null) {
			query.setString(6, null);
			query.setString(7, null);
			query.setString(8, null);
		}
		else {
			query.setInt(6, vehicle.getFirstParamedic().getStaffMemberId());
			query.setString(7, vehicle.getFirstParamedic().getLastName());
			query.setString(8, vehicle.getFirstParamedic().getFirstName());
		}
		// assert we have a secondary medic
		if (vehicle.getSecondParamedic() == null) {
			query.setString(9, null);
			query.setString(10, null);
			query.setString(11, null);
		}
		else {
			query.setInt(9, vehicle.getSecondParamedic().getStaffMemberId());
			query.setString(10, vehicle.getSecondParamedic().getLastName());
			query.setString(11, vehicle.getSecondParamedic().getFirstName());
		}
		if (vehicle.getCurrentStation() == null) {
			query.setString(12, null);
			query.setString(13, null);
		}
		else {
			query.setInt(12, vehicle.getCurrentStation().getId());
			query.setString(13, vehicle.getCurrentStation().getLocationName());
		}
		query.setString(14, vehicle.getVehicleNotes());
		query.setInt(15, transportId);
		if (query.executeUpdate() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Searches for a canceled transport numer.
	 * 
	 * @param locationId
	 *            the location to search the transport number
	 * @return the canceled transport number, -1 if nothing found or -3 if an
	 *         error occured
	 */
	private int getTransportNrFromTemp(int locationId, int year) throws SQLException {
		// search through the tmp table whether tmp transportNumber exists
		// or not
		final PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.tmpTransportNr"));
		queryStmt.setInt(1, locationId);
		queryStmt.setInt(2, year);
		final ResultSet rs = queryStmt.executeQuery();
		// return the transport number and remove the value from the table
		if (rs.next()) {
			// the found transport number
			int transportNr = rs.getInt("transportNr");

			// remove from the table
			final PreparedStatement removeStmt = connection.prepareStatement(queries.getStatment("remove.tmpTransportNr"));
			removeStmt.setInt(1, locationId);
			removeStmt.setInt(2, transportNr);
			removeStmt.setInt(3, year);
			// assert the remove was successfully
			if (removeStmt.executeUpdate() == 0)
				return TransportService.TRANSPORT_ERROR;

			// we have a transport number and removed the old from the temp
			return transportNr;
		}
		// no transport number found
		return -1;
	}

	/**
	 * Returns the highest transport number in the transport table for the given
	 * year and location. If no transport number matches the criteria the method
	 * will return 0<br>
	 * <b>The returned number have to be count up by one before use</b>
	 * 
	 * @param location
	 *            the location to get the number
	 * @param year
	 *            the year to get the number
	 * @return the found transport number.
	 */
	private int getHighestTransportNumber(int locationId, int year) throws SQLException {
		// calculate new transport number from transports
		final PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.MaxTransportNr"));
		queryStmt.setInt(1, locationId);
		queryStmt.setString(2, new Integer(year).toString());
		final ResultSet rs = queryStmt.executeQuery();
		// assert we have a result
		if (rs.next()) {
			int nr = rs.getInt(1);
			return nr;
		}
		// nothing found so return the number 0 to start again
		return 0;
	}

	/**
	 * Sets the transport number for the given transport
	 * 
	 * @param transportId
	 *            the transport to set the number
	 * @param transportNr
	 *            the number to set
	 * @return true if the update was successfully
	 */
	private boolean updateTransportNr(int transportId, int transportNr, int year) throws SQLException {
		PreparedStatement updateStmt = connection.prepareStatement(queries.getStatment("update.transportNr"));
		updateStmt.setInt(1, transportNr);
		updateStmt.setString(2, new Integer(year).toString());
		updateStmt.setInt(3, transportId);
		// assert the transport is updated
		if (updateStmt.executeUpdate() == 0)
			return false;
		return true;
	}

	/**
	 * Returns the transport number identified by the transport id.
	 * 
	 * @param transportId
	 *            the transport to get the number from
	 * @return the transport number or -1 in case of an error
	 */
	private int getTransportNrById(int transportId) throws SQLException {
		PreparedStatement queryStmt = connection.prepareStatement(queries.getStatment("get.transportNr"));
		queryStmt.setInt(1, transportId);
		ResultSet rs = queryStmt.executeQuery();
		if (rs.next())
			return rs.getInt("transportNr");
		// no result set
		return -1;
	}

	/**
	 * Returns the location to which the vehicle of the given transport is
	 * assigned
	 * 
	 * @param transportId
	 *            the id of the transport to get the location
	 * @return the assigned location
	 */
	private int getLocationOfTransport(int transportId) throws SQLException {
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.LocationFromTransport"));
		stmt.setInt(1, transportId);
		ResultSet rs = stmt.executeQuery();
		if (rs.next())
			return rs.getInt("location_ID");
		// nothing found
		return -1;
	}

	/**
	 * Writes the current transport number in the tmp table so that it can be
	 * reused
	 * 
	 * @param transportNr
	 *            the transport number
	 * @param locationId
	 *            the location id
	 * @return if the archive was successfull
	 */
	private boolean archiveTransportNumber(int transportNr, int locationId, int actualYear) throws SQLException {
		// adds the old transportnumber to the tmp table
		final PreparedStatement query3 = connection.prepareStatement(queries.getStatment("add.tmpTransport"));
		query3.setInt(1, transportNr);
		query3.setInt(2, locationId);
		query3.setString(3, new Integer(actualYear).toString());
		// assert the archive was successfully
		if (query3.executeUpdate() == 0)
			return false;
		return true;
	}

	/**
	 * Assigns a new transport state to an existing transport
	 * 
	 * @param transport
	 * @return boolean if insert was successful or not
	 */
	private boolean assignTransportstate(Transport transport) throws SQLException {
		// remove all curren states to set them new
		if (!removeTransportstate(transport))
			return false;
		for (Entry<Integer, Long> entry : transport.getStatusMessages().entrySet()) {
			int state = entry.getKey();
			long time = entry.getValue();

			// transportstate, transport_ID, date
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.transportstate"));
			query.setInt(1, state);
			query.setInt(2, transport.getTransportId());
			query.setString(3, MyUtils.timestampToString(time, MyUtils.timeAndDateFormat));
			if (query.executeUpdate() == 0)
				return false;
		}
		return true;
	}

	/**
	 * Clears the transportstate table.
	 * 
	 * @param transport
	 *            the transport to clear the state table
	 * @return boolean if remove was successful or not
	 */
	private boolean removeTransportstate(Transport transport) throws SQLException {
		final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("remove.transportstate"));
		query1.setInt(1, transport.getTransportId());
		// remove the transport state
		query1.executeUpdate();
		return true;
	}

	/**
	 * inserts a new transportstate to the database
	 * 
	 * @param transportId
	 * @param selectedId
	 * @return true if insert was successful
	 */
	private boolean addTransportItem(int transportId, long timestamp, int selectedId) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("add.selectedTransportItem"));
		query.setInt(1, selectedId);
		query.setString(2, MyUtils.timestampToString(timestamp, MyUtils.timeAndDateFormat));
		query.setInt(3, transportId);
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	/**
	 * Helper method to setup a transport instance with the result set.
	 * 
	 * @param the
	 *            result set from the database
	 * @return the transport instance
	 */
	private Transport setupTransport(ResultSet rs) throws SQLException {
		// create the new transport
		Transport transport = new Transport();
		transport.setTransportId(rs.getInt("transport_ID"));
		transport.setTransportNumber(rs.getInt("transportNr"));

		if (rs.getInt("planned_location") != 0) {
			Location station = new Location();
			station.setId(rs.getInt("planned_location"));
			station.setLocationName(rs.getString("locationname"));
			transport.setPlanedLocation(station);
		}

		if (rs.getInt("caller_ID") != 0) {
			CallerDetail caller = new CallerDetail();
			caller.setId(rs.getInt("caller_ID"));
			caller.setCallerName(rs.getString("callername"));
			caller.setCallerTelephoneNumber(rs.getString("caller_phonenumber"));
			transport.setCallerDetail(caller);
		}
		transport.setCreatedByUsername(rs.getString("createdBy_user"));
		if (rs.getString("disposedBy_user") != null)
			transport.setDisposedByUsername(rs.getString("disposedBy_user"));
		if (rs.getString("note") == null)
			transport.setNotes("");
		else
			transport.setNotes(rs.getString("note"));
		if (rs.getString("feedback") == null)
			transport.setFeedback("");
		else
			transport.setFeedback(rs.getString("feedback"));

		transport.setCreationTime(MyUtils.stringToTimestamp(rs.getString("creationDate"), MyUtils.sqlServerDateTime));
		transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp(rs.getString("departure"), MyUtils.sqlServerDateTime));
		transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp(rs.getString("appointment"), MyUtils.sqlServerDateTime));
		transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp(rs.getString("appointmentPatient"), MyUtils.sqlServerDateTime));

		// setup the disease
		if (rs.getString("disease") != null) {
			Disease disease = new Disease();
			disease.setDiseaseName(rs.getString("disease"));
			transport.setKindOfIllness(disease);
		}
		// set the from address
		transport.setFromStreet(rs.getString("from_street"));
		transport.setFromCity(rs.getString("from_city"));

		// set the to address
		transport.setToStreet(rs.getString("to_street"));
		transport.setToCity(rs.getString("to_city"));
		transport.setProgramStatus(rs.getInt("programstate"));

		// setup the transport type
		transport.setKindOfTransport(rs.getString("transporttype"));

		transport.setTransportPriority(rs.getString("priority"));
		transport.setDirection(rs.getInt("direction"));
		transport.setDateOfTransport(MyUtils.stringToTimestamp(rs.getString("dateOfTransport"), MyUtils.sqlServerDateTime));

		// The patient of the transport
		if (rs.getString("firstname") != null || rs.getString("lastname") != null) {
			Patient patient = new Patient();
			patient.setFirstname(rs.getString("firstname"));
			patient.setLastname(rs.getString("lastname"));
			transport.setPatient(patient);
		}

		// The assigned vehicle of the transport
		if (rs.getString("location_ID") != null) {
			// get the location
			Location location = new Location();
			location.setId(rs.getInt("location_ID"));
			location.setLocationName(rs.getString("location_name"));
			// get the vehicle
			VehicleDetail vehicle = new VehicleDetail();
			vehicle.setCurrentStation(location);
			vehicle.setVehicleName(rs.getString("vehicle_ID"));
			vehicle.setVehicleNotes(rs.getString("note"));
			vehicle.setVehicleType(rs.getString("vehicletype"));

			// the staff of the vehicle
			if (rs.getString("driver_ID") != null) {
				StaffMember driver = new StaffMember();
				driver.setStaffMemberId(rs.getInt("driver_ID"));
				driver.setLastName(rs.getString("driver_lastname"));
				driver.setFirstName(rs.getString("driver_firstname"));
				vehicle.setDriver(driver);
			}
			// test the first medic
			if (rs.getString("medic1_ID") != null) {
				StaffMember medic1 = new StaffMember();
				medic1.setStaffMemberId(rs.getInt("medic1_ID"));
				medic1.setLastName(rs.getString("medic1_lastname"));
				medic1.setFirstName(rs.getString("medic1_firstname"));
				vehicle.setFirstParamedic(medic1);
			}
			// test the second medic
			if (rs.getString("medic2_ID") != null) {
				StaffMember medic2 = new StaffMember();
				medic2.setStaffMemberId(rs.getInt("medic2_ID"));
				medic2.setLastName(rs.getString("medic2_lastname"));
				medic2.setFirstName(rs.getString("medic2_firstname"));
				vehicle.setSecondParamedic(medic2);
			}
			transport.setVehicleDetail(vehicle);
		}

		// get the transport states
		final PreparedStatement stateQuery = connection.prepareStatement(queries.getStatment("list.transportstates"));
		stateQuery.setInt(1, transport.getTransportId());
		final ResultSet stateResult = stateQuery.executeQuery();
		// loop over the result set
		while (stateResult.next()) {
			transport.addStatus(stateResult.getInt("transportstate"), MyUtils.stringToTimestamp(stateResult.getString("date"),
					MyUtils.sqlServerDateTime));
		}

		// find the selected items (boolean values)
		querySelectedItems(transport);

		return transport;
	}

	/**
	 * Helper method to setup a list of transports from an result set.
	 * 
	 * @param rs
	 *            the unmodified result set from the query
	 * @return the list of transports
	 */
	private List<Transport> setupTransportList(ResultSet rs) throws SQLException {
		List<Transport> transports = new ArrayList<Transport>();
		while (rs.next()) {
			Transport transport = setupTransport(rs);
			transports.add(transport);
		}
		return transports;
	}

	/**
	 * Helper method that queries the selected items and sets them
	 * 
	 * @param transport
	 *            the transport to set the selected states
	 */
	private void querySelectedItems(Transport transport) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.selectedTransportItems"));
		query.setInt(1, transport.getTransportId());
		final ResultSet resultSet = query.executeQuery();
		// loop over the result set and set the states
		while (resultSet.next()) {
			if (resultSet.getInt("selected_ID") == 1) {
				transport.setEmergencyDoctorAlarming(true);
				transport.settimestampNA(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}
			if (resultSet.getInt("selected_ID") == 2) {
				transport.setPoliceAlarming(true);
				transport.settimestampPolizei(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 3) {
				transport.setFirebrigadeAlarming(true);
				transport.settimestampFW(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 4) {
				transport.setMountainRescueServiceAlarming(true);
				transport.settimestampBergrettung(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 5) {
				transport.setDfAlarming(true);
				transport.settimestampDF(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 6) {
				transport.setBrkdtAlarming(true);
				transport.settimestampBRKDT(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 7) {
				transport.setBlueLightToGoal(true);
			}

			if (resultSet.getInt("selected_ID") == 8) {
				transport.setHelicopterAlarming(true);
				transport.settimestampRTH(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 9) {
				transport.setAssistantPerson(true);
			}

			if (resultSet.getInt("selected_ID") == 10) {
				transport.setBackTransport(true);
			}

			if (resultSet.getInt("selected_ID") == 11) {
				transport.setLongDistanceTrip(true);
			}

			if (resultSet.getInt("selected_ID") == 12) {
				transport.setEmergencyPhone(true);
			}

			if (resultSet.getInt("selected_ID") == 13) {
				transport.setKITAlarming(true);
				transport.settimestampKIT(MyUtils.stringToTimestamp(resultSet.getString("alarmingDateTime"), MyUtils.sqlServerDateTime));
			}

			if (resultSet.getInt("selected_ID") == 14) {
				transport.setBlueLight1(true);
			}
		}
	}

	/**
	 * Removes all old transport items and sets the new value in the database
	 * 
	 * @param transport
	 * @return true if update was ok
	 */
	private boolean assignTransportItems(Transport transport) throws SQLException {
		// removes all selected transports for specific transportId
		if (!removeTransportItems(transport)) {
			log.error("Failed to clear the selected transports");
			return false;
		}
		if (transport.isEmergencyDoctorAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampNA(), 1);
		if (transport.isPoliceAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampPolizei(), 2);
		if (transport.isFirebrigadeAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampFW(), 3);
		if (transport.isMountainRescueServiceAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampBergrettung(), 4);
		if (transport.isDfAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampDF(), 5);
		if (transport.isBrkdtAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampBRKDT(), 6);
		if (transport.isBlueLightToGoal() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 7);
		if (transport.isHelicopterAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampRTH(), 8);
		if (transport.isAssistantPerson() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 9);
		if (transport.isBackTransport() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 10);
		if (transport.isLongDistanceTrip() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 11);
		if (transport.isEmergencyPhone() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 12);
		if (transport.isKITAlarming() == true)
			addTransportItem(transport.getTransportId(), transport.getTimestampKIT(), 13);
		if (transport.isBlueLight1() == true)
			addTransportItem(transport.getTransportId(), transport.getCreationTime(), 14);
		return true;
	}

	/**
	 * Removes all selected items from a transport in the database
	 * 
	 * @param transport
	 * @return true if all items were removed
	 */
	private boolean removeTransportItems(Transport transport) throws SQLException {
		final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("remove.AllSelectedTransportItems"));
		query1.setInt(1, transport.getTransportId());
		query1.executeUpdate();
		return true;
	}

	private boolean doUpdateTransport(Transport transport) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.transport"));
		query.setInt(1, transport.getDirection());
		// no caller, so set to null
		if (transport.getCallerDetail() == null)
			query.setString(2, null);
		else
			query.setInt(2, transport.getCallerDetail().getId());
		query.setString(3, transport.getNotes());
		query.setString(4, transport.getCreatedByUsername());
		query.setString(5, transport.getTransportPriority());
		query.setString(6, transport.getFeedback());
		query.setString(7, MyUtils.timestampToString(transport.getCreationTime(), MyUtils.timeAndDateFormat));
		query.setString(8, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.timeAndDateFormat));
		query.setString(9, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.timeAndDateFormat));
		query.setString(10, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.timeAndDateFormat));
		if (transport.getKindOfTransport() != null)
			query.setString(11, transport.getKindOfTransport());
		else
			query.setString(11, "");
		if (transport.getKindOfIllness() == null)
			query.setString(12, null);
		else
			query.setString(12, transport.getKindOfIllness().getDiseaseName());
		if (transport.getPatient() == null) {
			query.setString(13, null);
			query.setString(14, null);
		}
		else {
			query.setString(13, transport.getPatient().getFirstname());
			query.setString(14, transport.getPatient().getLastname());
		}
		if (transport.getPlanedLocation() == null)
			query.setString(15, null);
		else
			query.setInt(15, transport.getPlanedLocation().getId());
		query.setString(16, transport.getFromStreet());
		query.setString(17, transport.getFromCity());
		query.setString(18, transport.getToStreet());
		query.setString(19, transport.getToCity());
		query.setInt(20, transport.getProgramStatus());
		query.setString(21, MyUtils.timestampToString(transport.getDateOfTransport(), MyUtils.timeAndDateFormat));
		query.setInt(22, transport.getTransportNumber());
		query.setString(23, transport.getDisposedByUsername());
		query.setInt(24, transport.getTransportId());
		// assert the update was successful
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}
}

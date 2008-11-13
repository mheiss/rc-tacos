package at.rc.tacos.server.dbal.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.platform.services.dbal.StaffMemberService;
import at.rc.tacos.platform.services.dbal.VehicleService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for vehicle detail.
 * 
 * @author Michael
 */
public class VehicleDetailSqlService implements VehicleService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	@Service(clazz = LocationService.class)
	private LocationService locationDAO;

	@Service(clazz = StaffMemberService.class)
	private StaffMemberService staffMemberDAO;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public boolean addVehicle(VehicleDetail vehicle) throws SQLException {
		// vehicle_ID, vehicletype, primaryLocation, currentLocation,
		// phonenumber_ID, note, readyForAction, outOfOrder
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.vehicle"));
		query.setString(1, vehicle.getVehicleName());
		query.setString(2, vehicle.getVehicleType());
		query.setInt(3, vehicle.getBasicStation().getId());
		query.setInt(4, vehicle.getCurrentStation().getId());
		query.setInt(5, vehicle.getMobilePhone().getId());
		query.setString(6, vehicle.getVehicleNotes());
		query.setString(7, vehicle.getLastDestinationFree());
		query.setBoolean(8, vehicle.isReadyForAction());
		query.setBoolean(9, vehicle.isOutOfOrder());
		// assert the vehicle was added
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public VehicleDetail getVehicleByName(String vehicleName) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.vehicleByID"));
		query.setString(1, vehicleName);
		final ResultSet rs = query.executeQuery();

		// v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID,
		// v.currentLocation, v.primaryLocation, lo.locationname,
		// lo.location_ID,
		// v.vehicletype, v.readyForAction, v.outOfOrder, v.transportStatus,
		// v.phonenumber_ID, pn.phonenumber, v.note
		if (rs.next()) {
			VehicleDetail vehicle = new VehicleDetail();
			vehicle.setVehicleName(rs.getString("vehicle_ID"));
			vehicle.setVehicleType(rs.getString("vehicletype"));
			vehicle.setReadyForAction(rs.getBoolean("readyForAction"));
			vehicle.setOutOfOrder(rs.getBoolean("outOfOrder"));
			vehicle.setVehicleNotes(rs.getString("note"));
			vehicle.setLastDestinationFree(rs.getString("lastDestinationFree"));
			vehicle.setTransportStatus(rs.getInt("transportStatus"));
			// the mobile phone for the vehicle
			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			vehicle.setMobilPhone(phone);
			// the basic location
			Location basicStation = locationDAO.getLocation(rs.getInt("primaryLocation"));
			vehicle.setBasicStation(basicStation);
			// the current location
			Location currentStation = locationDAO.getLocation(rs.getInt("currentLocation"));
			vehicle.setCurrentStation(currentStation);
			// the driver
			StaffMember driver = staffMemberDAO.getStaffMemberByID(rs.getInt("driver_ID"));
			vehicle.setDriver(driver);
			// the first paramedic
			StaffMember firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("medic1_ID"));
			vehicle.setFirstParamedic(firstParamedic);
			// the second paramedic
			StaffMember secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("medic2_ID"));
			vehicle.setSecondParamedic(secondParamedic);
			return vehicle;
		}
		// no result set
		return null;
	}

	@Override
	public List<VehicleDetail> listVehicles() throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.vehicles"));
		final ResultSet rs = query.executeQuery();
		// v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID,
		// v.currentLocation, v.primaryLocation, lo.locationname,
		// lo.location_ID,
		// v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID,
		// pn.phonenumber, v.note
		List<VehicleDetail> vehicles = new ArrayList<VehicleDetail>();
		while (rs.next()) {
			VehicleDetail vehicle = new VehicleDetail();
			vehicle.setVehicleName(rs.getString("vehicle_ID"));
			vehicle.setVehicleType(rs.getString("vehicletype"));
			vehicle.setReadyForAction(rs.getBoolean("readyForAction"));
			vehicle.setOutOfOrder(rs.getBoolean("outOfOrder"));
			vehicle.setVehicleNotes(rs.getString("note"));
			vehicle.setLastDestinationFree(rs.getString("lastDestinationFree"));
			vehicle.setTransportStatus(rs.getInt("transportStatus"));

			MobilePhoneDetail phone = new MobilePhoneDetail();
			phone.setId(rs.getInt("phonenumber_ID"));
			phone.setMobilePhoneNumber(rs.getString("phonenumber"));
			phone.setMobilePhoneName(rs.getString("phonename"));
			vehicle.setMobilPhone(phone);

			Location basicStation = locationDAO.getLocation(rs.getInt("primaryLocation"));
			vehicle.setBasicStation(basicStation);

			Location currentStation = locationDAO.getLocation(rs.getInt("currentLocation"));
			vehicle.setCurrentStation(currentStation);

			StaffMember driver = staffMemberDAO.getStaffMemberByID(rs.getInt("driver_ID"));
			vehicle.setDriver(driver);

			StaffMember firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("medic1_ID"));
			vehicle.setFirstParamedic(firstParamedic);

			StaffMember secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("medic2_ID"));
			vehicle.setSecondParamedic(secondParamedic);
			vehicles.add(vehicle);
		}
		return vehicles;
	}

	@Override
	public boolean removeVehicle(VehicleDetail vehicle) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.vehicle"));
		query.setString(1, vehicle.getVehicleName());
		// assert the vehicle was removed
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateVehicle(VehicleDetail vehicle) throws SQLException {
		// driver_ID = ?, medic1_ID = ?, medic2_ID = ?, phonenumber_ID = ?,
		// vehicletype = '?', currentLocation = ?,
		// primaryLocation = ?, note = '?', readyForAction = ?, outOfOrder =
		// ?, transportStatus = ? where vehicle_ID = '?';
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.vehicle"));
		// assert we have a driver
		if (vehicle.getDriver() == null)
			query.setString(1, null);
		else
			query.setInt(1, vehicle.getDriver().getStaffMemberId());
		// assert we have a medic
		if (vehicle.getFirstParamedic() == null)
			query.setString(2, null);
		else
			query.setInt(2, vehicle.getFirstParamedic().getStaffMemberId());
		// assert we have a secondary medic
		if (vehicle.getSecondParamedic() == null)
			query.setString(3, null);
		else
			query.setInt(3, vehicle.getSecondParamedic().getStaffMemberId());
		// assert we have a phone
		if (vehicle.getMobilePhone() == null)
			query.setString(4, null);
		else
			query.setInt(4, vehicle.getMobilePhone().getId());
		// update the other properties
		query.setString(5, vehicle.getVehicleType());
		query.setInt(6, vehicle.getCurrentStation().getId());
		query.setInt(7, vehicle.getBasicStation().getId());
		query.setString(8, vehicle.getVehicleNotes());
		query.setString(9, vehicle.getLastDestinationFree());
		query.setBoolean(10, vehicle.isReadyForAction());
		query.setBoolean(11, vehicle.isOutOfOrder());
		query.setInt(12, vehicle.getTransportStatus());
		query.setString(13, vehicle.getVehicleName());
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}
}

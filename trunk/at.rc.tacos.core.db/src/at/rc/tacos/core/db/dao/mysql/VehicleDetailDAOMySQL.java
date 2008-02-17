package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.*;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailDAOMySQL implements VehicleDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();
	//the dependent dao classes
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();

	@Override
	public boolean addVehicle(VehicleDetail vehicle) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			//vehicle_ID, vehicletype, primaryLocation, currentLocation, phonenumber_ID, note, readyForAction, outOfOrder
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.vehicle"));
			query.setString(1, vehicle.getVehicleName());
			query.setString(2, vehicle.getVehicleType());
			query.setInt(3, vehicle.getBasicStation().getId());
			query.setInt(4, vehicle.getCurrentStation().getId());
			query.setInt(5, vehicle.getMobilePhone().getId());
			query.setString(6, vehicle.getVehicleNotes());
			query.setBoolean(7, vehicle.isReadyForAction());
			query.setBoolean(8, vehicle.isOutOfOrder());
			//assert the vehicle was added
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public VehicleDetail getVehicleByName(String vehicleName) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.vehicleByID"));
			query.setString(1, vehicleName);
			final ResultSet rs = query.executeQuery();

			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.transportStatus, v.phonenumber_ID, pn.phonenumber, v.note
			if(rs.first())
			{
				VehicleDetail vehicle = new VehicleDetail();
				vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
				vehicle.setVehicleType(rs.getString("v.vehicletype"));
				vehicle.setReadyForAction(rs.getBoolean("v.readyForAction"));
				vehicle.setOutOfOrder(rs.getBoolean("v.outOfOrder"));
				vehicle.setVehicleNotes(rs.getString("v.note"));
				vehicle.setTransportStatus(rs.getInt("v.transportStatus"));
				//the mobile phone for the vehicle
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("v.phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				vehicle.setMobilPhone(phone);
				//the basic location
				Location basicStation = new Location();
				basicStation = locationDAO.getLocation(rs.getInt("v.primaryLocation"));
				vehicle.setBasicStation(basicStation);
				//the current location
				Location currentStation = new Location();
				currentStation = locationDAO.getLocation(rs.getInt("v.currentLocation"));
				vehicle.setCurrentStation(currentStation);
				//the driver
				StaffMember driver = new StaffMember();
				driver = staffMemberDAO.getStaffMemberByID(rs.getInt("v.driver_ID"));
				vehicle.setDriver(driver);
				//the first paramedic
				StaffMember firstParamedic = new StaffMember();
				firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic1_ID"));
				vehicle.setFirstParamedic(firstParamedic);
				//the second paramedic
				StaffMember secondParamedic = new StaffMember();
				secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic2_ID"));
				vehicle.setSecondParamedic(secondParamedic);
				return vehicle;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public List<VehicleDetail> listVehicles() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.vehicles"));
			final ResultSet rs = query.executeQuery();
			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID, pn.phonenumber, v.note
			List<VehicleDetail> vehicles = new ArrayList<VehicleDetail>();
			while(rs.next())
			{
				VehicleDetail vehicle = new VehicleDetail();
				vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
				vehicle.setVehicleType(rs.getString("v.vehicletype"));
				vehicle.setReadyForAction(rs.getBoolean("v.readyForAction"));
				vehicle.setOutOfOrder(rs.getBoolean("v.outOfOrder"));
				vehicle.setVehicleNotes(rs.getString("v.note"));

				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("v.phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				vehicle.setMobilPhone(phone);

				Location basicStation = new Location();
				basicStation = locationDAO.getLocation(rs.getInt("v.primaryLocation"));
				vehicle.setBasicStation(basicStation);

				Location currentStation = new Location();
				currentStation = locationDAO.getLocation(rs.getInt("v.currentLocation"));
				vehicle.setCurrentStation(currentStation);

				StaffMember driver = new StaffMember();
				driver = staffMemberDAO.getStaffMemberByID(rs.getInt("v.driver_ID"));
				vehicle.setDriver(driver);

				StaffMember firstParamedic = new StaffMember();
				firstParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic1_ID"));
				vehicle.setFirstParamedic(firstParamedic);

				StaffMember secondParamedic = new StaffMember();
				secondParamedic = staffMemberDAO.getStaffMemberByID(rs.getInt("v.medic2_ID"));
				vehicle.setSecondParamedic(secondParamedic);
				vehicles.add(vehicle);
			}
			return vehicles;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeVehicle(VehicleDetail vehicle) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.vehicle"));
			query.setString(1, vehicle.getVehicleName());
			//assert the vehicle was removed
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean updateVehicle(VehicleDetail vehicle) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//driver_ID = ?, medic1_ID = ?, medic2_ID = ?, phonenumber_ID = ?, vehicletype = '?', currentLocation = ?,
			//primaryLocation = ?, note = '?', readyForAction = ?, outOfOrder = ?, transportStatus = ? where vehicle_ID = '?';
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.vehicle"));
			//assert we have a driver
			if(vehicle.getDriver() == null)
				query.setString(1,null);
			else
				query.setInt(1, vehicle.getDriver().getStaffMemberId());
			//assert we have a medic
			if(vehicle.getFirstParamedic() == null)
				query.setString(2, null);
			else
				query.setInt(2, vehicle.getFirstParamedic().getStaffMemberId());
			//assert we have a secondary medic
			if(vehicle.getSecondParamedic() == null)
				query.setString(3, null);
			else
				query.setInt(3, vehicle.getSecondParamedic().getStaffMemberId());
			//assert we have a phone
			if(vehicle.getMobilePhone() == null)
				query.setString(4, null);
			else
				query.setInt(4, vehicle.getMobilePhone().getId());
			//update the other properties
			query.setString(5, vehicle.getVehicleType());
			query.setInt(6, vehicle.getCurrentStation().getId());
			query.setInt(7, vehicle.getBasicStation().getId());
			query.setString(8, vehicle.getVehicleNotes());
			query.setBoolean(9, vehicle.isReadyForAction());
			query.setBoolean(10, vehicle.isOutOfOrder());
			query.setString(11, vehicle.getVehicleName());
			if(query.executeUpdate() == 0)
				return false;
			return true;
		}
		finally
		{
			connection.close();
		}
	}
}
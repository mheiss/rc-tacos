package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.*;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailDAOMySQL implements VehicleDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();

	@Override
	public boolean addVehicle(VehicleDetail vehicle)
	{
		try
		{	
			//vehicle_ID, driver_ID, medic1_ID, medic2_ID, phonenumber_ID, vehicletype, currentLocation, primaryLocation, note, readyForAction, outOfOrder
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.vehicle"));
			query.setString(1, vehicle.getVehicleName());
			if(vehicle.getDriver() == null)
				query.setString(2,null);
			else
				query.setInt(2, vehicle.getDriver().getStaffMemberId());
			if(vehicle.getFirstParamedic() == null)
				query.setString(3,null);
			else
				query.setInt(3, vehicle.getFirstParamedic().getStaffMemberId());
			if(vehicle.getSecondParamedic() == null)
				query.setString(4,null);
			else
				query.setInt(4, vehicle.getSecondParamedic().getStaffMemberId());
			if(vehicle.getMobilePhone() == null)
				query.setString(5,null);
			else
				query.setInt(5, vehicle.getMobilePhone().getId());
			query.setString(6, vehicle.getVehicleType());
			if(vehicle.getCurrentStation() == null)
				query.setString(7,null);
			else
				query.setInt(7, vehicle.getCurrentStation().getId());
			query.setInt(8, vehicle.getBasicStation().getId());
			query.setString(9, vehicle.getVehicleNotes());
			query.setBoolean(10, vehicle.isReadyForAction());
			query.setBoolean(11, vehicle.isOutOfOrder());

			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public VehicleDetail getVehicleByName(String vehicleName)
	{
		VehicleDetail vehicle = new VehicleDetail();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleByID"));
			query.setString(1, vehicleName);
			final ResultSet rs = query.executeQuery();

			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID, pn.phonenumber, v.note
			if(rs.first())
			{
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
				// TODO Transportstate is now in the transport object
				//vehicle.setTransportStatus(transportStatus)
			}
			else return null;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return vehicle;
	}

	@Override
	public List<VehicleDetail> listVehicles()
	{

		List<VehicleDetail> vehicles = new ArrayList<VehicleDetail>();

		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.vehicles"));
			final ResultSet rs = query.executeQuery();

			//v.vehicle_ID, v.medic1_ID, v.medic2_ID, v.driver_ID, v.currentLocation, v.primaryLocation, lo.locationname, lo.location_ID,
			//v.vehicletype, v.readyForAction, v.outOfOrder, v.phonenumber_ID, pn.phonenumber, v.note
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
				// TODO Transportstate is now in the transport object
				//vehicle.setTransportStatus(transportStatus)
				vehicles.add(vehicle);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return vehicles;
	}

	@Override
	public boolean removeVehicle(VehicleDetail vehicle)
	{
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.vehicle"));
			query.setString(1, vehicle.getVehicleName());

			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean updateVehicle(VehicleDetail vehicle)
	{
		try
		{
			//driver_ID = ?, medic1_ID = ?, medic2_ID = ?, phonenumber_ID = ?, vehicletype = '?', currentLocation = ?,
			//primaryLocation = ?, note = '?', readyForAction = ?, outOfOrder = ? where vehicle_ID = '?';
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.vehicle"));
			query.setInt(1, vehicle.getDriver().getStaffMemberId());
			query.setInt(2, vehicle.getFirstParamedic().getStaffMemberId());
			query.setInt(3, vehicle.getSecondParamedic().getStaffMemberId());
			query.setInt(4, vehicle.getMobilePhone().getId());
			query.setString(5, vehicle.getVehicleType());
			query.setInt(6, vehicle.getCurrentStation().getId());
			query.setInt(7, vehicle.getBasicStation().getId());
			query.setString(8, vehicle.getVehicleNotes());
			query.setBoolean(9, vehicle.isReadyForAction());
			query.setBoolean(10, vehicle.isOutOfOrder());
			query.setString(11, vehicle.getVehicleName());

			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}



}

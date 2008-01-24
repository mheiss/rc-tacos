package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

public class VehicleDetailDAOMySQL implements VehicleDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public int addVehicle(VehicleDetail vehicle)
	{
		Integer rosterId=null;
		try
		{	
			//vehicle_ID, phonenumber_ID, vehicletype_ID, primaryLocation, note
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.vehicle"));
			query.setString(1, vehicle.getVehicleName());
			query.setInt(2, vehicle.getMobilePhone().getId());
			query.setString(3, vehicle.getVehicleType());
			query.setInt(4, vehicle.getCurrentStation().getId());
			query.setString(5, vehicle.getVehicleNotes());
			
			query.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	@Override
	public VehicleDetail getVehicleById(String vehicleId)
	{
		VehicleDetail vehicle = new VehicleDetail();
    	try
    	{
		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.vehicleByID"));
		query.setString(1, vehicleId);
		final ResultSet rs = query.executeQuery();

		//v.vehicle_ID, vt.vehicletype_ID, lo.locationname, pn.phonenumber, v.note
		rs.first();
		vehicle.setVehicleId(rs.getString("v.vehicle_ID"));
		vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
		vehicle.setVehicleType(rs.getString("vt.vehicletype_ID"));
		vehicle.setCurrentStation(rs.getString("lo.locationname"));
		vehicle.setMobilPhone(rs.getString("pn.phonenumber"));
		vehicle.setVehicleNotes(rs.getString("v.note"));
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

		//v.vehicle_ID, vt.vehicletype_ID, lo.locationname, pn.phonenumber, v.note
		while(rs.next())
		{
			VehicleDetail vehicle = new VehicleDetail();
			vehicle.setVehicleId(rs.getString("v.vehicle_ID"));
			vehicle.setVehicleName(rs.getString("v.vehicle_ID"));
			vehicle.setVehicleType(rs.getString("vt.vehicletype_ID"));
			vehicle.setCurrentStation(rs.getString("lo.locationname"));
			vehicle.setMobilPhone(rs.getString("pn.phonenumber"));
			vehicle.setVehicleNotes(rs.getString("v.note"));
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
		// TODO Auto-generated method stub
		return false;
		
	}

	@Override
	public boolean updateVehicle(VehicleDetail vehicle)
	{
		try
		{
			//phonenumber_ID = ?, vehicletype_ID = '?', primaryLocation = ?, note = '?' where vehicle_ID = '?'
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.vehicle"));
			query.setInt(1, vehicle.getMobilePhone().getMobilePhoneId());
			query.setString(2, vehicle.getVehicleType());
			query.setInt(3, 1); //primaryLocation (ID int)
			query.setString(4, vehicle.getVehicleNotes());
			query.setString(5, vehicle.getVehicleId());

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

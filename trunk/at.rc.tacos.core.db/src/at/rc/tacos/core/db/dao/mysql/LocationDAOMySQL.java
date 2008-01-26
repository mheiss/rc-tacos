package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.model.*;


public class LocationDAOMySQL implements LocationDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public Location getLocation(int locationID)
	{
		Location location = new Location();
		MobilePhoneDetail phone = new MobilePhoneDetail();
		try
		{
			//lo.location_ID, lo.locationname, lo.street, lo. streetnumber, lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.LocationByID"));
			query.setInt(1, locationID);
			final ResultSet rs = query.executeQuery();

			rs.first();
			location.setCity(rs.getString("lo.city"));
			location.setId(rs.getInt("lo.location_ID"));
			location.setLocationName(rs.getString("lo.locationname"));
			location.setNotes(rs.getString("lo.note"));
			
			phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
			phone.setMobilePhoneName(rs.getString("pn.phonename"));
			phone.setId(rs.getInt("lo.phonenumber_ID"));
			location.setPhone(phone);
			
			location.setStreet(rs.getString("lo.street"));
			location.setStreetNumber(rs.getString("lo.streetnumber"));
			location.setZipcode(rs.getInt("lo.zipcode"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return location;
	}


	@Override
	public List<Location> listLocations()
	{

		List<Location> locations = new ArrayList<Location>();
		MobilePhoneDetail phone = new MobilePhoneDetail();
		try
		{
			//lo.location_ID, lo.locationname, lo.street, lo. streetnumber, lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("list.locations"));
			final ResultSet rs = query.executeQuery();

			while(rs.next())
			{
				Location location = new Location();
				location.setCity(rs.getString("lo.city"));
				location.setId(rs.getInt("lo.location_ID"));
				location.setLocationName(rs.getString("lo.locationname"));
				location.setNotes(rs.getString("lo.note"));
				
				phone.setId(rs.getInt("lo.phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				location.setPhone(phone);
				
				location.setStreet(rs.getString("lo.street"));
				location.setStreetNumber(rs.getString("lo.streetnumber"));
				location.setZipcode(rs.getInt("lo.zipcode"));

				locations.add(location);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return locations;
	}


	@Override
	public int addLocation(Location location)
	{
		int locationId = 0;
		try
		{	
			// location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("insert.location"));
			query.setInt(1, location.getId());
			query.setString(2, location.getLocationName());
			query.setString(3, location.getStreet());
			query.setString(4, location.getStreetNumber());
			query.setInt(5, location.getZipcode());
			query.setString(6, location.getCity());
			query.setString(7, location.getNotes());
			query.setInt(8, location.getPhone().getId());
			query.executeUpdate();
			
			//locationname, zipcode, street
			final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.locationID"));
			query1.setString(1, location.getLocationName());
			query1.setInt(2, location.getZipcode());
			query1.setString(3, location.getStreet());
			final ResultSet rsLocationId = query1.executeQuery();
			
			if(rsLocationId.first())
				locationId = rsLocationId.getInt("location_ID");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		return locationId;
	}


	@Override
	public boolean removeLocation(int id)
	{
		try
    	{
    		final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("delete.location"));
    		query.setInt(1, id);
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
	public boolean updateLocation(Location location)
	{
    	try
		{
    	// locationname, street, streetnumber, zipcode, city, note, phonenumber_ID, location_ID
    	final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("update.location"));
		query.setString(1, location.getLocationName());
		query.setString(2, location.getStreet());
		query.setString(3, location.getStreetNumber());
		query.setInt(4, location.getZipcode());
		query.setString(5, location.getCity());
		query.setString(6, location.getNotes());
		query.setInt(7, location.getPhone().getId());
		query.setInt(8, location.getId());
		
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

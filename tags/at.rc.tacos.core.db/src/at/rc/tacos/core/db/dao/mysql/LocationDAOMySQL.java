package at.rc.tacos.core.db.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.Queries;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.model.*;

public class LocationDAOMySQL implements LocationDAO
{
	//The data source to get the connection and the queries file
	private final DataSource source = DataSource.getInstance();
	private final Queries queries = Queries.getInstance();

	@Override
	public Location getLocation(int locationID) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//lo.location_ID, lo.locationname, lo.street, lo. streetnumber, lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.LocationByID"));
			query.setInt(1, locationID);
			final ResultSet rs = query.executeQuery();
			//assert we have a result
			if(rs.first())
			{
				Location location = new Location();
				location.setCity(rs.getString("lo.city"));
				location.setId(rs.getInt("lo.location_ID"));
				location.setLocationName(rs.getString("lo.locationname"));
				location.setNotes(rs.getString("lo.note"));
				location.setStreet(rs.getString("lo.street"));
				location.setStreetNumber(rs.getString("lo.streetnumber"));
				location.setZipcode(rs.getInt("lo.zipcode"));

				//get the mobile phone
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				phone.setId(rs.getInt("lo.phonenumber_ID"));
				location.setPhone(phone);

				return location;
			}
			//no result
			return null;
		}
		finally
		{
			connection.close();
		}
	}


	@Override
	public List<Location> listLocations() throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//lo.location_ID, lo.locationname, lo.street, lo. streetnumber, lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.locations"));
			final ResultSet rs = query.executeQuery();
			//assert we have a result
			List<Location> locations = new ArrayList<Location>();
			while(rs.next())
			{
				Location location = new Location();
				location.setCity(rs.getString("lo.city"));
				location.setId(rs.getInt("lo.location_ID"));
				location.setLocationName(rs.getString("lo.locationname"));
				location.setNotes(rs.getString("lo.note"));
				//set the mobile phone
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setId(rs.getInt("lo.phonenumber_ID"));
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				location.setPhone(phone);

				location.setStreet(rs.getString("lo.street"));
				location.setStreetNumber(rs.getString("lo.streetnumber"));
				location.setZipcode(rs.getInt("lo.zipcode"));

				locations.add(location);
			}
			return locations;
		}
		finally
		{
			connection.close();
		}
	}


	@Override
	public int addLocation(Location location) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{	
			// location_ID, locationname, street, streetnumber, zipcode, city, note, phonenumber_ID
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.location"));
			query.setString(1, location.getLocationName());
			query.setString(2, location.getStreet());
			query.setString(3, location.getStreetNumber());
			query.setInt(4, location.getZipcode());
			query.setString(5, location.getCity());
			query.setString(6, location.getNotes());
			query.setInt(7, location.getPhone().getId());
			query.executeUpdate();

			//get the last inserted id
			final ResultSet rs = query.getGeneratedKeys();
			if (rs.next()) 
				return rs.getInt(1);
			return -1;
		}
		finally
		{
			connection.close();
		}
	}

	@Override
	public boolean removeLocation(int id) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.location"));
			query.setInt(1, id);
			//assert the location removed was successfully
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
	public boolean updateLocation(Location location) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			// locationname, street, streetnumber, zipcode, city, note, phonenumber_ID, location_ID
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.location"));
			query.setString(1, location.getLocationName());
			query.setString(2, location.getStreet());
			query.setString(3, location.getStreetNumber());
			query.setInt(4, location.getZipcode());
			query.setString(5, location.getCity());
			query.setString(6, location.getNotes());
			query.setInt(7, location.getPhone().getId());
			query.setInt(8, location.getId());
			//assert the update was successfully
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
	public Location getLocationByName(String locationname) throws SQLException
	{
		Connection connection = source.getConnection();
		try
		{
			//lo.location_ID, lo.locationname, lo.street, lo. streetnumber, lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
			final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.locationByName"));
			query.setString(1, locationname);
			final ResultSet rs = query.executeQuery();
			//assert we have a result set
			if(rs.first())
			{
				Location location = new Location();
				location.setCity(rs.getString("lo.city"));
				location.setId(rs.getInt("lo.location_ID"));
				location.setLocationName(rs.getString("lo.locationname"));
				location.setNotes(rs.getString("lo.note"));
				//get the phone
				MobilePhoneDetail phone = new MobilePhoneDetail();
				phone.setMobilePhoneNumber(rs.getString("pn.phonenumber"));
				phone.setMobilePhoneName(rs.getString("pn.phonename"));
				phone.setId(rs.getInt("lo.phonenumber_ID"));
				location.setPhone(phone);
				location.setStreet(rs.getString("lo.street"));
				location.setStreetNumber(rs.getString("lo.streetnumber"));
				location.setZipcode(rs.getInt("lo.zipcode"));
				return location;
			}
			//no result set
			return null;
		}
		finally
		{
			connection.close();
		}
	}
}

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
			phone.setMobilePhoneName(null);
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
				phone.setMobilePhoneName(null);
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
	public int addLocation(Location location) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean removeLocation(int id) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean updateLocation(Location location) {
		// TODO Auto-generated method stub
		return false;
	}


}

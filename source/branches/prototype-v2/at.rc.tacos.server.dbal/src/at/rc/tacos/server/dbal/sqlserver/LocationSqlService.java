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
import at.rc.tacos.platform.services.dbal.LocationService;
import at.rc.tacos.server.dbal.SQLQueries;

/**
 * Provides CRUD operation for location.
 * 
 * @author Michael
 */
public class LocationSqlService implements LocationService {

	@Resource(name = "sqlConnection")
	protected Connection connection;

	// the source for the queries
	protected final SQLQueries queries = SQLQueries.getInstance();

	@Override
	public Location getLocation(int locationID) throws SQLException {
		// lo.location_ID, lo.locationname, lo.street, lo. streetnumber,
		// lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.LocationByID"));
		query.setInt(1, locationID);
		final ResultSet rs = query.executeQuery();
		// assert we have a result
		if (rs.next()) {
			return setupLocation(rs);
		}
		// no result
		return null;
	}

	@Override
	public List<Location> listLocations() throws SQLException {
		// lo.location_ID, lo.locationname, lo.street, lo. streetnumber,
		// lo.city, lo.zipcode, lo.phonenumber_ID, pn.phonenumber, lo.note
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.locations"));
		final ResultSet rs = query.executeQuery();
		return setupLocationList(rs);
	}

	@Override
	public int addLocation(Location location) throws SQLException {
		// get the next id
		final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextLocationID"));
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next())
			return -1;

		int id = rs.getInt(1);

		// location_ID, locationname, street, streetnumber, zipcode, city,
		// note, phonenumber_ID
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.location"));
		query.setInt(1, id);
		query.setString(2, location.getLocationName());
		query.setString(3, location.getStreet());
		query.setString(4, location.getStreetNumber());
		query.setInt(5, location.getZipcode());
		query.setString(6, location.getCity());
		query.setString(7, location.getNotes());
		query.setInt(8, location.getPhone().getId());

		if (query.executeUpdate() == 0)
			return -1;

		return id;
	}

	@Override
	public boolean removeLocation(int id) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.location"));
		query.setInt(1, id);
		// assert the location removed was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public boolean updateLocation(Location location) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.location"));
		query.setString(1, location.getLocationName());
		query.setString(2, location.getStreet());
		query.setString(3, location.getStreetNumber());
		query.setInt(4, location.getZipcode());
		query.setString(5, location.getCity());
		query.setString(6, location.getNotes());
		query.setInt(7, location.getPhone().getId());
		query.setInt(8, location.getId());
		// assert the update was successfully
		if (query.executeUpdate() == 0)
			return false;
		return true;
	}

	@Override
	public Location getLocationByName(String locationname) throws SQLException {
		final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.locationByName"));
		query.setString(1, locationname);
		final ResultSet rs = query.executeQuery();
		// assert we have a result set
		if (rs.next()) {
			return setupLocation(rs);
		}
		// no result set
		return null;
	}

	/**
	 * Helper method to setup a list of location entries
	 */
	private List<Location> setupLocationList(ResultSet rs) throws SQLException {
		// assert we have a result
		List<Location> locationList = new ArrayList<Location>();
		while (rs.next()) {
			Location location = setupLocation(rs);
			locationList.add(location);
		}
		return locationList;
	}

	/**
	 * Helper method to setup a single location
	 */
	private Location setupLocation(ResultSet rs) throws SQLException {
		Location location = new Location();
		location.setCity(rs.getString("city"));
		location.setId(rs.getInt("location_ID"));
		location.setLocationName(rs.getString("locationname"));
		location.setNotes(rs.getString("note"));
		// get the phone
		MobilePhoneDetail phone = new MobilePhoneDetail();
		phone.setMobilePhoneNumber(rs.getString("phonenumber"));
		phone.setMobilePhoneName(rs.getString("phonename"));
		phone.setId(rs.getInt("phonenumber_ID"));
		location.setPhone(phone);
		location.setStreet(rs.getString("street"));
		location.setStreetNumber(rs.getString("streetnumber"));
		location.setZipcode(rs.getInt("zipcode"));
		return location;
	}
}

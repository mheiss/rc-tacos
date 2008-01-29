package at.rc.tacos.core.db.dao.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.model.*;


public class LocationDAOMySQL implements LocationDAO
{
	public static final String QUERIES_BUNDLE_PATH = "at.rc.tacos.core.db.queries";

	@Override
	public Location getLocation(int locationID) throws SQLException
	{
		// TODO vorher Location Objekt fertigstellen
		Location location = new Location();
		try
		{
			final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(RosterDAOMySQL.QUERIES_BUNDLE_PATH).getString("get.LocationByID"));
			query.setInt(1, locationID);
			final ResultSet rs = query.executeQuery();

			rs.first();
			//location.set...
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
		return location;
	}


@Override
	public List<Location> listLocations() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

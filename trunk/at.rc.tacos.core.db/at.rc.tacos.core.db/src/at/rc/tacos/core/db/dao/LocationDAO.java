package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;
import at.rc.tacos.model.*;

public interface LocationDAO 
{
	public List<Location> listLocations() throws SQLException;
	public Location getLocation(int locationID) throws SQLException;
}

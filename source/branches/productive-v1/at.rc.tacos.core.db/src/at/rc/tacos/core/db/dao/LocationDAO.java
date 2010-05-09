/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.Location;

public interface LocationDAO {

	public static final String TABLE_NAME = "location";

	/**
	 * Adds a new location to the database and returns the generated is
	 * 
	 * @param location
	 *            the location to add
	 * @return the generated id
	 */
	public int addLocation(Location location) throws SQLException;

	/**
	 * Updates the location in the database
	 * 
	 * @param location
	 *            the location to update
	 * @return true if the updates was successfull
	 */
	public boolean updateLocation(Location location) throws SQLException;

	/**
	 * Removes the location out of the database
	 * 
	 * @param id
	 *            the id of the location to remove
	 * @return true if the remove was successfull
	 */
	public boolean removeLocation(int id) throws SQLException;

	/**
	 * Returns a list of all locations stored in the database
	 * 
	 * @return the list of locations
	 */
	public List<Location> listLocations() throws SQLException;

	/**
	 * Returns a specific id identified by the location id
	 * 
	 * @param id
	 *            the id of the location to get
	 * @return the location or null if no location with this id was found
	 */
	public Location getLocation(int id) throws SQLException;

	/**
	 * Returns the location by the locationname
	 * 
	 * @param locationname
	 * @return the location
	 */
	public Location getLocationByName(String locationname) throws SQLException;
}
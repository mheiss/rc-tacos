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

import at.rc.tacos.model.VehicleDetail;

public interface VehicleDAO {

	public static final String TABLE_NAME = "vehicles";

	/**
	 * Adds a new vehicle to the list of vehicles
	 * 
	 * @param vehicle
	 *            the vehicle to add
	 * @return the id of the added vehicle
	 */
	public boolean addVehicle(VehicleDetail vehicle) throws SQLException;

	/**
	 * Updates the vehicle in the database
	 * 
	 * @param vehicle
	 *            the vehicl to update
	 * @return true if the update was successfull.
	 */
	public boolean updateVehicle(VehicleDetail vehicle) throws SQLException;

	/**
	 * Remove the vehicle from the database
	 * 
	 * @param id
	 *            the id of the vehicle to remve
	 * @return true if the remove was successfull
	 */
	public boolean removeVehicle(VehicleDetail vehicle) throws SQLException;

	/**
	 * Returns the vehicle accociated with the given name. The method will
	 * return null if no vehicle was found.
	 * 
	 * @param vehicleName
	 *            the name of the vehicle to get
	 * @return the vehicle or null if nothing was found
	 */
	public VehicleDetail getVehicleByName(String vehicleName) throws SQLException;

	/**
	 * Returns a list of all vehicles
	 * 
	 * @return the list of all vehicles
	 */
	public List<VehicleDetail> listVehicles() throws SQLException;
}

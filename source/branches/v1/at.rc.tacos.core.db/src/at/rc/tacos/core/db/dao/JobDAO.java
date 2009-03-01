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

import at.rc.tacos.model.Job;

public interface JobDAO {

	public static final String TABLE_NAME = "job";

	/**
	 * Adds a new job to the database and returns the unique id.
	 * 
	 * @param job
	 *            the job to add
	 * @return the unique id of the job in the database
	 */
	public int addJob(Job job) throws SQLException;

	/**
	 * Updates a job in the database
	 * 
	 * @param job
	 *            the job to update
	 * @return true if the update was successfully otherwise false
	 */
	public boolean updateJob(Job job) throws SQLException;

	/**
	 * Removes the job from the database.
	 * 
	 * @param id
	 *            the id of the job to remove
	 * @return true if the deletion was successfully.
	 */
	public boolean removeJob(int id) throws SQLException;

	/**
	 * Returns the job identified by the given id.
	 * 
	 * @param id
	 *            the id to get the job from
	 * @return the queried job
	 */
	public Job getJobById(int id) throws SQLException;

	/**
	 * Returns a list of all stored jobs in the database
	 * 
	 * @return the complete list of all jobs
	 */
	public List<Job> listJobs() throws SQLException;
}

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
package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

public class Disease extends AbstractMessage {

	// unique identification string
	public final static String ID = "disease";

	// properties
	private int id;
	private String diseaseName;

	/**
	 * Default class constructor
	 */
	public Disease() {
		super(ID);
		// set default values
		id = -1;
		diseaseName = "";
	}

	/**
	 * Default class constructor for a complete disease object
	 */
	public Disease(String diseaseName) {
		this();
		this.diseaseName = diseaseName;
	}

	// METHODS
	/**
	 * Returns the string based description
	 * 
	 * @return the string description
	 */
	@Override
	public String toString() {
		return "id: " + id + "; diseaseName: " + diseaseName;
	}

	/**
	 * Returns the calculated hash code based on the disease id.<br>
	 * Two diseases have the same hash code if the id is the same.
	 * 
	 * @return the calculated hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * Returns whether the objects are equal or not.<br>
	 * Two disease are equal if, and only if, the job id is the same.
	 * 
	 * @return true if the id is the same otherwise false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Disease other = (Disease) obj;
		if (id != other.id)
			return false;
		return true;
	}

	// GETTERS AND SETTERS

	/**
	 * Returns the id of the disease
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the disease
	 * 
	 * @return the diseaseName
	 */
	public String getDiseaseName() {
		return diseaseName;
	}

	/**
	 * Sets the id of the disease
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name of the disease
	 * 
	 * @param diseaseName
	 *            the diseaseName to set
	 */
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
}

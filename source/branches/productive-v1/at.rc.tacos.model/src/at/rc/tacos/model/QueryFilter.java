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

import java.util.HashMap;
import java.util.Map;

import at.rc.tacos.common.IFilterTypes;

/**
 * The class supports custom filters to apply to the server query.
 * 
 * @author Michael
 * @see IFilterTypes for a definition of available filter types
 */
public class QueryFilter {

	// properties
	private Map<String, String> filterList;

	/**
	 * Default class constructor
	 */
	public QueryFilter() {
		filterList = new HashMap<String, String>();
	}

	/**
	 * Class constructor specifying a filter and the value.
	 * 
	 * @param type
	 *            the type of the filter to add
	 * @param value
	 *            the value for the filter
	 */
	public QueryFilter(String type, String value) {
		filterList = new HashMap<String, String>();
		filterList.put(type, value);
	}

	/**
	 * Adds a new filter to the list.
	 * 
	 * @param type
	 *            the type of the filter to add
	 * @param value
	 *            the value for the filter
	 */
	public void add(String type, String value) {
		filterList.put(type, value);
	}

	/**
	 * Returns the complete filter list.
	 * 
	 * @return the filter list
	 */
	public Map<String, String> getFilterList() {
		return filterList;
	}

	/**
	 * Returns a specific value of the filter list.
	 * 
	 * @param type
	 *            the type of the filter to get the value
	 * @return the value for the filter type or null if the type is not existing
	 */
	public String getFilterValue(String type) {
		return filterList.get(type);
	}

	/**
	 * Convinient method to check if a specific filter type is existing
	 * 
	 * @param type
	 *            the filter type to chek
	 * @return true if a value for this type is existing, oterwise false
	 */
	public boolean containsFilterType(String type) {
		return filterList.containsKey(type);
	}
}

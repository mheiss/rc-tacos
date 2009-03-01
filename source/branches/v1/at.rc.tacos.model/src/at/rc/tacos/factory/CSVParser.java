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
package at.rc.tacos.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a CSV File into a list of maps
 * 
 * @author Harald Pittesser
 * @version 0.1 alpha
 */
public class CSVParser {

	private static CSVParser instance = null;

	/**
	 * Returns an instance of singleton CSVParser
	 * 
	 * @return instance of CSVParser
	 */
	public static CSVParser getInstance() {
		if (instance == null) {
			instance = new CSVParser();
		}
		return instance;
	}

	/**
	 * parses the csv file
	 * 
	 * @param file
	 * @return List of maps <element's name, element>
	 */
	public List<Map<String, Object>> parseCSV(File file) throws Exception {
		// prepare variables
		List<Map<String, Object>> elementList = new ArrayList<Map<String, Object>>();
		List<String> elementNames = null, elementsInLine = null;
		String tempLine;
		int lineCount = 0;
		// read the csv file
		BufferedReader in = new BufferedReader(new FileReader(file));
		// step through the lines
		while ((tempLine = in.readLine()) != null) {
			if (lineCount == 0) {
				// get the names of the elements in the first line (index line)
				elementNames = Arrays.asList(tempLine.split(";"));
			}
			else {
				Map<String, Object> elements = new HashMap<String, Object>();
				// get the elements in the next line
				elementsInLine = Arrays.asList(tempLine.split(";"));
				// name the elements
				for (int i = 0; i < elementsInLine.size(); i++) {
					// store element with name in map
					elements.put(elementNames.get(i), elementsInLine.get(i));
				}
				// add map (representing one line) to the list
				elementList.add(elements);
			}
			lineCount++;
		}
		return elementList;
	}

}

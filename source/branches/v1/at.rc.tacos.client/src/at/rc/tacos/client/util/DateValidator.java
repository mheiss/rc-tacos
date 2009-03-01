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
package at.rc.tacos.client.util;

import org.eclipse.jface.dialogs.IInputValidator;

import at.rc.tacos.util.MyUtils;

/**
 * This class validates a date. It makes sure that the given string can be
 * parsed into a valid date.
 * 
 * @author Michael
 */
public class DateValidator implements IInputValidator {

	/**
	 * Validates the String. Returns null for no error, or an error message
	 * 
	 * @param newDate
	 *            the string to validate
	 * @return null if everything is ok, or the error message
	 */
	public String isValid(String newDate) {
		if (MyUtils.stringToTimestamp(newDate, MyUtils.timeFormat) == -1)
			return "Dies ist keine gültige Uhrzeit\n" + "Bitte im Format hh:mm eingeben";

		// input is ok
		return null;
	}
}

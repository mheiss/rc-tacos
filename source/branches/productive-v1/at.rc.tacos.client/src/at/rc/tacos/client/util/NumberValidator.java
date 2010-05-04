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

/**
 * This class validates numbers so that only numeric values can be entered
 * 
 * @author Michael
 */
public class NumberValidator implements IInputValidator {

	@Override
	public String isValid(String input) {
		// validate the phone number
		String pattern1 = "\\d{4,5}-\\d{4,8}";
		String pattern2 = "\\d{4,5}-\\d{4,9}";
		if (input.matches(pattern1) || input.matches(pattern2)) {
			return null;
		}
		else {
			return "Dies ist keine gültige Telefonnummer\n"
					+ "Bitte die Vorwahl und die Nummer mit einem Bindestrich getrennt eingeben. Bsp: 0699-12345678";
		}
	}

}

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

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TransformTimeToLong {

	String stringTime;
	long longTime;

	GregorianCalendar cal = new GregorianCalendar();

	public TransformTimeToLong() {
	}

	public long transform(String stringTime) {
		if (!stringTime.equalsIgnoreCase("")) {
			String[] theTerm = stringTime.split(":");

			int hoursTerm = Integer.valueOf(theTerm[0]).intValue();
			int minutesTerm = Integer.valueOf(theTerm[1]).intValue();
			cal.set(Calendar.HOUR_OF_DAY, hoursTerm);
			cal.set(Calendar.MINUTE, minutesTerm);
			longTime = cal.getTimeInMillis();

			return longTime;
		}
		else
			return 0;
	}
}

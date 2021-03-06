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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidator {

	String checkStatus = "";
	String time;
	String field;

	public TimeValidator() {
	}

	public void checkTime(String time, String field) {
		this.time = time;
		this.field = field;

		Pattern p4 = Pattern.compile("(\\d{2})(\\d{2})");// if content is e.g.
															// 1234
		Pattern p5 = Pattern.compile("(\\d{2}):(\\d{2})");// if content is e.g.
															// 12:34

		// for each field
		if (!time.equalsIgnoreCase("")) {
			Matcher m41 = p4.matcher(time);
			Matcher m51 = p5.matcher(time);
			if (m41.matches()) {
				int hour = Integer.parseInt(m41.group(1));
				int minutes = Integer.parseInt(m41.group(2));

				if (hour >= 0 && hour <= 23 && minutes >= 0 && minutes <= 59) {
					time = hour + ":" + minutes;// for the splitter
					this.setTime(time);
				}
				else {
					checkStatus = " " + field;
				}
			}
			else if (m51.matches()) {
				int hour = Integer.parseInt(m51.group(1));
				int minutes = Integer.parseInt(m51.group(2));

				if (!(hour >= 0 && hour <= 23 && minutes >= 0 && minutes <= 59)) {
					checkStatus = " " + field;
				}
			}
			else {
				checkStatus = " " + field;
			}
		}
		else
			checkStatus = "";
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public String getCheckStatus() {
		return checkStatus;
	}
}

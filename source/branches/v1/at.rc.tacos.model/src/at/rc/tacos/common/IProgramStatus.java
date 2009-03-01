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
package at.rc.tacos.common;

/**
 * The available program status for a transport, used by the transport view
 * filter
 * 
 * @author b.thek
 */

public interface IProgramStatus {

	/** 0 - prebooking */
	public final static int PROGRAM_STATUS_PREBOOKING = 0;
	/** 1 - outstanding */
	public final static int PROGRAM_STATUS_OUTSTANDING = 1;
	/** 2 - underway */
	public final static int PROGRAM_STATUS_UNDERWAY = 2;
	/** 3 - journal */
	public final static int PROGRAM_STATUS_JOURNAL = 3;
}

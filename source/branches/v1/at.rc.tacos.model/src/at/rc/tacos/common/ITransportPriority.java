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
 * The available priorities for a transport Available transport priorities in
 * the system: A to G The user shown transort priorities are 1 to 7
 * 
 * @author b.thek
 */
public interface ITransportPriority {

	/** A (1) NEF und RTW entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL = "A";
	/** B (2) RTW mit BD1 entsenden */
	public final static String TRANSPORT_PRIORITY_BLUELIGHT = "B";
	/** C (3) normaler Transport */
	public final static String TRANSPORT_PRIORITY_TRANSPORT = "C";
	/** D (4) Rücktransport (von ambulant) */
	public final static String TRANSPORT_PRIORITY_BACK_TRANSPORT = "D";
	/** E (5) Heimtransport (von stationär) */
	public final static String TRANSPORT_PRIORITY_HOME_TRANSPORT = "E";
	/** F (6) Sonstiges (Dienstfahrten,...) */
	public final static String TRANSPORT_PRIORITY_OTHER = "F";
	/** G (7) NEF für andere Bezirksstelle entsenden */
	public final static String TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_EXTERNAL = "G";
}

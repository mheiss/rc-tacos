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
 * The possible direction for a transport.
 * 
 * @author b.thek
 */
public interface IDirectness {

	/** Richtung Bezirk (default, wenn nichts anderes angegeben */
	public final static int TOWARDS_BRUCK = 1;
	/** Richtung Graz */
	public final static int TOWARDS_GRAZ = 2;
	/** Richtung Leoben */
	public final static int TOWARDS_LEOBEN = 3;
	/** Richtung Wien */
	public final static int TOWARDS_VIENNA = 4;
	/** Richtung Mariazell */
	public final static int TOWARDS_MARIAZELL = 5;
	/** Richtung Mariazell */
	public final static int TOWARDS_KAPFENBERG = 6;

}

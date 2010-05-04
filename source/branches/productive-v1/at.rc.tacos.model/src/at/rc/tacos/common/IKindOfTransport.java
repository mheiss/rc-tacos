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

public interface IKindOfTransport {

	/** gehend */
	public final static String TRANSPORT_KIND_GEHEND = "Gehend";
	/** Tragsessel */
	public final static String TRANSPORT_KIND_TRAGSESSEL = "Tragsessel";
	/** Krankentrage */
	public final static String TRANSPORT_KIND_KRANKENTRAGE = "Krankentrage";
	/** Eigener Rollstuhl */
	public final static String TRANSPORT_KIND_ROLLSTUHL = "Eigener Rollstuhl";
	/** All */
	public final static String KINDS[] = { TRANSPORT_KIND_GEHEND, TRANSPORT_KIND_TRAGSESSEL, TRANSPORT_KIND_KRANKENTRAGE, TRANSPORT_KIND_ROLLSTUHL };

}

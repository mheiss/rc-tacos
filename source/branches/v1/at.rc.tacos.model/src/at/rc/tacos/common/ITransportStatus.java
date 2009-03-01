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
 * The available status messages for a transport, set by time. Below the status
 * types for the visialisation are shown - green if : 0,1,2,5,6 - yellow if :
 * 2,4,9 - red if: 3,7
 * 
 * @author b.thek
 */
public interface ITransportStatus {

	/** S0 Auftrag erteilt */
	public final static int TRANSPORT_STATUS_ORDER_PLACED = 0;
	/** S1 Fahrzeug unterwegs */
	public final static int TRANSPORT_STATUS_ON_THE_WAY = 1;
	/** S2 Fahrzeug bei Patient */
	public final static int TRANSPORT_STATUS_AT_PATIENT = 2;
	/** S3 Abfahrt mit Patient */
	public final static int TRANSPORT_STATUS_START_WITH_PATIENT = 3;
	/** S4 Ankunft Ziel */
	public final static int TRANSPORT_STATUS_AT_DESTINATION = 4;
	/** S5 Ziel frei */
	public final static int TRANSPORT_STATUS_DESTINATION_FREE = 5;
	/** S6 einger�ckt */
	public final static int TRANSPORT_STATUS_CAR_IN_STATION = 6;
	/** S7 verl�sst Einsatzgebiet */
	public final static int TRANSPORT_STATUS_OUT_OF_OPERATION_AREA = 7;
	/** S8 Wieder im Einsatzgebiet */
	public final static int TRANSPORT_STATUS_BACK_IN_OPERATION_AREA = 8;
	/** S9 Sonderstatus, Sonstiges (z.B. Ambulanz, Essen,...) */
	public final static int TRANSPORT_STATUS_OTHER = 9;
}

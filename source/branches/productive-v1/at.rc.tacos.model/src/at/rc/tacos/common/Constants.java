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
 * Provides some basic constants for the client
 * 
 * @author Michael
 */
public class Constants {

	// Stations
	public final static String STATION_BEZIRK = "Bruck - Kapfenberg";
	public final static String STATION_KAPFENBERG = "Kapfenberg";
	public final static String STATION_BRUCK = "Bruck an der Mur";
	public final static String STATION_MAREIN = "St. Marein";
	public final static String STATION_THOERL = "Thoerl";
	public final static String STATION_TURNAU = "Turnau";
	public final static String STATION_BREITENAU = "Breitenau";

	// competence
	public final static String COMPETENCE_DRIVER = "Sicherer Einsatzfahrer";
	public final static String COMPETENCE_SANI = "Rettungssanitäter";
	public final static String COMPETENCE_EXECUTIVE_INSP = "Insp";
	public final static String COMPETENCE_EXECUTIVE_DF = "DF";
	public final static String COMPETENCE_DISPON = "Leitstellendisponent";
	public final static String COMPETENCE_EMERGENCY = "Notfallsanitäter";
	public final static String COMPETENCE_DOCTOR = "Notarzt";
	public final static String COMPETENCE_OTHER = "Sonstiges";
	public final static String COMPETENCE_INTERVENTION = "KIT";
	public final static String COMPETENCE_BKTW_DRIVER = "Fahrerberechtigung";

	// Service
	public final static String SERVICE_MAIN = "Hauptamtlich";
	public final static String SERVICE_VOLUNT = "Freiwilliger";
	public final static String SERVICE_ZIVI = "Zivildienstleistender";
	public final static String SERVICE_OTHER = "Sonstiges";
	public final static String SERVICE_TEMP = "Ersatzeinstellung";

	// Job
	public final static String JOB_DRIVER = "Fahrer";
	public final static String JOB_SANI = "Sanitäter";
	public final static String JOB_EMERGENCY = "Notfallsanitäter";
	public final static String JOB_DOCTOR = "Notarzt";
	public final static String JOB_DISPON = "Leitstellendisponent";
	public final static String JOB_DF = "Dienstführender";
	public final static String JOB_BRKDT = "Bezirksrettungskommandant";
	public final static String JOB_INSP = "Inspektionsdienst";
	public final static String JOB_BKTW_DRIVER = "BKTW- Fahrer";
	public final static String JOB_JOURNAL = "Journaldienst";
	public final static String JOB_VOLON = "Volontär";
	public final static String JOB_OTHER = "Sonstiges";

	/** Array of all available stations */
	public static String[] stations = { STATION_BEZIRK, STATION_KAPFENBERG, STATION_BRUCK, STATION_MAREIN, STATION_THOERL, STATION_TURNAU,
			STATION_BREITENAU };

	/** Array of all competence types */
	public static String[] competence = { COMPETENCE_DRIVER, COMPETENCE_SANI, COMPETENCE_EXECUTIVE_INSP, COMPETENCE_EXECUTIVE_DF, COMPETENCE_DISPON,
			COMPETENCE_EMERGENCY, COMPETENCE_DOCTOR, COMPETENCE_OTHER, COMPETENCE_INTERVENTION, COMPETENCE_BKTW_DRIVER };

	/** Array of all service types */
	public static String[] service = { SERVICE_MAIN, SERVICE_VOLUNT, SERVICE_ZIVI, SERVICE_OTHER, SERVICE_TEMP };

	/** Array for the time */
	public static String[] timeArray = { "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
			"11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00",
			"18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00", "01:30",
			"02:00", "02:30", "03:00", "03:30", "04:00", "04:30" };

	/** Array of all jobs */
	public static String[] job = { JOB_DRIVER, JOB_SANI, JOB_EMERGENCY, JOB_DOCTOR, JOB_DISPON, JOB_DF, JOB_BRKDT, JOB_INSP, JOB_BKTW_DRIVER,
			JOB_JOURNAL, JOB_VOLON, JOB_OTHER };

}

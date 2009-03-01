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
package at.rc.tacos.core.net.internal;

/**
 * Interface definition for the identification of the servers
 * 
 * @author Michael
 */
public interface IServerInfo {

	/**
	 * Identification to use for the primary server
	 */
	public final static String PRIMARY_SERVER = "server.primary";

	/**
	 * Identification to use for the failover server
	 */
	public final static String FAILOVER_SERVER = "server.failover";
}

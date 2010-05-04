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
package at.rc.tacos.core.net.socket;

/**
 * This class contains information about a server.
 * 
 * @author Michael
 */
public class ServerInfo {

	// properties
	private String id;
	private String host;
	private int port;
	private String description;

	/**
	 * Default constructor for a server info
	 * 
	 * @param id
	 *            the unique identification string
	 * @param host
	 *            the ip address or host name
	 * @param port
	 *            the port number of the server
	 * @param description
	 *            the description to identify the server
	 */
	public ServerInfo(String id, String host, int port, String description) {
		this.id = id;
		this.host = host;
		this.port = port;
		this.description = description;
	}

	// GETTERS FOR THE VALUES

	/**
	 * Returns the uniqe identification string of this server
	 * 
	 * @return the identification string
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the description text of this server
	 * 
	 * @return the description text to display
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the host name or the ip address of the server
	 * 
	 * @return the hostname or ip address
	 */
	public String getHostName() {
		return host;
	}

	/**
	 * Returns the port to which the server is listening
	 * 
	 * @return port the port number
	 */
	public int getPort() {
		return port;
	}
}

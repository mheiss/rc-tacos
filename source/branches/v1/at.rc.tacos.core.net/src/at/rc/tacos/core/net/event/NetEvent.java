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
package at.rc.tacos.core.net.event;

import at.rc.tacos.core.net.internal.MyClient;

/**
 * Simple class for network events.
 * 
 * @author Michael
 */
public class NetEvent {

	// properties
	private String message;
	private MyClient client;

	/**
	 * Default class constructor
	 */
	public NetEvent() {
	}

	/**
	 * Create a NetEvent
	 * 
	 * @param client
	 *            the socket that is the source for the event
	 * @param message
	 *            the data that is reveived
	 */
	public NetEvent(MyClient client, String message) {
		this.client = client;
		this.message = message;
	}

	/**
	 * This method returns the message that was reveived with the client
	 * 
	 * @return the message itself
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Method to set the message for this netEvent
	 * 
	 * @param message
	 *            the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the client object that is the source for this event
	 * 
	 * @return the client object
	 */
	public MyClient getClient() {
		return client;
	}

	/**
	 * Method to set the client for that event
	 * 
	 * @param client
	 *            the client
	 */
	public void setSocket(MyClient client) {
		this.client = client;
	}
}

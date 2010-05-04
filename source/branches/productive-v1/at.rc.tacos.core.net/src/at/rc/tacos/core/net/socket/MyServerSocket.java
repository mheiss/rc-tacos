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

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Specialized server socket class to connect with a custom implementation of a
 * client socket.
 * 
 * @author Michael
 */
public class MyServerSocket extends ServerSocket {

	public MyServerSocket(int port) throws IOException {
		super(port);
	}

	// Override the ServerSocket methode accept()
	// It is overriden to initiate a custom Socket (MySocket) instad of a
	// standard socket object
	@Override
	public MySocket accept() throws IOException {
		MySocket s = new MySocket();
		// this methode waits until a new client is connected
		implAccept(s);
		return s;
	}
}

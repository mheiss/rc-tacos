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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The socket class wrapps the socket object and provides common methods to use.
 * 
 * @author Michael
 */
public class MySocket extends Socket {

	// Streams for this socket
	protected PrintWriter out = null;
	protected BufferedReader in = null;

	/**
	 * Default class constructor to create an empty socket object;
	 */
	public MySocket() {
		super();
	}

	/**
	 * Constructor to create a MyClient object with the given host and port
	 * values
	 * 
	 * @param host
	 *            the host (name or address) to connect to
	 * @param port
	 *            the port number
	 * @throws IOException
	 *             when a io error occured during the socket init
	 * @throws UnknownHostException
	 *             when the host is unknown
	 */
	public MySocket(String host, int port) throws IOException, UnknownHostException {
		super(host, port);
	}

	/**
	 * Creates and returns a buffered input stream to read data from the socket.
	 * 
	 * @return the opened input stream
	 */
	public BufferedReader getBufferedInputStream() throws IOException {
		// assert we have a input stream
		if (in == null)
			in = new BufferedReader(new InputStreamReader(getInputStream()));
		return in;
	}

	/**
	 * Creates and returns a buffered output stream to write data to the socket.
	 * 
	 * @return the opened output stream
	 */
	public PrintWriter getBufferedOutputStream() throws IOException {
		// assert we have a output stream
		if (out == null)
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(getOutputStream())), true);
		return out;
	}

	/**
	 * This method will cleanup the socket and close all created readers and
	 * writers
	 */
	public void cleanup() throws IOException {
		close();
		// assert valid
		if (in != null) {
			in.close();
			in = null;
		}
		if (out != null) {
			out.close();
			out = null;
		}
	}
}

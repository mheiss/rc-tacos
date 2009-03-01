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
package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Login;

/**
 * This class manages the login information for each user that can be edited in
 * the administration section.
 * 
 * @author Michael
 */
public class LoginManager extends PropertyManager {

	// the list
	private List<Login> objectList = new ArrayList<Login>();

	/**
	 * Default class constructor
	 */
	public LoginManager() {
	}

	/**
	 * Adds a new login to the list
	 * 
	 * @param login
	 *            the login info to add
	 */
	public void add(final Login login) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// add the item
				objectList.add(login);
				// notify the view
				firePropertyChange("LOGIN_ADD", null, login);
			}
		});
	}

	/**
	 * Removes the login information from the list
	 * 
	 * @param login
	 *            the login information to remove
	 */
	public void remove(final Login login) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.remove(login);
				firePropertyChange("LOGIN_REMOVE", login, null);
			}
		});
	}

	/**
	 * Updates the login information in the list
	 * 
	 * @param login
	 *            the login information to update
	 */
	public void update(final Login login) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// assert we have this login
				if (!objectList.contains(login))
					return;
				// get the position of the entry
				int id = objectList.indexOf(login);
				objectList.set(id, login);
				firePropertyChange("LOGIN_UPDATE", null, login);
			}
		});
	}

	/**
	 * Removes all elements form the list
	 */
	public void removeAllEntries() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.clear();
				firePropertyChange("LOGIN_CLEAR", null, null);
			}
		});
	}

	/**
	 * Returns whether or not this login is in the list of managed object
	 * 
	 * @param newLogin
	 *            the login object to check
	 */
	public boolean contains(Login newLogin) {
		return objectList.contains(newLogin);
	}

	/**
	 * Returns the given login information by the username
	 * 
	 * @param username
	 *            the username to get the login information
	 * @return the accociated login info or null if nothing found
	 */
	public Login getLoginByUsername(String username) {
		// loop and search
		for (Login login : objectList) {
			if (login.getUsername().equalsIgnoreCase(username))
				return login;
		}
		// nothing found
		return null;
	}

	/**
	 * Returns all login informations in the list
	 * 
	 * @return the login list
	 */
	public List<Login> getLocations() {
		return objectList;
	}

	/**
	 * Converts the list to an array
	 * 
	 * @return the list as a array
	 */
	public Object[] toArray() {
		return objectList.toArray();
	}
}

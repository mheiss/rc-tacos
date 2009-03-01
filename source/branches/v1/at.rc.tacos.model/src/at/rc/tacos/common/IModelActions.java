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
 * Interface definition for the available actions
 * 
 * @author Michael
 */
public interface IModelActions {

	/** Login message */
	public final static String LOGIN = "message.login";
	/** Logout message */
	public final static String LOGOUT = "message.logout";
	/** General system messages */
	public final static String SYSTEM = "message.system";

	/** Add message */
	public final static String ADD = "message.add";
	/** Add message */
	public final static String ADD_ALL = "message.addAll";

	/** Remove message */
	public final static String REMOVE = "message.remove";
	/** List message */
	public final static String LIST = "message.list";
	/** Update message */
	public final static String UPDATE = "message.update";
}

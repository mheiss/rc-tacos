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
package at.rc.tacos.model;

/**
 * DAO Exception is used to encapsulate all error that occured during the query
 * of the database.
 * 
 * @author Michael
 */
public class DAOException extends Exception {

	/**
	 * The identification string
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Throws a new DAO exception with a source and a error message.
	 * 
	 * @param source
	 *            the source where the exception occured
	 * @param errorMessage
	 *            the error that occured
	 */
	public DAOException(String source, String errorMessage) {
		super("DAOError occured in " + source + ": " + errorMessage);
	}
}

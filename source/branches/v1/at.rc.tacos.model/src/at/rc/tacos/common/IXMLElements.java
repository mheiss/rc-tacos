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
 * Definition of the used xml elements
 * 
 * @author Michael
 */
public class IXMLElements {

	/** The root object */
	public static final String ROOT_ELEMENT = "message";
	/** The header object */
	public static final String HEADER_ELEMENT = "header";
	/** The userId */
	public static final String HEADER_USERID_ELEMENT = "userid";
	/** The unique sequence number field */
	public static final String HEADER_SEQUENCE_ELEMENT = "sequenceNumber";
	/** The timestamp */
	public static final String HEADER_TIMESTAMP_ELEMENT = "timestamp";
	/** the type */
	public static final String HEADER_TYPE_ELEMENT = "contentType";
	/** the query string */
	public static final String HEADER_QUERY_ELEMENT = "queryString";
	/** the query filter */
	public static final String HEADER_FILTER_ELEMENT = "queryFilter";
	/** The content wrapper */
	public static final String CONTENT_ELEMENT = "content";
}

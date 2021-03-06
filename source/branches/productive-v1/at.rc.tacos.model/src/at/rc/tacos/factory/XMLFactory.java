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
package at.rc.tacos.factory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.codec.MessageDecoder;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IXMLElements;
import at.rc.tacos.model.QueryFilter;

/**
 * Provides methods to enocde and decode the messages. This class uses StAx for
 * xml operations.
 * 
 * @author Michael
 */
public class XMLFactory {

	// the xml input
	private String xmlSource;

	// the header fields
	private String userId; // the username of the loged in user
	private String sequenceId; // the (uniqe) number to identify a package
	private long timestamp; // the timestamp of the message
	private String contentType; // the type of the body content. e.g.
								// RosterEntry.ID
	private String queryString; // the type of the query. e.g message.add
	private QueryFilter queryFilter;// the result should be filtered. e.g by id

	/**
	 * Default constructor specifying a user for the session
	 */
	public XMLFactory() {
		timestamp = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Sets up the factory to decode a object.
	 * 
	 * @param xmlSource
	 *            the xml stream to decode.
	 * @throws IllegalArgumentException
	 *             if the given string is null or contains no xml
	 */
	public void setupDecodeFactory(String xmlSource) {
		if (xmlSource == null || xmlSource.trim().isEmpty())
			throw new IllegalArgumentException("The source string cannot be null or empty");
		if (!xmlSource.startsWith("<") & !xmlSource.endsWith(">"))
			throw new IllegalArgumentException("This source string must be encoded in xml");
		this.xmlSource = xmlSource;
		// create a new filter
		queryFilter = new QueryFilter();
	}

	/**
	 * Encode the object into xml and returns the encoded message.<br>
	 * The timestamp will be set to the actual time when the message was encoded<br>
	 * 
	 * @param messageList
	 *            the objects to encode into xml
	 * @return the serialized object list
	 */
	public String encode(List<AbstractMessage> messageList) {
		// Create an output factory
		XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
		XMLStreamWriter xmlw = null;

		StringWriter output = null;
		try {
			// the writer for the xml result
			output = new StringWriter();
			// Create an XML stream writer
			xmlw = xmlof.createXMLStreamWriter(output);

			// Write XML prologue
			xmlw.writeStartDocument();
			xmlw.writeStartElement(IXMLElements.ROOT_ELEMENT);
			xmlw.writeStartElement(IXMLElements.HEADER_ELEMENT);

			// write the user id
			xmlw.writeStartElement(IXMLElements.HEADER_USERID_ELEMENT);
			xmlw.writeCharacters(userId);
			xmlw.writeEndElement();

			// write the sequence number
			xmlw.writeStartElement(IXMLElements.HEADER_SEQUENCE_ELEMENT);
			xmlw.writeCharacters(sequenceId);
			xmlw.writeEndElement();

			// write the timestamp
			xmlw.writeStartElement(IXMLElements.HEADER_TIMESTAMP_ELEMENT);
			xmlw.writeCharacters(String.valueOf(timestamp));
			xmlw.writeEndElement();

			// write the type
			xmlw.writeStartElement(IXMLElements.HEADER_TYPE_ELEMENT);
			xmlw.writeCharacters(contentType);
			xmlw.writeEndElement();

			// write the queryString
			xmlw.writeStartElement(IXMLElements.HEADER_QUERY_ELEMENT);
			xmlw.writeCharacters(queryString);
			xmlw.writeEndElement();

			// loop and write the query filters to apply
			if (queryFilter != null) {
				// encode the status messages
				for (Entry<String, String> entry : queryFilter.getFilterList().entrySet()) {
					xmlw.writeStartElement(IXMLElements.HEADER_FILTER_ELEMENT);
					xmlw.writeAttribute("type", entry.getKey());
					xmlw.writeAttribute("value", entry.getValue());
					xmlw.writeEndElement();
				}
			}

			// end of the header
			xmlw.writeEndElement();

			// check if we have a list of objects to encode
			if (messageList != null && messageList.size() > 0) {
				// write the content
				xmlw.writeStartElement(IXMLElements.CONTENT_ELEMENT);

				// loop and encode the object
				for (AbstractMessage message : messageList)
					ProtocolCodecFactory.getDefault().getEncoder(contentType).doEncode(message, xmlw);

				// end of the body
				xmlw.writeEndElement();
			}

			// End of the root element
			xmlw.writeEndElement();
			// Write document end. This closes all open structures
			xmlw.writeEndDocument();
			// Close the writer to flush the output
			xmlw.close();

			// replace unwanted characters
			String message = output.toString();
			message = message.replaceAll("\\s\\s+|\\n|\\r", "<![CDATA[<br/>]]>");
			return message;
		}
		catch (XMLStreamException xmlse) {
			System.out.println("Failed to serialize " + contentType + " to xml");
			System.out.println(xmlse.getMessage());
			xmlse.printStackTrace();
			return null;
		}
		finally {
			try {
				if (xmlw != null)
					xmlw.close();
				if (output != null)
					output.close();
			}
			catch (IOException ioe) {
				System.out.println("Error while closing the output stream");
				System.out.println(ioe.getMessage());
			}
			catch (XMLStreamException xmlSe) {
				System.out.println("Error while closing the xml writers");
				System.out.println(xmlSe.getMessage());
			}
		}
	}

	/**
	 * Decodes the message and returns a string list with the content items as
	 * String
	 * 
	 * @return the decoded list of objects
	 */
	public ArrayList<AbstractMessage> decode() {
		ArrayList<AbstractMessage> objects = new ArrayList<AbstractMessage>();

		// create the input stream out of the input
		StringReader input = new StringReader(xmlSource);
		XMLEventReader r = null;
		XMLInputFactory f = XMLInputFactory.newInstance();
		// body element
		boolean isBodyElement = false;
		// do
		try {
			r = f.createXMLEventReader(input);
			while (r.hasNext()) {
				// the element
				XMLEvent event = r.nextEvent();
				if (event.isStartElement()) {
					// get the element
					StartElement start = event.asStartElement();
					String startName = start.getName().getLocalPart();

					// get the type of the element and set the corresponding
					// value
					if (IXMLElements.HEADER_USERID_ELEMENT.equalsIgnoreCase(startName))
						userId = r.getElementText();
					if (IXMLElements.HEADER_SEQUENCE_ELEMENT.equalsIgnoreCase(startName))
						sequenceId = r.getElementText();
					if (IXMLElements.HEADER_TIMESTAMP_ELEMENT.equalsIgnoreCase(startName))
						timestamp = Long.parseLong(r.getElementText());
					if (IXMLElements.HEADER_TYPE_ELEMENT.equalsIgnoreCase(startName))
						contentType = r.getElementText();
					if (IXMLElements.HEADER_QUERY_ELEMENT.equalsIgnoreCase(startName))
						queryString = r.getElementText();
					if (IXMLElements.HEADER_FILTER_ELEMENT.equalsIgnoreCase(startName)) {
						Attribute typeAttr = start.getAttributeByName(new QName("type"));
						Attribute valueAttr = start.getAttributeByName(new QName("value"));
						queryFilter.add(typeAttr.getValue(), valueAttr.getValue());
					}
					// check if we have a body item
					if (IXMLElements.CONTENT_ELEMENT.equalsIgnoreCase(startName))
						isBodyElement = true;
					// body of the message
					if (isBodyElement) {
						// get a decoder
						MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(contentType);
						// decode the message
						AbstractMessage message = decoder.doDecode(r);
						objects.add(message);
					}
				}
			}
			// return the parsed elements
			return objects;
		}
		catch (XMLStreamException xmlSe) {
			System.out.println("Error while parsing the given input stream");
			System.out.println(xmlSe.getMessage());
			xmlSe.printStackTrace();
			// error occured
			return null;
		}
		// cleanup
		finally {
			try {
				if (r != null)
					r.close();
				if (input != null)
					input.close();
			}
			catch (XMLStreamException xmlSe) {
				System.out.println("Error while closing the reader and input stream");
				System.out.println(xmlSe.getMessage());
			}
		}
	}

	// SETTERS FOR THE ENCODE VALUES
	/**
	 * Sets the timestamp when the message is send
	 * 
	 * @param timestamp
	 *            the current time in milli seconds
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets the user id of the message owner.
	 * 
	 * @param userId
	 *            the username
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Sets the (unique) sequence number for this message
	 * 
	 * @param sequenceId
	 *            the unique number to identify the package
	 */
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * Sets the content type of the message
	 * 
	 * @param contentType
	 *            the content type of the message
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Sets the query string (the used operation like add or listing) for that
	 * package
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * Sets a filter for the query string.
	 * 
	 * @param queryFilter
	 *            the filter to apply
	 */
	public void setFilter(QueryFilter queryFilter) {
		this.queryFilter = queryFilter;
	}

	// GETTERS FOR THE DECODED VALUES
	/**
	 * Returns the user identification string.<br>
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Returns the sequence number for that message.
	 * 
	 * @return the used sequence number
	 */
	public String getSequenceId() {
		return sequenceId;
	}

	/**
	 * Returns the timestamp of the xml message
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns the type of this xml message.<br>
	 * The type specifies the content of the message.<br>
	 * Examples for the type: <code>RosterEntry.ID</code><br>
	 * When you call <code>decode</code> you will get a list of Objects from the
	 * given message type.
	 * 
	 * @return the messageType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns the queryString used in this xml message.<br>
	 * The queryString helps to categorize the message.<br>
	 * Examples for the query string <code>message.add</code><br>
	 * With this string you can process the data and set the actions needed. For
	 * example add the content to a list.
	 * 
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * Returns the applied query filter for the message.<br>
	 * The query filter is used to filter the result by specific values like the
	 * id, so that only results are listed with the id 1 for example.
	 * 
	 * @return the applied filter
	 */
	public QueryFilter getQueryFilter() {
		return queryFilter;
	}
}

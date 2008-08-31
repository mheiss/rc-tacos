package at.rc.tacos.factory;

import java.io.IOException;

import java.io.StringReader;
import java.io.StringWriter;
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
import at.rc.tacos.common.Message;

/**
 * Provides methods to enocde and decode the messages.
 * This class uses StAx for xml operations.
 * @author Michael
 */
public class XMLFactory
{   
	/**
	 * Default constructor specifying a user for the session
	 */
	public XMLFactory() { }

	/**
	 * Encode the object into xml and returns the encoded message.<br>
	 * The timestamp will be set to the actual time when the message was encoded<br>
	 * @param messageList the objects to encode into xml
	 * @return the serialized object list
	 */
	public String encode(Message message) throws XMLStreamException, IOException
	{
		// Create an output factory
		XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
		XMLStreamWriter xmlw = null;

		StringWriter output = null;
		try
		{
			//the writer for the xml result
			output = new StringWriter();
			// Create an XML stream writer
			xmlw = xmlof.createXMLStreamWriter(output);

			// Write XML prologue
			xmlw.writeStartDocument();
			xmlw.writeStartElement(IXMLElements.ROOT_ELEMENT);
			xmlw.writeStartElement(IXMLElements.HEADER_ELEMENT);

			//write the user id
			xmlw.writeStartElement(IXMLElements.HEADER_USERID_ELEMENT);
			xmlw.writeCharacters(message.getUsername());
			xmlw.writeEndElement();

			//write the sequence number
			xmlw.writeStartElement(IXMLElements.HEADER_SEQUENCE_ELEMENT);
			xmlw.writeCharacters(message.getSequenceId());
			xmlw.writeEndElement();

			//write the timestamp
			xmlw.writeStartElement(IXMLElements.HEADER_TIMESTAMP_ELEMENT);
			xmlw.writeCharacters(String.valueOf(message.getTimestamp()));
			xmlw.writeEndElement();

			//write the type
			xmlw.writeStartElement(IXMLElements.HEADER_TYPE_ELEMENT);
			xmlw.writeCharacters(message.getContentType());
			xmlw.writeEndElement();

			//write the queryString
			xmlw.writeStartElement(IXMLElements.HEADER_QUERY_ELEMENT);
			xmlw.writeCharacters(message.getQueryString());
			xmlw.writeEndElement();

			//loop and write the query filters to apply
			if(message.getQueryFilter() != null)
			{
				//encode the status messages
				for(Entry<String,String> entry:message.getQueryFilter().getFilterList().entrySet())
				{
					xmlw.writeStartElement(IXMLElements.HEADER_FILTER_ELEMENT);
					xmlw.writeAttribute("type", entry.getKey());
					xmlw.writeAttribute("value", entry.getValue());
					xmlw.writeEndElement();
				}
			}

			//end of the header
			xmlw.writeEndElement();

			//check if we have a list of objects to encode
			if(message.getMessageList() != null && message.getMessageList().size() > 0)
			{
				//write the content
				xmlw.writeStartElement(IXMLElements.CONTENT_ELEMENT);

				//loop and encode the object
				for(AbstractMessage messageContent: message.getMessageList())
					ProtocolCodecFactory.getDefault().getEncoder(message.getContentType()).doEncode(messageContent, xmlw);

				//end of the body
				xmlw.writeEndElement();
			}

			// End of the root element
			xmlw.writeEndElement();
			// Write document end. This closes all open structures
			xmlw.writeEndDocument();
			// Close the writer to flush the output
			xmlw.close();

			//replace unwanted characters
			String strMessage = output.toString();
			return strMessage.replaceAll("\\s\\s+|\\n|\\r", "<![CDATA[<br/>]]>");
		}
		finally
		{
			if( xmlw!= null)
				xmlw.close();
			if(output != null)
				output.close();
		}
	}

	/**
	 * Decodes the message and returns a string list with the content items as String
	 * @return the decoded list of objects
	 */
	public Message decode(String xmlSource) throws XMLStreamException
	{
		//validate the passed string
		if(xmlSource == null || xmlSource.trim().isEmpty())
			throw new IllegalArgumentException("The source string cannot be null or empty");
		if(!xmlSource.startsWith("<") &! xmlSource.endsWith(">"))
			throw new IllegalArgumentException("This source string must be encoded in xml");
		
		//the decoded message
		Message message = new Message();	

		//create the input stream out of the input
		StringReader input = new StringReader(xmlSource);
		XMLEventReader r = null;
		XMLInputFactory f = XMLInputFactory.newInstance();
		//body element
		boolean isBodyElement = false;

		try
		{
			r = f.createXMLEventReader(input);   
			while (r.hasNext()) 
			{
				//the element
				XMLEvent event = r.nextEvent();
				if (event.isStartElement()) 
				{
					//get the element
					StartElement start = event.asStartElement();
					String startName = start.getName().getLocalPart();

					//get the type of the element and set the corresponding value
					if(IXMLElements.HEADER_USERID_ELEMENT.equalsIgnoreCase(startName))
						message.setUsername(r.getElementText());
					if(IXMLElements.HEADER_SEQUENCE_ELEMENT.equalsIgnoreCase(startName))
						message.setSequenceId(r.getElementText());
					if(IXMLElements.HEADER_TIMESTAMP_ELEMENT.equalsIgnoreCase(startName))
						message.setTimestamp(Long.parseLong(r.getElementText()));
					if(IXMLElements.HEADER_TYPE_ELEMENT.equalsIgnoreCase(startName))
						message.setContentType(r.getElementText());
					if(IXMLElements.HEADER_QUERY_ELEMENT.equalsIgnoreCase(startName))
						message.setQueryString(r.getElementText());
					if(IXMLElements.HEADER_FILTER_ELEMENT.equalsIgnoreCase(startName))
					{
						Attribute typeAttr = start.getAttributeByName(new QName("type"));
						Attribute valueAttr = start.getAttributeByName(new QName("value"));
						message.getQueryFilter().add(typeAttr.getValue(),valueAttr.getValue());
					}
					//check if we have a body item
					if(IXMLElements.CONTENT_ELEMENT.equalsIgnoreCase(startName))
						isBodyElement = true;
					//body of the message
					if(isBodyElement)
					{
						//get a decoder
						MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(message.getContentType());
						//decode the message
						message.addMessage(decoder.doDecode(r));
					}
				}
			}
			//return the decoded objects
			return message;
		}
		finally
		{
			if(r!= null)
				r.close();
			if(input != null)
				input.close();
		}
	}
}

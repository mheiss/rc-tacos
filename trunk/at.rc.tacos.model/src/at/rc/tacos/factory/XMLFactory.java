package at.rc.tacos.factory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.ArrayList;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.codec.MessageDecoder;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IXMLElements;

/**
 * Provides methods to enocde and decode the messages.
 * This class uses StAx for xml operations.
 * @author Michael
 */
public class XMLFactory
{   
    //the xml input
    private String xmlSource;

    //the header fields
    private String userId;          //the username of the loged in user
    private long timestamp;         //the timestamp of the message
    private String contentType;     //the type of the body content. e.g. RosterEntry.ID
    private String queryString;     //the type of the query. e.g message.add

    /**
     * Default constructor specifying a user for the session
     */
    public XMLFactory() { }

    /**
     * Sets up the factory to encode a object.
     * @param userId the identification of the authenticated user
     * @param contentType the type of the message content. e.g <code>RosterEntry.ID</code>
     * @param queryString the type of the query. e.g message.add
     */
    public void setupEncodeFactory(String userId,String contentType,String queryString)
    {
        this.userId = userId;
        this.contentType = contentType;
        this.queryString = queryString;
        this.timestamp = new Date().getTime();
    }

    /**
     * Sets up the factory to decode a object.
     * @param xmlSource the xml stream to decode.
     * @throws IllegalArgumentException if the given string is null or contains no xml
     */
    public void setupDecodeFactory(String xmlSource)
    {
        if(xmlSource == null || xmlSource.trim().isEmpty())
            throw new IllegalArgumentException("The source string canno be null or empty");
        if(!xmlSource.startsWith("<") &! xmlSource.endsWith(">"))
            throw new IllegalArgumentException("This source string must be encoded in xml");
        this.xmlSource = xmlSource;
    }

    /**
     * Encode the object into xml and returns the encoded message.<br>
     * The timestamp will be set to the actual time when the message was encoded<br>
     * @return the serialized object list
     */
    public String encode(ArrayList<AbstractMessage> messageList)
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
            xmlw.writeCharacters(userId);
            xmlw.writeEndElement();

            //write the timestamp
            xmlw.writeStartElement(IXMLElements.HEADER_TIMESTAMP_ELEMENT);
            xmlw.writeCharacters(String.valueOf(timestamp));
            xmlw.writeEndElement();

            //write the type
            xmlw.writeStartElement(IXMLElements.HEADER_TYPE_ELEMENT);
            xmlw.writeCharacters(contentType);
            xmlw.writeEndElement();

            //write the queryString
            xmlw.writeStartElement(IXMLElements.HEADER_QUERY_ELEMENT);
            xmlw.writeCharacters(queryString);
            xmlw.writeEndElement();

            //end of the header
            xmlw.writeEndElement();

            //check if we have a list of objects to encode
            if(messageList != null)
            {
                //write the content
                xmlw.writeStartElement(IXMLElements.CONTENT_ELEMENT);
                
                //loop and encode the object
                for(AbstractMessage message:messageList)
                    ProtocolCodecFactory.getDefault().getEncoder(contentType).doEncode(message, xmlw);
                
                //end of the body
                xmlw.writeEndElement();
            }

            // End of the root element
            xmlw.writeEndElement();
            // Write document end. This closes all open structures
            xmlw.writeEndDocument();
            // Close the writer to flush the output
            xmlw.close();

            return output.toString();
        }
        catch(XMLStreamException xmlse)
        {
            System.out.println("Failed to serialize "+contentType+" to xml");
            System.out.println(xmlse.getMessage());
            xmlse.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
                if( xmlw!= null)
                    xmlw.close();
                if(output != null)
                    output.close();
            }
            catch (IOException ioe) 
            {
                System.out.println("Error while closing the output stream");
                System.out.println(ioe.getMessage());
            }
            catch(XMLStreamException xmlSe)
            {
                System.out.println("Error while closing the xml writers");
                System.out.println(xmlSe.getMessage());
            }
        }
    }

    /**
     * Decodes the message and returns a string list with the 
     * content items as String
     */
    public ArrayList<AbstractMessage> decode()
    {
        ArrayList<AbstractMessage> objects = new ArrayList<AbstractMessage>();
        //create the input stream out of the input
        StringReader input = new StringReader(xmlSource);
        XMLEventReader r = null;
        XMLInputFactory f = XMLInputFactory.newInstance();
        //body element
        boolean isBodyElement = false;
        //do
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
                        userId = r.getElementText();
                    if(IXMLElements.HEADER_TIMESTAMP_ELEMENT.equalsIgnoreCase(startName))
                        timestamp = Long.parseLong(r.getElementText());
                    if(IXMLElements.HEADER_TYPE_ELEMENT.equalsIgnoreCase(startName))
                        contentType = r.getElementText();
                    if(IXMLElements.HEADER_QUERY_ELEMENT.equalsIgnoreCase(startName))
                        queryString = r.getElementText();
                    //check if we have a body item
                    if(IXMLElements.CONTENT_ELEMENT.equalsIgnoreCase(startName))
                        isBodyElement = true;
                    //body of the message
                    if(isBodyElement)
                    {
                        //get a decoder
                        MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(contentType);
                        //decode the message
                        AbstractMessage message = decoder.doDecode(r);
                        objects.add(message);
                    }
                }
            }
            //return the parsed elements
            return objects;
        } 
        catch(XMLStreamException xmlSe)
        {
            System.out.println("Error while parsing the given input stream");
            System.out.println(xmlSe.getMessage());
            xmlSe.printStackTrace();
            //error occured
            return null;
        }
        //cleanup
        finally
        {
            try
            {
                if( r!= null)
                    r.close();
                if(input != null)
                    input.close();
            }
            catch(XMLStreamException xmlSe)
            {
                System.out.println("Error while closing the reader and input stream");
                System.out.println(xmlSe.getMessage());
            }
        }
    }

    // GETTERS FOR THE DECODED VALUES
    /**
     * Returns the user identification string.<br>
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Returns the timestamp of the xml message
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Returns the type of this xml message.<br>
     * The type specifies the content of the message.<br>
     * Examples for the type: <code>RosterEntry.ID</code><br>
     * When you call <code>decode</code> you will get a list of
     * Objects from the given message type.
     * @return the messageType
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Returns the queryString used in this xml message.<br>
     * The queryString helps to categorize the message.<br>
     * Examples for the query string <code>message.add</code><br>
     * With this string you can process the data and set the actions needed.
     * For example add the content to a list.
     * @return the queryString
     */
    public String getQueryString()
    {
        return queryString;
    }
}

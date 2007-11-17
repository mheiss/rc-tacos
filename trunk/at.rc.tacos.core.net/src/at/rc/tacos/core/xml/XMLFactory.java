package at.rc.tacos.core.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.xml.codec.MessageDecoder;
import at.rc.tacos.core.xml.codec.ProtocolCodecFactory;

/**
 * Provides methods to enocde and decode the net messages
 * @author Michael
 */
public class XMLFactory
{   
    /** The root object */
    public static final String ROOT_ELEMENT = "message";
    /** The header object */
    public static final String HEADER_ELEMENT = "header";
    /** The userId */
    public static final String HEADER_USERID_ELEMENT = "userid";
    /** The timestamp */
    public static final String HEADER_TIMESTAMP_ELEMENT = "timestamp";
    /** the type */
    public static final String HEADER_TYPE_ELEMENT = "type";
    /** the action */
    public static final String HEADER_ACTION_ELEMENT = "action";
    /** the sequence */
    public static final String HEADER_SEQUENCE_ELEMENT = "sequence";
    /** The body object */
    public static final String BODY_ELEMENT = "body";
    /** The data item */
    public static final String BODY_DATA_ELEMENT = "data";

    //the xml input
    private String xmlSource;

    //the header fields
    private String userId;
    private long timestamp;
    private String type;
    private String action;
    private long sequence;

    /**
     * Default constructor specifying a user for the session
     */
    public XMLFactory() { }

    /**
     * Sets up the factory to encode a object
     */
    public void setupEncodeFactory(String userId,long timestamp,String type,String action,long sequence)
    {
        this.userId = userId;
        this.timestamp = timestamp;
        this.type = type;
        this.action = action;
        this.sequence = sequence;
    }

    /**
     * Sets up the factory to decode a object
     */
    public void setupDecodeFactory(String xmlSource)
    {
        this.xmlSource = xmlSource;
    }

    /**
     * Encode the object and return the result.
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
            // Now start with root element
            xmlw.writeStartElement(ROOT_ELEMENT);

            //write the header element
            xmlw.writeStartElement(HEADER_ELEMENT);

            //write the user id
            xmlw.writeStartElement(HEADER_USERID_ELEMENT);
            xmlw.writeCharacters(userId);
            xmlw.writeEndElement();

            //write the timestamp
            xmlw.writeStartElement(HEADER_TIMESTAMP_ELEMENT);
            xmlw.writeCharacters(String.valueOf(timestamp));
            xmlw.writeEndElement();

            //write the type
            xmlw.writeStartElement(HEADER_TYPE_ELEMENT);
            xmlw.writeCharacters(type);
            xmlw.writeEndElement();

            //write the action
            xmlw.writeStartElement(HEADER_ACTION_ELEMENT);
            xmlw.writeCharacters(action);
            xmlw.writeEndElement();

            //write the sequence
            xmlw.writeStartElement(HEADER_SEQUENCE_ELEMENT);
            xmlw.writeCharacters(String.valueOf(sequence));
            xmlw.writeEndElement();

            //end of the header
            xmlw.writeEndElement();

            //write the body
            xmlw.writeStartElement(BODY_ELEMENT);
            //loop and write the item
            for(AbstractMessage message:messageList)
            {
                xmlw.writeStartElement(AbstractMessage.ID);
                //encode the object
                ProtocolCodecFactory.getDefault().getEncoder(type).doEncode(message, xmlw);
                //end
                xmlw.writeEndElement();
            }
            //end of the body
            xmlw.writeEndElement();

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
            System.out.println("Failed to serialize "+type+" to xml");
            System.out.println(xmlse.getMessage());
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
                System.out.println("Errow while closing output stream");
                System.out.println(ioe.getMessage());
            }
            catch(XMLStreamException xmlSe)
            {
                System.out.println("Errow while closing the xml writers");
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
                    if(HEADER_USERID_ELEMENT.equalsIgnoreCase(startName))
                        userId = r.getElementText();
                    if(HEADER_TIMESTAMP_ELEMENT.equalsIgnoreCase(startName))
                        timestamp = Long.parseLong(r.getElementText());
                    if(HEADER_TYPE_ELEMENT.equalsIgnoreCase(startName))
                        type = r.getElementText();
                    if(HEADER_ACTION_ELEMENT.equalsIgnoreCase(startName))
                        action = r.getElementText();
                    if(HEADER_SEQUENCE_ELEMENT.equalsIgnoreCase(startName))
                        sequence = Long.parseLong(r.getElementText());
                    //check if we have a body item
                    if(BODY_ELEMENT.equalsIgnoreCase(startName))
                    {
                        //get a decoder
                        MessageDecoder decoder = ProtocolCodecFactory.getDefault().getDecoder(type);
                        //decode the message
                        objects.add(decoder.doDecode(r));
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
                System.out.println("Errow while closing the reader and input stream");
                System.out.println(xmlSe.getMessage());
            }
        }
    }

//  GETTERS FOR THE DECODED VALUES
    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @return the action
     */
    public String getAction()
    {
        return action;
    }

    /**
     * @return the sequence
     */
    public long getSequence()
    {
        return sequence;
    }
}

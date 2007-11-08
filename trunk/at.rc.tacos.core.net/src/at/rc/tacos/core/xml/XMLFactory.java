package at.rc.tacos.core.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.IXMLSerialize;

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

    //the xml input
    private String xmlSource;

    //the header fields
    private String userId;
    private long timestamp;
    private String type;
    private String action;
    private long sequence;

    /**
     * Default constructor
     */
    public XMLFactory() {}

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
     * Encode the object and return the result
     * @return
     */
    public String encode(IXMLSerialize xmlObject)
    {
        try
        {
            // Create an output factory
            XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

            //the writer for the xml result
            StringWriter output = new StringWriter();

            // Create an XML stream writer
            XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(output);

            // Write XML prologue
            xmlw.writeStartDocument();
            // Now start with root element
            xmlw.writeStartElement(ROOT_ELEMENT);

            //write the header element
            xmlw.writeStartElement("header");

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
            xmlObject.toXML(xmlw);
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
    }

    /**
     * Decodes the header of the message.
     */
    public XMLEventReader decodeHeader()
    {
        //create the input stream out of the input
        StringReader input = new StringReader(xmlSource);
        XMLInputFactory f = XMLInputFactory.newInstance();
        try 
        {
            XMLEventReader r = f.createXMLEventReader(input);   
            while (r.hasNext()) 
            {
                XMLEvent event = r.nextEvent();
                if (event.isStartElement()) 
                {
                    //get the element
                    StartElement start = event.asStartElement();
                    String startName = start.getName().getLocalPart();

                    //get the type of the element and set the corresponding value
                    if(HEADER_ACTION_ELEMENT.equalsIgnoreCase(startName))
                        userId = r.getElementText();
                    if(HEADER_TIMESTAMP_ELEMENT.equalsIgnoreCase(startName))
                        timestamp = Long.parseLong(r.getElementText());
                    if(HEADER_TYPE_ELEMENT.equalsIgnoreCase(startName))
                        type = r.getElementText();
                    if(HEADER_ACTION_ELEMENT.equalsIgnoreCase(startName))
                        action = r.getElementText();
                    if(HEADER_SEQUENCE_ELEMENT.equalsIgnoreCase(startName))
                        sequence = Long.parseLong(r.getElementText());
                    //skip the body
                    if(BODY_ELEMENT.equalsIgnoreCase(startName))
                        break;
                }
                // keep the reader open and return it
                return r;
            }
            //cleanup
            r.close();
        } 
        catch(XMLStreamException xmlSE)
        {
            System.out.println("Error while parsing the given input stream");
            System.out.println(xmlSE.getMessage());
        }
        //nothing to do
        return null;
    }
}

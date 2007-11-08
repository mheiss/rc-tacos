package at.rc.tacos.core.model;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.IXMLSerialize;

/**
 * A simple itemen that has a name
 * @author Michael
 */
public class Item implements IXMLSerialize
{
    private String id = null;

    /**
     * Default constructor
     */
    public Item() { }

    /**
     * Default constructor specifying the name of the item
     * @param id the name of the item
     */
    public Item(String id)
    {
        this.id = id;
    }

    //METHODS
    /**
     * Serialize the content to xml
     */
    @Override
    public void toXML(XMLStreamWriter writer)
    {
        try
        {
            //write the body
            writer.writeStartElement("id");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id1");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id2");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id3");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id4");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id5");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id6");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id7");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id8");
            writer.writeCharacters(id);
            writer.writeEndElement();
            //write the body
            writer.writeStartElement("id9");
            writer.writeCharacters(id);
            writer.writeEndElement();
        }
        catch(XMLStreamException xmlse)
        {
            System.out.println("Failed to serialize object: item");
            System.out.println(xmlse.getMessage());
        }
    }


    @Override
    public Item toObject(XMLEventReader reader)
    {
        //create new object
        Item newItem = new Item();
        try
        {
            //parse and set up the object
            while(reader.hasNext())
            {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) 
                {
                    //get the element
                    StartElement start = event.asStartElement();
                    String startName = start.getName().getLocalPart();

                    //get the type of the element and set the corresponding value
                    if("id".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id1".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id2".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id3".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id4".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id5".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id6".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id7".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id8".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                    //get the type of the element and set the corresponding value
                    if("id9".equalsIgnoreCase(startName))
                        newItem.setId(reader.getElementText());
                }
            }
            //return the element
            return newItem;
        }
        catch(XMLStreamException xmlse)
        {
            System.out.println("Failed to create item from xml");
            System.out.println(xmlse.getMessage());
            return null;
        }
        finally
        {
            try
            {
                //close the source
                reader.close();
            }
            catch(XMLStreamException xmlse)
            {
                System.out.println("Failed to close the reader");
                System.out.println(xmlse.getMessage());
            }
        }
    }

    //GETTERS AND SETTERS   
    /**
     * @return the id
     */
    public String getId() 
    {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) 
    {
        this.id = id;
    }
}

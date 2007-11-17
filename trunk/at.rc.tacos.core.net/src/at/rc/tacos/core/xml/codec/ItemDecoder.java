package at.rc.tacos.core.xml.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Item;

public class ItemDecoder implements MessageDecoder
{   
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {       
        //Create a new Item
        Item item = new Item();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Item.ID.equalsIgnoreCase(startName))
                    item = new Item();
                
                //get the type of the element and set the corresponding value
                if("name".equalsIgnoreCase(startName))
                    item.setName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Item.ID.equalsIgnoreCase(endElement))
                    return item;
            }
        }
        return null;
    }
}

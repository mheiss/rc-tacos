package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Competence;

public class CompetenceDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //create a new Competence
        Competence competence = new Competence();
        
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Competence.ID.equalsIgnoreCase(startName))
                    competence = new Competence();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    competence.setId(Integer.valueOf(reader.getElementText()));
                if("competenceName".equalsIgnoreCase(startName))
                    competence.setCompetenceName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Competence.ID.equalsIgnoreCase(endElement))
                    return competence;
            }
        }
        return null;
    }
}

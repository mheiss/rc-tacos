package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Period;

public class PeriodDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //Create a new notifier
       Period period = new Period();
            
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Period.ID.equalsIgnoreCase(startName))
                   period = new Period();
                
                //get the type of the element and set the corresponding value
                if("periodId".equalsIgnoreCase(startName))
                    period.setPeriodId(Integer.valueOf(reader.getElementText()));
                if("period".equalsIgnoreCase(startName))
                   period.setPeriod(reader.getElementText());
                if("serviceTypeCompetence".equalsIgnoreCase(startName))
                	period.setServiceTypeCompetence(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Period.ID.equalsIgnoreCase(endElement))
                    return period;
            }
        }
        return null;
    }
}

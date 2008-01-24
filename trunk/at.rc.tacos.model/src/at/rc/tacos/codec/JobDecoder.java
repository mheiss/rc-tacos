package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Job;

public class JobDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //create a new job
        Job job = new Job();
        
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Job.ID.equalsIgnoreCase(startName))
                    job = new Job();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    job.setId(Integer.valueOf(reader.getElementText()));
                if("jobName".equalsIgnoreCase(startName))
                    job.setJobName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Job.ID.equalsIgnoreCase(endElement))
                    return job;
            }
        }
        return null;
    }
}

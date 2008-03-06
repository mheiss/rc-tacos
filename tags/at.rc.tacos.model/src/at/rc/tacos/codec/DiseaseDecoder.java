package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Disease;

public class DiseaseDecoder implements MessageDecoder
{
    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
    {
        //create a new disease
        Disease disease = new Disease();
        
        //parse and set up the object
        while(reader.hasNext())
        {
            //the type of the event
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) 
            {
                String startName = event.asStartElement().getName().getLocalPart();
                //create a new item 
                if(Disease.ID.equalsIgnoreCase(startName))
                    disease = new Disease();
                
                //get the type of the element and set the corresponding value
                if("id".equalsIgnoreCase(startName))
                    disease.setId(Integer.valueOf(reader.getElementText()));
                if("diseaseName".equalsIgnoreCase(startName))
                    disease.setDiseaseName(reader.getElementText());
            }
            //check for the end element, and return the object
            if(event.isEndElement())
            {
                //get the name
                String endElement = event.asEndElement().getName().getLocalPart();
                //check if we have reached the end
                if (Disease.ID.equalsIgnoreCase(endElement))
                    return disease;
            }
        }
        return null;
    }
}

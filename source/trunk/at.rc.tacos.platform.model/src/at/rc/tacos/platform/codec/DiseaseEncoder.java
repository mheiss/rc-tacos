package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Disease;

/**
 * Class to encode a disease to xml
 * @author b.thek
 */
public class DiseaseEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a disease
    	at.rc.tacos.platform.model.Disease disease = (Disease)message;
    	
        //assert valid
        if(disease ==  null)
        {
            System.out.println("WARNING: Object disease is null and cannot be encoded");
            return;
        }
      
        //write the start element
        writer.writeStartElement(Disease.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(disease.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        if(disease.getDiseaseName() != null)
        {
	        writer.writeStartElement("diseaseName");
	        writer.writeCharacters(disease.getDiseaseName());
	        writer.writeEndElement();
        }
        
        //end of the disease
        writer.writeEndElement();
    }
}

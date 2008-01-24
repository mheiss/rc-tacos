package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Disease;

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
    	Disease disease = (Disease)message;
      
        //write the start element
        writer.writeStartElement(Disease.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(disease.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("diseaseName");
        writer.writeCharacters(disease.getDiseaseName());
        writer.writeEndElement();
        
        //end of the disease
        writer.writeEndElement();
    }
}

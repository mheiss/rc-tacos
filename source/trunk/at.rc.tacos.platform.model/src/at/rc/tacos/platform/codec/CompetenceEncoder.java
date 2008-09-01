package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Competence;

/**
 * Class to encode a competence to xml
 * @author Michael
 */
public class CompetenceEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a Competence
        Competence competence = (Competence)message;
        
        //assert valid
        if(competence ==  null)
        {
            System.out.println("WARNING: Object competence is null and cannot be encoded");
            return;
        }
        
        //write the start element
        writer.writeStartElement(Competence.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(competence.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        if(competence.getCompetenceName() != null)
        {
	        writer.writeStartElement("competenceName");
	        writer.writeCharacters(competence.getCompetenceName());
	        writer.writeEndElement();
        }
        
        //end of the competence
        writer.writeEndElement();
    }
}

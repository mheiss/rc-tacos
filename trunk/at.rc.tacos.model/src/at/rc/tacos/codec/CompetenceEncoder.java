package at.rc.tacos.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Competence;

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
        
        //write the start element
        writer.writeStartElement(Competence.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(competence.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        writer.writeStartElement("competenceName");
        writer.writeCharacters(competence.getCompetenceName());
        writer.writeEndElement();
        
        //end of the competence
        writer.writeEndElement();
    }
}

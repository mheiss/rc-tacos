package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Job;

/**
 * Class to encode a job to xml
 * @author Michael
 */
public class JobEncoder implements MessageEncoder
{
    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
    {
        //Cast the object to a job
        Job job = (Job)message;
        
        //assert valid
        if(job ==  null)
        {
            System.out.println("WARNING: Object job is null and cannot be encoded");
            return;
        }
        
        //write the start element
        writer.writeStartElement(Job.ID);
       
        //write the elements and attributes
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(job.getId()));
        writer.writeEndElement();
        //write the elements and attributes
        if(job.getJobName() != null)
        {
	        writer.writeStartElement("jobName");
	        writer.writeCharacters(job.getJobName());
	        writer.writeEndElement();
        }
        
        //end of the job
        writer.writeEndElement();
    }
}

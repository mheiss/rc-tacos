package at.rc.tacos.platform.codec;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Period;

public class PeriodEncoder implements MessageEncoder
{
	@Override
	public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException
	{
		//Cast the object to a item
		Period period = (Period)message;

		//assert valid
		if(period ==  null)
		{
			System.out.println("WARNING: Object "+ getClass().getName()+" is null and cannot be encoded");
			return;
		}

		writer.writeStartElement(Period.ID);

		//write the elements and attributes
        writer.writeStartElement("periodId");
        writer.writeCharacters(String.valueOf(period.getPeriodId()));
        writer.writeEndElement();

		//write the period
		if(period.getPeriod() != null)
		{
			writer.writeStartElement("period");
			writer.writeCharacters(period.getPeriod());
			writer.writeEndElement();
		}
		//the serviceType competence (_HA, _ZD, _LSD)
		if(period.getServiceTypeCompetence() != null)
		{
			writer.writeStartElement("serviceTypeCompetence");
			writer.writeCharacters(period.getServiceTypeCompetence());
			writer.writeEndElement();
		}

		//end
		writer.writeEndElement();
	}

}

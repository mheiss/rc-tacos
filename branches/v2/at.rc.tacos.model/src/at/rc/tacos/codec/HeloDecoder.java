package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Helo;

public class HeloDecoder implements MessageDecoder
{
	@Override
	public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException
	{
		//create a new disease
		Helo helo = new Helo();

		//parse and set up the object
		while(reader.hasNext())
		{
			//the type of the event
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) 
			{
				String startName = event.asStartElement().getName().getLocalPart();
				//create a new item 
				if(Helo.ID.equalsIgnoreCase(startName))
					helo = new Helo();

				//get the type of the element and set the corresponding value
				if("id".equalsIgnoreCase(startName))
					helo.setId(Integer.valueOf(reader.getElementText()));
				if("serverIp".equalsIgnoreCase(startName))
					helo.setServerIp(reader.getElementText());
				if("serverPort".equalsIgnoreCase(startName))
					helo.setServerPort(Integer.valueOf(reader.getElementText()));
				if("serverPrimary".equalsIgnoreCase(startName))
					helo.setServerPrimary(Boolean.valueOf(reader.getElementText()));
				if("redirect".equalsIgnoreCase(startName))
					helo.setRedirect(Boolean.valueOf(reader.getElementText()));
			}
			//check for the end element, and return the object
			if(event.isEndElement())
			{
				//get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				//check if we have reached the end
				if (Helo.ID.equalsIgnoreCase(endElement))
					return helo;
			}
		}
		return null;
	}
}
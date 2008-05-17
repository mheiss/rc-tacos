package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Lock;

public class LockDecoder implements MessageDecoder
{
	@Override
	public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException 
	{
		//create a new lock object
		Lock lock = new Lock();

		//parse and set up the object
		while(reader.hasNext())
		{
			//the type of the event
			XMLEvent event = reader.nextEvent();
			if(event.isStartElement()) 
			{
				String startName = event.asStartElement().getName().getLocalPart();
				//create a new item 
				if(Lock.ID.equalsIgnoreCase(startName))
					lock = new Lock();

				//get the type of the element and set the corresponding value
				if("contentId".equalsIgnoreCase(startName))
					lock.setContentId(reader.getElementText());
				if("lockedId".equalsIgnoreCase(startName))
					lock.setLockedId(Integer.valueOf(reader.getElementText()));
				if("lockedBy".equalsIgnoreCase(startName))
					lock.setLockedBy(reader.getElementText());
				if("hasLock".equalsIgnoreCase(startName))
					lock.setHasLock(Boolean.valueOf(reader.getElementText()));
			}
			//check for the end element, and return the object
			if(event.isEndElement())
			{
				//get the name
				String endElement = event.asEndElement().getName().getLocalPart();
				//check if we have reached the end
				if (Lock.ID.equalsIgnoreCase(endElement))
					return lock;
			}
		}
		return null;
	}
}
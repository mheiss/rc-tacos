package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SickPersonManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.SickPerson;

public class SickPersonListener extends ClientListenerAdapter
{
	//the sickPerson manager
	SickPersonManager manager = ModelFactory.getInstance().getSickPersonManager();
	
	@Override
	public void add(AbstractMessage addMessage)
	{
		//cast to a sick person and add it
        SickPerson person = (SickPerson)addMessage;
        manager.add(person);
	}
	
    @Override
    public void remove(AbstractMessage removeMessage)
    {
    	//cast to a sick person and remove it
    	SickPerson person = (SickPerson)removeMessage;
        manager.remove(person);
    }
	
	@Override
	public void update(AbstractMessage updateMessage)
	{
		//cast to a sick person and add it
		SickPerson person = (SickPerson)updateMessage;
        manager.update(person);
	}
	
    @Override
    public void list(List<AbstractMessage> listMessage)
    {    	
    	//remove all elements
    	manager.removeAllEntries();

        //loop and add all sick persons
        for(AbstractMessage detailObject:listMessage)
        {
        	//cast to a sick person and add it
        	SickPerson person = (SickPerson)detailObject;
        	manager.add(person);
        }
    }
}

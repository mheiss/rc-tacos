package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.DialysisTransportManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DialysisPatient;

public class DialysisPatientListener extends ClientListenerAdapter
{
	DialysisTransportManager manager = ModelFactory.getInstance().getDialyseManager();
	
    @Override
    public void add(AbstractMessage addMessage)
    {
        manager.add((DialysisPatient)addMessage);
    }
    
    @Override
    public void update(AbstractMessage updateMessage)
    {
        manager.update((DialysisPatient)updateMessage);  
    }
    
    @Override
    public void remove(AbstractMessage removeMessage)
    {
        manager.remove((DialysisPatient)removeMessage);  
    }
    
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
    	//reset the list
        manager.removeAllEntries();
        //add the entries
        for(AbstractMessage msg:listMessage)
	        manager.add((DialysisPatient)msg);
    }  
}
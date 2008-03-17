package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.CompetenceManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Competence;

public class CompetenceListener extends ClientListenerAdapter
{
	//the competence manager
	protected CompetenceManager manager = ModelFactory.getInstance().getCompetenceManager();
	
	@Override
	public void add(AbstractMessage addMessage)
	{
		//cast to a vehicle and add it
        Competence comp = (Competence)addMessage;
        manager.add(comp);
	}
	
    @Override
    public void remove(AbstractMessage removeMessage)
    {
    	//cast to a competence and remove it
        Competence comp = (Competence)removeMessage;
        manager.remove(comp);
    }
	
	@Override
	public void update(AbstractMessage updateMessage)
	{
		//cast to a competence and add it
        Competence comp = (Competence)updateMessage;
        manager.update(comp);
	}
	
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
    	//remove all stored Competences
    	manager.removeAllEntries();
        //loop and add all competences
        for(AbstractMessage detailObject:listMessage)
        {
        	//cast to a competence and add it
            Competence comp = (Competence)detailObject;
            manager.add(comp);
        }
    }
}

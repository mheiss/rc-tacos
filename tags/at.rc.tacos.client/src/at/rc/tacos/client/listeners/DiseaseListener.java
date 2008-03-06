package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.DiseaseManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Disease;

/**
 * Listens to changes of diseases
 * @author Michael
 *
 */
public class DiseaseListener extends ClientListenerAdapter
{
	//the disease manager
	private DiseaseManager manager = ModelFactory.getInstance().getDiseaseList();

	@Override
	public void add(AbstractMessage addMessage) 
	{
		Disease disease = (Disease)addMessage;
		manager.add(disease);
	}

	@Override
	public void list(ArrayList<AbstractMessage> listMessage) 
	{
		//remove all elements
		manager.removeAllEntries();
		//loop and add
		for(AbstractMessage listObject:listMessage)
		{
			manager.add((Disease)listObject);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage) 
	{
		//cast and remove
		Disease disease = (Disease)removeMessage;
		manager.remove(disease);
	}

	@Override
	public void update(AbstractMessage updateMessage) 
	{
		//cast and update
		Disease disease = (Disease)updateMessage;
		manager.update(disease);
	}
}

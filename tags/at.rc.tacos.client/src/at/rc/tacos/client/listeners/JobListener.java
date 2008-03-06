package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.JobManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Job;

public class JobListener extends ClientListenerAdapter
{
	//the job manager
	JobManager manager = ModelFactory.getInstance().getJobList();
	
	@Override
	public void add(AbstractMessage addMessage)
	{
		//cast to a job and add it
        Job job = (Job)addMessage;
        manager.add(job);
	}
	
    @Override
    public void remove(AbstractMessage removeMessage)
    {
    	//cast to a job and remove it
    	Job job = (Job)removeMessage;
        manager.remove(job);
    }
	
	@Override
	public void update(AbstractMessage updateMessage)
	{
		//cast to a job and add it
		Job job = (Job)updateMessage;
        manager.update(job);
	}
	
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
    	//remove all stored job
    	manager.removeAllEntries();
        //loop and add all job
        for(AbstractMessage detailObject:listMessage)
        {
        	//cast to a job and add it
            Job job = (Job)detailObject;
            manager.add(job);
        }
    }
}

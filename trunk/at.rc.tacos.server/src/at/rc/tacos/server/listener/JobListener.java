package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.QueryFilter;

public class JobListener extends ServerListenerAdapter
{
	private JobDAO jobDao = DaoFactory.MYSQL.createJobDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Job job = (Job)addObject;
        //add the job into the dao
        int id = jobDao.addJob(job);
        //set the returned id
        job.setId(id);
        return job;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	list.addAll(jobDao.listJobs());
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
    	Job job = (Job)removeObject;
    	jobDao.removeJob(job.getId());
    	return job;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
    	Job job = (Job)updateObject;
    	jobDao.updateJob(job);
    	return job;
    }
}
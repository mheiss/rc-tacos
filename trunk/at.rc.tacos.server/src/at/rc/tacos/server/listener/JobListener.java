package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.QueryFilter;

public class JobListener extends ServerListenerAdapter
{
	private JobDAO jobDao = DaoFactory.SQL.createJobDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
    {
        Job job = (Job)addObject;
        //add the job into the dao
        int id = jobDao.addJob(job);
        if(id == -1)
        	throw new DAOException("JobListener","Failed to add the job "+job);
        //set the returned id
        job.setId(id);
        return job;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	List<Job> jobList = jobDao.listJobs();
    	if(jobList == null)
    		throw new DAOException("JobListener","Failed to list the jobs"); 
    	list.addAll(jobList);
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
    {
    	Job job = (Job)removeObject;
    	if(!jobDao.removeJob(job.getId()))
    		throw new DAOException("JobListener","Failed to remove the job: "+job); 
    	return job;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
    {
    	Job job = (Job)updateObject;
    	if(!jobDao.updateJob(job))
    		throw new DAOException("JobListener","Failed to update the job: "+job); 
    	return job;
    }
}
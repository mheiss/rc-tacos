package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.model.Job;

public class JobDAOMemory implements JobDAO
{
	//the shared instance
	private static JobDAOMemory instance;
	//the list
	private List<Job> jobList;
	
	/**
	 * Default class constructor
	 */
	private JobDAOMemory()
	{
		jobList = new ArrayList<Job>();
	}
	
	/**
	 * Returns the shared instace
	 * @return the instance
	 */
	public static JobDAOMemory getInstance()
	{
		if(instance == null)
			instance = new JobDAOMemory();
		return instance;
	}
	
	@Override
	public int addJob(Job job) 
	{
		jobList.add(job);
		return jobList.size();
	}

	@Override
	public Job getJobById(int id) 
	{
		return jobList.get(id);
	}

	@Override
	public List<Job> listJobs() 
	{
		return jobList;
	}

	@Override
	public boolean removeJob(int id) 
	{
		if(jobList.remove(id) != null)
			return true;
		//nothing removed
		return false;
	}

	@Override
	public boolean updateJob(Job job) 
	{
		int index = jobList.indexOf(job);
		jobList.set(index, job);
		return true;
	}
}

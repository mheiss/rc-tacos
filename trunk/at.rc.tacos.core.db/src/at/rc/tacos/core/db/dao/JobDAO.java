package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Job;

public interface JobDAO 
{
	/**
	 * Adds a new job to the database and returns the unique id.
	 * @param job the job to add
	 * @return the unique id of the job in the database
	 */
    public int addJob(Job job);
    
    /**
     * Updates a job in the database
     * @param job the job to update
     * @return true if the update was successfully otherwise false
     */
    public boolean updateJob(Job job);
    
    /**
     * Removes the job from the database.
     * @param id the id of the job to remove
     * @return true if the deletion was successfully.
     */
    public boolean removeJob(int id);
    
    /**
     * Returns the job identified by the given id.
     * @param id the id to get the job from
     * @return the queried job
     */
    public Job getJobById(int id);  
    
    /**    
     * Returns a list of all stored jobs in the database
     * @return the complete list of all jobs
     */
	public List<Job> listJobs();
}

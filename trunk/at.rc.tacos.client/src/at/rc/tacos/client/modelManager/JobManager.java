package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Job;

public class JobManager extends PropertyManager 
{
    //the list
    private List<Job> objectList = new ArrayList<Job>();

    /**
     * Default class constructor
     */
    public JobManager() { }

    /**
     * Adds a new job to the list
     * @param job the job to add
     */
    public void add(final Job job) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(job);
                //notify the view
                firePropertyChange("JOB_ADD", null, job);
            }
        }); 
    }    

    /**
     * Removes the job from the list
     * @param job the job to remove
     */
    public void remove(final Job job) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(job);
                firePropertyChange("JOB_REMOVE", job, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the job in the list
     * @param job the job to update
     */
    public void update(final Job job) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                //get the position of the entry
                int id = objectList.indexOf(job);
                objectList.set(id, job);
                firePropertyChange("JOB_UPDATE", null, job); 
            }
        }); 
    }
    
    /**
     * Removes all elements form the list
     */
    public void removeAllEntries()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("JOB_CLEARED",null,null);
            }
        }); 
    }
    
    /**
     * Returns a given job by the name
     * @param jobName the name of the job to get
     */
    public Job getJobByName(String jobName)
    {
        //loop and search
        for(Job job :objectList)
        {
            if(job.getJobName().equalsIgnoreCase(jobName))
                return job;
        }
        //nothing found
        return null;
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }
}
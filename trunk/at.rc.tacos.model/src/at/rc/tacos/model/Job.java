package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * The available jobs 
 * @author Michael
 */
public class Job extends AbstractMessage
{
    //unique identification string
    public final static String ID = "jobs";
    
    //properties   
    private int id;
    private String jobName;
    
    /**
     * Default class constructor
     */
    public Job()
    {
        super(ID);
    }
    
    /**
     * Default class constructor for a job
     */
    public Job(String jobName)
    {
        this();
        this.jobName = jobName;
    }
    
    
    //METHODS
    /**
     * Returns the string based description
     * @return the string description
     */
    @Override
    public String toString()
    {
        return id + ":"+jobName;
    }
    
    /**
     * Returns the calculated hash code based on the job id.<br>
     * Two jobs have the same hash code if the id is the same.
     * @return the calculated hash code
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }


    /**
     * Returns whether the objects are equal or not.<br>
     * Two jobs are equal if, and only if, the job id is the same.
     * @return true if the id is the same otherwise false.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Job other = (Job) obj;
        if (id != other.id)
            return false;
        return true;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the internal unique id of the job.
     * @return the id the id of the database
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the name of the job
     * @return the name of the job
     */
    public String getJobName()
    {
        return jobName;
    }

    /**
     * Sets the unique number of the job.
     * @param id the unique id for the job
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Sets the name of the job
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }
}

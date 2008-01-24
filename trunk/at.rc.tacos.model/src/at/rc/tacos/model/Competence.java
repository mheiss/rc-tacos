package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * The available competences for the staff members
 * @author Michael
 */
public class Competence extends AbstractMessage
{
    //unique identification string
    public final static String ID = "competence";
    
    //properties   
    private int id;
    private String competenceName;
    
    /**
     * Default class constructor
     */
    public Competence()
    {
        super(ID);
    }
    
    /**
     * Default class constructor for a competence
     */
    public Competence(String competenceName)
    {
        this();
        this.competenceName = competenceName;
    }
    
    
    //METHODS
    /**
     * Returns the string based description
     * @return the string description
     */
    @Override
    public String toString()
    {
        return id + ":"+competenceName;
    }
    
    /**
     * Returns the calculated hash code based on the competence id.<br>
     * Two competences have the same hash code if the id is the same.
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
     * Two competences are equal if, and only if, the competence id is the same.
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
        final Competence other = (Competence) obj;
        if (id != other.id)
            return false;
        return true;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the internal unique id of the competence.
     * @return the id the id of the database
     */
    public int getId()
    {
        return id;
    }

    /**
     * Returns the name of the competence
     * @return the name of the competence
     */
    public String getCompetenceName()
    {
        return competenceName;
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
     * Sets the name of the competence
     * @param competenceName the competenceName to set
     */
    public void setCompetenceName(String competenceName)
    {
        this.competenceName = competenceName;
    }
}

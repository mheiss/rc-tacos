package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Competence;

public class CompetenceManager extends PropertyManager 
{
    //the list
    private List<Competence> objectList = new ArrayList<Competence>();

    /**
     * Default class constructor
     */
    public CompetenceManager() { }

    /**
     * Adds a new competence to the list
     * @param competence the competence to add
     */
    public void add(final Competence competence) 
    {
        Display.getDefault().asyncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.add(competence);
                firePropertyChange("COMPETENCE_ADD", null, competence);
            }
        }); 
    }    

    /**
     * Removes the competence from the list
     * @param competence the competence to remove
     */
    public void remove(final Competence competence) 
    {
        Display.getDefault().asyncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(competence);
                firePropertyChange("COMPETENCE_REMOVE", competence, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the competence in the list
     * @param competence the competence to update
     */
    public void update(final Competence competence) 
    {
        Display.getDefault().asyncExec(new Runnable ()    
        {
            public void run ()       
            {   
            	//assert we have this competence
            	if(!objectList.contains(competence))
            		return;
                //get the position of the entry
                int id = objectList.indexOf(competence);
                objectList.set(id, competence);
                firePropertyChange("COMPETENCE_UPDATE", null, competence); 
            }
        }); 
    }
    
    /**
     * Removes all elements form the list
     */
    public void removeAllEntries()
    {
        Display.getDefault().asyncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("COMPETENCE_CLEARED",null,null);
            }
        }); 
    }
    
    /**
     * Returns whether or not this competence is in the list of managed object
     * @param newCompetence the comptence to check
     */
    public boolean contains(Competence newCompetence)
    {
    	return objectList.contains(newCompetence);
    }
    
    /**
     * Returns a given competence by the name
     * @param competence name the name of the competence to get
     */
    public Competence getCompetenceByName(String competence)
    {
        //loop and search
        for(Competence comp :objectList)
        {
            if(comp.getCompetenceName().equalsIgnoreCase(competence))
                return comp;
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
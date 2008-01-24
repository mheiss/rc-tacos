package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.CallerDetail;

/**
 * Data source for notifiers
 * @author Michael
 */
public class NotifierDAOMemory implements CallerDAO
{    
    //the shared instance
    private static NotifierDAOMemory instance;
    
    //the data list
    private ArrayList<CallerDetail> notifierList; 

    /**
     * Default class constructor
     */
    private NotifierDAOMemory()
    {
        notifierList = new ArrayList<CallerDetail>();  
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static NotifierDAOMemory getInstance()
    {
        if (instance == null)
            instance = new NotifierDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        notifierList = new ArrayList<CallerDetail>();
    }

    @Override
    public int addCaller(CallerDetail notifierDetail)
    {
        notifierList.add(notifierDetail);
        return notifierList.size();
    }

    @Override
    public CallerDetail getCallerByID(int callerID)
    {
        return notifierList.get(callerID);
    }

    @Override
    public boolean updateCaller(CallerDetail notifierDetail)
    {
        int index = notifierList.indexOf(notifierDetail);
        notifierList.set(index, notifierDetail);
        return true;
    }
}

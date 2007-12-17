package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
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
    public void updateCaller(CallerDetail notifierDetail)
    {
        int index = notifierList.indexOf(notifierDetail);
        notifierList.remove(index);
        notifierList.add(index, notifierDetail);        
    }
    
    @Override
    public void removeCaller(CallerDetail notifierDetail)
    {
        notifierList.remove(notifierDetail);
    }

    @Override
    public CallerDetail getCallerByID(String callerID)
    {
        for(CallerDetail detail:notifierList)
            if(detail.getCallerName().equalsIgnoreCase(callerID))
                return detail;
        return null;
    }

    @Override
    public List<CallerDetail> listCallers()
    {
        return notifierList;
    }
}

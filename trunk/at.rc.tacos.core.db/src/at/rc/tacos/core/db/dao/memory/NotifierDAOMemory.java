package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.NotifierDetail;

/**
 * Data source for notifiers
 * @author Michael
 */
public class NotifierDAOMemory implements CallerDAO
{    
    //the shared instance
    private static NotifierDAOMemory instance;
    
    //the data list
    private ArrayList<NotifierDetail> notifierList; 

    /**
     * Default class constructor
     */
    private NotifierDAOMemory()
    {
        notifierList = new ArrayList<NotifierDetail>();
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
        notifierList = new ArrayList<NotifierDetail>();
    }

    @Override
    public int addCaller(NotifierDetail notifierDetail)
    {
        notifierList.add(notifierDetail);
        return notifierList.size();
    }
    
    @Override
    public void updateCaller(NotifierDetail notifierDetail)
    {
        int index = notifierList.indexOf(notifierDetail);
        notifierList.remove(index);
        notifierList.add(index, notifierDetail);        
    }
    
    @Override
    public void removeCaller(NotifierDetail notifierDetail)
    {
        notifierList.remove(notifierDetail);
    }

    @Override
    public NotifierDetail getCallerByID(String callerID)
    {
        for(NotifierDetail detail:notifierList)
            if(detail.getNotifierName().equalsIgnoreCase(callerID))
                return detail;
        return null;
    }

    @Override
    public List<NotifierDetail> listCallers()
    {
        return notifierList;
    }
}

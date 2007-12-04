package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.CallerDAO;
import at.rc.tacos.model.NotifierDetail;

/**
 * Data source for notifiers
 * @author Michael
 */
public class NotifierDAOTest implements CallerDAO
{    
    //the shared instance
    private static NotifierDAOTest instance;
    
    //the data list
    private ArrayList<NotifierDetail> notifierList; 

    /**
     * Default class constructor
     */
    private NotifierDAOTest()
    {
        notifierList = new ArrayList<NotifierDetail>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static NotifierDAOTest getInstance()
    {
        if (instance == null)
            instance = new NotifierDAOTest();
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
        return notifierList.indexOf(notifierDetail);
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

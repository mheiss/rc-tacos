package at.rc.tacos.core.db.dao.memory;

import java.sql.SQLException;
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
        //provide some data
        CallerDetail n1 = new CallerDetail("Notifer1","0664-123456789","Notes taken");
        CallerDetail n2 = new CallerDetail("Notifer2","0784-1548154","Notes taken");
        CallerDetail n3 = new CallerDetail("Notifer3","2147-123456789","Notes taken");
        notifierList.add(n1);
        notifierList.add(n2);
        notifierList.add(n3);
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
    public List<CallerDetail> listCallers()
    {
        return notifierList;
    }

    @Override
    public CallerDetail getCallerByID(int callerID) throws SQLException
    {
        return notifierList.get(callerID);
    }

    @Override
    public int getCallerId(CallerDetail notifierDetail) throws SQLException
    {
       return notifierList.indexOf(notifierDetail);
    }

    @Override
    public boolean removeCaller(int id) throws SQLException
    {
        if(notifierList.remove(id) != null)
                return true;
        return false;
    }

    @Override
    public boolean updateCaller(CallerDetail notifierDetail, int id)  throws SQLException
    {
        int index = notifierList.indexOf(notifierDetail);
        notifierList.remove(index);
        notifierList.add(index, notifierDetail);  
        return true;
    }
}

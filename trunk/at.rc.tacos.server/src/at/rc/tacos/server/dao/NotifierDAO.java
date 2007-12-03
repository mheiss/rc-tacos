package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.NotifierDetail;

/**
 * Data source for notifiers
 * @author Michael
 */
public class NotifierDAO
{
    //the shared instance
    private static NotifierDAO instance;
    //the data list
    private ArrayList<NotifierDetail> notifierList; 

    /**
     * Default private class constructor
     */
    private NotifierDAO()
    {
        //create the list
        notifierList = new ArrayList<NotifierDetail>();
        //load dummy data
        NotifierDetail n1 = new NotifierDetail("Notifer1","0664-123456789","Notes taken");
        NotifierDetail n2 = new NotifierDetail("Notifer2","0784-1548154","Notes taken");
        NotifierDetail n3 = new NotifierDetail("Notifer3","2147-123456789","Notes taken");
        notifierList.add(n1);
        notifierList.add(n2);
        notifierList.add(n3);
    }

    /**
     * Creates and returns the shared instance
     */
    public static NotifierDAO getInstance()
    {
        if( instance == null)
            instance = new NotifierDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<NotifierDetail> getList()
    {
        return notifierList;
    }
}

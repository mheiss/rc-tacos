package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * Data source for mobile phones
 * @author Michael
 */
public class MobilePhoneDAO
{
    //the shared instance
    private static MobilePhoneDAO instance;
    //the data list
    private ArrayList<MobilePhoneDetail> phoneList; 
    
    /**
     * Default private class constructor
     */
    private MobilePhoneDAO()
    {
        //create the list
        phoneList = new ArrayList<MobilePhoneDetail>();
        //load dummy data
        MobilePhoneDetail p1 = new MobilePhoneDetail("P1","0699-123456789");
        MobilePhoneDetail p2 = new MobilePhoneDetail("P2","0664-123456789");
        MobilePhoneDetail p3 = new MobilePhoneDetail("P3","0676-123456789");
        phoneList.add(p1);
        phoneList.add(p2);
        phoneList.add(p3);
    }
    
    /**
     * Creates and returns the shared instance
     */
    public static MobilePhoneDAO getInstance()
    {
        if( instance == null)
            instance = new MobilePhoneDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<MobilePhoneDetail> getList()
    {
        return phoneList;
    }
}

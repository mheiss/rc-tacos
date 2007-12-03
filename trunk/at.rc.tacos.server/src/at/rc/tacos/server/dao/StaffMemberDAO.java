package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.StaffMember;

/**
 * Data source for staff members
 * @author Michael
 */
public class StaffMemberDAO
{
    //the shared instance
    private static StaffMemberDAO instance;
    //the data list
    private ArrayList<StaffMember> staffList; 
    
    /**
     * Default private class constructor
     */
    private StaffMemberDAO()
    {
        //create the list
        staffList = new ArrayList<StaffMember>();
        //load dummy data
        StaffMember s1 = new StaffMember(0,"Staff1","Staff1","nick.staff1");
        StaffMember s2 = new StaffMember(0,"Staff2","Staff2","nick.staff2");
        StaffMember s3 = new StaffMember(0,"Staff3","Staff3","nick.staff3");
        staffList.add(s1);
        staffList.add(s2);
        staffList.add(s3);
    }
    
    /**
     * Creates and returns the shared instance
     */
    public static StaffMemberDAO getInstance()
    {
        if( instance == null)
            instance = new StaffMemberDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<StaffMember> getList()
    {
        return staffList;
    }
}

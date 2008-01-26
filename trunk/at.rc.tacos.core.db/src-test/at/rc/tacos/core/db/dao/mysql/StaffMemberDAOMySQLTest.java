package at.rc.tacos.core.db.dao.mysql;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;

/**
 * This is a test class to test the functionality of the staffMember Details 
 * @author Walter
 */
public class StaffMemberDAOMySQLTest
{
    //the dao class
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();
    
    @Test
    public void testgetStaffMemberByID()
    {
        StaffMember staff = new StaffMember();
        
        //request the listing
        staff = staffMemberDAO.getStaffMemberByID(50100002);
        
        Assert.assertNotNull(staff);
    }
    
    @Test
    public void testgetAllStaffMembers()
    {
        //create the array list
        List<StaffMember> stafflist = new ArrayList<StaffMember>();
        
        //request the listing
        stafflist = staffMemberDAO.getAllStaffMembers();
        
        //Assert.assertNotNull(stafflist);
        Assert.assertEquals(4, stafflist.size());
        
    }
}


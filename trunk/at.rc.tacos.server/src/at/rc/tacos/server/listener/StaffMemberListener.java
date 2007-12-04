package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon staff member changes
 * @author Michael
 */
public class StaffMemberListener extends ServerListenerAdapter
{
    private EmployeeDAO staffDao = DaoService.getInstance().getFactory().createStaffMemberDAO();
    
    /**
     * Add a staff member
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        StaffMember member = (StaffMember)addObject;
        int id = staffDao.addEmployee(member);
        member.setPersonId(id);
        return member;
    }

    /**
     * Listing of all members
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest()
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(staffDao.listEmployees());
        return list;
    }

    /**
     * Remove a staff member
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        StaffMember member = (StaffMember)removeObject;
        staffDao.deleteEmployee(member);
        return member;
    }

    /**
     * Updates the staff member
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        StaffMember member = (StaffMember)updateObject;
        staffDao.updateEmployee(member);
        return member;
    }
}

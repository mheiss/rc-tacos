package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.model.QueryFilter;
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
        int id;
        try
        {
            id = staffDao.addStaffMember(member,"");
            member.setPersonId(id);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return member;
    }

    /**
     * Listing of all members
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        //if there is no filter -> request all
        if(queryFilter == null || queryFilter.getFilterList().isEmpty())
        {
            try
            {
                list.addAll(staffDao.getAllStaffMembers());
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
        else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            try
            {
                list.add(staffDao.getStaffMemberByID(id));
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.getMessage();
            }
        }
        return list;
    }

    /**
     * Remove a staff member
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        StaffMember member = (StaffMember)removeObject;
        try
        {
            staffDao.deleteStaffMember(member);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return member;
    }

    /**
     * Updates the staff member
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        StaffMember member = (StaffMember)updateObject;
        try
        {
            staffDao.updateStaffMember(member);
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.getMessage();
        }
        return member;
    }
}

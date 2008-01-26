package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.QueryFilter;

/**
 * This class will be notified uppon staff member changes
 * @author Michael
 */
public class StaffMemberListener extends ServerListenerAdapter
{
    private StaffMemberDAO staffDao = DaoFactory.MYSQL.createStaffMemberDAO();

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
           list.addAll(staffDao.getAllStaffMembers());
        }
        else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            list.add(staffDao.getStaffMemberByID(id));
        }
        return list;
    }
}

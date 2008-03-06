package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.StaffMember;

/**
 * This class will be notified uppon staff member changes
 * @author Michael
 */
public class StaffMemberListener extends ServerListenerAdapter
{
    private StaffMemberDAO staffDao = DaoFactory.MYSQL.createStaffMemberDAO();
    
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException, SQLException 
	{
		StaffMember addMember = (StaffMember)addObject;
		if(!staffDao.addStaffMember(addMember))
			throw new DAOException("StaffMemberListener","Failed to add the staff member: "+addMember);
		return addMember;
	}

	/**
	 * Update of a staff member
	 */
	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
	{
		StaffMember updateMember = (StaffMember)updateObject;
		if(!staffDao.updateStaffMember(updateMember))
			throw new DAOException("StaffMemberListener","Failed to update the staff member: "+updateMember);
		return updateMember;
	}
    
    /**
     * Listing of all members
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        List<StaffMember> memberList;
        //if there is no filter -> request all
        if(queryFilter == null || queryFilter.getFilterList().isEmpty())
        {
        	memberList = staffDao.getAllStaffMembers();
        	if(memberList == null)
        		throw new DAOException("StaffMemberListener","Failed to list all staff members");
           list.addAll(memberList);
        }
        else if(queryFilter.containsFilterType(IFilterTypes.ID_FILTER))
        {
            //get the query filter
            final String filter = queryFilter.getFilterValue(IFilterTypes.ID_FILTER);
            int id = Integer.parseInt(filter);
            StaffMember member = staffDao.getStaffMemberByID(id);
            if(member == null)
            	throw new DAOException("StaffMemberListener","Failed to get the staff member by id:"+id);
            list.add(member);
        }
        return list;
    }
}
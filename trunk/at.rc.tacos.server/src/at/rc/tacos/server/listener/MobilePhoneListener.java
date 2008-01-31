package at.rc.tacos.server.listener;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;

/**
 * This class will be notified uppon mobile phone changes
 * @author Michael
 */
public class MobilePhoneListener extends ServerListenerAdapter
{
    private MobilePhoneDAO mobilePhoneDao = DaoFactory.MYSQL.createMobilePhoneDAO();
    
    /**
     * Add a mobile phone
     */    
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)addObject;
        int id = mobilePhoneDao.addMobilePhone(phone);
        if(id == -1)
        	throw new DAOException("MobilePhoneListener","Failed to add the mobile phone:"+phone);
        phone.setId(id);
        return addObject;
    }

    /**
     * Listing of all mobile phones
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)  throws DAOException
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        List<MobilePhoneDetail> phoneList = mobilePhoneDao.listMobilePhones();
        if(phoneList == null)
        	throw new DAOException("MobilePhoneListener","Failed to list the mobile phones");
        list.addAll(phoneList);
        return list;
    }

    /**
     * Remove a mobile phone
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)  throws DAOException
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)removeObject;
        if(!mobilePhoneDao.removeMobilePhone(phone.getId()))
        	throw new DAOException("MobilePhoneListener","Failed to remove the mobile phone:"+phone);
        return phone;
    }

    /**
     * Update a mobile phone
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)  throws DAOException
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)updateObject;
        if(!mobilePhoneDao.updateMobilePhone(phone))
        	throw new DAOException("MobilePhoneListener","Failed to update the mobile phone:"+phone);
        return phone;
    }
}

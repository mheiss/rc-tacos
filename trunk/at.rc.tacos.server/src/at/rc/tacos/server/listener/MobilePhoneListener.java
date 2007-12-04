package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon mobile phone changes
 * @author Michael
 */
public class MobilePhoneListener extends ServerListenerAdapter
{
    private MobilePhoneDAO mobilePhoneDao = DaoService.getInstance().getFactory().createMobilePhoneDAO();
    
    /**
     * Add a mobile phone
     */    
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)addObject;
        mobilePhoneDao.addMobilePhone(phone);
        return addObject;
    }

    /**
     * Listing of all mobile phones
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest()
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(mobilePhoneDao.listMobilePhones());
        return list;
    }

    /**
     * Remove a mobile phone
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)removeObject;
        mobilePhoneDao.removeMobilePhone(phone);
        return phone;
    }

    /**
     * Update a mobile phone
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        MobilePhoneDetail phone = (MobilePhoneDetail)updateObject;
        mobilePhoneDao.updateMobilePhone(phone);
        return phone;
    }
}

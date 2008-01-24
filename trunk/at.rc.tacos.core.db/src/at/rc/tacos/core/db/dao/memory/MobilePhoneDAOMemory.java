package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * Data source for mobile phones
 * @author Michael
 */
public class MobilePhoneDAOMemory implements MobilePhoneDAO
{
    //the shared instance
    private static MobilePhoneDAOMemory instance;
    
    //the data list
    private ArrayList<MobilePhoneDetail> phoneList; 
    
    /**
     * Default class constructor
     */
    private MobilePhoneDAOMemory()
    {
        phoneList = new ArrayList<MobilePhoneDetail>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static MobilePhoneDAOMemory getInstance()
    {
        if (instance == null)
            instance = new MobilePhoneDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        phoneList = new ArrayList<MobilePhoneDetail>();
    }

    @Override
    public int addMobilePhone(MobilePhoneDetail phone)
    {
        phoneList.add(phone);
        return phoneList.size();
    }
    
    @Override
    public void updateMobilePhone(MobilePhoneDetail phone)
    {
        int index = phoneList.indexOf(phone);
        phoneList.remove(index);
        phoneList.add(index, phone);
    }
    
    @Override
    public void removeMobilePhone(MobilePhoneDetail phone)
    {
        phoneList.remove(phone);
    }

    @Override
    public MobilePhoneDetail getMobilePhoneById(String mobilePhoneId)
    {
        for(MobilePhoneDetail detail:phoneList)
            if(detail.getMobilePhoneName().equalsIgnoreCase(mobilePhoneId))
                return detail;
        return null;
    }

    @Override
    public List<MobilePhoneDetail> listMobilePhones()
    {
        return phoneList;
    }
}

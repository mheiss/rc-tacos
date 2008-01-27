package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.TestDataSource;

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
		//init the test data
		for(MobilePhoneDetail detail:TestDataSource.getInstance().phoneList)
			phoneList.add(detail);
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
    public boolean updateMobilePhone(MobilePhoneDetail phone)
    {
        int index = phoneList.indexOf(phone);
        phoneList.remove(index);
        phoneList.add(index, phone);
        return true;
    }
    
    @Override
    public boolean removeMobilePhone(int id)
    {
    	if(phoneList.remove(id) != null)
    		return true;
    	//nothing remove
    	return false;
    }

    @Override
    public MobilePhoneDetail getMobilePhoneByName(String mobilePhoneName)
    {
        for(MobilePhoneDetail detail:phoneList)
            if(detail.getMobilePhoneName().equalsIgnoreCase(mobilePhoneName))
                return detail;
        //Nothing found
        return null;
    }

    @Override
    public List<MobilePhoneDetail> listMobilePhones()
    {
        return phoneList;
    }

    @Override
    public List<MobilePhoneDetail> listMobilePhonesOfStaffMember(int id)
    {
        // TODO Auto-generated method stub
        return null;
    }
}

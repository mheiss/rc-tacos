package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneManager  extends PropertyManager 
{
//  the item list
    private List<MobilePhoneDetail> objectList = new ArrayList<MobilePhoneDetail>();

    /**
     * Default class constructor
     */
    public MobilePhoneManager()
    {
        objectList = new ArrayList<MobilePhoneDetail>();
    }

    /**
     * Adds a new phone to the phone manager.
     * @param phone the phone to add
     */
    public void add(final MobilePhoneDetail phone) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(phone);
                //notify the view
                firePropertyChange("PHONE_ADD", null, phone);
            }
        }); 
    }    

    /**
     * Removes the vehicle from the list
     * @param vehicle the vehicle to remove
     */
    public void remove(final MobilePhoneDetail phone) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(phone);
                firePropertyChange("PHONE_REMOVE", phone, null); 
            }
        });
    }

    /**
     * Updates the phone in the list
     */
    public void update(final MobilePhoneDetail phone)
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            { 
                //get the position of the entry
                int id = objectList.indexOf(phone);
                objectList.set(id, phone);
                firePropertyChange("STAFF_UPDATE", null, phone); 
            }
        });
    }

    /**
     * Clears the list of vehicles
     */
    public void resetPhones()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                firePropertyChange("PHONE_CLEAR", null, null);
                objectList.clear();
            }
        });
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }

    /**
     * Returns the current list of phones 
     * @return the phone list
     */
    public List<MobilePhoneDetail> getMobilePhoneList()
    {
        return objectList;
    }
}
package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.StaffManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.StaffMember;

/**
 * This class will be notified uppon staff member changes
 * @author Michael
 */
public class StaffMemberListener extends ClientListenerAdapter
{
    @Override
    public void add(AbstractMessage addMessage)
    {
        StaffManager manager = ModelFactory.getInstance().getStaffManager();
        manager.add((StaffMember)addMessage);
    }
    
    @Override
    public void update(AbstractMessage updateMessage)
    {
        StaffManager manager = ModelFactory.getInstance().getStaffManager();
        manager.update((StaffMember)updateMessage);  
    }
    
    @Override
    public void remove(AbstractMessage removeMessage)
    {
        StaffManager manager = ModelFactory.getInstance().getStaffManager();
        manager.remove((StaffMember)removeMessage);  
    }
    
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        StaffManager manager = ModelFactory.getInstance().getStaffManager();
        manager.removeAllElements();
        
        for(AbstractMessage msg:listMessage)
        {
            StaffMember member = (StaffMember)msg;
            manager.add(member);
        }
    }  
}

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
    StaffManager manager = ModelFactory.getInstance().getStaffList();
    
    @Override
    public void add(AbstractMessage addMessage)
    {
        manager.add((StaffMember)addMessage);
    }
    
    @Override
    public void update(AbstractMessage updateMessage)
    {
        manager.update((StaffMember)updateMessage);  
    }
    
    @Override
    public void remove(AbstractMessage removeMessage)
    {
        manager.remove((StaffMember)removeMessage);  
    }
    
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        manager.removeAllElements();
        for(AbstractMessage msg:listMessage)
        {
            StaffMember member = (StaffMember)msg;
            manager.add(member);
        }
    }  
}

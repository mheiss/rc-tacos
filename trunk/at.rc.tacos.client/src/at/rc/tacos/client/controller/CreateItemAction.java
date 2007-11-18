package at.rc.tacos.client.controller;

//rcp
import java.util.Date;
import org.eclipse.jface.action.Action;

//client
import at.rc.tacos.model.*;
import at.rc.tacos.core.service.ServiceWrapper;

/**
 * The action that is triggered by creating a new item
 * @author Michael
 */
public class CreateItemAction extends Action 
{
    private String id;

    public CreateItemAction(String id) 
    {
        this.id = id;
    }
    
    public void run() 
    {
        //Roster entry
    	RosterEntry entry = new RosterEntry();
    	StaffMember member = new StaffMember();
    	
    	member.setFirstName("test");
    	member.setLastName("last test");
    	member.setPersonId(0);
    	member.setUserName(id);
    	
    	entry.setCompetence("Fahrer");
    	entry.setDateOfRosterEntry(new Date().getTime());
    	entry.setPlannedEndOfWork(new Date().getTime());
    	entry.setPlannedStartofWork(new Date().getTime());
    	entry.setRealEndOfWork(new Date().getTime());
    	entry.setRealStartOfWork(new Date().getTime());
    	entry.setRosterId(0);
    	entry.setRosterNotes("mix");
    	entry.setServicetype("Zivi");
    	entry.setStaffMember(member);
    	entry.setStandby(false);
    	entry.setStation("Bruck");
    	//send
    	System.out.println("RosterEntry added: "+new Date().getTime());
    	ServiceWrapper.getDefault().getServiceLayer().addRosterEntry(entry);

    	//Item
        Item newItem = new Item(id);
        //send
        ServiceWrapper.getDefault().getServiceLayer().addItem(newItem);
        System.out.println("Item request send: "+new Date().getTime());
    }
}

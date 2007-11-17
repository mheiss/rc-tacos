package at.rc.tacos.client.controller;

//rcp
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;
//client
import at.rc.tacos.model.*;
import at.rc.tacos.client.Activator;
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
    	RosterEntry entry = new RosterEntry();
    	StaffMember member = new StaffMember();
    	
    	member.setFirstName("test");
    	member.setLastName("last test");
    	member.setPersonId(0);
    	member.setUserName(id);
    	
    	entry.setCompetence("Fahrer");
    	entry.setDateOfRosterEntry(new Date());
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
    	
    	Activator.getDefault().getRosterEntryList().add(entry);
        Item newItem = new Item(id);
        System.out.println("Created a new item: "+newItem);
        //Send a request to the server layer to add the item
        ServiceWrapper.getDefault().getServiceLayer().addItem(newItem);
    }
}

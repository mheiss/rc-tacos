package at.rc.tacos.client.controller;

import java.util.Date;

import org.eclipse.jface.action.Action;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.*;

/**
 * The action that is triggered by creating a new roster entry
 * @author b.thek
 */
public class CreateRosterEntryAction extends Action 
{
    private RosterEntry entry;
    
    /**
     * Creates a new RosterEntryAction.
     * @param the new roster entry
     */
    public CreateRosterEntryAction() 
    {
        //Roster entry
        RosterEntry entry = new RosterEntry();
        StaffMember member = new StaffMember();
        
        member.setFirstName("test");
        member.setLastName("last test");
        member.setPersonId(0);
        member.setUserName("user");
        
        entry.setCompetence("Fahrer");
        entry.setPlannedStartOfWork(new Date().getTime());
        entry.setPlannedEndOfWork(new Date().getTime());
        entry.setRealEndOfWork(new Date().getTime());
        entry.setRealStartOfWork(new Date().getTime());
        entry.setRosterId(0);
        entry.setRosterNotes("mix");
        entry.setServicetype("Zivi");
        entry.setStaffMember(member);
        entry.setStandby(false);
        entry.setStation("Bruck");
    }

    /**
     * Creates a new RosterEntryAction.
     * @param entry the new roster entry
     */
    public CreateRosterEntryAction(RosterEntry entry) 
    {
        this.entry = entry;
    }

    public void run() 
    {
        //send the entry
        NetWrapper.getDefault().sendAddMessage(RosterEntry.ID,entry);
    }
}
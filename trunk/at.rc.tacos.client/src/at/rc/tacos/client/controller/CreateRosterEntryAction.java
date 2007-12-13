package at.rc.tacos.client.controller;

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
    public CreateRosterEntryAction()  { }

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
package at.rc.tacos.client.controller;

//rcp
import org.eclipse.jface.action.Action;
//client
import at.rc.tacos.core.service.ServiceWrapper;
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
    public CreateRosterEntryAction(RosterEntry entry) 
    {
        this.entry = entry;
    }

    public void run() 
    {
        ServiceWrapper.getDefault().getServiceLayer().addRosterEntry(entry);
    }
}
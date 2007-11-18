package at.rc.tacos.client.controller;

import java.util.ArrayList;
import java.util.Date;

import at.rc.tacos.client.Activator;
import at.rc.tacos.model.*;
import at.rc.tacos.core.service.IServiceListener;

/**
 * Manages the update of the model.
 * @author Michael
 */
public class NetController implements IServiceListener
{
    /**
     * Default class constructor
     */
    public NetController() {}

    // SERVICE LAYER METHODS
    @Override
    public void itemAdded(Item newItem)
    {
        //add the item
        Activator.getDefault().getItemList().add(newItem);
        System.out.println("Item added: "+new Date().getTime());
    }

    @Override
    public void itemListing(ArrayList<Item> list)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void itemRemoved(Item item)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void itemUpdated(Item newItem)
    {
        // TODO Auto-generated method stub

    }

	@Override
	public void rosterEntryAdded(RosterEntry entry) 
	{
		Activator.getDefault().getRosterEntryList().add(entry);
	}

	@Override
	public void rosterEntryListing(ArrayList<RosterEntry> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rosterEntryRemoved(RosterEntry rosterEntry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rosterEntryUpdated(RosterEntry newRosterEntry) {
		// TODO Auto-generated method stub
		
	}
}

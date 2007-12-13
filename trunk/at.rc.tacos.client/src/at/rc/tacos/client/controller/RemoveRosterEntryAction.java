package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.RosterEntry;

public class RemoveRosterEntryAction extends Action
{
	private RosterEntry entry;

	public RemoveRosterEntryAction(RosterEntry entry)
	{
		this.entry = entry;
	}

	@Override
	public void run()
	{
		NetWrapper.getDefault().sendRemoveMessage(RosterEntry.ID, entry);
	}
}


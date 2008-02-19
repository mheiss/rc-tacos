package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;

public class UpdateTransportAction extends Action
{
	private Transport transport;

	public UpdateTransportAction(Transport transport)
	{
		this.transport = transport;
	}

	@Override
	public void run()
	{
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

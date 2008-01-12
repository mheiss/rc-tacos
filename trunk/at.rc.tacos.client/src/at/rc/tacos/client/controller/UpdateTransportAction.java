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
//		ModelFactory.getInstance().getTransportManager().update(transport);//TODO -
	}

	@Override
	public void run()
	{
//		ModelFactory.getInstance().getTransportManager().update(transport);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}

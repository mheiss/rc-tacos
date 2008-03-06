package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DialysisPatient;

public class UpdateDialysisTransportAction extends Action
{
	private DialysisPatient dia;

	public UpdateDialysisTransportAction(DialysisPatient dia)
	{
		this.dia = dia;
	}

	@Override
	public void run()
	{
		NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, dia);
	}
}

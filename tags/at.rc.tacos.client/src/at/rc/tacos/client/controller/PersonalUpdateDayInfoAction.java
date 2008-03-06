package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DayInfoMessage;

public class PersonalUpdateDayInfoAction extends Action
{
	private DayInfoMessage dayInfo;
	
	/**
	 * Default class constructor
	 */
	public PersonalUpdateDayInfoAction(DayInfoMessage dayInfo)
	{
		this.dayInfo = dayInfo;
	}
	
	/**
	 * Execute
	 */
	public void run()
	{
		NetWrapper.getDefault().sendUpdateMessage(DayInfoMessage.ID, dayInfo);
	}
}

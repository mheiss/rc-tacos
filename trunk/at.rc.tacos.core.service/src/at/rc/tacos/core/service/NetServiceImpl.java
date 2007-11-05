package at.rc.tacos.core.service;

import at.rc.tacos.core.net.NetWrapper;

public class NetServiceImpl implements INetLayer
{
	@Override
	public void login(String username, String password) 
	{
		NetWrapper.getDefault().login(username, password);
	}
}

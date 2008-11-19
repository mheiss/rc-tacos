package at.rc.tacos.client.listeners;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.client.modelManager.AddressManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Address;

public class AddressListener extends ClientListenerAdapter
{
	//the disease manager
	private AddressManager manager = ModelFactory.getInstance().getAddressManager();

	@Override
	public void add(AbstractMessage addMessage) 
	{
		Address address = (Address)addMessage;
		manager.add(address);
	}
	
	@Override 
	public void addAll(List<AbstractMessage> addList)
	{
		//create a new list and add all addresses
		List<Address> addressList = new ArrayList<Address>();
		for(AbstractMessage addObject:addList)
		{
			Address address = (Address)addObject;
			addressList.add(address);
		}
		//now add them
		manager.addAll(addressList);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) 
	{
		//remove all elements
		manager.removeAllElements();
		//create a new list and add all addresses
		List<Address> addressList = new ArrayList<Address>();
		for(AbstractMessage addObject:listMessage)
		{
			Address address = (Address)addObject;
			addressList.add(address);
		}
		manager.addAll(addressList);
	}

	@Override
	public void remove(AbstractMessage removeMessage) 
	{
		//cast and remove
		Address address = (Address)removeMessage;
		manager.remove(address);
	}

	@Override
	public void update(AbstractMessage updateMessage) 
	{
		//cast and update
		Address address = (Address)updateMessage;
		manager.update(address);
	}
}

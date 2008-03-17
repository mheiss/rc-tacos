package at.rc.tacos.client.modelManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import at.rc.tacos.client.Activator;
import at.rc.tacos.factory.CSVParser;
import at.rc.tacos.model.Address;

/**
 * Manages the addresses of the client programm and stores them in a files.
 * Updates are synchronized between the clients
 * @author Michael
 */
public class AddressManager extends PropertyManager 
{
	//the list
	private List<Address> objectList = new ArrayList<Address>();

	/**
	 * Default class constructor
	 */
	public AddressManager()
	{
		doLoad();
	}

	/**
	 * Returns the list the managed addresses.
	 * @return the managed addresses
	 */
	public List<Address> getAddressList()
	{
		return objectList;
	}

	/**
	 * Saves the current list of addresses in the file
	 */
	public void doSave()
	{
		//TODO: save the list
	}

	/**
	 * Loads the current list of all addresses from the file
	 */
	private void doLoad()
	{	
		//cleare the list
		removeAllElements();
		//load the file
		URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path("doc/addressData.csv"), null);
		if(url == null)
		{
			Activator.getDefault().log("Failed to locate the addressData.csv file", IStatus.ERROR);
			return;
		}
		
		//parse the file
		CSVParser parser = CSVParser.getInstance();
		try
		{
			String path = FileLocator.toFileURL(url).getPath();
			//parse the given file
			final List<Map<String, Object>> elementList = parser.parseCSV(new File(path));
			//loop an import
			for(int i = 0; i<elementList.size(); i++)
			{
				Map<String, Object> line = elementList.get(i);			
				//access every element of the line			
				int gkz = Integer.parseInt((String)line.get("GKZ"));
				String city = (String)line.get("Gemeindename");
				String street = (String)line.get("BEZEICHNUNG");
				//create the address and add to the list
				objectList.add(new Address(gkz,city,street));
			}
		}
		catch(IOException ioe)
		{
			Activator.getDefault().log("Failed to locate the addressData.csv file", IStatus.ERROR);
			return;
		}
		catch(Exception ex)
		{
			Activator.getDefault().log("Failed to parse the addressData.csv file", IStatus.ERROR);
		}
	}

	/**
	 * Adds a new address to the list
	 * @param address the address to add
	 */
	public void add(final Address address) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				objectList.add(address);
				firePropertyChange("ADDRESS_ADD", null, address);
			}
		}); 
	}   

	/**
	 * Adds all the messages to the list
	 * @param addressList the addresses to add
	 */
	public void addAll(final List<Address> addressList)
	{
		Display.getDefault().asyncExec(new Runnable()
		{
			public void run ()       
			{
				objectList.addAll(addressList);
				firePropertyChange("ADDRESS_ADD_ALL", null, addressList);
			}
		}); 
	}

	/**
	 * Removes the address from the list
	 * @param address the address to remove
	 */
	public void remove(final Address address) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				objectList.remove(address);
				firePropertyChange("ADDRESS_REMOVE", address, null); 
			}
		}); 
	}

	/**
	 * Updates the address in the list
	 * @param address the address to update
	 */
	public void update(final Address address) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{   
				//assert we have this address
				if(!objectList.contains(address))
					return;
				//get the position of the entry and update it
				int id = objectList.indexOf(address);
				objectList.set(id, address);
				firePropertyChange("ADDRESS_UPDATE", null, address); 
			}
		}); 
	}

	/**
	 * Removes all elements and clears the list
	 */
	public void removeAllElements()
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{   
				objectList.clear();
				firePropertyChange("ADDRESS_CLEARED",null,null);
			}
		}); 
	}

	/**
	 * Converts the list to an array
	 * @return the list as a array
	 */
	public Address[] toArray()
	{
		Address[] list = new Address[objectList.size()];
		for(int i = 0;i < objectList.size();i++)
			list[i] = objectList.get(i);
		return list;
	}

	/**
	 * Converts all the streets in the manager to an array and returns it
	 * @return the list of streets as array
	 */
	public Object[] toStreetArray()
	{
		List<String> streets = new ArrayList<String>();
		//loop and add the streets
		for(Address add:objectList)
			streets.add(add.getStreet());
		System.out.println("zahl:"+streets.size());
		return streets.toArray();
	}

	/**
	 * Converts all the cities in the manager to an array and returns it
	 * @return the list of cities as array
	 */
	public Object[] toCityArray()
	{
		List<String> cities = new ArrayList<String>();
		//loop and add the streets
		for(Address add:objectList)
		{
			if(cities.contains(add.getCity()))
				continue;
			cities.add(add.getCity());
		}
		System.out.println("cities:"+cities.size());
		return cities.toArray();
	}
}

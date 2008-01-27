package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * Data source for transports
 * @author Michael
 */
public class TransportDAOMemory implements TransportDAO
{
    //the shared instance
    private static TransportDAOMemory instance;
    
    //the data list
    private ArrayList<Transport> transportList; 
    private ArrayList<Transport> archivedList;
    
    /**
     * Default class constructor
     */
    private TransportDAOMemory()
    {
        transportList = new ArrayList<Transport>();
        archivedList = new ArrayList<Transport>();
        //add test data
        for(Transport transport:TestDataSource.getInstance().transportList)
        	transportList.add(transport);
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static TransportDAOMemory getInstance()
    {
        if (instance == null)
            instance = new TransportDAOMemory();
        return instance;
    }

	@Override
	public int addTransport(Transport transport) 
	{
		transportList.add(transport);
		return transportList.size();
	}

	@Override
	public boolean archiveTransport(Transport transport) 
	{
		transportList.remove(transport);
		archivedList.add(transport);
		return true;
	}

	@Override
	public int assignVehicleToTransport(Transport transport) 
	{
		Random r = new Random();
		int randomTransportNumber = r.nextInt(9999);
		transport.setTransportNumber(randomTransportNumber);
		return randomTransportNumber;
	}

	@Override
	public boolean cancelTransport(int transportId) 
	{
		Transport transport = transportList.get(transportId);
		if(transport != null)
		{
			transport.setTransportNumber(Transport.TRANSPORT_CANCLED);
			return true;
		}
		//failed to update
		return false;
	}

	@Override
	public List<Transport> getTransportsFromVehicle(String vehicleName) 
	{
		List<Transport> filteredList = new ArrayList<Transport>();
		//loop 
		for(Transport transport:transportList)
		{
			if(transport.getVehicleDetail().getVehicleName().equalsIgnoreCase(vehicleName))
				filteredList.add(transport);
		}
		//return the filtered list
		return filteredList;
	}

	@Override
	public List<Transport> listTransports(long startdate, long enddate) 
	{
		List<Transport> filteredList = new ArrayList<Transport>();
		//loop 
		for(Transport transport:transportList)
		{
			long transportTime = transport.getDateOfTransport();
			if(MyUtils.isEqualDate(startdate, transportTime))
				filteredList.add(transport);
		}
		//return the filtered list
		return filteredList;
	}

	@Override
	public boolean updateTransport(Transport transport) 
	{
		int index = transportList.indexOf(transport);
		transportList.set(index, transport);
		return true;
	}

	@Override
	public List<Transport> listArchivedTransports(long startdate, long enddate) 
	{
		List<Transport> filteredList = new ArrayList<Transport>();
		//loop 
		for(Transport transport:archivedList)
		{
			long transportTime = transport.getDateOfTransport();
			if(MyUtils.isEqualDate(startdate, transportTime))
				filteredList.add(transport);
		}
		//return the filtered list
		return filteredList;
	}

    @Override
    public boolean assignTransportstate(Transport transport)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeTransportstate(Transport transport)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeVehicleFromTransport(Transport transport)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateTransportstate(Transport transport)
    {
        // TODO Auto-generated method stub
        return false;
    }
}

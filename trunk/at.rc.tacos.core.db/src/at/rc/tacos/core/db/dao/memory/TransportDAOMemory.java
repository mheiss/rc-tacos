package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.model.Transport;;

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
    
    /**
     * Default class constructor
     */
    private TransportDAOMemory()
    {
        transportList = new ArrayList<Transport>();
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
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        transportList = new ArrayList<Transport>();
    }

    @Override
    public String addTransport(Transport transport,int test)
    {
        transportList.add(transport);
        return String.valueOf(transportList.size());
    }
    
    @Override
    public boolean updateTransport(Transport transport)
    {
        int index = transportList.indexOf(transport);
        transportList.remove(index);
        transportList.add(index,transport);
        return true;
    }

    @Override
    public boolean removeTransportByNr(long id)
    {
        Transport transportToRemove = null;
        //loop and check
        for(Transport transport:transportList)
        {
            if(transport.getTransportId() == id)
                transportToRemove = transport;
        }
        //do we have something to remove?
        if(transportToRemove != null)
        {
            transportList.remove(transportToRemove);
            return true;
        }
        return false;
    }

    @Override
    public Transport getTransportByNr(int transportId, int locationId)
    {
        for(Transport transport:transportList)
        {
            if(transport.getTransportId() == transportId)
                return transport;
        }
        return null;
    }

    @Override
    public List<Transport> listTransports()
    {
        return transportList;
    }

    @Override
    public List<Transport> listTransports(long startdate, long enddate)
    {
        return transportList;
    }
}

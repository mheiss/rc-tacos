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
    public int addTransport(Transport transport)
    {
        transportList.add(transport);
        return transportList.size();
    }
    
    @Override
    public void updateTransport(Transport transport)
    {
        int index = transportList.indexOf(transport);
        transportList.remove(index);
        transportList.add(index,transport);
    }

    @Override
    public void removeTransport(Transport transport)
    {
        transportList.remove(transport);
    }

    @Override
    public Transport getTransportById(int transportId)
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
}

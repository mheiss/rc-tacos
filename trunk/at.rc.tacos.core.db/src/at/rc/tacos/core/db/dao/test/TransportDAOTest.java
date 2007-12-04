package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.model.Transport;;

/**
 * Data source for transports
 * @author Michael
 */
public class TransportDAOTest implements TransportDAO
{
    //the shared instance
    private static TransportDAOTest instance;
    
    //the data list
    private ArrayList<Transport> transportList; 
    
    /**
     * Default class constructor
     */
    private TransportDAOTest()
    {
        transportList = new TestDataSource().transportList;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static TransportDAOTest getInstance()
    {
        if (instance == null)
            instance = new TransportDAOTest();
        return instance;
    }

    @Override
    public int addTransport(Transport transport)
    {
        transportList.add(transport);
        return transportList.indexOf(transport);
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
        return null;
    }

    @Override
    public List<Transport> listTransports()
    {
        return transportList;
    }
}

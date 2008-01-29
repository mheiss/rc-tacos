package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Transport;

public interface TransportDAO 
{
    public int addTransport(Transport transport);
    public void updateTransport(Transport transport);
    public void removeTransport(Transport transport);
    
    public Transport getTransportById(int transportId);
    public List<Transport> listTransports();
}

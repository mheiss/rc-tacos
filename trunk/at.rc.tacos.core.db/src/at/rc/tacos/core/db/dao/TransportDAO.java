package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.Transport;

public interface TransportDAO 
{
    public String addTransport(Transport transport, int staffmemberVehicleId);
    public boolean updateTransport(Transport transport);
    public boolean removeTransportByNr(long transportNr);
    
    public Transport getTransportByNr(int transportId, int locationId);
    public List<Transport> listTransports();
    public List<Transport> listTransports(long startdate, long enddate);
}

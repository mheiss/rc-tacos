package at.rc.tacos.server.listener;

import java.util.ArrayList;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon transport changes
 * @author Michael
 */
public class TransportListener extends ServerListenerAdapter
{
    private TransportDAO transportDao = DaoService.getInstance().getFactory().createTransportDAO();
    
    /**
     * Add a transport
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Transport transport = (Transport)addObject;
        int id = transportDao.addTransport(transport);
        transport.setTransportId(id);
        return transport;
    }

    /**
     * Transport listing
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(transportDao.listTransports());
        return list;
    }

    /**
     * Remove a transport
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        Transport transport = (Transport)removeObject;
        transportDao.removeTransport(transport);
        return transport;
    }

    /**
     *  Update a transport
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        Transport transport = (Transport)updateObject;
        transportDao.updateTransport(transport);
        return transport;
    }
}

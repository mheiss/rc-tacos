package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LinkService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LinkHandler implements Handler<Link> {

    @Service(clazz = LinkService.class)
    private LinkService linkService;

    @Override
    public void add(ServerIoSession session, Message<Link> message) throws ServiceException,
            SQLException {
        List<Link> linkList = message.getObjects();
        // loop and try to add each object
        for (Link link : linkList) {
            int id = linkService.addLink(link);
            if (id == -1)
                throw new ServiceException("Failed to add the link " + link);
            link.setId(id);
        }
        session.writeBrodcast(message, linkList);
    }

    @Override
    public void get(ServerIoSession session, Message<Link> message) throws ServiceException,
            SQLException {
        // get the params from the message
        Map<String, String> params = message.getParams();

        // request a single link by the id
        if (params.containsKey(IFilterTypes.ID_FILTER)) {
            int linkId = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
            final Link link = linkService.getLinkById(linkId);
            if (link == null) {
                throw new ServiceException("No link found with the id " + linkId);
            }
            session.write(message, link);
            return;
        }

        // if there is no request parameter then request all links
        List<Link> linkList = linkService.listLinks();
        if (linkList == null)
            throw new ServiceException("Failed to list the links");
        // write the result back
        session.write(message, linkList);
    }

    @Override
    public void remove(ServerIoSession session, Message<Link> message) throws ServiceException,
            SQLException {
        List<Link> linkList = message.getObjects();
        // loop and try to remove each object
        for (Link link : linkList) {
            if (!linkService.removeLink(link.getId()))
                throw new ServiceException("Failed to remove the link:" + link);
        }
        // brodcast the removed links
        session.writeBrodcast(message, linkList);
    }

    @Override
    public void update(ServerIoSession session, Message<Link> message) throws ServiceException,
            SQLException {
        List<Link> linkList = message.getObjects();
        // loop and try to update each object
        for (Link link : linkList) {
            if (!linkService.updateLink(link))
                throw new ServiceException("Failed to update the link:" + link);
        }
        // brodcast the updated links
        session.writeBrodcast(message, linkList);
    }

    @Override
    public void execute(ServerIoSession session, Message<Link> message) throws ServiceException,
            SQLException {
        // throw an execption because the 'exec' command is not implemented
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        throw new NoSuchCommandException(handler, command);
    }
}

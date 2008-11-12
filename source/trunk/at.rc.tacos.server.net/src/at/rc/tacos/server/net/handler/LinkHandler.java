package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public void add(ServerIoSession session, Message<Link> message) throws ServiceException, SQLException {
		int id = linkService.addLink(model);
		if (id == -1)
			throw new ServiceException("Failed to add the link " + model);
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<Link> message) throws ServiceException, SQLException {
		List<Link> linkList = new ArrayList<Link>();
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			int linkId = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
			final Link link = linkService.getLinkById(linkId);
			if (link == null) {
				throw new ServiceException("No link found with the id " + linkId);
			}
			linkList.add(link);
		}
		else {
			linkList = linkService.listLinks();
			if (linkList == null)
				throw new ServiceException("Failed to list the links");
		}
		return linkList;
	}

	@Override
	public void remove(ServerIoSession session, Message<Link> message) throws ServiceException, SQLException {
		if (!linkService.removeLink(model.getId()))
			throw new ServiceException("Failed to remove the link:" + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<Link> message) throws ServiceException, SQLException {
		if (!linkService.updateLink(model))
			throw new ServiceException("Failed to update the link:" + model);
		return model;
	}
	
	@Override
	public void execute(ServerIoSession session, Message<Link> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}

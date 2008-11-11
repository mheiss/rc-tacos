package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LinkService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LinkHandler implements INetHandler<Link> {

	@Service(clazz = LinkService.class)
	private LinkService linkService;

	@Override
	public Link add(Link model) throws ServiceException, SQLException {
		int id = linkService.addLink(model);
		if (id == -1)
			throw new ServiceException("Failed to add the link " + model);
		model.setId(id);
		return model;
	}

	@Override
	public List<Link> execute(String command, List<Link> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Link> get(Map<String, String> params) throws ServiceException, SQLException {
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
	public Link remove(Link model) throws ServiceException, SQLException {
		if (!linkService.removeLink(model.getId()))
			throw new ServiceException("Failed to remove the link:" + model);
		return model;
	}

	@Override
	public Link update(Link model) throws ServiceException, SQLException {
		if (!linkService.updateLink(model))
			throw new ServiceException("Failed to update the link:" + model);
		return model;
	}

}

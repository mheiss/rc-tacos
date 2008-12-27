package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Link;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.LinkService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class LinkHandler implements Handler<Link> {

	@Service(clazz = LinkService.class)
	private LinkService linkService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
		List<Link> linkList = message.getObjects();
		// loop and try to add each object
		for (Link link : linkList) {
			int id = linkService.addLink(link);
			if (id == -1)
				throw new ServiceException("Failed to add the link " + link);
			link.setId(id);
		}
		session.writeResponseBrodcast(message, linkList);
	}

	@Override
	public void get(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
		// get the params from the message
		Map<String, String> params = message.getParams();

		// request a single link by the id
		if (params.containsKey(IFilterTypes.ID_FILTER)) {
			int linkId = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
			final Link link = linkService.getLinkById(linkId);
			if (link == null) {
				throw new ServiceException("No link found with the id " + linkId);
			}
			// check for locks
			if (lockableService.containsLock(link)) {
				Lockable lockable = lockableService.getLock(link);
				link.setLocked(lockable.isLocked());
				link.setLockedBy(lockable.getLockedBy());
			}
			session.writeResponse(message, link);
			return;
		}

		// if there is no request parameter then request all links
		List<Link> linkList = linkService.listLinks();
		if (linkList == null)
			throw new ServiceException("Failed to list the links");

		// check for locks
		for (Link link : linkList) {
			if (!lockableService.containsLock(link)) {
				continue;
			}
			Lockable lockable = lockableService.getLock(link);
			link.setLocked(lockable.isLocked());
			link.setLockedBy(lockable.getLockedBy());
		}

		// write the result back
		session.writeResponse(message, linkList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
		List<Link> linkList = message.getObjects();
		// loop and try to remove each object
		for (Link link : linkList) {
			if (!linkService.removeLink(link.getId()))
				throw new ServiceException("Failed to remove the link:" + link);
			// remove the lock
			lockableService.removeLock(link);
		}
		// brodcast the removed links
		session.writeResponseBrodcast(message, linkList);
	}

	@Override
	public void update(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
		List<Link> linkList = message.getObjects();
		// loop and try to update each object
		for (Link link : linkList) {
			if (!linkService.updateLink(link))
				throw new ServiceException("Failed to update the link:" + link);
			// update the lock
			lockableService.updateLock(link);
		}
		// brodcast the updated links
		session.writeResponseBrodcast(message, linkList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Link> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}
		throw new NoSuchCommandException(handler, command);
	}

	@Override
	public Link[] toArray() {
		return null;
	}
}

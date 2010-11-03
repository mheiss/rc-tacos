package at.redcross.tacos.web.beans;

import java.util.Iterator;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.redcross.tacos.dbal.entity.Link;
import at.redcross.tacos.dbal.helper.LinkHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "linkMaintenanceBean")
public class LinkMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(LinkMaintenanceBean.class);

	/** the available links */
	private List<GenericDto<Link>> links;

	/** the id of the selected link */
	private long linkId;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			links = DtoHelper.fromList(Link.class, LinkHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------

	public void removeLink(ActionEvent event) {
		Iterator<GenericDto<Link>> iter = links.iterator();
		while (iter.hasNext()) {
			GenericDto<Link> dto = iter.next();
			Link link = dto.getEntity();
			if (link.getId() != linkId) {
				continue;
			}
			if (dto.getState() == EntryState.NEW) {
				iter.remove();
			}

			dto.setState(EntryState.DELETE);
		}
	}

	public void unremoveLink(ActionEvent event) {
		for (GenericDto<Link> dto : links) {
			Link link = dto.getEntity();
			if (link.getId() != linkId) {
				continue;
			}
			dto.setState(EntryState.SYNC);
		}
	}

	public void addLink(ActionEvent event) {
		GenericDto<Link> dto = new GenericDto<Link>(new Link());
		dto.setState(EntryState.NEW);
		links.add(dto);
	}

	public void saveLinks() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, links);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(links);
		} catch (Exception ex) {
			logger.error("Failed to remove link '" + linkId + "'", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getLinkId() {
		return linkId;
	}

	public List<GenericDto<Link>> getLinks() {
		return links;
	}
}

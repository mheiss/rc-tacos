package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Link;
import at.redcross.tacos.dbal.helper.LinkHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "linkOverviewBean")
public class LinkOverviewBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	/** queried results for visualization / reporting */
	private List<Link> links;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			loadFromDatabase(manager);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Private API
	// ---------------------------------
	private void loadFromDatabase(EntityManager manager) {
		links = LinkHelper.list(manager);
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public List<Link> getLinks() {
		return links;
	}

}

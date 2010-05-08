package at.redcross.tacos.web.beans;

import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.SoftwareVersion;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;

@KeepAlive
@ManagedBean(name = "softwareVersionBean")
public class SoftwareVersionBean extends at.redcross.tacos.web.beans.BaseBean {

	// the available versions
	private Collection<SoftwareVersion> versions;

	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			manager = EntityManagerHelper.createEntityManager();
			String query = "from SoftwareVersion";
			TypedQuery<SoftwareVersion> versionQuery = manager.createQuery(query,
					SoftwareVersion.class);
			versions = versionQuery.getResultList();
		}
		finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	public Collection<SoftwareVersion> getVersions() {
		return versions;
	}
}

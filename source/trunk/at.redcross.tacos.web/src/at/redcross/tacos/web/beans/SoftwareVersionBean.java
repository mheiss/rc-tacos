package at.redcross.tacos.web.beans;

import java.util.Calendar;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.SoftwareVersion;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "softwareVersionBean")
public class SoftwareVersionBean extends BaseBean {

    private static final long serialVersionUID = -3539845345240469593L;

    // the available versions
    private Collection<SoftwareVersion> versions;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();

            SoftwareVersion version = new SoftwareVersion();
            version.setDate(Calendar.getInstance().getTime());
            version.setVersion("myVersion");
            manager.persist(version);
            EntityManagerHelper.commit(manager);

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

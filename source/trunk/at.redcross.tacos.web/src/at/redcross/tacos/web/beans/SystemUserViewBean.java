package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.entity.LocationSystemUserEntry;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "systemUserViewBean")
public class SystemUserViewBean extends BaseBean {

	private static final long serialVersionUID = -5114023802685654841L;
	private List<LocationSystemUserEntry> locationEntry;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            List<Location> locations = manager.createQuery("from Location", Location.class)
                    .getResultList();
            locationEntry = new ArrayList<LocationSystemUserEntry>();
            for (Location location : locations) {
            	StringBuilder builder = new StringBuilder();
                builder.append(" select user from SystemUser user ");
                builder.append(" where user.location.id=:locationId ");
                builder.append(" order by user.lastName");

                TypedQuery<SystemUser> query = manager.createQuery(builder.toString(),
                        SystemUser.class);
                query.setParameter("locationId", location.getId());
                locationEntry.add(new LocationSystemUserEntry(location, query.getResultList()));
            }
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<LocationSystemUserEntry> getLocationEntry() {
        return locationEntry;
    }
}

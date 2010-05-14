package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.entity.LocationRosterEntry;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "rosterDayViewBean")
public class RosterDayViewBean extends BaseBean {

    private static final long serialVersionUID = 8817078489086816724L;

    private List<LocationRosterEntry> locationEntry;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            List<Location> locations = manager.createQuery("from Location", Location.class)
                    .getResultList();
            locationEntry = new ArrayList<LocationRosterEntry>();
            for (Location location : locations) {
                StringBuilder builder = new StringBuilder();
                builder.append(" select entry from RosterEntry entry ");
                builder.append(" where entry.location.id=:locationId ");

                TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(),
                        RosterEntry.class);
                query.setParameter("locationId", location.getId());

                locationEntry.add(new LocationRosterEntry(location, query.getResultList()));
            }
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<LocationRosterEntry> getLocationEntry() {
        return locationEntry;
    }
}

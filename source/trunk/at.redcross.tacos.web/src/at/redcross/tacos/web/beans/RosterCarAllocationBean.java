package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "rosterCarAllocationBean")
public class RosterCarAllocationBean extends BaseBean {

	private static final long serialVersionUID = -3165695266924986400L;

	private final static Log logger = LogFactory.getLog(RosterCarAllocationBean.class);

    /** the available roster entries */
    private List<GenericDto<RosterEntry>> rosterEntries;

    /** the id of the selected roster entry */
    private long rosterId;
    
    

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rosterEntries = DtoHelper.fromList(RosterEntry.class, RosterEntryHelper.list(manager));
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    
    public void saveRosterEntries() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            DtoHelper.syncronize(manager, rosterEntries);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(rosterEntries);
        }
        catch (Exception ex) {
            logger.error("Failed to save Roster Entries '" + rosterId + "'", ex);
            FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
        }
        finally {
            EntityManagerHelper.close(manager);
        }
    }
	

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setRosterEntries(List<GenericDto<RosterEntry>> rosterEntries) {
		this.rosterEntries = rosterEntries;
	}

	public void setRosterId(long rosterId) {
		this.rosterId = rosterId;
	}

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
	public List<GenericDto<RosterEntry>> getRosterEntries() {
		return rosterEntries;
	}
	
	public long getRosterId() {
		return rosterId;
	}
}

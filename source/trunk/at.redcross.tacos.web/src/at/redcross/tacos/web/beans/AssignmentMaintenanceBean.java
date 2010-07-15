package at.redcross.tacos.web.beans;

import java.util.Iterator;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import at.redcross.tacos.dbal.entity.Assignment;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "assignmentMaintenanceBean")
public class AssignmentMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = 1421604802176120990L;

	private final static Log logger = LogFactory.getLog(AssignmentMaintenanceBean.class);

    /** the available assignments */
    private List<GenericDto<Assignment>> assignments;

    /** the id of the selected assignment */
    private long assignmentId;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            assignments = DtoHelper.fromList(Assignment.class, AssignmentHelper.list(manager));
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void removeAssignment(ActionEvent event) {
        Iterator<GenericDto<Assignment>> iter = assignments.iterator();
        while (iter.hasNext()) {
            GenericDto<Assignment> dto = iter.next();
            Assignment assignment = dto.getEntity();
            if (assignment.getId() != assignmentId) {
                continue;
            }
            if (dto.getState() == DtoState.NEW) {
                iter.remove();
            }

            dto.setState(DtoState.DELETE);
        }
    }

    public void unremoveAssignment(ActionEvent event) {
        for (GenericDto<Assignment> dto : assignments) {
            Assignment assignment = dto.getEntity();
            if (assignment.getId() != assignmentId) {
                continue;
            }
            dto.setState(DtoState.SYNC);
        }
    }

    public void addAssignment(ActionEvent event) {
        GenericDto<Assignment> dto = new GenericDto<Assignment>(new Assignment());
        dto.setState(DtoState.NEW);
        assignments.add(dto);
    }

    public void saveAssignments() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            DtoHelper.syncronize(manager, assignments);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(assignments);
        }
        catch (Exception ex) {
            logger.error("Failed to remove assignment '" + assignmentId + "'", ex);
            FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
        }
        finally {
            EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getAssignmentId() {
        return assignmentId;
    }

    public List<GenericDto<Assignment>> getAssignments() {
        return assignments;
    }
}

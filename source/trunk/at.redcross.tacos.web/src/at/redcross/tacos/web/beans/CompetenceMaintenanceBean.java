package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.helper.CompetenceHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "competenceMaintenanceBean")
public class CompetenceMaintenanceBean extends BaseBean {

    private static final long serialVersionUID = -944190177115092754L;

    private final static Log logger = LogFactory.getLog(CompetenceMaintenanceBean.class);

    /** the available competences */
    private List<GenericDto<Competence>> competences;

    /** the id of the selected competence */
    private long competenceId;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            competences = DtoHelper.fromList(Competence.class, CompetenceHelper.list(manager));
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void removeCompetence(ActionEvent event) {
        Iterator<GenericDto<Competence>> iter = competences.iterator();
        while (iter.hasNext()) {
            GenericDto<Competence> dto = iter.next();
            Competence competence = dto.getEntity();
            if (competence.getId() != competenceId) {
                continue;
            }
            if (dto.getState() == DtoState.NEW) {
                iter.remove();
            }

            dto.setState(DtoState.DELETE);
        }
    }

    public void unremoveCompetence(ActionEvent event) {
        for (GenericDto<Competence> dto : competences) {
            Competence competence = dto.getEntity();
            if (competence.getId() != competenceId) {
                continue;
            }
            dto.setState(DtoState.SYNC);
        }
    }

    public void addCompetence(ActionEvent event) {
        GenericDto<Competence> dto = new GenericDto<Competence>(new Competence());
        dto.setState(DtoState.NEW);
        competences.add(dto);
    }

    public void saveCompetences() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            DtoHelper.syncronize(manager, competences);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(competences);
        }
        catch (Exception ex) {
            logger.error("Failed to remove competence '" + competenceId + "'", ex);
            FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
        }
        finally {
            EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setCompetenceId(long competenceId) {
        this.competenceId = competenceId;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getCompetenceId() {
        return competenceId;
    }

    public List<GenericDto<Competence>> getCompetences() {
        return competences;
    }
}

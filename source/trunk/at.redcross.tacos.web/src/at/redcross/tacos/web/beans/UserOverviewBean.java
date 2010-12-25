package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.CompetenceHelper;
import at.redcross.tacos.dbal.helper.GroupHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.SystemUserQueryParam;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "userOverviewBean")
public class UserOverviewBean extends PagingBean {

    private static final long serialVersionUID = -5114023802685654841L;

    /** available locations */
    private List<Location> locations;

    /** available groups */
    private List<SelectItem> groups;

    /** the available competences */
    private List<SelectItem> competences;

    /** filter value for the query */
    private SystemUserQueryParam queryParam;

    /** queried results for visualization / reporting */
    private List<GenericDto<SystemUser>> users;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            locations = LocationHelper.list(manager);
            groups = SelectableItemHelper.convertToItems(GroupHelper.list(manager));
            competences = SelectableItemHelper.convertToItems(CompetenceHelper.list(manager));
            queryParam = new SystemUserQueryParam();
            loadfromDatabase(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void tabChanged(ValueChangeEvent event) {
        EntityManager manager = null;
        try {
            page = 1;
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void filterChanged(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void resetFilter(ActionEvent event) {
        queryParam = new SystemUserQueryParam();
    }

    // ---------------------------------
    // Private API
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager) {
        users = DtoHelper.fromList(SystemUser.class, SystemUserHelper.list(manager, queryParam));
    }

    /**
     * Returns whether or not the current authenticated user can edit (delete or
     * change) a user entry. The following restrictions are considered:
     * <ul>
     * <li>Principal must have the permission to edit a role</li>
     * </ul>
     */
    public boolean isEditUserEnabled() {
        // editing is allowed for principals with permission
        if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToEditUser()) {
            return true;
        }
        // edit denied
        return false;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<Location> getLocations() {
        return locations;
    }

    public SystemUserQueryParam getQueryParam() {
        return queryParam;
    }

    public List<SelectItem> getCompetences() {
        return competences;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public List<GenericDto<SystemUser>> getUsers() {
        return users;
    }
}

package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Competence;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.Group;
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
import at.redcross.tacos.web.beans.dto.GenericGroupDto;
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

    /** queried results if not grouped (#isGrouped) */
    private List<GenericDto<SystemUser>> users;

    /** queried and grouped result (#isGrouped) */
    private List<GenericGroupDto<EntityImpl, EntityImpl>> usersGrouped;

    /** group the result by competence */
    private boolean groupByCompetence;

    /** group the result by the group of the user */
    private boolean groupByUserGroup;

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
        groupByCompetence = false;
        groupByUserGroup = false;
        queryParam = new SystemUserQueryParam();
    }

    /** Returns whether or not the result should be grouped */
    public boolean isGrouped() {
        return groupByCompetence || groupByUserGroup;
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
    // Private API
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager) {
        users = DtoHelper.fromList(SystemUser.class, SystemUserHelper.list(manager, queryParam));
        usersGrouped = new ArrayList<GenericGroupDto<EntityImpl, EntityImpl>>();
        if (isGroupByCompetence()) {
            usersGrouped = groupByCompetence(users);
        }
        if (isGroupByUserGroup()) {
            usersGrouped = groupByUserGroup(users);
        }
    }

    /** Returns the result grouped by the competence */
    private List<GenericGroupDto<EntityImpl, EntityImpl>> groupByCompetence(List<GenericDto<SystemUser>> users) {
        List<GenericGroupDto<EntityImpl, EntityImpl>> result = new ArrayList<GenericGroupDto<EntityImpl, EntityImpl>>();
        Map<Competence, GenericGroupDto<EntityImpl, EntityImpl>> cache = new HashMap<Competence, GenericGroupDto<EntityImpl, EntityImpl>>();
        for (GenericDto<SystemUser> userDto : users) {
            SystemUser user = userDto.getEntity();
            for (Competence competence : user.getCompetences()) {
                GenericGroupDto<EntityImpl, EntityImpl> group = cache.get(competence);
                if (group == null) {
                    group = new GenericGroupDto<EntityImpl, EntityImpl>(competence);
                    cache.put(competence, group);
                    result.add(group);
                }
                group.addElement(user);
            }
        }
        return result;
    }

    /** Returns the result grouped by the group of the user */
    private List<GenericGroupDto<EntityImpl, EntityImpl>> groupByUserGroup(List<GenericDto<SystemUser>> users) {
        List<GenericGroupDto<EntityImpl, EntityImpl>> result = new ArrayList<GenericGroupDto<EntityImpl, EntityImpl>>();
        Map<Group, GenericGroupDto<EntityImpl, EntityImpl>> cache = new HashMap<Group, GenericGroupDto<EntityImpl, EntityImpl>>();
        for (GenericDto<SystemUser> userDto : users) {
            SystemUser user = userDto.getEntity();
            for (Group userGroup : user.getGroups()) {
                GenericGroupDto<EntityImpl, EntityImpl> group = cache.get(userGroup);
                if (group == null) {
                    group = new GenericGroupDto<EntityImpl, EntityImpl>(userGroup);
                    cache.put(userGroup, group);
                    result.add(group);
                }
                group.addElement(user);
            }
        }
        return result;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setGroupByUserGroup(boolean groupByUserGroup) {
        this.groupByUserGroup = groupByUserGroup;
        if (groupByUserGroup) {
            groupByCompetence = false;
        }
    }

    public void setGroupByCompetence(boolean groupByCompetence) {
        this.groupByCompetence = groupByCompetence;
        if (groupByCompetence) {
            groupByUserGroup = false;
        }
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public boolean isGroupByCompetence() {
        return groupByCompetence;
    }

    public boolean isGroupByUserGroup() {
        return groupByUserGroup;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<SelectItem> getCompetences() {
        return competences;
    }

    public List<SelectItem> getGroups() {
        return groups;
    }

    public SystemUserQueryParam getQueryParam() {
        return queryParam;
    }

    /** Returns the appropriate result */
    public List<?> getResult() {
        if (isGrouped()) {
            return usersGrouped;
        }
        return users;
    }
}

package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.helper.GroupHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "groupMaintenanceBean")
public class GroupMaintenanceBean extends BaseBean {

	private static final long serialVersionUID = -8165438157429138118L;

	private final static Logger logger = LoggerFactory.getLogger(GroupMaintenanceBean.class);

	/** the available groups */
	private List<GenericDto<Group>> groups;

	/** the id of the selected resources */
	private long groupId;

	@Override
	public void prettyInit() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			groups = DtoHelper.fromList(Group.class, GroupHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	public void removeGroup(ActionEvent event) {
		Iterator<GenericDto<Group>> iter = groups.iterator();
		while (iter.hasNext()) {
			GenericDto<Group> dto = iter.next();
			Group group = dto.getEntity();
			if (group.getId() != groupId) {
				continue;
			}
			if (dto.getState() == DtoState.NEW) {
				iter.remove();
			}
			dto.setState(DtoState.DELETE);
		}
	}

	public void unremoveGroup(ActionEvent event) {
		for (GenericDto<Group> dto : groups) {
			Group group = dto.getEntity();
			if (group.getId() != groupId) {
				continue;
			}
			dto.setState(DtoState.SYNC);
		}
	}

	public void addGroup(ActionEvent event) {
		GenericDto<Group> dto = new GenericDto<Group>(new Group());
		dto.setState(DtoState.NEW);
		groups.add(dto);
	}

	public void saveGroups() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, groups);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(groups);
		} catch (Exception ex) {
			logger.error("Failed to persist the groups", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	public void validateGroup(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String stringValue = (String) value;

		// roles must start with this prefix
		if (stringValue.startsWith("ROLE_")) {
			return;
		}
		throw new ValidatorException(FacesUtils.createErrorMessage("Illegal role name '"
				+ stringValue + "'"));
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getGroupId() {
		return groupId;
	}

	public List<GenericDto<Group>> getGroups() {
		return groups;
	}
}
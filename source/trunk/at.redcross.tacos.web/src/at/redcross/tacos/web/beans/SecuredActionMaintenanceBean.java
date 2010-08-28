package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.dbal.entity.SecuredAction;
import at.redcross.tacos.dbal.helper.SecuredActionHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.DtoState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.faces.combo.DropDownItem;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebActionDefinition;
import at.redcross.tacos.web.security.WebActionDefinitionRegistry;

@KeepAlive
@ManagedBean(name = "securedActionMaintenanceBean")
public class SecuredActionMaintenanceBean extends SecuredBean {

	private static final long serialVersionUID = -3650316584496612766L;

	private final static Logger logger = LoggerFactory
			.getLogger(SecuredActionMaintenanceBean.class);

	/** the available secured resources */
	private List<GenericDto<SecuredAction>> actions;

	/** the available definitions */
	private List<SelectItem> definitions;

	/** the id of the selected resources */
	private long securedActionId;

	@Override
	protected void init() throws Exception {
		super.init();
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			actions = DtoHelper.fromList(SecuredAction.class, SecuredActionHelper.list(manager));
			WebActionDefinitionRegistry registry = (WebActionDefinitionRegistry) FacesUtils
					.lookupBean("actionDefinitionRegistry");
			definitions = new ArrayList<SelectItem>();
			for (WebActionDefinition definition : registry.getDefinitions()) {
				definitions.add(new DropDownItem(definition.getId(), definition.getId()).getItem());
			}
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
	public void addSecuredAction(ActionEvent event) {
		GenericDto<SecuredAction> dto = new GenericDto<SecuredAction>(new SecuredAction());
		dto.setState(DtoState.NEW);
		actions.add(dto);
	}

	public void removeSecuredAction(ActionEvent event) {
		Iterator<GenericDto<SecuredAction>> iter = actions.iterator();
		while (iter.hasNext()) {
			GenericDto<SecuredAction> dto = iter.next();
			SecuredAction securedAction = dto.getEntity();
			if (securedAction.getId() != securedActionId) {
				continue;
			}
			if (dto.getState() == DtoState.NEW) {
				iter.remove();
			}
			dto.setState(DtoState.DELETE);
		}
	}

	public void unremoveSecuredAction(ActionEvent event) {
		for (GenericDto<SecuredAction> dto : actions) {
			SecuredAction securedAction = dto.getEntity();
			if (securedAction.getId() != securedActionId) {
				continue;
			}
			dto.setState(DtoState.SYNC);
		}
	}

	public void saveSecuredResources() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			DtoHelper.syncronize(manager, actions);
			EntityManagerHelper.commit(manager);
			DtoHelper.filter(actions);
		} catch (Exception ex) {
			logger.error("Failed to persist the secured actions", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Validation methods
	// ---------------------------------
	public void validateSecuredAction(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String stringValue = (String) value;
		if (isValidExpression(stringValue)) {
			return;
		}
		throw new ValidatorException(FacesUtils.createErrorMessage("No valid access definition"));
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setSecuredActionId(long securedActionId) {
		this.securedActionId = securedActionId;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getSecuredActionId() {
		return securedActionId;
	}

	public List<SelectItem> getDefinitions() {
		return definitions;
	}

	public List<GenericDto<SecuredAction>> getActions() {
		return actions;
	}
}

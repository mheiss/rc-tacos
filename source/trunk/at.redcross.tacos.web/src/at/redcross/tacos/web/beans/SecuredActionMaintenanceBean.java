package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.dbal.entity.SecuredAction;
import at.redcross.tacos.dbal.helper.SecuredActionHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "securedActionMaintenanceBean")
public class SecuredActionMaintenanceBean extends SecuredBean {

	private static final long serialVersionUID = -3650316584496612766L;

	private final static Logger logger = LoggerFactory
			.getLogger(SecuredActionMaintenanceBean.class);

	/** the available secured resources */
	private List<GenericDto<SecuredAction>> actions;

	@Override
	protected void init() throws Exception {
		super.init();
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			actions = DtoHelper.fromList(SecuredAction.class, SecuredActionHelper.list(manager));
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Business methods
	// ---------------------------------
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
	// Getters for the properties
	// ---------------------------------
	public List<GenericDto<SecuredAction>> getActions() {
		return actions;
	}
}

package at.redcross.tacos.web.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebActionDefinition;
import at.redcross.tacos.web.security.WebActionDefinitionRegistry;
import at.redcross.tacos.web.security.WebPermissionListenerRegistry;

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
            syncronizeActions(manager);
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
            // commit the changes
            manager = EntityManagerFactory.createEntityManager();
            DtoHelper.syncronize(manager, actions);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(actions);

            // notify that the cache needs to be updated
            WebPermissionListenerRegistry.getInstance().notifyInvalidateCache();
        } catch (Exception ex) {
            logger.error("Failed to persist the secured actions", ex);
            FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
        } finally {
            EntityManagerHelper.close(manager);
        }
    }

    /**
     * Synchronizes the defined actions with the ones form the database
     */
    protected void syncronizeActions(EntityManager manager) {
        List<SecuredAction> actions = SecuredActionHelper.list(manager);
        Set<WebActionDefinition> definitions = WebActionDefinitionRegistry.getInstance()
                .getDefinitions();
        // put them into a map for comparison
        Map<String, SecuredAction> actionMap = new HashMap<String, SecuredAction>();
        for (SecuredAction action : actions) {
            actionMap.put(action.getName(), action);
        }
        Map<String, WebActionDefinition> definitionMap = new HashMap<String, WebActionDefinition>();
        for (WebActionDefinition defintion : definitions) {
            definitionMap.put(defintion.getId(), defintion);
        }

        // remove elements that are not in the local definition any more
        Set<String> actionSet = new HashSet<String>(actionMap.keySet());
        actionSet.removeAll(definitionMap.keySet());
        for (String actionId : actionSet) {
            SecuredAction action = actionMap.get(actionId);
            manager.remove(action);
            logger.info("Removing securedAction '" + action + "'");
        }

        // remove all elements that are already in the database
        Set<String> definitionSet = new HashSet<String>(definitionMap.keySet());
        definitionSet.removeAll(actionMap.keySet());
        for (String definitionId : definitionSet) {
            WebActionDefinition definition = definitionMap.get(definitionId);
            SecuredAction action = new SecuredAction();
            action.setName(definition.getId());
            action.setDescription(definition.getDescription());
            action.setActionExpression("permitAll");
            manager.persist(action);
            logger.info("Adding securedAction '" + action + "'");
        }

        // commit the changes to the database
        EntityManagerHelper.commit(manager);
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

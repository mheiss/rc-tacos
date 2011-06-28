package at.redcross.tacos.web.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.redcross.tacos.dbal.entity.SecuredAction;
import at.redcross.tacos.dbal.helper.SecuredActionHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

/**
 * Synchronizes the available actions with the database.
 */
public class WebActionDefinitionServlet extends HttpServlet {

    private static final long serialVersionUID = -874845960031052211L;

    /** log the changes */
    private final static Log logger = LogFactory.getLog(WebActionDefinitionServlet.class);

    @Override
    public void init(ServletConfig arg0) throws ServletException {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            syncronizeActions(manager);
        } catch (Exception ex) {
            throw new ServletException("Failed to initializ action registry", ex);
        } finally {
            manager = EntityManagerHelper.close(manager);
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
}

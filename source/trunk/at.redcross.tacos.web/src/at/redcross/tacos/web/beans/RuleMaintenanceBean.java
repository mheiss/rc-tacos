package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.helper.FilterRuleHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "ruleMainteanceBean")
public class RuleMaintenanceBean extends PagingBean {

    private static final long serialVersionUID = 8435960134794880939L;

    /** The available rules */
    private List<FilterRule> rules;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rules = FilterRuleHelper.list(manager);
            FilterRuleHelper.replaceParams(rules.toArray(new FilterRule[rules.size()]));
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<FilterRule> getRules() {
        return rules;
    }

}

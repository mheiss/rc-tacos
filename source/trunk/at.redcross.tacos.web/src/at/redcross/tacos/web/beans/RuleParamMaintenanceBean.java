package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.entity.FilterRuleParam;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "ruleParamMainteanceBean")
public class RuleParamMaintenanceBean extends PagingBean {

    private static final long serialVersionUID = 8435960134794880939L;

    /** the request parameter */
    private long ruleId = -1;

    /** The rule to be edited */
    private FilterRule rule;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rule = manager.find(FilterRule.class, ruleId);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public String persist() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            manager.merge(rule);
            for (FilterRuleParam param : rule.getParams()) {
                manager.merge(param);
            }
            EntityManagerHelper.commit(manager);
            return FacesUtils.pretty("admin-ruleMaintenance");
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Die Regel konte nicht gespeichert werden");
            return null;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------

    public long getRuleId() {
        return ruleId;
    }

    public FilterRule getRule() {
        return rule;
    }

}

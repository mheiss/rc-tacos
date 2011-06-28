package at.redcross.tacos.web.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.helper.FilterRuleHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "ruleMainteanceBean")
public class RuleMaintenanceBean extends PagingBean {

    private static final long serialVersionUID = 8435960134794880939L;

    /** The available rules */
    private List<FilterRule> rules;

    /** the selected rule */
    private int selectedRuleId;

    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rules = FilterRuleHelper.list(manager);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public void persist(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (FilterRule rule : rules) {
                manager.merge(rule);
            }
            EntityManagerHelper.commit(manager);
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Die Regeln konten nicht gespeichert werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setSelectedRuleId(int selectedRuleId) {
        this.selectedRuleId = selectedRuleId;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public List<FilterRule> getRules() {
        return rules;
    }

    public FilterRule getSelectedRule() {
        for (FilterRule rule : rules) {
            if (rule.getId() != selectedRuleId) {
                continue;
            }
            return rule;
        }
        return null;
    }

    public int getSelectedRuleId() {
        return selectedRuleId;
    }

}

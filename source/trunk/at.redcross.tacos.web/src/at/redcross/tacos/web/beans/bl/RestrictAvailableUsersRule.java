package at.redcross.tacos.web.beans.bl;

import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.FilterRule;
import at.redcross.tacos.dbal.entity.FilterRuleParam;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.FilterRuleHelper;
import at.redcross.tacos.web.beans.RosterMaintenanceBean;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.security.WebUserDetails;

public class RestrictAvailableUsersRule extends FilterRuleDefinition {

    private final static String REQUIRED_COMPETENCE_ID = "requiredCompetence";
    private final static String AFFECTED_COMPETENCE_ID = "affectedCompetence";

    public RestrictAvailableUsersRule() {
        super("availableRosterUsers");
    }

    @Override
    public String getDescription() {
        return "Mitarbeitereinschränkungen für Dienstplaneinträge festlegen";
    }

    @Override
    protected void init(List<FilterParamDefinition> params) {
        {
            FilterParamDefinition param = new FilterParamDefinition(REQUIRED_COMPETENCE_ID);
            param.setDescription("Kompetenz um Dienstplaneinträge für alle Mitarbeiter zu erstellen");
            params.add(param);
        }
        {
            FilterParamDefinition param = new FilterParamDefinition(AFFECTED_COMPETENCE_ID);
            param.setDescription("Kompetenz der Mitarbeiter die gefiltert werden");
            params.add(param);
        }
    }

    @Override
    public boolean applyFilter(EntityManager manager, Object object) {
        RosterMaintenanceBean bean = (RosterMaintenanceBean) object;

        // assert that all parameters are available
        FilterRule rule = FilterRuleHelper.getByName(manager, getId());
        FilterRuleParam requiredParam = rule.getParam(REQUIRED_COMPETENCE_ID);
        FilterRuleParam affectedParam = rule.getParam(AFFECTED_COMPETENCE_ID);
        if (requiredParam == null || affectedParam == null) {
            return false;
        }

        // filter out users if we do not have the required competence
        boolean filtered = false;
        WebUserDetails principal = FacesUtils.getPrincipal();
        SystemUser systemUser = principal.getLogin().getSystemUser();
        if (systemUser.hasCompetence(requiredParam.getValue())) {
            return filtered;
        }
        // filter out all entries with the given competence
        Iterator<SelectItem> iter = bean.getUserItems().iterator();
        while (iter.hasNext()) {
            SelectableItem item = (SelectableItem) iter.next().getValue();
            SystemUser user = (SystemUser) item.getValue();
            if (!user.hasCompetence(affectedParam.getValue())) {
                continue;
            }
            filtered = true;
            iter.remove();
        }
        return filtered;
    }
}

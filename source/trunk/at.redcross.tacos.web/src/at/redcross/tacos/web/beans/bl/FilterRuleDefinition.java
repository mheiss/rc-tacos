package at.redcross.tacos.web.beans.bl;

import java.util.ArrayList;
import java.util.List;

/** Default rule implementation that stores the metadata in the database */
public abstract class FilterRuleDefinition implements IFilterRule {

    /** the unique id of the rule */
    private final String ruleId;

    /** the list of parameters */
    private final List<FilterParamDefinition> params;

    /** Creates a new rule */
    public FilterRuleDefinition(String ruleId) {
        this.ruleId = ruleId;
        this.params = new ArrayList<FilterParamDefinition>();
        init(params);
    }

    /** Initializes this rule */
    protected abstract void init(List<FilterParamDefinition> params);

    @Override
    public String getId() {
        return ruleId;
    }

    @Override
    public List<FilterParamDefinition> getParams() {
        return params;
    }

}

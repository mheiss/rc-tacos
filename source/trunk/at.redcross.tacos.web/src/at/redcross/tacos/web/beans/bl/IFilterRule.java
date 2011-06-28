package at.redcross.tacos.web.beans.bl;

import java.util.List;

import javax.persistence.EntityManager;

public interface IFilterRule {

    /** Returns the unique id of the rule */
    public String getId();

    /** Returns the description of the rule */
    public String getDescription();

    /** Returns the parameters of this rule */
    public List<FilterParamDefinition> getParams();

    /** Applies the given rule to the given object */
    public boolean applyFilter(EntityManager manager, Object bean);

}

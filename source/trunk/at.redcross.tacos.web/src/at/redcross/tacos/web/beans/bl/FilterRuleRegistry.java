package at.redcross.tacos.web.beans.bl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Contains business relevant rules */
public class FilterRuleRegistry {

    /** the one and only instance */
    private static FilterRuleRegistry instance;

    /** the list of registered rules */
    private final List<IFilterRule> rules = new ArrayList<IFilterRule>();

    /** Creates a new instance of the registry */
    private FilterRuleRegistry() {
        init();
    }

    /**
     * Returns the shared registry instance.
     * 
     * @return the registry
     */
    public static synchronized FilterRuleRegistry getInstance() {
        if (instance == null) {
            instance = new FilterRuleRegistry();
        }
        return instance;
    }

    /** Returns an unmodifiable list of registered rules */
    public List<IFilterRule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    /** Initializes all available rules */
    private void init() {
        rules.add(new RestrictAvailableUsersRule());
    }

}

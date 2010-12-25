package at.redcross.tacos.dbal.utils;

import java.io.Serializable;

/** Contains the changed attribute along with the old and new value */
public class ObjectChange implements Serializable {

    private static final long serialVersionUID = 2305463681902116251L;

    /** the name of the attribute */
    private final String attribute;

    /** the old value */
    private final Object oldValue;

    /** the new value */
    private final Object newValue;

    /**
     * Creates a new change instance for the given attribute
     */
    public ObjectChange(String attribute, Object newValue, Object oldValue) {
        this.attribute = attribute;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public String getAttribute() {
        return attribute;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

}

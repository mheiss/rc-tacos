package at.redcross.tacos.dbal.entity;

import java.io.Serializable;

/** The {@code RevisionInfoChange} holds a particular attribute change */
public class RevisionInfoChange implements Serializable {

    private static final long serialVersionUID = 1781384181622633393L;

    /** the changed property */
    private final String property;

    /** the old value */
    private Object oldValue;

    /** the new value */
    private Object newValue;

    /** Creates a new change entry */
    public RevisionInfoChange(String property) {
        this.property = property;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getProperty() {
        return property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }
}

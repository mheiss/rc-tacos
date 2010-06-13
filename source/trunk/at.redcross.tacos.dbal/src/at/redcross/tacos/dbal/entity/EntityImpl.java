package at.redcross.tacos.dbal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

/**
 * The {@code EntityImpl} is the base class for all entity instances.
 */
@MappedSuperclass
public abstract class EntityImpl implements Serializable {

    private static final long serialVersionUID = -3033645633746573785L;

    @Embedded
    @Column(nullable = true)
    private History history;

    // ---------------------------------
    // Public API
    // ---------------------------------
    /**
     * Returns a meaningful string that will be displayed in the UI to render
     * the object.
     * 
     * @param a
     *            string that describes this entity
     */
    public abstract String getDisplayString();

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public History getHistory() {
        return history;
    }
}

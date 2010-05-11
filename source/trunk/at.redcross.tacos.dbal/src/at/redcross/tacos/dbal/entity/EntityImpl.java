package at.redcross.tacos.dbal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

/**
 * The {@code EntityImpl} is the base class for all entity instances.
 */
@MappedSuperclass
public class EntityImpl implements Serializable {

    private static final long serialVersionUID = -3033645633746573785L;

    @Embedded
    @Column(nullable = true)
    private History history;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setHistory(History history) {
        this.history = history;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public History getHistory() {
        return history;
    }
}

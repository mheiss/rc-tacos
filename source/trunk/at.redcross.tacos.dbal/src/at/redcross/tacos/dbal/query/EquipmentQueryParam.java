package at.redcross.tacos.dbal.query;

import java.io.Serializable;
import java.util.Date;

/** Holds query parameters for the equipment entry */
public class EquipmentQueryParam implements Serializable {

    private static final long serialVersionUID = 2748483802555885057L;

    /** the name of the entry */
    public String name;

    /** the notes of the entry */
    public String notes;

    /** show entries by the inventory number */
    public int inventoryNumber;

    /** Show entries target > actual */
    public boolean targetGreaterActual = false;

    /** Show entries actual > target */
    public boolean actualGreaterTarget = false;

    /** Show records from the given expiration date */
    public Date expirationStart;

    /** Show records until the given expiration date */
    public Date expirationEnd;

    /** Show records from the given inventory date */
    public Date inventoryStart;

    /** Show records until the given inventory date */
    public Date inventoryEnd;

    // ---------------------------------
    // Getters and Setters
    // ---------------------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public boolean isTargetGreaterActual() {
        return targetGreaterActual;
    }

    public void setTargetGreaterActual(boolean targetGreaterActual) {
        this.targetGreaterActual = targetGreaterActual;
    }

    public boolean isActualGreaterTarget() {
        return actualGreaterTarget;
    }

    public void setActualGreaterTarget(boolean actualGreaterTarget) {
        this.actualGreaterTarget = actualGreaterTarget;
    }

    public Date getExpirationStart() {
        return expirationStart;
    }

    public void setExpirationStart(Date expirationStart) {
        this.expirationStart = expirationStart;
    }

    public Date getExpirationEnd() {
        return expirationEnd;
    }

    public void setExpirationEnd(Date expirationEnd) {
        this.expirationEnd = expirationEnd;
    }

    public Date getInventoryStart() {
        return inventoryStart;
    }

    public void setInventoryStart(Date inventoryStart) {
        this.inventoryStart = inventoryStart;
    }

    public Date getInventoryEnd() {
        return inventoryEnd;
    }

    public void setInventoryEnd(Date inventoryEnd) {
        this.inventoryEnd = inventoryEnd;
    }

}

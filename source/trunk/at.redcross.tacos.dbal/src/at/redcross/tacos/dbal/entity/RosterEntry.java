package at.redcross.tacos.dbal.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RosterEntry")
public class RosterEntry extends EntityImpl {

    private static final long serialVersionUID = -8201138084188688001L;

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Location location;

    @OneToOne
    private ServiceType serviceType;

    @OneToOne
    private Assignment assignment;

    @Temporal(TemporalType.DATE)
    private Calendar plannedStart;

    @Temporal(TemporalType.DATE)
    private Calendar plannedEnd;

    @Temporal(TemporalType.DATE)
    private Calendar realStart;

    @Temporal(TemporalType.DATE)
    private Calendar realEnd;

    @Column
    private String notes;

    @Column
    private boolean standby;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setLocation(Location location) {
        this.location = location;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setPlannedStart(Calendar plannedStart) {
        this.plannedStart = plannedStart;
    }

    public void setPlannedEnd(Calendar plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public void setRealStart(Calendar realStart) {
        this.realStart = realStart;
    }

    public void setRealEnd(Calendar realEnd) {
        this.realEnd = realEnd;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStandby(boolean standby) {
        this.standby = standby;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Location getLocation() {
        return location;
    }

    public Calendar getPlannedStart() {
        return plannedStart;
    }

    public Calendar getPlannedEnd() {
        return plannedEnd;
    }

    public Calendar getRealStart() {
        return realStart;
    }

    public Calendar getRealEnd() {
        return realEnd;
    }

    public boolean isStandby() {
        return standby;
    }

    public String getNotes() {
        return notes;
    }
}

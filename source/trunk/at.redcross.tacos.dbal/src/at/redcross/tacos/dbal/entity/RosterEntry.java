package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "RosterEntry")
public class RosterEntry extends EntityImpl {

    private static final long serialVersionUID = -8201138084188688001L;

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private SystemUser systemUser;

    @OneToOne
    private Location location;

    @OneToOne
    private ServiceType serviceType;

    @OneToOne
    private Assignment assignment;

    @Temporal(TemporalType.DATE)
    private Date plannedStart;

    @Temporal(TemporalType.DATE)
    private Date plannedEnd;

    @Temporal(TemporalType.DATE)
    private Date realStart;

    @Temporal(TemporalType.DATE)
    private Date realEnd;

    @Column
    private String notes;

    @Column
    private boolean standby;

    @Column
    private boolean specialService;

    // ---------------------------------
    // 
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("user", systemUser).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        RosterEntry rhs = (RosterEntry) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setPlannedStart(Date plannedStart) {
        this.plannedStart = plannedStart;
    }

    public void setPlannedEnd(Date plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public void setRealStart(Date realStart) {
        this.realStart = realStart;
    }

    public void setRealEnd(Date realEnd) {
        this.realEnd = realEnd;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStandby(boolean standby) {
        this.standby = standby;
    }

    public void setSpecialService(boolean specialService) {
        this.specialService = specialService;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public SystemUser getSystemUser() {
        return systemUser;
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

    public Date getPlannedStart() {
        return plannedStart;
    }

    public Date getPlannedEnd() {
        return plannedEnd;
    }

    public Date getRealStart() {
        return realStart;
    }

    public Date getRealEnd() {
        return realEnd;
    }

    public boolean isStandby() {
        return standby;
    }

    public boolean isSpecialService() {
        return specialService;
    }

    public String getNotes() {
        return notes;
    }
}

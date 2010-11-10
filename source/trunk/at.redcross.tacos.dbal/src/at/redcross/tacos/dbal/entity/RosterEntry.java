package at.redcross.tacos.dbal.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;

@Entity
@Table(name = "RosterEntry")
public class RosterEntry extends EntityImpl {

    private static final long serialVersionUID = -8201138084188688001L;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private SystemUser systemUser;

    @ManyToOne(optional = false)
    private Location location;

    @ManyToOne(optional = false)
    private ServiceType serviceType;

    @ManyToOne(optional = false)
    private Assignment assignment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date plannedStartDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date plannedEndDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date realStartDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date realEndDateTime;

    @Column(length=1024)
    private String notes;

    @Column
    private boolean standby;

    @Column
    private boolean specialService;

    @ManyToOne(optional = true)
    private Car car;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataState state = DataState.NORMAL;

    // ---------------------------------
    // EntityImpl
    // ---------------------------------
    @Override
    public String getDisplayString() {
        return String.valueOf(id);
    }

    // ---------------------------------
    // Object related methods
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
    // Business relevant methods
    // ---------------------------------
    public boolean isToDelete() {
        return state.equals(DataState.DELETE);
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

    public void setPlannedStartDateTime(Date plannedStartDateTime) {
        this.plannedStartDateTime = DateUtils.truncate(plannedStartDateTime, Calendar.MINUTE);
    }

    public void setPlannedEndDateTime(Date plannedEndDateTime) {
        this.plannedEndDateTime = DateUtils.truncate(plannedEndDateTime, Calendar.MINUTE);
    }

    public void setRealStartDateTime(Date realStartDateTime) {
        this.realStartDateTime = DateUtils.truncate(realStartDateTime, Calendar.MINUTE);
    }

    public void setRealEndDateTime(Date realEndDateTime) {
        this.realEndDateTime = DateUtils.truncate(realEndDateTime, Calendar.MINUTE);
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

    public void setCar(Car car) {
        this.car = car;
    }

    public void setState(DataState state) {
        this.state = state;
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

    public Date getPlannedStartDateTime() {
        return plannedStartDateTime;
    }

    public Date getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public Date getRealStartDateTime() {
        return realStartDateTime;
    }

    public Date getRealEndDateTime() {
        return realEndDateTime;
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

    public Car getCar() {
        return car;
    }

    public DataState getState() {
        return state;
    }
}

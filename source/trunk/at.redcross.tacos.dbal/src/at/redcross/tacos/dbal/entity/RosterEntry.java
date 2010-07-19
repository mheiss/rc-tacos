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

    // PLANNED START DATE AND TIME
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date plannedStartDate;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date plannedStartTime;

    // PLANNED END DATE AND TIME
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date plannedEndDate;

    @Temporal(TemporalType.TIME)
    @Column(nullable = false)
    private Date plannedEndTime;

    // REAL START DATE AND TIME
    @Temporal(TemporalType.DATE)
    private Date realStartDate;

    @Temporal(TemporalType.TIME)
    private Date realStartTime;

    // REAL END DATE AND TIME
    @Temporal(TemporalType.DATE)
    private Date realEndDate;

    @Temporal(TemporalType.TIME)
    private Date realEndTime;

    @Column
    private String notes;

    @Column
    private boolean standby;

    @Column
    private boolean specialService;
    
    @OneToOne
    private Car car;
    
    

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

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public void setPlannedEndTime(Date plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public void setRealEndDate(Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }

    public void setRealStartDate(Date realStartDate) {
        this.realStartDate = realStartDate;
    }

    public void setRealStartTime(Date realStartTime) {
        this.realStartTime = realStartTime;
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
    
    public void setCar(Location location) {
        this.location = location;
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

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public Date getPlannedEndTime() {
        return plannedEndTime;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    public Date getRealStartDate() {
        return realStartDate;
    }

    public Date getRealStartTime() {
        return realStartTime;
    }

    public Date getRealEndDate() {
        return realEndDate;
    }

    public Date getRealEndTime() {
        return realEndTime;
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
}

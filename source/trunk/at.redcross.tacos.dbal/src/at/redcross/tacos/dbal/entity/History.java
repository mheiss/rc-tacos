package at.redcross.tacos.dbal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class History implements Serializable {

    private static final long serialVersionUID = -3160225389716712769L;

    @Column(nullable = false)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    private String changedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date changedAt;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public Date getChangedAt() {
        return changedAt;
    }
}

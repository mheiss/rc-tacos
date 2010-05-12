package at.redcross.tacos.dbal.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "SoftwareVersion")
public class SoftwareVersion extends EntityImpl {

    private static final long serialVersionUID = -5465614486707438472L;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private java.util.Date date;

    @Column(name = "version", nullable = false)
    private String version;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "SoftwareVersion_Fk")
    private Collection<ChangelogEntry> changelogs;

    // ---------------------------------
    // 
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).toString();
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
        SoftwareVersion rhs = (SoftwareVersion) obj;
        return new EqualsBuilder().append(id, rhs.id).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setVersion(String version) {
        this.version = version;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public void addChangelogEntry(ChangelogEntry entry) {
        getChangelogs().add(entry);
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public long getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public java.util.Date getDate() {
        return date;
    }

    public Collection<ChangelogEntry> getChangelogs() {
        if (changelogs == null) {
            changelogs = new ArrayList<ChangelogEntry>();
        }
        return changelogs;
    }

}

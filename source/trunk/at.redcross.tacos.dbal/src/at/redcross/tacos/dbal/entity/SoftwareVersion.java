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

/**
 * The {@code SoftwareVersion} describes the version of the product.
 */
@Entity
@Table(name = "SoftwareVersion")
public class SoftwareVersion extends EntityImpl {

	private static final long serialVersionUID = -5465614486707438472L;

	@Id
	@Column(name = "id")
	@GeneratedValue
	private long id;

	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	private java.util.Date date;

	@Column(name = "version", nullable = false)
	private String version;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "SoftwareVersion")
	private Collection<ChangelogEntry> changelogs;

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

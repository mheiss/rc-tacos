package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ChangelogEntry")
public class ChangelogEntry extends EntityImpl {

	private static final long serialVersionUID = -6956043329210269429L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String message;

	// -----------------------------------
	// Setters for the properties
	// -----------------------------------
	public void setMessage(String message) {
		this.message = message;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
}

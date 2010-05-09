package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserAuthority")
public class Authority extends EntityImpl {

	private static final long serialVersionUID = -722724664751403613L;

	@Id
	private String id;

	@Column(nullable = false)
	private String description;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setId(String id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}

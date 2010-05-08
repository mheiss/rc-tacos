package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Assignment")
public class Assignment extends EntityImpl {

	private static final long serialVersionUID = -2510131078051328478L;

	@Id
	private String name;

	@Column(nullable = false)
	private String description;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}

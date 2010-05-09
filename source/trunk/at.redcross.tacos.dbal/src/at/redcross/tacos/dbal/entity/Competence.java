
package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Competence")
public class Competence extends EntityImpl {

	private static final long serialVersionUID = -2510131078051328478L;

	@Id
	private String name;

	@Column(nullable = false)
	private String description;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}

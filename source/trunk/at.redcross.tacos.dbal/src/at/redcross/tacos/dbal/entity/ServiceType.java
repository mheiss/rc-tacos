package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ServiceType")
public class ServiceType extends EntityImpl {

	private static final long serialVersionUID = 7780994907233444353L;

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

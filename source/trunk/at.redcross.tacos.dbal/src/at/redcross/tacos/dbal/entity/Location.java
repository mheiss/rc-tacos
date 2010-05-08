package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class Location extends EntityImpl {

	private static final long serialVersionUID = 6997613929181751597L;

	@Id
	private String name;

	@Embedded
	@Column(nullable = false)
	private Address address;

	@OneToOne(mappedBy = "location")
	@Column(nullable = false)
	private District district;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	public District getDistrict() {
		return district;
	}
}

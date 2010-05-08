package at.redcross.tacos.dbal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Entity
@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = -8467253043757429402L;

	@Column
	private String zipCode;

	@Column
	private String city;

	@Column
	private String street;

	@Column
	private String phone;

	@Column(nullable = false, unique = true)
	private String email;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getZipCode() {
		return zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}
}

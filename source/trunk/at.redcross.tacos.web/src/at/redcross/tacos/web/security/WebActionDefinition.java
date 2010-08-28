package at.redcross.tacos.web.security;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The {@code WebActionDefinition} defines a single action that can be
 * performed.
 */
public class WebActionDefinition {

	/** the unique action id */
	private final String id;

	/** the description for the action */
	private String description;

	/**
	 * Creates a new {@code WebActionDefinition} using the given.
	 * 
	 * @param id
	 *            the unique action id
	 */
	public WebActionDefinition(String id) {
		this.id = id;
	}

	// ---------------------------------
	// Object related methods
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
		WebActionDefinition wad = (WebActionDefinition) obj;
		return new EqualsBuilder().append(id, wad.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	/**
	 * Sets the free-text that describes the action
	 * 
	 * @param description
	 *            the action description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	/**
	 * Returns the unique action id.
	 * 
	 * @return the action id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the free-text that describes what this action is about
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}

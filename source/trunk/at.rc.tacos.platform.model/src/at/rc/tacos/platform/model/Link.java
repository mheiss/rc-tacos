package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a single hyperlink that can be displayed in the webinterfac
 * 
 * @author Payer Martin
 * @version 1.0
 */
public class Link extends Lockable {

	private int id;
	private String innerText;
	private String href;
	private String title;
	private String username;

	public Link() {
		id = -1;
		innerText = null;
		href = null;
		title = null;
		username = null;
	}

	/**
	 * Returns the human readable string for this <code>Link</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("href", href);
		builder.append("value", innerText);
		builder.append("tooltip", title);
		builder.append("username", username);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Link</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Link#getHref()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(31, 41);
		builder.append(href);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Link</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Link#getHref()}.
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
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
		Link link = (Link) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(href, link.href);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return id;
	}

	@Override
	public Class<?> getLockedClass() {
		return Link.class;
	}

	// GETTERS AND SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInnerText() {
		return innerText;
	}

	public void setInnerText(String innerText) {
		this.innerText = innerText;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

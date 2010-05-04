package at.rc.tacos.platform.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;

import at.rc.tacos.platform.util.MyUtils;

/**
 * Info text for each day to display.
 * 
 * @author Michael
 */
public class DayInfoMessage extends Lockable {

	// properties
	private long timestamp;
	private String message;
	private String lastChangedBy;

	// additonal information
	private boolean dirty;

	/**
	 * Default class constructor
	 */
	public DayInfoMessage() {
	}

	/**
	 * Default class constructor for a complete day info
	 * 
	 * @param message
	 *            the text to display
	 * @param timestamp
	 *            the day, month and year values when the text should be
	 *            displayed
	 * @param lastChangedBy
	 *            the username who changed the message
	 */
	public DayInfoMessage(String message, long timestamp, String lastChangedBy) {
		setMessage(message);
		setTimestamp(timestamp);
		setLastChangedBy(lastChangedBy);
	}

	/**
	 * Returns the human readable string for this <code>DayInfoMessage</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("updated", MyUtils.timestampToString(timestamp, MyUtils.dateFormat));
		builder.append("changedBy", lastChangedBy);
		builder.append("message", message);
		builder.append("dirty", dirty);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>DayInfoMessage</code>
	 * instance.
	 * <p>
	 * The hashCode is based uppon the {@link DayInfoMessage#timestamp}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(23, 43);
		builder.append(DateUtils.truncate(new Date(timestamp), Calendar.DAY_OF_MONTH));
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not the two <code>DayInfoMessage</code> instances are
	 * equal.
	 * <p>
	 * The compared fields are {@link DayInfoMessage#timestamp}
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
		DayInfoMessage dayInfoMessage = (DayInfoMessage) obj;
		if (!DateUtils.isSameDay(new Date(timestamp), new Date(dayInfoMessage.timestamp))) {
			return false;
		}
		return true;
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return hashCode();
	}

	@Override
	public Class<?> getLockedClass() {
		return DayInfoMessage.class;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the timestamp containing the day the message is assigned to
	 * 
	 * @return the timestamp the day the message is assigned to
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Returns the message for the given day.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the username of the member that changed the message at last.
	 * 
	 * @return the lastChangedBy the username of the member
	 */
	public String getLastChangedBy() {
		return lastChangedBy;
	}

	/**
	 * Returns whether the day info has local unsaved changes and should be
	 * saved in order to keep the changes.
	 * 
	 * @return true if there are local changes
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the flag to indicate that the day info was changed locally.
	 * 
	 * @param dirty
	 *            set to true to indicate local changes
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * Sets the timestamp for which day the message is
	 * 
	 * @param timestamp
	 *            the day the message is assigned to
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Sets the specifiy day info message
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the username of the member who lastly changed the message
	 * 
	 * @param lastChangedBy
	 *            the lastly edited username
	 */
	public void setLastChangedBy(String lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}
}

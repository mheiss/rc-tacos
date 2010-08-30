package at.redcross.tacos.web.reporting;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.redcross.tacos.dbal.entity.RosterEntry;

/**
 * The {@code RosterReportParam} is used prepare and format the required fields
 * of a {@linkplain RosterEntry roster entry} for the BIRT reporting.
 * <p>
 * Please note that this class will be passed to the report engine. The engine
 * relies on the method types and names that are provided here so be carefully
 * when changing them.
 *</p>
 */
public class RosterReportParam {

	/** formats a date using dd.MM.yyyy */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	/** formats a date using HH:mm */
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	/** last name and first name */
	private final String staffMember;

	/** name of the location */
	private final String location;

	/** formatted planned time [start] */
	private final String plannedStartTime;

	/** formatted planned time [end] */
	private final String plannedEndTime;

	/** formatted real time [start] */
	private final String realStartTime;
	/** formatted real time [end] */
	private final String realEndTime;

	/** name of the assignment */
	private final String assignment;

	/** name of the service type */
	private final String serviceType;

	/**
	 * Creates a new report parameter using the values of the given entry
	 * 
	 * @param entry
	 *            the entry to get the values from
	 */
	public RosterReportParam(RosterEntry entry) {
		this.staffMember = formatStaffMember(entry);
		this.location = formatLocation(entry);
		this.plannedStartTime = formatDate(entry.getPlannedStartDate(), entry.getPlannedStartTime());
		this.plannedEndTime = formatDate(entry.getPlannedEndDate(), entry.getPlannedEndTime());
		this.realStartTime = formatDate(entry.getRealStartDate(), entry.getRealStartTime());
		this.realEndTime = formatDate(entry.getRealEndDate(), entry.getRealEndTime());
		this.assignment = formatAssignment(entry);
		this.serviceType = formatServiceType(entry);
	}

	// --------------------------------------------
	// Getters for the properties
	// ---------------------------------------------
	public String getStaffMember() {
		return staffMember;
	}

	public String getLocation() {
		return location;
	}

	public String getPlannedStartTime() {
		return plannedStartTime;
	}

	public String getPlannedEndTime() {
		return plannedEndTime;
	}

	public String getRealStartTime() {
		return realStartTime;
	}

	public String getRealEndTime() {
		return realEndTime;
	}

	public String getAssignment() {
		return assignment;
	}

	public String getServiceType() {
		return serviceType;
	}

	// ----------------------------------------------
	// Helper method to extract and format parameter
	// ----------------------------------------------
	private String formatStaffMember(RosterEntry entry) {
		StringBuilder builder = new StringBuilder();
		builder.append(entry.getSystemUser().getLastName());
		builder.append(" ");
		builder.append(entry.getSystemUser().getFirstName());
		return builder.toString();
	}

	private String formatLocation(RosterEntry entry) {
		return entry.getLocation().getName();
	}

	private String formatDate(Date date, Date time) {
		StringBuilder builder = new StringBuilder();
		builder.append(formatDate(date));
		builder.append(" ");
		builder.append(formatTime(time));
		return builder.toString();
	}

	private String formatAssignment(RosterEntry entry) {
		return entry.getAssignment().getName();
	}

	private String formatServiceType(RosterEntry entry) {
		return entry.getServiceType().getName();
	}

	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormat.format(date);
	}

	private String formatTime(Date date) {
		if (date == null) {
			return "";
		}
		return timeFormat.format(date);
	}
}

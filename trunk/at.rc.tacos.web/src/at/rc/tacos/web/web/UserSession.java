package at.rc.tacos.web.web;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.Competence;

/**
 * Session information
 * @author Nechan
 */
public class UserSession 
{
	private Boolean loggedIn;
	private String username;
	private WebClient connection;
	private StaffMember staffMember;
	private Competence competence;
	private Job job;
	private ServiceType serviceType;
	private Location location;

	public Competence getCompetence() {
		return competence;
	}
	public void setCompetence(Competence competence) {
		this.competence = competence;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public ServiceType getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public StaffMember getStaffMember() {
		return staffMember;
	}
	public void setStaffMember(StaffMember staffMember) {
		this.staffMember = staffMember;
	}

	/**
	 * Default constructor
	 */
	public UserSession()
	{
		loggedIn = false;
	}

	/**
	 * Returns whether the user is logged in or not.
	 * @return true if the user is logged in
	 */
	public Boolean getLoggedIn()
	{
		return loggedIn;
	}

	/**
	 * Logs in the current user and stores the information
	 * about the user.
	 * @param loggedIn true if the login was successfully
	 * @param userData the information about the user
	 */
	public void setLoggedIn(Boolean loggedIn,String username,WebClient connection)
	{
		this.loggedIn = loggedIn;
		this.username = username;
		this.connection = connection;
	}

	/**
	 * Returns the username.
	 * @return the username.
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Returns the connection to the server
	 * @return the connection
	 */
	public WebClient getConnection()
	{
		return connection;
	}
}

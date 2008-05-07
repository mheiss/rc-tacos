package at.rc.tacos.web.session;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;

/**
 * Stores user (session) specific values.
 * @author Payer Martin
 * @version 1.0
 */
public class UserSession 
{
	private Boolean loggedIn;
	private String username;
	private WebClient connection;
	private Login loginInfo;
	private FormDefaultValues formDefaultValues;
	private boolean internalSession;
	private List<Competence> competenceList;
	private List<Job> jobList;
	private List<ServiceType> serviceTypeList;
	private List<Location> locationList;
	private List<StaffMember> staffList;
	private List<DayInfoMessage> dayInfoList;

	/**
	 * Default constructor
	 */
	public UserSession()
	{
		loggedIn = false;
		competenceList = new ArrayList<Competence>();
		jobList = new ArrayList<Job>();
		locationList = new ArrayList<Location>();
		staffList = new ArrayList<StaffMember>();
		serviceTypeList = new ArrayList<ServiceType>();
		dayInfoList = new ArrayList<DayInfoMessage>();
	}

	//HELPER METHODS
	public Job getJobById(int id)
	{
		for(Job job:jobList)
		{
			if(job.getId() == id)
				return job;
		}
		//nothing found
		return null;
	}

	public Location getLocationById(int id)
	{
		for(Location loc:locationList)
		{
			if(loc.getId() == id)
				return loc;
		}
		//nothing found
		return null;
	}

	public Competence getCompetenceById(int id)
	{
		for(Competence comp:competenceList)
		{
			if(comp.getId() == id)
				return comp;
		}
		//nothing found
		return null;
	}

	public ServiceType getServiceTypeById(int id)
	{
		for(ServiceType st:serviceTypeList)
		{
			if(st.getId() == id)
				return st;
		}
		//nothing found
		return null;
	}

	public StaffMember getStaffMemberById(int id)
	{
		for(StaffMember sm:staffList)
		{
			if(sm.getStaffMemberId() == id)
				return sm;
		}
		//nothing found
		return null;
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
	public void setLoggedIn(Boolean loggedIn,Login loginInfo,WebClient connection)
	{
		this.loggedIn = loggedIn;
		this.loginInfo = loginInfo;
		this.username = loginInfo.getUsername();
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

	/**
	 * @return the competenceList
	 */
	public List<Competence> getCompetenceList() {
		return competenceList;
	}

	/**
	 * @return the jobList
	 */
	public List<Job> getJobList() {
		return jobList;
	}

	/**
	 * @return the serviceTypeList
	 */
	public List<ServiceType> getServiceTypeList() {
		return serviceTypeList;
	}

	/**
	 * @return the locationList
	 */
	public List<Location> getLocationList() {
		return locationList;
	}

	/**
	 * @return the staffList
	 */
	public List<StaffMember> getStaffList() {
		return staffList;
	}

	/**
	 * Returns the staff member accociated with this user session. <br >
	 * The staffMember contains the personal information about the member
	 * @return the staffMember
	 */
	public StaffMember getStaffMember() 
	{
		return loginInfo.getUserInformation();
	}
	
	/**
	 * Returns the login information and the authorization for this user. <br>
	 * Note that the password is not set in this login info.
	 * @return the login information
	 */
	public Login getLoginInformation()
	{
		return loginInfo;
	}
	
	public void setLoginInformation(Login login) {
		this.loginInfo = login;
	}

	/**
	 * Adds a competence
	 * @param competenceList the competenceList to set
	 */
	public void addCompetence(Competence competence) 
	{
		competenceList.add(competence);
	}

	/**
	 * @param jobList the jobList to set
	 */
	public void addJob(Job job) 
	{
		this.jobList.add(job);
	}

	/**
	 * @param serviceTypeList the serviceTypeList to set
	 */
	public void addServiceType(ServiceType serviceType) 
	{
		serviceTypeList.add(serviceType);
	}

	/**
	 * @param locationList the locationList to set
	 */
	public void addLocation(Location location) 
	{
		locationList.add(location);
	}

	/**
	 * @param staffList the staffList to set
	 */
	public void addStaffMember(StaffMember staffMember) 
	{
		staffList.add(staffMember);
	}

	/**
	 * @return the dayInfoMessageList
	 */
	public List<DayInfoMessage> getDayInfoList() {
		return dayInfoList;
	}

	/**
	 * @param dayInfoList 
	 */
	public void addDayInfo(List<DayInfoMessage> dayInfoList) {
		this.dayInfoList = dayInfoList;
	}

	public FormDefaultValues getFormDefaultValues() {
		return formDefaultValues;
	}

	public void setFormDefaultValues(FormDefaultValues formDefaultValues) {
		this.formDefaultValues = formDefaultValues;
	}

	public boolean isInternalSession() {
		return internalSession;
	}

	public void setInternalSession(boolean internalSession) {
		this.internalSession = internalSession;
	}
}

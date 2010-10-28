package at.redcross.tacos.web.beans;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.dbal.utils.TacosDateUtils;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.combo.DropDownHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;

@KeepAlive
@ManagedBean(name = "rosterStatisticOverviewBean")
public class RosterStatisticOverviewBean extends RosterOverviewBean {

	private static final long serialVersionUID = -1809399594337778364L;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	
	//filter parameters
	//TODO
	protected String locationOfUser = "*";
	protected String systemUserName = "*";
	protected String locationOfRosterEntry = "*";
	protected String month = "*";
	protected String year = "*";
	protected String serviceType = "*";
	
	private String fullName;
	protected List<SystemUser> users;
	protected List<ServiceType> serviceTypes;

	private List<SelectItem> userItems;
	private List<SelectItem> serviceTypeItems;
	
	SimpleDateFormat df = new SimpleDateFormat("MMM");
	
	private long amount;
	


	@Override
	protected Date getPreviousDate(Date date) {
		return DateUtils.addDays(date, -1);
	}

	@Override
	protected Date getNextDate(Date date) {
		return DateUtils.addDays(date, +1);
	}

	@Override
	protected List<RosterDto> getEntries(EntityManager manager, RosterQueryParam params) {
		return RosterDto.fromList(RosterEntryHelper.listByDay(manager, params));
	}

	@Override
	protected ReportRenderParameters getReportParams() {
		ReportRenderParameters params = new ReportRenderParameters();
		params.reportName = "Dienstplan_" + sdf.format(date) + ".pdf";
		params.reportFile = "rosterDayReport.rptdesign";
		params.arguments.put("reportParam", getParamForReport());
		params.arguments.put("reportDate", date);
		return params;
	}
	
	// ---------------------------------
	// Initialization
	// ---------------------------------
	@Override
	protected void init() throws Exception {
		EntityManager manager = null;
		try {
			// query data from the database
			manager = EntityManagerFactory.createEntityManager();
			date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
			locations = LocationHelper.list(manager);
			locationItems = DropDownHelper.convertToItems(locations);
			entries = getEntries(manager, getParamForQuery());
			users = SystemUserHelper.list(manager);
			userItems = DropDownHelper.convertToItems(users);
			serviceTypes = ServiceTypeHelper.list(manager);
			serviceTypeItems = DropDownHelper.convertToItems(serviceTypes);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}
	
	// ---------------------------------
	// Helper methods
	// ---------------------------------
	@Override
	protected RosterQueryParam getParamForQuery() {
		RosterQueryParam param = new RosterQueryParam();
		param.date = date;
		param.locationOfUser = getLocationByName(locationOfUser);
		param.location = getLocationByName(locationOfRosterEntry);
		//param.month = month; --> wie?
		//param.year = year; --> wie?
		param.serviceType = getServiceTypeByName(serviceType);
		//TODO
		return param;
	}
	
	protected SystemUser getUserByName(String systemUserName) {
		if (systemUserName == null || "*".equals(systemUserName)) {
			return null;
		}
		for (SystemUser user : users) {
			fullName = user.getLastName() +" " +user.getFirstName();
			if (fullName.equals(systemUserName)) {
				return user;
			}
		}
		return null;
	}
	
	protected ServiceType getServiceTypeByName(String serviceTypeName) {
		if (serviceTypeName == null || "*".equals(serviceTypeName)) {
			return null;
		}
		for (ServiceType serviceType : serviceTypes) {
			if (serviceTypeName.equals(serviceTypeName)) {
				return serviceType;
			}
		}
		return null;
	}
	
	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setAmount(long amount){
		this.amount = amount;
	}
	
	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getLocationOfUser() {
		return locationOfUser;
	}
	
	public String getSystemUserName() {
		return systemUserName;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	
	public List<SelectItem> getUserItems() {
		return userItems;
	}
	
	public List<SystemUser> getUsers() {
		return users;
	}
	
	public String getLocationOfRosterEntry() {
		return locationOfRosterEntry;
	}
	
	public List<SelectItem> getServiceTypeItems() {
		return serviceTypeItems;
	}
	
	public long getAmount(){
		return amount;
	}
	
	
}

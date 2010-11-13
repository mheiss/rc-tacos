package at.redcross.tacos.web.beans;

import java.text.DateFormatSymbols;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.reporting.ReportRenderer.ReportRenderParameters;
import at.redcross.tacos.web.utils.TacosDateUtils;

@KeepAlive
@ManagedBean(name = "rosterStatisticOverviewBean")
public class RosterStatisticOverviewBean extends RosterOverviewBean {

    private static final long serialVersionUID = -1809399594337778364L;

    // TODO filter parameters
    private String locationOfUser = "*";
    private String systemUserName = "*";
    private String locationOfRosterEntry = "*";
    private String month = "*";
    private String year = "*";
    private String serviceType = "*";

    private String fullName;
    private List<SystemUser> users;
    private List<ServiceType> serviceTypes;

    private List<SelectItem> userItems;
    private List<SelectItem> serviceTypeItems;
    private List<SelectItem> monthItems;
    private List<SelectItem> yearItems;

    private long amount;
    
    protected List<RosterDto> getStatisticEntries(EntityManager manager, RosterQueryParam params) {
        return RosterDto.fromList(RosterEntryHelper.listStatistic(manager, params));
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
            locationItems = SelectableItemHelper.convertToItems(locations);
            entries = getEntries(manager, getParamForQuery());
            users = SystemUserHelper.list(manager);
            userItems = SelectableItemHelper.convertToItems(users);
            serviceTypes = ServiceTypeHelper.list(manager);
            serviceTypeItems = SelectableItemHelper.convertToItems(serviceTypes);
            monthItems = this.createMonthItemEntries();
            yearItems = this.createYearItemEntries();
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

 // ---------------------------------
    // Actions
    // ---------------------------------
    @Override
    public void filterChanged(ActionEvent event) {
        EntityManager manager = null;
        try {
            // provide default value if date is null
            //if (date == null) {
             //   date = TacosDateUtils.getCalendar(System.currentTimeMillis()).getTime();
            //}
            manager = EntityManagerFactory.createEntityManager();
            entries = getStatisticEntries(manager, getParamForStatisticQuery());
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
    
    // ---------------------------------
    // Helper methods
    // ---------------------------------
    protected RosterQueryParam getParamForStatisticQuery() {
        RosterQueryParam param = new RosterQueryParam();
        param.location = getLocationByName(locationOfRosterEntry);
        param.serviceType = getServiceTypeByName(serviceType);
        param.year = Integer.parseInt(year);
        param.month = Integer.parseInt(month);
        //param.systemUserId =
        return param;
    }

    protected SystemUser getUserByName(String systemUserName) {
        if (systemUserName == null || "*".equals(systemUserName)) {
            return null;
        }
        for (SystemUser user : users) {
            fullName = user.getLastName() + " " + user.getFirstName();
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
    
    private List<SelectItem> createMonthItemEntries() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		String[] germanyMonths = new DateFormatSymbols(Locale.GERMANY).getMonths();
		for (int i = 0; i < germanyMonths.length; i++) {
			String germanyMonth = germanyMonths[i];
			//January = 0!
			items.add(new SelectableItem(germanyMonth, i).getItem());
		}
		return items;
	}
    
    private List<SelectItem> createYearItemEntries() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 2010; i <= 2015; i++) {
			items.add(new SelectableItem(String.valueOf(i), i).getItem());
		}
		return items;
	}

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setAmount(long amount) {
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

    public long getAmount() {
        return amount;
    }
    
    public String getMonth(){
    	return month;
    }
    
    public List<SelectItem> getMonthItems(){
    	return monthItems;
    }
    
    public String getYear(){
    	return year;
    }
    
    public List<SelectItem> getYearItems(){
    	return yearItems;
    }


	@Override
	protected List<RosterDto> getEntries(EntityManager manager,
			RosterQueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Date getNextDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Date getPreviousDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected ReportRenderParameters getReportParams() {
		// TODO Auto-generated method stub
		return null;
	}

}

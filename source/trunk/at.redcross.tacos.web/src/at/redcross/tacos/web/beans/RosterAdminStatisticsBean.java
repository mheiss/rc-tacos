package at.redcross.tacos.web.beans;

import java.text.DateFormatSymbols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;

import com.ibm.icu.util.Calendar;

import at.redcross.tacos.dbal.entity.DataState;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
import at.redcross.tacos.dbal.query.RosterStatisticQueryParam;
import at.redcross.tacos.web.beans.dto.RosterStatisticDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "rosterAdminStatisticsBean")
public class RosterAdminStatisticsBean extends PagingBean {

    private static final long serialVersionUID = -1809399594337778364L;

    /** holds the current filter parameters that are selected */
    private RosterStatisticQueryParam queryParam = new RosterStatisticQueryParam();

    /** the search result to display */
    private List<RosterStatisticDto> rosterEntries;

    /** values for the drop down fields */
    private List<SelectItem> userItems;
    private List<SelectItem> serviceTypeItems;
    private List<SelectItem> monthItems;
    private List<SelectItem> yearItems;
    private List<SelectItem> locationItems;
    
    

    // ---------------------------------
    // Initialization
    // ---------------------------------
    @Override
    protected void init() throws Exception {
        EntityManager manager = null;
        try {
            // query data from the database
            manager = EntityManagerFactory.createEntityManager();
            userItems = SelectableItemHelper.convertToItems(SystemUserHelper.list(manager));
            locationItems = SelectableItemHelper.convertToItems(LocationHelper.list(manager));
            serviceTypeItems = SelectableItemHelper.convertToItems(ServiceTypeHelper.list(manager));
            monthItems = createMonthItemEntries();
            yearItems = createYearItemEntries();
            //initialize the previous month and actual year
            Calendar cal = Calendar.getInstance();
            queryParam.month = cal.get(Calendar.MONTH);
            queryParam.year = cal.get(Calendar.YEAR);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public void search(ActionEvent event) {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            List<RosterEntry> rosterList = doSearch(manager, queryParam);
            // create a mapping between the user and the entries
            Map<SystemUser, List<RosterEntry>> mapping = new HashMap<SystemUser, List<RosterEntry>>();
            for (RosterEntry entry : rosterList) {
                SystemUser user = entry.getSystemUser();
                List<RosterEntry> list = mapping.get(user);
                if (list == null) {
                    list = new ArrayList<RosterEntry>();
                    mapping.put(user, list);
                }
                list.add(entry);
            }
            // create the resulting list
            rosterEntries = new ArrayList<RosterStatisticDto>();
            for (Map.Entry<SystemUser, List<RosterEntry>> entry : mapping.entrySet()) {
                RosterStatisticDto dto = new RosterStatisticDto(entry.getKey());
                dto.addEntires(entry.getValue());
                rosterEntries.add(dto);
            }
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Fehler beim suchen der Dienstplaneinträge");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    /** Returns a list of all available entries for the month filter */
    private List<SelectItem> createMonthItemEntries() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        String[] germanyMonths = new DateFormatSymbols(Locale.GERMANY).getMonths();
        for (int i = 0; i < germanyMonths.length; i++) {
            String germanyMonth = germanyMonths[i];
            items.add(new SelectableItem(germanyMonth, i + 1).getItem());
        }
        return items;
    }

    /** Returns a list of all available entries for the year filter */
    private List<SelectItem> createYearItemEntries() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        for (int i = 2010; i <= 2015; i++) {
            items.add(new SelectableItem(String.valueOf(i), i).getItem());
        }
        return items;
    }

    /** Performs the search using the given parameters and manager */
    private List<RosterEntry> doSearch(EntityManager manager, RosterStatisticQueryParam param) {
        // the HQL query
        StringBuilder builder = new StringBuilder();
        builder.append(" FROM RosterEntry entry ");
        builder.append(" WHERE ");
        builder.append(" ( ");
        builder.append("    ( ");
        builder.append("        month(entry.plannedStartDateTime) = :month ");
        builder.append("        OR  ");
        builder.append("        month(entry.plannedEndDateTime) = :month ");
        builder.append("    ) ");
        builder.append("    AND ");
        builder.append("    ( ");
        builder.append("        year(entry.plannedStartDateTime) = :year ");
        builder.append("        AND  ");
        builder.append("        year(entry.plannedEndDateTime) = :year ");
        builder.append("    ) ");
        builder.append(" ) ");
        builder.append(" AND entry.state IN (:states)");
        if (param.location != null) {
            builder.append(" AND entry.location.id = :locationId");
        }
        if (param.homeLocation != null) {
            builder.append(" AND entry.systemUser.location.id = :homeLocationId");
        }
        if (param.systemUser != null) {
            builder.append(" AND entry.systemUser.id = :systemUserId");
        }
        if (param.serviceType != null) {
            builder.append(" AND entry.serviceType.id = :serviceTypeId");
        }

        // set the parameters for the date range
        TypedQuery<RosterEntry> query = manager.createQuery(builder.toString(), RosterEntry.class);
        query.setParameter("month", param.month);
        query.setParameter("year", param.year);

        // filter by location of the entry
        if (param.location != null) {
            query.setParameter("locationId", param.location.getId());
        }
        // filter by location of the user
        if (param.homeLocation != null) {
            query.setParameter("homeLocationId", param.homeLocation.getId());
        }
        // filter by the system user
        if (param.systemUser != null) {
            query.setParameter("systemUserId", param.systemUser.getId());
        }
        // filter by the service type
        if (param.serviceType != null) {
            query.setParameter("serviceTypeId", param.serviceType.getId());
        }
        // filter by state
        query.setParameter("states", new ArrayList<DataState>(Arrays.asList(DataState.NORMAL)));
        return query.getResultList();
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public RosterQueryParam getQueryParam() {
        return queryParam;
    }

    public List<RosterStatisticDto> getRosterEntries() {
        return rosterEntries;
    }

    public List<SelectItem> getUserItems() {
        return userItems;
    }

    public List<SelectItem> getLocationItems() {
        return locationItems;
    }

    public List<SelectItem> getServiceTypeItems() {
        return serviceTypeItems;
    }

    public List<SelectItem> getMonthItems() {
        return monthItems;
    }

    public List<SelectItem> getYearItems() {
        return yearItems;
    }
}

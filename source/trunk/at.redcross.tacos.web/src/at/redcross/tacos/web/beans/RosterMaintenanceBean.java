package at.redcross.tacos.web.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.EntityUtils;
import at.redcross.tacos.web.beans.bl.FilterRuleRegistry;
import at.redcross.tacos.web.beans.bl.IFilterRule;
import at.redcross.tacos.web.beans.dto.RosterDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.model.SelectableItemHelper;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.utils.TacosDateUtils;

@KeepAlive
@ManagedBean(name = "rosterMaintenanceBean")
public class RosterMaintenanceBean extends BaseBean {

    private static final long serialVersionUID = 3440196753805921232L;

    // the entry to create or edit
    private long rosterId = -1;
    private RosterEntry rosterEntry;

    // start date and time
    private Date entryStartDate;
    private Date entryStartTime;

    // end date and time
    private Date entryEndDate;
    private Date entryEndTime;

    // holds whether or not some values are filtered
    private boolean filtered;

    // the suggested values for the drop down boxes
    private List<SelectItem> userItems;
    private List<SelectItem> locationItems;
    private List<SelectItem> serviceTypeItems;
    private List<SelectItem> assignmentItems;
    private List<SelectItem> timeItems;

    // the maximum length of the description
    private int maxDescLength = -1;

    @Override
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rosterEntry = loadfromDatabase(manager, rosterId);
            RosterDto dto = new RosterDto(rosterEntry);
            if (isBackedUp() && !dto.isEditEnabled()) {
                FacesUtils.redirectAccessDenied("Entry '" + rosterEntry + "' cannot be edited");
                return;
            }
            // request default data from the database
            entryStartDate = entryStartTime = rosterEntry.getPlannedStartDateTime();
            entryEndDate = entryEndTime = rosterEntry.getPlannedEndDateTime();
            userItems = SelectableItemHelper.convertToItems(SystemUserHelper.list(manager));
            locationItems = SelectableItemHelper.convertToItems(LocationHelper.list(manager));
            serviceTypeItems = SelectableItemHelper.convertToItems(ServiceTypeHelper.list(manager));
            assignmentItems = SelectableItemHelper.convertToItems(AssignmentHelper.list(manager));
            timeItems = createTimeItemEntries();
            maxDescLength = EntityUtils.getColumnLength(RosterEntry.class, "notes");

            // apply any custom filters
            FilterRuleRegistry registry = FilterRuleRegistry.getInstance();
            for (IFilterRule rule : registry.getRules()) {
                boolean resultAffected = rule.applyFilter(manager, this);
                if (resultAffected) {
                    filtered = true;
                }
            }
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Business methods
    // ---------------------------------
    public String persist() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rosterEntry.setPlannedStartDateTime(TacosDateUtils.mergeDateAndTime(entryStartDate,
                    entryStartTime).getTime());
            rosterEntry.setPlannedEndDateTime(TacosDateUtils.mergeDateAndTime(entryEndDate,
                    entryEndTime).getTime());
            if (!isBackedUp()) {
                manager.persist(rosterEntry);
            } else {
                manager.merge(rosterEntry);
            }
            EntityManagerHelper.commit(manager);
            return FacesUtils.pretty("roster-dayOverview");
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gespeichert werden");
            return null;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public String revert() {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, rosterEntry.getId());
            return FacesUtils.pretty("roster-editMaintenance");
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht zurückgesetzt werden");
            return null;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    private RosterEntry loadfromDatabase(EntityManager manager, long id) {
        RosterEntry rosterEntry = manager.find(RosterEntry.class, id);
        if (rosterEntry != null) {
            return rosterEntry;
        }
        rosterId = -1;
        return new RosterEntry();
    }

    private List<SelectItem> createTimeItemEntries() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < 24; i++) {
            String time = i + ":00";
            try {
                Date date = sdf.parse(time);
                items.add(new SelectableItem(time, date).getItem());
            } catch (ParseException e) {
                // just ignore
            }
        }
        return items;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public long getRosterId() {
        return rosterId;
    }

    public void setRosterId(long rosterId) {
        this.rosterId = rosterId;
    }

    public void setEntryStartDate(Date entryStartDate) {
        this.entryStartDate = entryStartDate;
    }

    public void setEntryStartTime(Date entryStartTime) {
        this.entryStartTime = entryStartTime;
    }

    public void setEntryEndDate(Date entryEndDate) {
        this.entryEndDate = entryEndDate;
    }

    public void setEntryEndTime(Date entryEndTime) {
        this.entryEndTime = entryEndTime;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public boolean isBackedUp() {
        return rosterId > 0;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public int getMaxDescLength() {
        return maxDescLength;
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

    public List<SelectItem> getAssignmentItems() {
        return assignmentItems;
    }

    public List<SelectItem> getTimeItems() {
        return timeItems;
    }

    public RosterEntry getRosterEntry() {
        return rosterEntry;
    }

    public Date getEntryStartDate() {
        return entryStartDate;
    }

    public Date getEntryStartTime() {
        return entryStartTime;
    }

    public Date getEntryEndDate() {
        return entryEndDate;
    }

    public Date getEntryEndTime() {
        return entryEndTime;
    }
}

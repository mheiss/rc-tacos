package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;

import at.redcross.tacos.dbal.entity.Assignment;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.utils.DateUtils;

@KeepAlive
@ManagedBean(name = "rosterEntryBean")
public class RosterEntryBean extends BaseBean {

    private static final long serialVersionUID = 3440196753805921232L;

    // the entry to create
    private long rosterId = -1;
    private RosterEntry rosterEntry;

    // the suggested values for the UI
    private String selectedUser;
    private List<SystemUser> users;
    private List<SelectItem> userItems;

    private String selectedLocation;
    private List<Location> locations;
    private List<SelectItem> locationItems;

    private String selectedServiceType;
    private List<ServiceType> serviceTypes;
    private List<SelectItem> serviceTypeItems;

    private String selectedAssignment;
    private List<Assignment> assignments;
    private List<SelectItem> assignmentItems;

    // start and end time
    private Date plannedStartTime;
    private Date plannedStartDate;

    private Date plannedEndTime;
    private Date plannedEndDate;

    @Override
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            rosterEntry = loadRosterEntry(manager, rosterId);

            users = manager.createQuery("from SystemUser", SystemUser.class).getResultList();
            locations = manager.createQuery("from Location", Location.class).getResultList();
            assignments = manager.createQuery("from Assignment", Assignment.class).getResultList();
            serviceTypes = manager.createQuery("from ServiceType", ServiceType.class)
                    .getResultList();

            userItems = new ArrayList<SelectItem>();
            for (SystemUser user : users) {
                userItems.add(new SelectItem(user.getLastName() + " " + user.getFirstName()));
            }
            locationItems = new ArrayList<SelectItem>();
            for (Location location : locations) {
                locationItems.add(new SelectItem(location.getName()));
            }
            serviceTypeItems = new ArrayList<SelectItem>();
            for (ServiceType type : serviceTypes) {
                serviceTypeItems.add(new SelectItem(type.getName()));
            }
            assignmentItems = new ArrayList<SelectItem>();
            for (Assignment assignment : assignments) {
                assignmentItems.add(new SelectItem(assignment.getId()));
            }
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public String persistUser() {
        // set the missing attributes
        rosterEntry.setSystemUser(lookupUser(selectedUser));
        rosterEntry.setLocation(lookupLocation(selectedLocation));
        rosterEntry.setAssignment(lookupAssignment(selectedAssignment));
        rosterEntry.setServiceType(lookupServiceType(selectedServiceType));

        // merge the separate date and time values
        rosterEntry.setPlannedStart(DateUtils.mergeDateAndTime(plannedStartDate, plannedStartTime));
        rosterEntry.setPlannedEnd(DateUtils.mergeDateAndTime(plannedEndDate, plannedEndTime));

        // write to the database
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            if (isNew()) {
                manager.persist(rosterEntry);
            }
            else {
                manager.merge(rosterEntry);
            }
            EntityManagerHelper.commit(manager);
            return "pretty:roster-dayView";
        }
        catch (Exception ex) {
            FacesUtils.addErrorMessage("Der Dienstplaneintrag konnte nicht gespeichert werden");
            return null;
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    private RosterEntry loadRosterEntry(EntityManager manager, long id) {
        rosterEntry = manager.find(RosterEntry.class, id);
        if (rosterEntry == null) {
            rosterId = -1;
            return new RosterEntry();
        }
        SystemUser systemUser = rosterEntry.getSystemUser();
        selectedUser = systemUser.getLastName() + " " + systemUser.getFirstName();
        selectedAssignment = rosterEntry.getAssignment().getId();
        selectedLocation = rosterEntry.getLocation().getName();
        selectedServiceType = rosterEntry.getServiceType().getName();
        plannedStartDate = rosterEntry.getPlannedStart();
        plannedStartTime = rosterEntry.getPlannedStart();
        plannedEndDate = rosterEntry.getPlannedEnd();
        plannedEndTime = rosterEntry.getPlannedEnd();
        return rosterEntry;
    }

    private SystemUser lookupUser(Object value) {
        for (SystemUser user : users) {
            String userId = user.getLastName() + " " + user.getFirstName();
            if (userId.equals(value)) {
                return user;
            }
        }
        return null;
    }

    private Location lookupLocation(Object value) {
        for (Location location : locations) {
            String name = location.getName();
            if (name.equals(value)) {
                return location;
            }
        }
        return null;
    }

    private Assignment lookupAssignment(Object value) {
        for (Assignment assignment : assignments) {
            String name = assignment.getId();
            if (name.equals(value)) {
                return assignment;
            }

        }
        return null;
    }

    private ServiceType lookupServiceType(Object value) {
        for (ServiceType serviceType : serviceTypes) {
            String name = serviceType.getName();
            if (name.equals(value)) {
                return serviceType;
            }
        }
        return null;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setRosterId(long rosterId) {
        this.rosterId = rosterId;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public void setSelectedServiceType(String selectedServiceType) {
        this.selectedServiceType = selectedServiceType;
    }

    public void setSelectedAssignment(String selectedAssignment) {
        this.selectedAssignment = selectedAssignment;
    }

    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public void setPlannedEndTime(Date plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public boolean isNew() {
        return rosterId == -1;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public String getSelectedServiceType() {
        return selectedServiceType;
    }

    public String getSelectedAssignment() {
        return selectedAssignment;
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

    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public Date getPlannedEndTime() {
        return plannedEndTime;
    }

    public RosterEntry getRosterEntry() {
        return rosterEntry;
    }
}

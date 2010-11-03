package at.redcross.tacos.web.beans;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import at.redcross.tacos.dbal.entity.Assignment;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.ServiceType;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.beans.dto.RosterParserEntryDto;
import at.redcross.tacos.web.beans.dto.RosterParserMetadataDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.parser.RosterParser;
import at.redcross.tacos.web.parser.RosterParserEntry;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;

@KeepAlive
@ManagedBean(name = "rosterImportBean")
public class RosterImportBean extends BaseBean {

    private static final long serialVersionUID = -4428380184844072809L;

    private static final Log logger = LogFactory.getLog(RosterImportBean.class);

    /** the list of raw entries as parsed in the file */
    private Collection<RosterParserEntryDto> entryList;

    /** the list of new entities */
    private Collection<GenericDto<RosterEntry>> newRosterList;

    /** the list of duplicate entries */
    private Collection<GenericDto<RosterEntry>> duplicateRosterList;

    /** the list of error entries */
    private Collection<RosterParserEntryDto> errorEntryList;

    /** the metadata as parsed in the file */
    private RosterParserMetadataDto metadata;

    /** the item that was uploaded */
    private UploadItem entryItem;

    /** Mapping between Location and LocationName */
    private Map<String, Location> locationCache;

    /** Mapping between Location and LocationName */
    private Map<String, SystemUser> systemUserCache;

    /** Mapping between Location and LocationName */
    private Map<String, ServiceType> serviceTypeCache;

    /** Mapping between Location and LocationName */
    private Map<String, Assignment> assignmentCache;

    /** progress indicator: current */
    private int progressIndicatorCurrent;

    /** progress indicator: maximum */
    private int progressIndicatorMax;

    @Override
    protected void init() throws Exception {
        // initialize resulting list
        entryList = new ArrayList<RosterParserEntryDto>();
        newRosterList = new ArrayList<GenericDto<RosterEntry>>();
        duplicateRosterList = new ArrayList<GenericDto<RosterEntry>>();
        errorEntryList = new ArrayList<RosterParserEntryDto>();
        // initialize caching maps
        locationCache = new HashMap<String, Location>();
        systemUserCache = new HashMap<String, SystemUser>();
        serviceTypeCache = new HashMap<String, ServiceType>();
        assignmentCache = new HashMap<String, Assignment>();
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void uploadEntries(UploadEvent event) throws Exception {
        // get the uploaded file
        entryItem = event.getUploadItem();
        File file = entryItem.getFile();
        logger.info("New roster file uploaded ('" + entryItem.getFileName() + "')");
        // parse the new file
        RosterParser parser = new RosterParser(file);
        metadata = new RosterParserMetadataDto(parser.parsetMetadata());
        entryList = filterRawEntries(parser.parse());
        logger.info("Parsed '" + entryList.size() + "' entries from the file");
    }

    public void syncronizeEntries(ActionEvent event) {
        /** reset the progress indicator */
        progressIndicatorCurrent = 0;
        progressIndicatorMax = entryList.size();

        /** clear the old result */
        newRosterList.clear();
        duplicateRosterList.clear();
        errorEntryList.clear();

        /** Process each entry in the list */
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            for (RosterParserEntryDto entryDto : entryList) {
                try {
                    progressIndicatorCurrent++;
                    processRawEntry(manager, entryDto);
                }
                catch (Exception ex) {
                    errorEntryList.add(entryDto);
                    entryDto.setMessage("Fehlerhafte oder unvollständige Daten");
                    logger.error("Failed to prcess record '" + entryDto + "'", ex);
                }
            }
        }
        catch (Exception ex) {
            logger.fatal("Failed to syncronize entries with the database.", ex);
            FacesUtils.addErrorMessage("Interner fehler beim Syncronisieren der Daten");
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void persistEntries(ActionEvent event) {
        EntityManager manager = null;
        try {
            /** Persist all records in the database */
            manager = EntityManagerFactory.createEntityManager();
            for (GenericDto<RosterEntry> entry : newRosterList) {
                if (entry.getState() == EntryState.SYNC) {
                    continue;
                }
                manager.persist(entry.getEntity());
            }
            EntityManagerHelper.commit(manager);

            /** Mark the entries as saved */
            DtoHelper.filter(newRosterList);
        }
        catch (Exception ex) {
            logger.fatal("Failed to syncronize entries with the database.", ex);
            FacesUtils.addErrorMessage("Interner fehler beim Syncronisieren der Daten");
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    /** Process a single raw data entry */
    protected void processRawEntry(EntityManager manager, RosterParserEntryDto rawEntry) throws Exception {
        // determine the corresponding entities
        Location location = queryLocation(manager, rawEntry.getLocationName());
        if (location == null) {
            rawEntry.setMessage("Dienststelle nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        SystemUser user = querySystemUser(manager, rawEntry.getPersonalNumber());
        if (user == null) {
            rawEntry.setMessage("Mitarbeiter nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        ServiceType serviceType = queryServiceType(manager, rawEntry.getServiceTypeName());
        if (serviceType == null) {
            rawEntry.setMessage("Dienstverhältnis nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        Assignment assignment = queryAssignment(manager, rawEntry.getAssignmentName());
        if (assignment == null) {
            rawEntry.setMessage("Verwendung nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        // now we can build a new entry
        RosterEntry entry = new RosterEntry();
        entry.setSystemUser(user);
        entry.setServiceType(serviceType);
        entry.setLocation(location);
        entry.setAssignment(assignment);

        // if start or end is missing then this is a failure
        if (StringUtils.isEmpty(rawEntry.getStartTime())) {
            rawEntry.setMessage("Startzeitpunkt undefiniert");
            errorEntryList.add(rawEntry);
            return;
        }
        if (StringUtils.isEmpty(rawEntry.getEndTime())) {
            rawEntry.setMessage("Endzeitpunkt undefiniert");
            errorEntryList.add(rawEntry);
            return;
        }
        // parse the time information
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        entry.setPlannedStartTime(timeFormat.parse(rawEntry.getStartTime()));
        entry.setPlannedEndTime(timeFormat.parse(rawEntry.getEndTime()));

        // if the date is valid we also apply it
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        Date startDate = dateFormat.parse(rawEntry.getDay() + "_" + metadata.getMonthAndYear());

        // check if the entry spans multiple days
        Date endDate = new Date(startDate.getTime());
        if (entry.getPlannedStartTime().after(entry.getPlannedEndTime())) {
            endDate = DateUtils.addDays(endDate, 1);
        }
        entry.setPlannedStartDate(startDate);
        entry.setPlannedEndDate(endDate);

        // successfully completed the new entry :)
        newRosterList.add(new GenericDto<RosterEntry>(entry));
    }

    /** Returns the location for the given name */
    protected Location queryLocation(EntityManager manager, String locationName) {
        // direct cache hit?
        if (locationCache.containsKey(locationName)) {
            return locationCache.get(locationName);
        }
        // query entry in the database
        Location location = LocationHelper.getByName(manager, locationName);
        locationCache.put(locationName, location);
        return location;
    }

    /** Returns the system user for the given personal number */
    protected SystemUser querySystemUser(EntityManager manager, String personalNumber) {
        // direct cache hit?
        if (systemUserCache.containsKey(personalNumber)) {
            return systemUserCache.get(personalNumber);
        }
        // query entry in the database
        SystemUser systemUser = SystemUserHelper.getByPersonalNumber(manager, personalNumber);
        systemUserCache.put(personalNumber, systemUser);
        return systemUser;
    }

    /** Returns the service type for the name */
    protected ServiceType queryServiceType(EntityManager manager, String serviceTypeName) {
        // direct cache hit?
        if (serviceTypeCache.containsKey(serviceTypeName)) {
            return serviceTypeCache.get(serviceTypeName);
        }
        // query entry in the database
        ServiceType serviceType = ServiceTypeHelper.getByName(manager, serviceTypeName);
        serviceTypeCache.put(serviceTypeName, serviceType);
        return serviceType;
    }

    /** Returns the system user for the given personal number */
    protected Assignment queryAssignment(EntityManager manager, String assignmentName) {
        // direct cache hit?
        if (assignmentCache.containsKey(assignmentName)) {
            return assignmentCache.get(assignmentName);
        }
        // query entry in the database
        Assignment assignment = AssignmentHelper.getByName(manager, assignmentName);
        assignmentCache.put(assignmentName, assignment);
        return assignment;
    }

    /** Process the raw imported data */
    protected Collection<RosterParserEntryDto> filterRawEntries(Collection<RosterParserEntry> rawEntries) {
        Collection<RosterParserEntryDto> entryList = new ArrayList<RosterParserEntryDto>();
        for (RosterParserEntry entry : rawEntries) {
            // filter duplicate entries out of the list
            RosterParserEntryDto dtoEntry = new RosterParserEntryDto(entry);
            if (entryList.contains(dtoEntry)) {
                continue;
            }
            // filter entries that does not contain a start and end time
            if (StringUtils.isEmpty(entry.startTime) && StringUtils.isEmpty(entry.endTime)) {
                continue;
            }
            entryList.add(dtoEntry);
        }
        return entryList;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public UploadItem getEntryItem() {
        return entryItem;
    }

    public int getProgressIndicatorCurrent() {
        return progressIndicatorCurrent;
    }

    public int getProgressIndicatorMax() {
        return progressIndicatorMax;
    }

    public Collection<RosterParserEntryDto> getEntryList() {
        return entryList;
    }

    public Collection<RosterParserEntryDto> getErrorEntryList() {
        return errorEntryList;
    }

    public Collection<GenericDto<RosterEntry>> getNewRosterList() {
        return newRosterList;
    }

    public Collection<GenericDto<RosterEntry>> getDuplicateRosterList() {
        return duplicateRosterList;
    }

    public RosterParserMetadataDto getMetadata() {
        return metadata;
    }
}

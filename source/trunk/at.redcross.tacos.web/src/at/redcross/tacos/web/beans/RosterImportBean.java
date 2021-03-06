package at.redcross.tacos.web.beans;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import at.redcross.tacos.dbal.helper.RosterEntryHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.query.RosterQueryParam;
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
public class RosterImportBean extends PagingBean {

    private static final long serialVersionUID = -4428380184844072809L;

    private static final Log logger = LogFactory.getLog(RosterImportBean.class);

    /** the list of raw entries as parsed in the file */
    private Collection<RosterParserEntryDto> entryList;

    /** the list of new entities */
    private Collection<GenericDto<RosterEntry>> newRosterList;

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
        errorEntryList.clear();

        // check if we can parse the presented date
        Date date = parseDateString(metadata.getMonthAndYear(), new SimpleDateFormat("MM_yyyy"));
        if (date == null) {
            FacesUtils.addErrorMessage("Das Datum des Dienstplanes konnte nicht erkannt werden");
            return;
        }

        /** Process each entry in the list */
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            // convert a parsed entry to a entity
            for (RosterParserEntryDto entryDto : entryList) {
                progressIndicatorCurrent++;
                try {
                    convertParsedEntry(manager, entryDto);
                } catch (Exception ex) {
                    errorEntryList.add(entryDto);
                    entryDto.addMessage(null, "Fehlerhafte oder unvollständige Daten");
                    logger.error("Failed to process record '" + entryDto + "'", ex);
                }
            }

            // query existing entries for this month
            RosterQueryParam param = new RosterQueryParam();
            param.startDate = DateUtils.setDays(date, 1);
            param.endDate = DateUtils.addMonths(date, 1);

            // filter duplicate entries
            List<RosterEntry> existingEntries = RosterEntryHelper.list(manager, param);
            Iterator<GenericDto<RosterEntry>> iter = newRosterList.iterator();
            while (iter.hasNext()) {
                GenericDto<RosterEntry> dto = iter.next();
                RosterEntry lhs = dto.getEntity();
                for (RosterEntry rhs : existingEntries) {
                    if (RosterEntryHelper.isSameEntity(lhs, rhs)) {
                        dto.setState(EntryState.SYNC);
                    }
                }
            }
        } catch (Exception ex) {
            logger.fatal("Failed to syncronize entries with the database.", ex);
            FacesUtils.addErrorMessage("Interner fehler beim Syncronisieren der Daten");
        } finally {
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
            DtoHelper.filter(newRosterList);
        } catch (Exception ex) {
            logger.fatal("Failed to syncronize entries with the database.", ex);
            FacesUtils.addErrorMessage("Interner fehler beim Syncronisieren der Daten");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    // ---------------------------------
    // Helper methods
    // ---------------------------------
    /** Process a single raw data entry */
    protected void convertParsedEntry(EntityManager manager, RosterParserEntryDto rawEntry) throws Exception {
        // determine the corresponding entities
        Location location = queryLocation(manager, rawEntry.getLocationName());
        if (location == null) {
            rawEntry.addMessage("location", "Dienststelle nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        SystemUser user = querySystemUser(manager, rawEntry.getPersonalNumber());
        if (user == null) {
            rawEntry.addMessage("systemUser", "Mitarbeiter nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        ServiceType serviceType = queryServiceType(manager, rawEntry.getServiceTypeName());
        if (serviceType == null) {
            rawEntry.addMessage("serviceType", "Dienstverhältnis nicht vorhanden");
            errorEntryList.add(rawEntry);
            return;
        }

        Assignment assignment = queryAssignment(manager, rawEntry.getAssignmentName());
        if (assignment == null) {
            rawEntry.addMessage("assignment", "Verwendung nicht vorhanden");
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
            rawEntry.addMessage("startTime", "Startzeitpunkt undefiniert");
            errorEntryList.add(rawEntry);
            return;
        }
        if (StringUtils.isEmpty(rawEntry.getEndTime())) {
            rawEntry.addMessage("endTime", "Endzeitpunkt undefiniert");
            errorEntryList.add(rawEntry);
            return;
        }
        // parse the date and time information
        String dateString = rawEntry.getDay() + "_" + metadata.getMonthAndYear();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd_MM_yyyy HH:mm");

        String startDateTimeString = dateString + " " + rawEntry.getStartTime();
        Date startDateTime = dateTimeFormat.parse(startDateTimeString);

        String endDateTimeString = dateString + " " + rawEntry.getEndTime();
        Date endDateTime = dateTimeFormat.parse(endDateTimeString);

        // check if the entry spans multiple days
        if (startDateTime.after(endDateTime)) {
            endDateTime = DateUtils.addDays(endDateTime, 1);
        }
        entry.setPlannedStartDateTime(startDateTime);
        entry.setPlannedEndDateTime(endDateTime);

        // successfully completed the new entry :)
        GenericDto<RosterEntry> newEntry = new GenericDto<RosterEntry>(entry);
        newEntry.setState(EntryState.NEW);
        newRosterList.add(newEntry);
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

    /** Parses and returns the given string using the given format */
    protected Date parseDateString(String source, DateFormat format) {
        try {
            return format.parse(source);
        } catch (Exception ex) {
            return null;
        }
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

    public RosterParserMetadataDto getMetadata() {
        return metadata;
    }
}

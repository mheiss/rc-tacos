package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;
import java.util.List;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.SystemUser;

public class RosterStatisticDto {

    /** the system user of the entry */
    private final SystemUser systemUser;

    /** the entries of the user */
    private final List<RosterEntry> entryList;

    /** the total amount of hours */
    private double amount;

    /**
     * Creates a new statistic entry for the given user
     */
    public RosterStatisticDto(SystemUser systemUser) {
        this.systemUser = systemUser;
        this.entryList = new ArrayList<RosterEntry>();
    }

    /**
     * Filters the given list and adds only the entries that are matching the
     * user
     * 
     * @param entries
     *            the entries to add
     */
    public void addEntires(List<RosterEntry> entries) {
        for (RosterEntry entry : entries) {
            entryList.add(entry);
            amount += 1;
        }
    }

    public List<RosterEntry> getEntryList() {
        return entryList;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public double getAmount() {
        return amount;
    }
}

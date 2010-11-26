package at.redcross.tacos.web.beans.dto;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;

public class RosterStatisticDto {

    /** the system user of the entry */
    private final SystemUser systemUser;

    /** the entries of the user */
    private final List<RosterStatisticEntry> entryList;

    /** the total amount of hours */
    private double amount;

    /**
     * Creates a new statistic entry for the given user
     */
    public RosterStatisticDto(SystemUser systemUser) {
        this.systemUser = systemUser;
        this.entryList = new ArrayList<RosterStatisticEntry>();
    }

    /**
     * Adds all roster entries to this statistic DTO. The amount of the all
     * passed roster entries is automatically added to the sum of the amount.
     * 
     * @param entries
     *            the entries to add
     */
    public void addEntires(List<RosterEntry> entries) {
        for (RosterEntry entry : entries) {
            RosterStatisticEntry statisticEntry = new RosterStatisticEntry(entry);
            entryList.add(statisticEntry);
            amount += statisticEntry.getAmount();
        }
        // rounding is done by using a helper
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(this.amount));
        amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * Returns whether or not the current authenticated user can change the user
     * The following restrictions are considered:
     * <ul>
     * <li>Principal must have the permission of the statistic administrator</li>
     * </ul>
     */
    public boolean isStatisticAdminAllowed() {
        // editing is allowed for principals with permission
        if (FacesUtils.lookupBean(WebPermissionBean.class).isAuthorizedToViewAdminStatistic()) {
            return true;
        }
        // edit denied
        return false;
    }

    public List<RosterStatisticEntry> getEntryList() {
        return entryList;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public double getAmount() {
        return amount;
    }
}
package at.redcross.tacos.web.beans.dto;

import at.redcross.tacos.dbal.entity.RosterEntry;

public class RosterStatisticEntry {

    /** the wrapped entry */
    private final RosterEntry entity;

    /** the amount of hours */
    private final double amount;

    /**
     * Creates a new statistic entry for the given entry
     */
    public RosterStatisticEntry(RosterEntry entity) {
        this.entity = entity;
        this.amount = 1;
    }

    public RosterEntry getEntity() {
        return entity;
    }

    public double getAmount() {
        return amount;
    }
}

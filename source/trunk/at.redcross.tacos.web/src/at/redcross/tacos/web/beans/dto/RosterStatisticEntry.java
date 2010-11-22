package at.redcross.tacos.web.beans.dto;

import java.math.BigDecimal;
import java.util.Date;

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
        this.amount = calcAmount(entity);
    }

    private double calcAmount(RosterEntry entry) {
        // if no end or start is set then nothing can be done
        Date start = entry.getRealStartDateTime();
        Date end = entry.getRealEndDateTime();
        if (start == null || end == null) {
            return 0;
        }
        // we use a helper object to determine the amount
        long duration = (end.getTime() - start.getTime()) / 1000 / 3600;
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(duration));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public RosterEntry getEntity() {
        return entity;
    }

    public double getAmount() {
        return amount;
    }
}

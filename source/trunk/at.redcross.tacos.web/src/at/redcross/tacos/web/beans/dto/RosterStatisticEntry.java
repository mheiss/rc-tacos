package at.redcross.tacos.web.beans.dto;


import com.ibm.icu.util.Calendar;
import at.redcross.tacos.dbal.entity.RosterEntry;

public class RosterStatisticEntry {

    /** the wrapped entry */
    private final RosterEntry entity;

    /** the amount of hours */
    private final double amount;
    
    private long start;
    private long end;
    private double diff;
    /**
     * Creates a new statistic entry for the given entry
     */
    public RosterStatisticEntry(RosterEntry entity) {
    	 Calendar cal = Calendar.getInstance();
    	 start = 0;
    	 end = 0;
    	 if (entity.getRealStartDateTime() != null){
    		 cal.setTime(entity.getRealStartDateTime());
    		 start = cal.getTimeInMillis();
    	 }

         if(entity.getRealEndDateTime() != null){
        	 cal.setTime(entity.getRealEndDateTime());
        	 end = cal.getTimeInMillis();
         }

        this.entity = entity;
        diff = (end - start);
        //the amount is <0 if the entry has a real start time but no end time
        if (diff > 0){
        	//round
        	double tmp = 0;
        	tmp = diff/1000/3600;
        	tmp = Math.round(tmp*100);
        	this.amount = tmp/100;
        }
        else{
        	this.amount = 0;
        }
    }

    public RosterEntry getEntity() {
        return entity;
    }

    public double getAmount() {
        return amount;
    }
}

package at.rc.tacos.web.container;

/**
 * Roster Month Stat
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthStat {
	private long realDurationWeighted;
	private long realDuration;
	private long plannedDurationWeighted;
	private long plannedDuration;
	public RosterMonthStat() {
		realDurationWeighted = 0;
		realDuration = 0;
		plannedDurationWeighted = 0;
		plannedDuration = 0;
	}

	public long getRealDurationWeighted() {
		return realDurationWeighted;
	}
	public void setRealDurationWeighted(long realDurationWeighted) {
		this.realDurationWeighted = realDurationWeighted;
	}
	public long getRealDuration() {
		return realDuration;
	}
	public void setRealDuration(long realDuration) {
		this.realDuration = realDuration;
	}
	public long getPlannedDurationWeighted() {
		return plannedDurationWeighted;
	}
	public void setPlannedDurationWeighted(long plannedDurationWeighted) {
		this.plannedDurationWeighted = plannedDurationWeighted;
	}
	public long getPlannedDuration() {
		return plannedDuration;
	}
	public void setPlannedDuration(long plannedDuration) {
		this.plannedDuration = plannedDuration;
	}
	
	public int getRealDurationHours() {
		return (int)realDuration/(1000*60*60);
	}
	
	public int getRealDurationMinutes() {
		return (int)realDuration%(1000*60*60);
	}
	
	public int getRealDurationWeightedHours() {
		return (int)realDurationWeighted/(1000*60*60);
	}
	
	public int getRealDurationWeightedMinutes() {
		return (int)realDurationWeighted%(1000*60*60);
	}
	
	public int getPlannedDurationHours() {
		return (int)plannedDuration/(1000*60*60);
	}
	
	public int getPlannedDurationMinutes() {
		return (int)plannedDuration%(1000*60*60);
	}
	
	public int getPlannedDurationWeightedHours() {
		return (int)plannedDurationWeighted/(1000*60*60);
	}
	
	public int getPlannedDurationWeightedMinutes() {
		return (int)plannedDurationWeighted%(1000*60*60);
	}
	
	public void addPlannedDuration(long plannedDuration) {
		this.plannedDuration = this.plannedDuration + plannedDuration;
	}
	
	public void addPlannedDurationWeighted(long plannedDurationWeighted) {
		this.plannedDurationWeighted = this.plannedDurationWeighted + plannedDurationWeighted;
	}
	
	public void addRealDuration(long realDuration) {
		this.realDuration = this.realDuration + realDuration;
	}
	
	public void addRealDurationWeighted(long realDurationWeighted) {
		this.realDurationWeighted = this.realDurationWeighted + realDurationWeighted;
	}

}

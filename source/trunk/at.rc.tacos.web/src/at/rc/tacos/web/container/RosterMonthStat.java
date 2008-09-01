package at.rc.tacos.web.container;

/**
 * Roster Month Stat
 * @author Payer Martin
 * @version 1.0
 */
public class RosterMonthStat {
	private long realDuration;
	private long realDurationWeighted;
	private long plannedDuration;
	private long plannedDurationWeighted;
	private long durationForStatistic;
	private long durationForStatisticWeighted;
	public RosterMonthStat() {
		realDuration = 0;
		realDurationWeighted = 0;
		plannedDuration = 0;
		plannedDurationWeighted = 0;
		durationForStatistic = 0;
		durationForStatisticWeighted = 0;
	}

	public long getRealDuration() {
		return realDuration;
	}
	public void setRealDuration(long realDuration) {
		this.realDuration = realDuration;
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
		return (int)((realDuration-getRealDurationHours()*(1000*60*60))/(1000*60));
	}
	
	public int getPlannedDurationHours() {
		return (int)plannedDuration/(1000*60*60);
	}
	
	public int getPlannedDurationMinutes() {
		return (int)((plannedDuration-getPlannedDurationHours()*(1000*60*60))/(1000*60));
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
	
	public void addDurationForStatistic(long durationForStatistic) {
		this.durationForStatistic = this.durationForStatistic + durationForStatistic;
	}
	
	public void addDurationForStatisticWeighted(long durationForStatisticWeighted) {
		this.durationForStatisticWeighted = this.durationForStatisticWeighted + durationForStatisticWeighted;
	}

	public long getRealDurationWeighted() {
		return realDurationWeighted;
	}

	public void setRealDurationWeighted(long realDurationWeighted) {
		this.realDurationWeighted = realDurationWeighted;
	}

	public long getPlannedDurationWeighted() {
		return plannedDurationWeighted;
	}

	public void setPlannedDurationWeighted(long plannedDurationWeighted) {
		this.plannedDurationWeighted = plannedDurationWeighted;
	}

	public long getDurationForStatistic() {
		return durationForStatistic;
	}
	
	public int getDurationForStatisticHours() {
		return (int)durationForStatistic/(1000*60*60);
	}
	
	public int getDurationForStatisticMinutes() {
		return (int)((durationForStatistic-getDurationForStatisticHours()*(1000*60*60))/(1000*60));
	}

	public void setDurationForStatistic(long durationForStatistic) {
		this.durationForStatistic = durationForStatistic;
	}

	public long getDurationForStatisticWeighted() {
		return durationForStatisticWeighted;
	}
	
	public int getDurationForStatisticWeightedHours() {
		return (int)durationForStatisticWeighted/(1000*60*60);
	}
	
	public int getDurationForStatisticWeightedMinutes() {
		return (int)((durationForStatisticWeighted-getDurationForStatisticWeightedHours()*(1000*60*60))/(1000*60));
	}

	public void setDurationForStatisticWeighted(long durationForStatisticWeighted) {
		this.durationForStatisticWeighted = durationForStatisticWeighted;
	}
	
}

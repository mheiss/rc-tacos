package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.model.StaffMember;

/**
 * StaffMemberStatisticContainer
 * @author Payer Martin
 * @version 1.0
 */
public class RosterStatContainer {
	private List<RosterEntryContainer> rosterEntryContainerList;
	private long realDurationWeighted;
	private long realDuration;
	private long plannedDurationWeighted;
	private long plannedDuration;
	public RosterStatContainer() {
		realDurationWeighted = 0;
		realDuration = 0;
		plannedDurationWeighted = 0;
		plannedDuration = 0;
		rosterEntryContainerList = new ArrayList<RosterEntryContainer>();
	}

	public List<RosterEntryContainer> getRosterEntryContainerList() {
		return rosterEntryContainerList;
	}

	public void setRosterEntryContainerList(
			List<RosterEntryContainer> rosterEntryContainerList) {
		this.rosterEntryContainerList = rosterEntryContainerList;
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

}

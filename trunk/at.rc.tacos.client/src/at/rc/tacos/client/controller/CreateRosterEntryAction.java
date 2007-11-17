package at.rc.tacos.client.controller;

//rcp
import org.eclipse.jface.action.Action;
//client
import at.rc.tacos.core.service.ServiceWrapper;
import at.rc.tacos.model.*;

/**
 * The action that is triggered by creating a new roster entry
 * @author b.thek
 */
public class CreateRosterEntryAction extends Action 
{
	private long rosterId;
	private StaffMember staffMember;
	private long dateOfRosterEntry;
	private long plannedStartofWork;
	private long plannedEndOfWork;
	private long realStartOfWork;
	private long realEndOfWork;
	private String station;
	private String competence;
	private String servicetype;
	private String rosterNotes;
	private boolean standby;

    /**
	 * @param rosterId
	 * @param staffMember
	 * @param dateOfRosterEntry
	 * @param plannedStartofWork
	 * @param plannedEndOfWork
	 * @param realStartOfWork
	 * @param realEndOfWork
	 * @param station
	 * @param competence
	 * @param servicetype
	 * @param rosterNotes
	 * @param standby
	 */
	public CreateRosterEntryAction(long rosterId, StaffMember staffMember,
			long dateOfRosterEntry, long plannedStartofWork,
			long plannedEndOfWork, long realStartOfWork, long realEndOfWork,
			String station, String competence, String servicetype,
			String rosterNotes, boolean standby) 
	{
		
		this.rosterId = rosterId;
		this.staffMember = staffMember;
		this.dateOfRosterEntry = dateOfRosterEntry;
		this.plannedStartofWork = plannedStartofWork;
		this.plannedEndOfWork = plannedEndOfWork;
		this.realStartOfWork = realStartOfWork;
		this.realEndOfWork = realEndOfWork;
		this.station = station;
		this.competence = competence;
		this.servicetype = servicetype;
		this.rosterNotes = rosterNotes;
		this.standby = standby;
	}
    
    public void run() 
    {
    	RosterEntry newRosterEntry = new RosterEntry(rosterId,staffMember,dateOfRosterEntry,
    			plannedStartofWork, plannedEndOfWork,
    			realStartOfWork, realEndOfWork, station,
    			competence, servicetype, rosterNotes,
    			standby);
    	ServiceWrapper.getDefault().getServiceLayer().addRosterEntry(newRosterEntry);
    }
}

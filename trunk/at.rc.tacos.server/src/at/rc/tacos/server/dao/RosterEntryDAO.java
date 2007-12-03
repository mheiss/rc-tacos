package at.rc.tacos.server.dao;

import java.util.ArrayList;
import java.util.Date;
import at.rc.tacos.model.RosterEntry;

/**
 * Data source for roster entries
 * @author Michael
 */
public class RosterEntryDAO
{
    //the shared instance
    private static RosterEntryDAO instance;
    //the data list
    private ArrayList<RosterEntry> rosterList; 

    /**
     * Default private class constructor
     */
    private RosterEntryDAO()
    {
        //create the list
        rosterList = new ArrayList<RosterEntry>();
        //load dummy data
        RosterEntry e1 = new RosterEntry();
        e1.setRosterId(0);
        e1.setCompetence("Fahrer");
        e1.setPlannedStartOfWork(new Date().getTime());
        e1.setPlannedEndOfWork(new Date().getTime());
        e1.setRealEndOfWork(new Date().getTime());
        e1.setRealStartOfWork(new Date().getTime());
        e1.setRosterId(0);
        e1.setRosterNotes("mix");
        e1.setServicetype("Zivi");
        e1.setStaffMember(StaffMemberDAO.getInstance().getList().get(0));
        e1.setStandby(false);
        e1.setStation("Bruck");
        //second entry
        RosterEntry e2 = new RosterEntry();
        e2.setRosterId(1);
        e2.setCompetence("Fahrer");
        e2.setPlannedStartOfWork(new Date().getTime());
        e2.setPlannedEndOfWork(new Date().getTime());
        e2.setRealEndOfWork(new Date().getTime());
        e2.setRealStartOfWork(new Date().getTime());
        e2.setRosterId(0);
        e2.setRosterNotes("mix");
        e2.setServicetype("Zivi");
        e2.setStaffMember(StaffMemberDAO.getInstance().getList().get(1));
        e2.setStandby(false);
        e2.setStation("Bruck");
        //third entry
        RosterEntry e3 = new RosterEntry();
        e3.setRosterId(2);
        e3.setCompetence("Fahrer");
        e3.setPlannedStartOfWork(new Date().getTime());
        e3.setPlannedEndOfWork(new Date().getTime());
        e3.setRealEndOfWork(new Date().getTime());
        e3.setRealStartOfWork(new Date().getTime());
        e3.setRosterId(0);
        e3.setRosterNotes("mix");
        e3.setServicetype("Zivi");
        e3.setStaffMember(StaffMemberDAO.getInstance().getList().get(2));
        e3.setStandby(false);
        e3.setStation("Bruck");
        //add to list
        rosterList.add(e1);
        rosterList.add(e2);
        rosterList.add(e3);
    }

    /**
     * Creates and returns the shared instance
     */
    public static RosterEntryDAO getInstance()
    {
        if( instance == null)
            instance = new RosterEntryDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<RosterEntry> getList()
    {
        return rosterList;
    }

}

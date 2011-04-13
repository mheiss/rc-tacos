package at.redcross.tacos.datasetup.stage2;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.RosterEntry;
import at.redcross.tacos.dbal.helper.AssignmentHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;
import at.redcross.tacos.dbal.helper.ServiceTypeHelper;
import at.redcross.tacos.dbal.helper.SystemUserHelper;

public class RosterStatistikStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<RosterEntry> query = manager.createQuery("from RosterEntry", RosterEntry.class);
        for (RosterEntry rosterEntry : query.getResultList()) {
            manager.remove(rosterEntry);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        for (int i = 0; i < 100; i++) {
            createEntry(manager, i, i < 60);
        }
    }

    /** Creates a new entry */
    private void createEntry(EntityManager manager, int startDay, boolean hasEnd) {
        char locationPrefix = (char) ((startDay % 4) + 65);
        char userPrefix = (char) ((startDay % 24) + 65);

        RosterEntry entry = new RosterEntry();
        entry.setLocation(LocationHelper.getByName(manager, "Location_"
                + Character.toString(locationPrefix).toUpperCase()));
        entry.setServiceType(ServiceTypeHelper.getByName(manager, "Hauptamtlich"));
        entry.setAssignment(AssignmentHelper.getByName(manager, "Fahrer"));
        entry.setSystemUser(SystemUserHelper.getByLogin(manager, userPrefix + "User"));

        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.DAY_OF_MONTH, startDay % 30);
        startCal.set(Calendar.HOUR_OF_DAY, 10);
        startCal.set(Calendar.MINUTE, 00);

        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DAY_OF_MONTH, startDay % 30);
        endCal.set(Calendar.HOUR_OF_DAY, 18);
        endCal.set(Calendar.MINUTE, 00);

        entry.setPlannedStartDateTime(startCal.getTime());
        entry.setPlannedEndDateTime(endCal.getTime());

        if (hasEnd) {
            entry.setRealStartDateTime(startCal.getTime());
            entry.setRealEndDateTime(endCal.getTime());
        }
        manager.persist(entry);
    }
}

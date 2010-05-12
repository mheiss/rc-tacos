package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Assignment;

// creates assignment stages
public class AssignmentStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Assignment> query = manager.createQuery("from Assignment", Assignment.class);
        for (Assignment assignment : query.getResultList()) {
            manager.remove(assignment);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            Assignment assignment = new Assignment("Fahrer");
            assignment.setDescription("Fahrer");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Sanitäter");
            assignment.setDescription("Sanitäter");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Notfallsanitäter");
            assignment.setDescription("Notfallsanitäter");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Notarzt");
            assignment.setDescription("Notarzt");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Leitstellendisponent");
            assignment.setDescription("Leitstellendisponent");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Dienstführender");
            assignment.setDescription("Dienstführender");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Inspektionsdienst");
            assignment.setDescription("Inspektionsdienst");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("BKTW-Fahrer");
            assignment.setDescription("BKTW-Fahrer");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Journaldienst");
            assignment.setDescription("Journaldienst");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Volontär");
            assignment.setDescription("Volontär");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment("Sonstiges");
            assignment.setDescription("Sonstiges");
            manager.persist(assignment);
        }
    }
}

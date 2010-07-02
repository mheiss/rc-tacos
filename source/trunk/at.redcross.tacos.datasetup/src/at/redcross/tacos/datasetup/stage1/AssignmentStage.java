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
            Assignment assignment = new Assignment();
            assignment.setName("Fahrer");
            assignment.setDescription("Fahrer");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Sanitäter");
            assignment.setDescription("Sanitäter");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Notfallsanitäter");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Notarzt");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Leitstellendisponent");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Dienstführender");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Inspektionsdienst");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("BKTW-Fahrer");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Journaldienst");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Volontär");
            manager.persist(assignment);
        }
        {
            Assignment assignment = new Assignment();
            assignment.setName("Sonstiges");
            manager.persist(assignment);
        }
    }
}

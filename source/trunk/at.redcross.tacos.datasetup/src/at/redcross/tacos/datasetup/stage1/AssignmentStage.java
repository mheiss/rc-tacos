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
            assignment.setName("Sanit�ter");
            assignment.setDescription("Sanit�ter");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Notfallsanit�ter");
            assignment.setDescription("Notfallsanit�ter");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Notarzt");
            assignment.setDescription("Notarzt");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Leitstellendisponent");
            assignment.setDescription("Leitstellendisponent");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Dienstf�hrender");
            assignment.setDescription("Dienstf�hrender");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Inspektionsdienst");
            assignment.setDescription("Inspektionsdienst");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("BKTW-Fahrer");
            assignment.setDescription("BKTW-Fahrer");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Journaldienst");
            assignment.setDescription("Journaldienst");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Volont�r");
            assignment.setDescription("Volont�r");
            manager.persist(assignment);
        }
        {
        	Assignment assignment = new Assignment();
            assignment.setName("Sonstiges");
            assignment.setDescription("Sonstiges");
            manager.persist(assignment);
        }
    }
}

package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Competence;

// creates competence stages
public class CompetenceStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Competence> query = manager.createQuery("from Competence", Competence.class);
        for (Competence competence : query.getResultList()) {
            manager.remove(competence);
        }
    }
      
    @Override
    public void performImport(EntityManager manager) {
        {
            Competence competence = new Competence();
            competence.setName("Fahrer");
            competence.setDescription("Fahrer");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Sanitäter");
            competence.setDescription("Sanitäter");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Notfallsanitäter");
            competence.setDescription("Notfallsanitäter");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Notarzt");
            competence.setDescription("Notarzt");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Leitstellendisponent");
            competence.setDescription("Leitstellendisponent");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Dienstführender");
            competence.setDescription("Dienstführender");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Inspektionsdienst");
            competence.setDescription("Inspektionsdienst");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("BKTW-Fahrer");
            competence.setDescription("BKTW-Fahrer");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Journaldienst");
            competence.setDescription("Journaldienst");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Volontär");
            competence.setDescription("Volontär");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("Sonstiges");
            competence.setDescription("Sonstiges");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("_HA");
            competence.setDescription("_HA");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("_ZD");
            competence.setDescription("_ZD");
            manager.persist(competence);
        }
        {
        	Competence competence = new Competence();
            competence.setName("_LS");
            competence.setDescription("_LS");
            manager.persist(competence);
        }
    }
}

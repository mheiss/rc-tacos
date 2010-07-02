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
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Sanitäter");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Notfallsanitäter");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Notarzt");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Dienstführender");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Inspektionsdienst");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("BKTW-Fahrer");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Journaldienst");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Volontär");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Sonstiges");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Hauptamtlicher");
            competence.setShortName("HA");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Zivieldiener");
            competence.setShortName("ZD");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence();
            competence.setName("Leitstellendisponent");
            competence.setShortName("LS");
            manager.persist(competence);
        }
    }
}

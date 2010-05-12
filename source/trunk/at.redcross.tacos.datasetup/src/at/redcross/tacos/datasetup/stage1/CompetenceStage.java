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
            Competence competence = new Competence("Fahrer");
            competence.setDescription("Fahrer");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Sanitäter");
            competence.setDescription("Sanitäter");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Notfallsanitäter");
            competence.setDescription("Notfallsanitäter");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Notarzt");
            competence.setDescription("Notarzt");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Leitstellendisponent");
            competence.setDescription("Leitstellendisponent");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Dienstführender");
            competence.setDescription("Dienstführender");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Inspektionsdienst");
            competence.setDescription("Inspektionsdienst");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("BKTW-Fahrer");
            competence.setDescription("BKTW-Fahrer");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Journaldienst");
            competence.setDescription("Journaldienst");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Volontär");
            competence.setDescription("Volontär");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("Sonstiges");
            competence.setDescription("Sonstiges");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("HA");
            competence.setDescription("Hauptamtlicher");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("ZD");
            competence.setDescription("Zivieldiener");
            manager.persist(competence);
        }
        {
            Competence competence = new Competence("LS");
            competence.setDescription("Leitstellendisponent");
            manager.persist(competence);
        }
    }
}

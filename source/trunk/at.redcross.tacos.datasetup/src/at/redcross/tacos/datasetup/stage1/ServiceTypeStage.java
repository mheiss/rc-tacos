package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.ServiceType;

// creates service stages
public class ServiceTypeStage implements DatasetupStage {

    @Override
    public void performImport(EntityManager manager) {
        {
            ServiceType type = new ServiceType();
            type.setName("Azubi");
            type.setDescription("Azubi");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Bezirksrettungskommandant");
            type.setDescription("Bezirksrettungskommandant");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("BKTW-Fahrer");
            type.setDescription("BKTW-Fahrer");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Dienstführender");
            type.setDescription("Dienstführender");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Fahrer");
            type.setDescription("Fahrer");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("First Responder Böhler");
            type.setDescription("First Responder Böhler");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("NEF Azubi");
            type.setDescription("NEF Azubi");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Notarzt");
            type.setDescription("Notarzt");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Sanitäter");
            type.setDescription("Sanitäter");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType();
            type.setName("Volontär");
            type.setDescription("Volontär");
            manager.persist(type);
        }
    }
}

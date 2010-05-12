package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.ServiceType;

// creates service types
public class ServiceTypeStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<ServiceType> query = manager.createQuery("from ServiceType", ServiceType.class);
        for (ServiceType type : query.getResultList()) {
            manager.remove(type);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            ServiceType type = new ServiceType("Hauptamtlich");
            type.setDescription("Hauptamtlich");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType("Freiwillig");
            type.setDescription("Freiwillig");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType("Ersatzeinstellung");
            type.setDescription("Ersatzeinstellung");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType("Zivildiener");
            type.setDescription("Zivildiener");
            manager.persist(type);
        }
        {
            ServiceType type = new ServiceType("Sonstiges");
            type.setDescription("Sonstiges");
            manager.persist(type);
        }
    }
}

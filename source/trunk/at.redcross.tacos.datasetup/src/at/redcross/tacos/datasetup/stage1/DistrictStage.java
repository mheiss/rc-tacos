package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.District;

public class DistrictStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<District> query = manager.createQuery("from District", District.class);
        for (District district : query.getResultList()) {
            manager.remove(district);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            District district = new District();
            district.setName("Bruck an der Mur");
            district.setShortName("BM");
            manager.persist(district);
        }
    }

}

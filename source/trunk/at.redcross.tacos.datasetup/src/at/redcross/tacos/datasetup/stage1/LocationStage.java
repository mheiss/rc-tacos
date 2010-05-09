package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Location;

// creates service stages
public class LocationStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Location> query = manager.createQuery("from Location", Location.class);
        for (Location location : query.getResultList()) {
            manager.remove(location);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            Location location = new Location();
            location.setName("Bezirk: Bruck - Kapfenberg");
            //location.setAddress(address);
            //location.setDistrict(district);
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("Bruck an der Mur");
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("Kapfenberg");
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("Thörl");
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("Turnau");
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("St. Marein");
            manager.persist(location);
        }
        {
            Location location = new Location();
            location.setName("Breitenau");
            manager.persist(location);
        }
    }
}

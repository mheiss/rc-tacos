package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
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
            Address address1 = new Address();
            address1.setCity("Bruck an der Mur");
            address1.setEmail("bruck@st.roteskreuz.at");
            location.setAddress(address1);
            //location.setDistrict(district);
            manager.persist(location);
        }
        /*
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
            location.setName("Th�rl");
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
        */
    }
}

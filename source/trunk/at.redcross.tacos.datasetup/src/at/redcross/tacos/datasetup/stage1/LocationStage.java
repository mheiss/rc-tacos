package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.District;
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
        District district = manager.find(District.class, "BM");
        {
            Address address = new Address();
            address.setCity("Bruck an der Mur");
            address.setEmail("bruck@st.roteskreuz.at");

            Location location = new Location(district, "BK");
            location.setName("Bezirk: Bruck - Kapfenberg");
            location.setAddress(address);

            manager.persist(location);
        }
        /*
         * { Location location = new Location();
         * location.setName("Bruck an der Mur"); manager.persist(location); } {
         * Location location = new Location(); location.setName("Kapfenberg");
         * manager.persist(location); } { Location location = new Location();
         * location.setName("Th�rl"); manager.persist(location); } { Location
         * location = new Location(); location.setName("Turnau");
         * manager.persist(location); } { Location location = new Location();
         * location.setName("St. Marein"); manager.persist(location); } {
         * Location location = new Location(); location.setName("Breitenau");
         * manager.persist(location); }
         */
    }
}

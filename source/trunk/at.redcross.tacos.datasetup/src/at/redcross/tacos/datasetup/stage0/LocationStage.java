package at.redcross.tacos.datasetup.stage0;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
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
            Address address = new Address();
            address.setCity("City_A");
            address.setEmail("no-reply@st.roteskreuz.at");

            Location location = new Location();
            location.setName("Location_A");
            location.setShortName("L_A");

            manager.persist(location);
        }
        {
            Address address = new Address();
            address.setCity("City_B");
            address.setEmail("no-reply@st.roteskreuz.at");

            Location location = new Location();
            location.setName("Location_B");
            location.setShortName("L_B");

            manager.persist(location);
        }
        {
            Address address = new Address();
            address.setCity("City_C");
            address.setEmail("no-reply@st.roteskreuz.at");

            Location location = new Location();
            location.setName("Location_C");
            location.setShortName("L_C");

            manager.persist(location);
        }
        {
            Address address = new Address();
            address.setCity("City_D");
            address.setEmail("no-reply@st.roteskreuz.at");

            Location location = new Location();
            location.setName("Location_D");
            location.setShortName("L_D");

            manager.persist(location);
        }
    }
}

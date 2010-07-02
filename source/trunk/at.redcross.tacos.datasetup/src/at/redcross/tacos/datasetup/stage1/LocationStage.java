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
        District district = getDistrictByName(manager, "Bruck an der Mur");
        {
            Address address = new Address();
            address.setCity("Bruck an der Mur");
            address.setEmail("bruck-kapfenberg@st.roteskreuz.at");

            Location location = new Location();
            location.setDistrict(district);
            location.setName("Bezirk: Bruck - Kapfenberg");
            location.setShortName("BK");
            location.setAddress(address);

            manager.persist(location);
        }
        {
            Address address = new Address();
            address.setCity("Bruck an der Mur");
            address.setEmail("bruck@st.roteskreuz.at");

            Location location = new Location();
            location.setDistrict(district);
            location.setName("Bruck");
            location.setShortName("B");
            location.setAddress(address);

            manager.persist(location);
        }
        {
            Address address = new Address();
            address.setCity("Kapfenberg");
            address.setEmail("kapfenberg@st.roteskreuz.at");

            Location location = new Location();
            location.setDistrict(district);
            location.setShortName("K");
            location.setName("Kapfenberg");
            location.setAddress(address);

            manager.persist(location);
        }
    }

    private District getDistrictByName(EntityManager manager, String name) {
        String hqlQuery = "select district from District district where district.name = :name";
        TypedQuery<District> query = manager.createQuery(hqlQuery, District.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}

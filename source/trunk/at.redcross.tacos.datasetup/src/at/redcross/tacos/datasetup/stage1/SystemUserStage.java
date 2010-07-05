package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Location;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;

// creates service stages
public class SystemUserStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<SystemUser> query = manager.createQuery("from SystemUser", SystemUser.class);
        for (SystemUser user : query.getResultList()) {
            manager.remove(user);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        Location location1 = getLocationByName(manager, "Bruck");
        Location location2 = getLocationByName(manager, "Kapfenberg");

        {
            Address address = new Address();
            address.setCity("Graz");
            address.setZipCode("8053");
            address.setStreet("Straße 1");
            address.setPhone("0664/1231212");
            address.setEmail("michael.heiss@st.roteskreuz.at");

            SystemUser sysUser = new SystemUser();
            sysUser.setPnr(50000000);
            sysUser.setFirstName("Michael");
            sysUser.setLastName("Heiss");
            sysUser.setAddress(address);

            Login login = new Login();
            login.setSystemUser(sysUser);
            login.setAlias("m.heiss");
            login.setPassword("ea6c321a04a32b5721b1f7385f79fb3ca704caa4");
            sysUser.setLocation(location1);

            manager.persist(login);
            manager.persist(sysUser);
        }
        {
            Address address = new Address();
            address.setCity("St.Lorenzen");
            address.setZipCode("8642");
            address.setStreet("Straße 2");
            address.setPhone("0664/4143824");
            address.setPhone2("03862/1212121");
            address.setEmail("birgit.thek@st.roteskreuz.at");

            SystemUser sysUser = new SystemUser();
            sysUser.setPnr(50000001);
            sysUser.setFirstName("Birgit");
            sysUser.setLastName("Thek");
            sysUser.setAddress(address);

            Login login = new Login();
            login.setSystemUser(sysUser);
            login.setAlias("b.thek");
            login.setPassword("7d246ef870273a53a1b6f6eac1e724d40ecc783d");

            sysUser.setLocation(location2);

            manager.persist(login);
            manager.persist(sysUser);
        }
    }

    private Location getLocationByName(EntityManager manager, String name) {
        String hqlQuery = "from Location location where location.name = :name";
        TypedQuery<Location> query = manager.createQuery(hqlQuery, Location.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}

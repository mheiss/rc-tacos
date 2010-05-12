package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
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
        {
            Address address = new Address();
            address.setCity("Graz");
            address.setZipCode("8053");
            address.setStreet("Straße 1");
            address.setEmail("michael.heiss@st.roteskreuz.at");

            SystemUser sysUser = new SystemUser();
            sysUser.setFirstName("Michael");
            sysUser.setLastName("Heiss");
            sysUser.setAddress(address);

            Login login = new Login(sysUser, "m.heiss");
            login.setPassword("m.heiss");

            manager.persist(login);
            manager.persist(sysUser);
        }
        {
            Address address = new Address();
            address.setCity("St.Lorenzen");
            address.setZipCode("8642");
            address.setStreet("Straße 2");
            address.setEmail("birgit.thek@st.roteskreuz.at");

            SystemUser sysUser = new SystemUser();
            sysUser.setFirstName("Birgit");
            sysUser.setLastName("Thek");
            sysUser.setAddress(address);

            Login login = new Login(sysUser, "b.thek");
            login.setPassword("b.thek");

            manager.persist(login);
            manager.persist(sysUser);
        }
    }
}

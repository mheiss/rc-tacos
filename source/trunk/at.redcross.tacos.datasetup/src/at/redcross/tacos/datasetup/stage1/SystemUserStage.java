package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;

// creates service stages
public class SystemUserStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Login> query = manager.createQuery("from Login", Login.class);
        for (Login type : query.getResultList()) {
            manager.remove(type);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            SystemUser sysUser = new SystemUser();
            sysUser.setFirstName("Michael");
            sysUser.setLastName("Heiss");
            manager.persist(sysUser);

            Login login = new Login();
            login.setAlias("m.heiss");
            login.setPassword("m.heiss");
            login.setSystemUser(sysUser);
            manager.persist(login);
        }
        {
            SystemUser sysUser = new SystemUser();
            sysUser.setFirstName("Birgit");
            sysUser.setLastName("Thek");
            manager.persist(sysUser);

            Login login = new Login();
            login.setAlias("b.thek");
            login.setPassword("b.thek");
            login.setSystemUser(sysUser);
            manager.persist(login);
        }
    }
}

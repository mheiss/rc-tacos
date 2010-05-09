
package at.redcross.tacos.datasetup.stage1;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Login;

// creates service stages
public class LoginStage implements DatasetupStage {

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
            Login login = new Login();
            login.setAlias("m.heiss");
            login.setPassword("F7nhxkWIx/pkGbTSncH0QmJ5ugE=");
            login.setInvalidLogout(false);
            //SystemUser sysUser = new SystemUser();
            //sysUser.setFirstName("Michael");
            //sysUser.setLastName("Heiss");
            //login.setSystemUser(sysUser);
            manager.persist(login);
        }
        {
            Login login = new Login();
            login.setAlias("b.thek");
            login.setPassword("F7nhxkWIx/pkGbTSncH0QmJ5ugE=");
            login.setInvalidLogout(false);
            //SystemUser sysUser = new SystemUser();
            //sysUser.setFirstName("Birgit");
            //sysUser.setLastName("Thek");
            //login.setSystemUser(sysUser);
            manager.persist(login);
        }
    }
}


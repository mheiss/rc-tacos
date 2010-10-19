package at.redcross.tacos.datasetup.stage0;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.GroupHelper;
import at.redcross.tacos.dbal.helper.LocationHelper;

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
			address.setCity("City_A");
			address.setZipCode("Zip_A");
			address.setStreet("Street_A");
			address.setPhone("Phone_A");
			address.setEmail("no-reply@st.roteskreuz.at");
			
			Login login = new Login();
			login.setLoginName("tacos");
			login.setPassword("1734c8d3b028f603483d60ab3ab8a61cf6a41f93709cf47fa01d30a90ee2282c");
			
			SystemUser sysUser = new SystemUser();
			sysUser.setPnr(50000000);
			sysUser.setFirstName("Tacos");
			sysUser.setLastName("Tacos");
			sysUser.setAddress(address);
			sysUser.setGroups(GroupHelper.getByName(manager, "ROLE_ADMIN"));
			sysUser.setLocation(LocationHelper.getByName(manager, "Location_A"));
			
			sysUser.setLogin(login);
			login.setSystemUser(sysUser);
			
			manager.persist(sysUser);
			manager.persist(login);
			
			sysUser = manager.merge(sysUser);
			login = manager.merge(login);
		}
	}
}

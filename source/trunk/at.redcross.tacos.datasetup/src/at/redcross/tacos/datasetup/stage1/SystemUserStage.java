package at.redcross.tacos.datasetup.stage1;

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
			sysUser.setGroups(GroupHelper.getByName(manager, "ROLE_ANONYMOUS"));

			Login login = new Login();
			login.setSystemUser(sysUser);
			login.setLoginName("m.heiss");
			login.setPassword("14b364f298530bcb96e260e02ef3f362120bb06f998eb488ca2e159cb8177310");
			sysUser.setLocation(LocationHelper.getByName(manager, "Bruck"));

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
			sysUser.setGroups(GroupHelper.getByName(manager, "ROLE_ADMIN"));

			Login login = new Login();
			login.setSystemUser(sysUser);
			login.setLoginName("b.thek");
			login.setPassword("f27fe25460cdb4bcd43f51c3d4fed2084996c21664898f7425ccea2f5dc156a3");

			sysUser.setLocation(LocationHelper.getByName(manager, "Kapfenberg"));

			manager.persist(login);
			manager.persist(sysUser);
		}
	}
}

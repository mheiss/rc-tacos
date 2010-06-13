package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.SystemUser;

public class SystemUserHelper {

	public static List<SystemUser> list(EntityManager manager) {
		return manager.createQuery("from SystemUser", SystemUser.class).getResultList();
	}
}

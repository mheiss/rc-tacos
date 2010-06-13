package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.ServiceType;

public class ServiceTypeHelper {

	public static List<ServiceType> list(EntityManager manager) {
		return manager.createQuery("from ServiceType", ServiceType.class).getResultList();
	}

}

package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Location;

public class LocationHelper {

	public static List<Location> list(EntityManager manager) {
		return manager.createQuery("from Location", Location.class).getResultList();
	}

}

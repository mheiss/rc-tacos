package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Car;

public class CarHelper {

	public static List<Car> list(EntityManager manager, boolean outOfOrder) {
		String hqlQuery = "from Car c where c.outOfOrder = :outOfOrder";
		TypedQuery<Car> query = manager.createQuery(hqlQuery, Car.class);
		query.setParameter("outOfOrder", outOfOrder);
		return query.getResultList();
	}

	public static Car getByName(EntityManager manager, String name) {
		String hqlQuery = "from Car c where c.name = :name";
		TypedQuery<Car> query = manager.createQuery(hqlQuery, Car.class);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

	public static List<Car> listByLocationName(EntityManager manager, String locationName) {
		StringBuilder builder = new StringBuilder();
		builder.append("from Car car ");
		if (locationName != null) {
			builder.append("where car.location.name = :locationName");
		}
		TypedQuery<Car> query = manager.createQuery(builder.toString(), Car.class);
		if (locationName != null) {
			query.setParameter("locationName", locationName);
		}
		return query.getResultList();
	}

}

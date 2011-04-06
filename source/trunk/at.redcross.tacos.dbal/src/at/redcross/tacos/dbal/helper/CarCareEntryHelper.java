package at.redcross.tacos.dbal.helper;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import at.redcross.tacos.dbal.entity.CarCareEntry;

public class CarCareEntryHelper {

    public static List<CarCareEntry> list(EntityManager manager, long carId) {
        String hqlQuery = ("from CarCareEntry where car_id = :carId");
        TypedQuery<CarCareEntry> query = manager.createQuery(hqlQuery, CarCareEntry.class);
		query.setParameter("carId", carId);
		List <CarCareEntry> resultList;
		resultList = query.getResultList();
		return resultList;
    }
}

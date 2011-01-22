package at.redcross.tacos.dbal.helper;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import at.redcross.tacos.dbal.entity.CarCareEntry;

public class CarCareEntryHelper {

    public static List<CarCareEntry> list(EntityManager manager, long carId) {
    	System.out.println("carId: "+carId);
        String hqlQuery = ("from CarCareEntry where car_id = :carId");
        TypedQuery<CarCareEntry> query = manager.createQuery(hqlQuery, CarCareEntry.class);
		query.setParameter("carId", carId);
		System.out.println("drinnen");
		System.out.println("");
		List <CarCareEntry> resultList;
		resultList = query.getResultList();
		System.out.println("result: " +resultList.size());
		return resultList;
    }

//    public static CarCareEntry getByName(EntityManager manager, String name) {
//        String hqlQuery = "from CarCareEntry a where a.name = :name";
//        TypedQuery<CarCareEntry> query = manager.createQuery(hqlQuery, CarCareEntry.class);
//        query.setParameter("name", name);
//        List<CarCareEntry> resultList = query.getResultList();
//        if (!resultList.isEmpty()) {
//            return resultList.iterator().next();
//        }
//        return null;
//    }

}

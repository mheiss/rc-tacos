package at.redcross.tacos.datasetup.stage1;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Car;
import at.redcross.tacos.dbal.entity.Location;

// creates assignment stages
public class CarStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Car> query = manager.createQuery("from Car", Car.class);
        for (Car car : query.getResultList()) {
            manager.remove(car);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
    	Location location1 = getLocationByName(manager, "Bruck");
        Location location2 = getLocationByName(manager, "Kapfenberg");
        {
        	Car car = new Car();
        	car.setCarName("125");
        	car.setStandby(true);
        	car.setNotes("nicht zu schnell fahren, Bremsen funktionieren nicht!");
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("126");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("127");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("128");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("140");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("141");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("142");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("143");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("144");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("145");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("146");
        	car.setStandby(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
    }
    private Location getLocationByName(EntityManager manager, String name) {
        String hqlQuery = "from Location location where location.name = :name";
        TypedQuery<Location> query = manager.createQuery(hqlQuery, Location.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}

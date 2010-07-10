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
        Location location3 = getLocationByName(manager, "Bezirk: Bruck - Kapfenberg");
        {
        	Car car = new Car();
        	car.setCarName("5.01 01");
        	car.setStandby(true);
        	car.setNotes("nicht zu schnell fahren, Bremsen funktionieren nicht!");
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 03");
        	car.setStandby(true);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 04");
        	car.setStandby(true);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 05");
        	car.setStandby(true);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 28");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 22");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 23");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 24");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 25");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 26");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 27");
        	car.setStandby(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 42");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 43");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 44");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 45");
        	car.setStandby(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 46");
        	car.setStandby(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 47");
        	car.setStandby(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 48");
        	car.setStandby(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setCarName("5.01 49");
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

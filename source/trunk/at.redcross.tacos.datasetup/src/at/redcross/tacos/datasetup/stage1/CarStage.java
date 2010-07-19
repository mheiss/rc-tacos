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
        	car.setName("5.01 01");
        	car.setOutOfOrder(true);
        	car.setNotes("nicht zu schnell fahren, Bremsen funktionieren nicht!");
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 03");
        	car.setOutOfOrder(false);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 04");
        	car.setOutOfOrder(true);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 05");
        	car.setOutOfOrder(true);
        	car.setLocation(location3);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 28");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 22");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 23");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 24");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 25");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 26");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 27");
        	car.setOutOfOrder(true);
        	car.setLocation(location1);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 42");
        	car.setOutOfOrder(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 43");
        	car.setOutOfOrder(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 44");
        	car.setOutOfOrder(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 45");
        	car.setOutOfOrder(true);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 46");
        	car.setOutOfOrder(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 47");
        	car.setOutOfOrder(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 48");
        	car.setOutOfOrder(false);
        	car.setLocation(location2);

            manager.persist(car);
        }
        {
        	Car car = new Car();
        	car.setName("5.01 49");
        	car.setOutOfOrder(false);
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

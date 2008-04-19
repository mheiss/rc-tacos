package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.ServiceType;;

public interface ServiceTypeDAO 
{
    public static final String TABLE_NAME = "servicetype";
    
	/**
	 * Adds a new service type to the database and returns the unique id.
	 * @param serviceType the service type to add
	 * @return the unique id of the service type in the database
	 */
    public int addServiceType(ServiceType serviceType) throws SQLException;
    
    /**
     * Updates a service type in the database
     * @param serviceType the service type to update
     * @return true if the update was successfully otherwise false
     */
    public boolean updateServiceType(ServiceType serviceType) throws SQLException;
    
    /**
     * Removes the service type from the database.
     * @param id the id of the service type to remove
     * @return true if the deletion was successfully.
     */
    public boolean removeServiceType(int id) throws SQLException;
    
    /**
     * Returns the service type identified by the given id.
     * @param id the id to get the service type from
     * @return the queried serviceType
     */
    public ServiceType getServiceTypeId(int id) throws SQLException;  
    
    /**    
     * Returns a list of all stored service type in the database
     * @return the complete list of all service type
     */
	public List<ServiceType> listServiceTypes() throws SQLException;

	public List<ServiceType> listServiceTypesByName(String name) throws SQLException;
}

package at.rc.tacos.platform.services;

import at.rc.tacos.platform.services.exception.NoSuchServiceException;

/**
 * Provides access to the database services.
 * 
 * @author Michael
 */
public interface DbalServiceFactory {

    /**
     * Returns the appropriate dbal service that is responsible for the given class.
     * 
     * @param modelClazz
     *            the fully qualified class name to get the accociated service
     * @return the service that can handle the request for the model
     */
    public Object getService(String modelClazz) throws NoSuchServiceException;
}

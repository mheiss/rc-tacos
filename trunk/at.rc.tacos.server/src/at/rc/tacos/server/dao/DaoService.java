package at.rc.tacos.server.dao;

import at.rc.tacos.core.db.dao.factory.DaoFactory;

/**
 * The service class manages the dao used in the server
 * @author Michael
 */
public class DaoService
{
    //the shared instance
    public static DaoService instance;
    
    //the data source
    private DaoFactory factory;
    
    /**
     * Default private class constructor
     */
    private DaoService()
    {
        factory = DaoFactory.TEST;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static DaoService getInstance()
    {
        if( instance == null )
            instance = new DaoService();
        return instance;
    }
    
    /**
     * Returns the data access object factory used 
     * in this service instance.
     * @return the dao factory
     */
    public DaoFactory getFactory()
    {
        return factory;
    }
}

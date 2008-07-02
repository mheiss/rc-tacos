package at.rc.tacos.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import at.rc.tacos.common.IServerListener;

/**
 * <p> <strong>ServerListenerFactory</strong> holds the references to the listener classes.
 * @author Michael
 */
public class ServerListenerFactory
{
    //the shared instance
    private static ServerListenerFactory instance;
    //the listener classes
    private HashMap<String, Class<?>> serverListeners;
    
    /**
     * Default private class constructor
     */
    private ServerListenerFactory()
    {
       serverListeners = new HashMap<String, Class<?>>();
    }
    
    /**
     * Returns the shared instance.
     * @return the instance
     */
    public static ServerListenerFactory getInstance()
    {
        if (instance == null)
            instance = new ServerListenerFactory();
        return instance;
    }
    
    /**
     * Registers a listener class for the given message identification string.<br>
     * There can only be one listener for one event.
     * @param id the purpose to register the listener
     * @param listener the listener class to register
     */
    public void addListener(String id, Class<?> listener)
    {
        serverListeners.put(id, listener);
    }
    
    /**
     * Removes a listener form the listener list. <br>
     * The listener wont receive any updates about this message id.
     * @param id the purpose of the register to remove
     */
    public void removeModelListener(String id)
    {
        serverListeners.remove(id);
    }
    
    /**
     * Returns a EventListener for this type of message.<br>
     * When no listener is registered the method will return null.
     * @param id the message to get the listener
     * @return the listener for this type of message
     */
    public IServerListener buildListener(String id) throws Exception
    {
    	//the class loader params
    	Class<?>[] classParm = null;
    	Object[] objectParm = null;
    	
		//get the class for this entry
    	Class<?> cl= serverListeners.get(id);
    	if(cl == null)
    		throw new ClassNotFoundException("The class for the name "+id+ " cannot be found");
    	
		//load and create a new instance of the class
		Constructor<?> co = cl.getConstructor(classParm);
    	
		return (IServerListener)co.newInstance(objectParm);
    }
}

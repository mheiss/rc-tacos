package at.rc.tacos.client.net;

import java.util.HashMap;
import java.util.Map;

import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;

/**
 * The handler factory returns the appropriate handler implementations based uppon the request.
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class HandlerFactoryImpl implements HandlerFactory {

    private static final HashMap<String, Handler<?>> HANDLER_MAP = new HashMap<String, Handler<?>>();

    // populate the default handlers
    static {

    }

    private Map<String, Handler<?>> handlerMap;

    /**
     * Default class constructor
     */
    public HandlerFactoryImpl() {
        handlerMap = new HashMap<String, Handler<?>>();
        handlerMap.putAll(HANDLER_MAP);
    }

    /**
     * Returns a type save handler instance for the given model clazz
     * 
     * @param modelClazz
     *            the clazz of the model object to get the handler
     */
    @Override
    public <T> Handler<T> getHandler(T modelClazz) {
        Handler<T> handler = (Handler<T>) handlerMap.get(modelClazz.getClass().getName());
        return handler;
    }

}

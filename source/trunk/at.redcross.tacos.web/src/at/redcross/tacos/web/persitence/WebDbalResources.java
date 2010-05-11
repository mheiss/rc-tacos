package at.redcross.tacos.web.persitence;

import java.util.Map;

import at.redcross.tacos.dbal.manager.DbalResources;

public class WebDbalResources extends DbalResources {

    // the one and only
    private static WebDbalResources instance;

    private WebDbalResources() {
        // prevent instantiation
    }

    /**
     * Creates or returns the shared {@code DbalResources} instance.
     * 
     * @return the shared instance.
     */
    public synchronized static DbalResources getInstance() {
        if (instance == null) {
            instance = new WebDbalResources();
        }
        return instance;
    }

    @Override
    protected void initFactory(Map<String, String> map) {
        map.put("hibernate.ejb.interceptor", WebHistoryInterceptor.class.getName());
    }

}

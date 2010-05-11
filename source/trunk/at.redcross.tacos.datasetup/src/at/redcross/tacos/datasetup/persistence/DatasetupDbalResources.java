package at.redcross.tacos.datasetup.persistence;

import java.util.Map;

import at.redcross.tacos.dbal.manager.DbalResources;

public class DatasetupDbalResources extends DbalResources {

    // the one and only
    private static DatasetupDbalResources instance;

    private DatasetupDbalResources() {
        // prevent instantiation
    }

    /**
     * Creates or returns the shared {@code DbalResources} instance.
     * 
     * @return the shared instance.
     */
    public synchronized static DbalResources getInstance() {
        if (instance == null) {
            instance = new DatasetupDbalResources();
        }
        return instance;
    }

    @Override
    protected void initFactory(Map<String, String> map) {
        map.put("hibernate.ejb.interceptor", DatasetupHistoryInterceptor.class.getName());
    }

}

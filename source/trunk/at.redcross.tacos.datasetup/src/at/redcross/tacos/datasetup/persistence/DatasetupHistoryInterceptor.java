package at.redcross.tacos.datasetup.persistence;

import at.redcross.tacos.dbal.entity.listener.HistoryInterceptor;

public class DatasetupHistoryInterceptor extends HistoryInterceptor {

    private static final long serialVersionUID = 2033491648030100418L;

    @Override
    protected String getAuthenticatedUserId() {
        return "(DataImport)";
    }
}

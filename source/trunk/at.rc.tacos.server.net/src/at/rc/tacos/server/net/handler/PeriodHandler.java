package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.PeriodsService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class PeriodHandler implements Handler<Period> {

    @Service(clazz = PeriodsService.class)
    private PeriodsService periodService;

    @Override
    public void add(ServerIoSession session, Message<Period> message) throws ServiceException,SQLException {
        List<Period> periodList = message.getObjects();
        // loop and add the new objects
        for (Period period : periodList) {
            int id = periodService.addPeriod(period);
            if (id == -1)
                throw new ServiceException("Failed to add the period record: " + period);
            period.setPeriodId(id);
        }
        // brodcast the new objects
        session.writeBrodcast(message, periodList);
    }

    @Override
    public void get(ServerIoSession session, Message<Period> message) throws ServiceException,SQLException {
        // get the params of the request
        Map<String, String> params = message.getParams();

        // filter the request
        if (params.containsKey(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER)) {
            final String filter = params.get(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER);
            List<Period> periodList = periodService.getPeriodListByServiceTypeCompetence(filter);
            if (periodList == null) {
                throw new ServiceException("Failed to list the periods by serviceTypeCompetence: "+ filter);
            }
            // send back the result
            session.write(message, periodList);
        }
        throw new ServiceException("Listing of all period records is denied");
    }

    @Override
    public void remove(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
        List<Period> periodList = message.getObjects();
        // loop and remove the new objects
        for (Period period : periodList) {
            if (!periodService.removePeriod(period.getPeriodId()))
                throw new ServiceException("Failed to remove the period record: " + period);
        }
        // brodcast the removed objects
        session.writeBrodcast(message, periodList);
    }

    @Override
    public void update(ServerIoSession session, Message<Period> message) throws ServiceException,SQLException {
        List<Period> periodList = message.getObjects();
        // loop and update the new objects
        for (Period period : periodList) {
            if (!periodService.updatePeriod(period))
                throw new ServiceException("Failed to update the period record: " + period);
        }
        // brodcast the updated objects
        session.writeBrodcast(message, periodList);
    }

    @Override
    public void execute(ServerIoSession session, Message<Period> message) throws ServiceException,
            SQLException {
        // throw an execption because the 'exec' command is not implemented
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        throw new NoSuchCommandException(handler, command);
    }
}

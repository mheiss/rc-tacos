package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class MobilePhoneHandler implements Handler<MobilePhoneDetail> {

    @Service(clazz = MobilePhoneService.class)
    private MobilePhoneService phoneService;

    @Override
    public void add(ServerIoSession session, Message<MobilePhoneDetail> message)
            throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        // loop and add the phones
        for (MobilePhoneDetail phone : phoneList) {
            int id = phoneService.addMobilePhone(phone);
            if (id == -1)
                throw new ServiceException("Failed to add the mobile phone:" + phone);
            phone.setId(id);
        }
        // brodcast the added phones
        session.writeBrodcast(message, phoneList);
    }

    @Override
    public void get(ServerIoSession session, Message<MobilePhoneDetail> message)
            throws ServiceException, SQLException {
        // query the mobile phones
        List<MobilePhoneDetail> phoneList = phoneService.listMobilePhones();
        if (phoneList == null)
            throw new ServiceException("Failed to list the mobile phones");
        // send back the results
        session.write(message, phoneList);
    }

    @Override
    public void remove(ServerIoSession session, Message<MobilePhoneDetail> message)
            throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        // loop and remove the phones
        for (MobilePhoneDetail phone : phoneList) {
            if (!phoneService.removeMobilePhone(phone.getId()))
                throw new ServiceException("Failed to remove the mobile phone:" + phone);
        }
        // brodcast the removed phones
        session.writeBrodcast(message, phoneList);
    }

    @Override
    public void update(ServerIoSession session, Message<MobilePhoneDetail> message)
            throws ServiceException, SQLException {
        List<MobilePhoneDetail> phoneList = message.getObjects();
        // loop and update the phones
        for (MobilePhoneDetail phone : phoneList) {
            if (!phoneService.updateMobilePhone(phone))
                throw new ServiceException("Failed to update the mobile phone:" + phone);
        }
        // brodcast the updated phones
        session.writeBrodcast(message, phoneList);
    }

    @Override
    public void execute(ServerIoSession session, Message<MobilePhoneDetail> message)
            throws ServiceException, SQLException {
        // throw an execption because the 'exec' command is not implemented
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        throw new NoSuchCommandException(handler, command);
    }
}

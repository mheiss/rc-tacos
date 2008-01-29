package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.MobilePhoneManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;


/**
 * This class will be notified uppon mobile phone changes
 * @author Michael
 */
public class MobilePhoneListener extends ClientListenerAdapter
{
    @Override
    public void add(AbstractMessage addMessage)
    {
        ModelFactory.getInstance().getMobilePhoneManager().add((MobilePhoneDetail)addMessage);
    }

    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        MobilePhoneManager manager = ModelFactory.getInstance().getMobilePhoneManager();
        manager.resetPhones();
        for(AbstractMessage msg:listMessage)
        {
            MobilePhoneDetail detail = (MobilePhoneDetail)msg;
            manager.add(detail);
        }
    }

    @Override
    public void remove(AbstractMessage removeMessage)
    {
        ModelFactory.getInstance().getMobilePhoneManager().remove((MobilePhoneDetail)removeMessage);
    }

    @Override
    public void update(AbstractMessage updateMessage)
    {
        ModelFactory.getInstance().getMobilePhoneManager().update((MobilePhoneDetail)updateMessage);
    }
}

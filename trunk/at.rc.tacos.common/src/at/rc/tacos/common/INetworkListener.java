package at.rc.tacos.common;

import java.util.ArrayList;

/**
 * This interface describes the methods that the service
 * layer must provide to receive network updates.
 * @author Michael
 */
public interface INetworkListener
{
    /** Fired when new data arrived */
    public void fireNetMessage(String type,String action,ArrayList<IXMLObject> objects);
}

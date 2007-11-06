package at.rc.tacos.core.service;

import at.rc.tacos.common.INetClientLayer;
import at.rc.tacos.common.INetServerLayer;
import at.rc.tacos.core.net.NetWrapper;

public class NetLayerImpl implements INetServerLayer,INetClientLayer
{
    @Override
    public void requestAddItem(String item)
    {
        NetWrapper.getDefault().requestAddItem(item);
    }

    @Override
    public void itemAdded(String item)
    {
       ServiceWrapper.getDefault().getNetManager().fireItemAdded(item);
    }
}

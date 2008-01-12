package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.VehicleDetail;

/**
 * Sends a update message for the given vehicle to the server
 * @author Michael
 */
public class VehicleUpdateAction extends Action
{
    private VehicleDetail detail;

    public VehicleUpdateAction(VehicleDetail detail)
    {
        this.detail = detail;
    }

    @Override
    public void run()
    {
        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
    }
}


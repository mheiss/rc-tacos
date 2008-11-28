package at.rc.tacos.server.tasks.impl;

import at.rc.tacos.server.tasks.AbstractTask;

/**
 * The <code>TransportTask</code> is a background task that the status of the transports on a
 * regular base. When a prebooked transport is within the next 2 hours then the task updates the
 * transport and moves it to the outstanding transports by changing the status and sending a update
 * request.
 * <p>
 * After the transport has been updated a notifcation will be send to the client.
 * </p>
 */
public class TransportTask extends AbstractTask {
    
    //the scheduling base
    private final static int INTERVAL = 2;

    /**
     * Creates a new transport background task
     */
    public TransportTask() {
        super("TransportTask",INTERVAL);
    }

    @Override
    public void runTask() {
        
        // query all open transports
        
        // do the update

    }

}

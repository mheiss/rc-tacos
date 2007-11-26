package at.rc.tacos.client.util;

import at.rc.tacos.client.Activator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Util class for frequently used methods
 * @author Michael
 */
public class Util
{
    //section image
    public final static String APP_LOGO = "icons/logo.jpg";
    
    //paths to the grey pictures that means there is no information available
    public final static String IMAGE_MOBILE_PHONE_PATH_GREY = "icons/vehicle/handy_na.gif";
    public final static String IMAGE_VEHICLE_NOTES_PATH_GREY = "icons/vehicle/notes_na.gif";
    public final static String IMAGE_CURRENT_STATION_PATH_GREY = "icons/vehicle/haus_na.gif";
    public final static String IMAGE_READY_FOR_ACTION_PATH_GREY = "icons/vehicle/ok_na.gif";
    public final static String IMAGE_OUT_OF_ORDER_PATH_GREY = "icons/vehicle/repair_na.gif";
    
    //paths to the black pictures that means there is some information available
    public final static String IMAGE_MOBILE_PHONE_PATH_BLACK = "icons/vehicle/handy.gif";
    public final static String IMAGE_VEHICLE_NOTES_PATH_BLACK = "icons/vehicle/notes.gif";
    public final static String IMAGE_CURRENT_STATION_PATH_BLACK = "icons/vehicle/haus.gif";
    public final static String IMAGE_READY_FOR_ACTION_PATH_BLACK = "icons/vehicle/ok.gif";
    public final static String IMAGE_OUT_OF_ORDER_PATH_BLACK = "icons/vehicle/repair.gif";
    
    //paths to the status
    public final static String IMAGE_TRANSPORT_STATUS_PATH_RED = "icons/vehicle/ampel_rot.gif";
    public final static String IMAGE_TRANSPORT_STATUS_PATH_YELLOW = "icons/vehicle/ampel_gelb.gif";
    public final static String IMAGE_TRANSPORT_STATUS_PATH_GREEN = "icons/vehicle/ampel_gruen.gif";
    public final static String IMAGE_TRANSPORT_STATUS_PATH_GREY = "icons/vehicle/ampel.gif";
    
    /**
     * Loads the given image and returns it.
     * @param nameFilePath the path and the name of the image
     * @return the created image
     */
    public static Image getImagePath(String nameFilePath)
    {
        return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, nameFilePath).createImage();

    }
}

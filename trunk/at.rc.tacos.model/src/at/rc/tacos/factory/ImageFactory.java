package at.rc.tacos.factory;

import java.util.HashMap;
import org.eclipse.swt.graphics.Image;

/**
 * The image facotry manages all available images
 * @author Michael
 */
public class ImageFactory
{
    //instance
    private static ImageFactory instance;
    
    /** List of all images */
    private HashMap<String, Image> images;
    
    /**
     * Default private constructor
     */
    private ImageFactory() 
    {
        images = new HashMap<String, Image>();
    }
    
    /**
     * Returns a valid instance of this facotry
     * to use.
     */
    public static ImageFactory getInstance()
    {
        if(instance == null)
            instance = new ImageFactory();
        return instance;
    }
    
    /**
     * Registers a image in the image facotry.<br>
     * When two images are registered with the same id
     * the older one will be replaced.
     * @param imageID the id for the image to register
     * @param image the image itself
     */
    public void registerImage(String imageID,Image image)
    {
        images.put(imageID,image);
    }
    
    /**
     * Returns the image registered with the given image
     * identification.
     * @param imageId the identification string of the image
     * @return the registered image or null if no image was found
     */
    public Image getRegisteredImage(String imageId)
    {
        return images.get(imageId);
    }
}

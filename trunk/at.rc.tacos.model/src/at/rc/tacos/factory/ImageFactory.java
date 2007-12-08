package at.rc.tacos.factory;

import java.util.HashMap;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The image facotry manages all available images
 * @author Michael
 */
public class ImageFactory
{
    //instance
    private static ImageFactory instance;
    
    /** List of all images */
    private HashMap<String, ImageDescriptor> images;
    
    /**
     * Default private constructor
     */
    private ImageFactory() 
    {
        images = new HashMap<String, ImageDescriptor>();
    }
    
    /**
     * Returns a valid instance of this facotry to use.
     * @return the shared instance
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
     * @param imageDescriptor the image descriptor
     */
    public void registerImage(String imageID,ImageDescriptor imageDescriptor)
    {
        images.put(imageID,imageDescriptor);
    }
    
    /**
     * Returns the image registered with the given image
     * identification.
     * @param imageId the identification string of the image
     * @return the registered image or null if no image was found
     */
    public Image getRegisteredImage(String imageId)
    {
        return images.get(imageId).createImage();
    }
    
    /**
     * Returns the imagedescriptor registered with the given
     * identification string.
     * @param imageId the identification string of the image
     * @return the registered imageDescriptor or null if no image was found
     */
    public ImageDescriptor getRegisteredImageDescriptor(String imageId)
    {
        return images.get(imageId);
    }
}

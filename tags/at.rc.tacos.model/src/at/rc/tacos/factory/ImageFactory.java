package at.rc.tacos.factory;

import java.util.HashMap;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The image factory manages all available images.
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
    	if(!images.containsKey(imageId))
    	{
    		System.out.println("The image for the key: "+imageId +" cannot be found");
    		throw new IllegalArgumentException("The image for the key: "+imageId +" cannot be found");
    	}
    	if(images.get(imageId) == null)
    	{
    		System.out.println("The image file for the key: "+ imageId +" cannot be found");
    		throw new IllegalArgumentException("The image file for the key: "+ imageId +" cannot be found");
    	}
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
    	if(!images.containsKey(imageId))
    	{
    		System.out.println("The image description for the key: "+imageId +" cannot be found");
    		throw new IllegalArgumentException("The image description for the key: "+imageId +" cannot be found");
    	}
    	if(images.get(imageId) == null)
    	{
    		System.out.println("The image file for the key:"+ imageId +" cannot be found");
    		throw new IllegalArgumentException("The image file for the key:"+ imageId +" cannot be found");
    	}
        return images.get(imageId);
    }
}

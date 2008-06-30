package at.rc.tacos.server;

import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.factory.ImageFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.server";
	// Configuration file for the images
	public static final String IMAGE_SERVER_CONFIG_PATH = "at.rc.tacos.server.config.images";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() { }

	/**
	 * Startup the plugin
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
		//initialize log4j
		PropertyConfigurator.configureAndWatch("log4j.properties", 60*1000 );
		//load all needed images and register them
		loadAndRegisterImages();
	}

	/**
	 * Shutdown and stop the plugin
	 */
	public void stop(BundleContext context) throws Exception 
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static Activator getDefault() 
	{
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) 
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Logs the given message
	 * @param message the message
	 * @param type the type of the message
	 */
	public static void log(String message,int type)
	{
		Status status = new Status(type,Activator.PLUGIN_ID,message); 
		Activator.getDefault().getLog().log(status);
	}
	
	/**
	 * Loads all image files from the image.properties 
	 * and registers them in the application.<br>
	 * The images can be accessed through the key value of the
	 * properties file.
	 */
	private void loadAndRegisterImages()
	{
		try
		{
			//the factory to register the images
			ImageFactory f = ImageFactory.getInstance();
			//open the properties file
			ResourceBundle imageBundle = ResourceBundle.getBundle(Activator.IMAGE_SERVER_CONFIG_PATH);
			//loop and register all images
			for(String imageKey:imageBundle.keySet())
			{
				//Create the image file with the given path
				String imagePath = imageBundle.getString(imageKey);
				ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
				f.registerImage(imageKey, imageDescriptor);
			}
		}
		catch(NullPointerException npe)
		{
			Activator.log("Please check the images and the properties file", IStatus.ERROR);
			System.out.println("Failed to load the images files");
			System.out.println("Please check the images and the properties file");
			npe.printStackTrace();
		}
	}
}

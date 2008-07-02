package at.rc.tacos.server;

import java.lang.reflect.Constructor;
import java.util.ResourceBundle;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.codec.MessageDecoder;
import at.rc.tacos.codec.MessageEncoder;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.ServerListenerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.server";
	// Configuration file for the images
	public static final String IMAGE_CONFIG_PATH = "at.rc.tacos.server.config.images";
	public static final String SERVER_CONFIG_PATH = "at.rc.tacos.server.config.server";

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
		registerModelListeners();
		registerEncodersAndDecoders();
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
		//the factory to register the images
		ImageFactory f = ImageFactory.getInstance();
		//open the properties file
		ResourceBundle imageBundle = ResourceBundle.getBundle(IMAGE_CONFIG_PATH);
		//loop and register all images
		for(String imageKey:imageBundle.keySet())
		{
			try
			{
				//Create the image file with the given path
				String imagePath = imageBundle.getString(imageKey);
				ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
				f.registerImage(imageKey, imageDescriptor);
			}
			catch(NullPointerException npe)
			{
				Activator.log("Failed to load the image for the key: "+imageKey, IStatus.ERROR);
				npe.printStackTrace();
			}
		}
	}

	/**
	 * Loads and registeres the encoders and decoders from the properties file
	 */
	private void registerEncodersAndDecoders()
	{
		//the class loader params
		Class<?>[] classParm = null;
		Object[] objectParm = null;
		
		//the factory to register the encoders
		ProtocolCodecFactory codecFactory = ProtocolCodecFactory.getDefault();
		//open the proerties file
		ResourceBundle codecBundle = ResourceBundle.getBundle(SERVER_CONFIG_PATH);
		//loop and register all decoders
		for(String codecKey:codecBundle.keySet())
		{
			try
			{				
				//assert this is a codec
				if(!codecKey.startsWith("decoder") &! codecKey.startsWith("encoder"))
					continue;

				//get the id of the decoder
				String codecId = codecKey.substring(codecKey.indexOf(".")+1,codecKey.length());
				String codecClass = codecBundle.getString(codecKey);

				//load and create a new instance of the class
				Class<?> cl = Class.forName(codecClass);
				Constructor<?> co = cl.getConstructor(classParm);

				//assert that this is a decoder
				if(codecKey.startsWith("decoder"))
				{
					MessageDecoder decoder = (MessageDecoder)co.newInstance(objectParm);
					codecFactory.registerDecoder(codecId, decoder);
				}
				//assert that this is a encoder
				if(codecKey.startsWith("encoder"))
				{
					MessageEncoder encoder = (MessageEncoder)co.newInstance(objectParm);
					codecFactory.registerEncoder(codecId, encoder);
				}
			}
			catch(Exception e)
			{
				Activator.log("Failed to load the codec for the key: "+codecKey +" (Class not found: "+e.getMessage()+")", IStatus.ERROR);
			}
		}
	}

	/**
	 * Loads and registers the listener classes from the properties file
	 */
	private void registerModelListeners()
	{		
		//the factory to register the encoders
		ServerListenerFactory listenerFactory = ServerListenerFactory.getInstance();
		//open the proerties file
		ResourceBundle listenerBundle = ResourceBundle.getBundle(SERVER_CONFIG_PATH);
		//loop and register all decoders
		for(String listenerKey:listenerBundle.keySet())
		{
			try
			{				
				//assert this is a listener
				if(!listenerKey.startsWith("listener"))
					continue;

				//get the id of the decoder
				String listenerId = listenerKey.substring(listenerKey.indexOf(".")+1,listenerKey.length());
				String listenerClass = listenerBundle.getString(listenerKey);

				//load and create a new instance of the class
				Class<?> cl = Class.forName(listenerClass);
				listenerFactory.addListener(listenerId, (Class<?>)cl);
			}
			catch(Exception e)
			{
				Activator.log("Failed to load the listener for the key: "+listenerKey +" (Class not found: "+e.getMessage() +")", IStatus.ERROR);
			}
		}
	}
}

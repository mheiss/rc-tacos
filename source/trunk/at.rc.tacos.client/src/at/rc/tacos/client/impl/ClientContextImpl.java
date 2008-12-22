package at.rc.tacos.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.config.ClientConfiguration;
import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.net.listeners.DataChangeListenerFactory;
import at.rc.tacos.platform.xstream.XStream2;

/**
 * The client context handles the resources and factory implementations for the
 * client application
 * 
 * @author Michael
 */
public class ClientContextImpl implements ClientContext {

	private Logger log = LoggerFactory.getLogger(ClientContextImpl.class);

	private File configurationFile;
	private ClientConfiguration clientConfiguration;
	private HandlerFactory handlerFactory;
	private DataChangeListenerFactory dataChangeFactory;

	// the xstream instance to load and persist
	private XStream2 xStream = new XStream2();

	/**
	 * Default class constructor to create a new client context instance
	 * 
	 * @param configurationFile
	 *            the configuation file for the client to use
	 */
	public ClientContextImpl(File configurationFile) {
		this.configurationFile = configurationFile;
		this.handlerFactory = new HandlerFactoryImpl();
		this.dataChangeFactory = new DataChangeFactoryImpl();
		this.clientConfiguration = new ClientConfiguration();
	}

	/**
	 * Loads the persistet configuration from the specified configuration file
	 */
	public void loadConfiguration() throws Exception {
		// check the config file
		if (configurationFile == null | !configurationFile.exists()) {
			log.warn("The configuration file 'config.xml' cannot be found in the workspace");
			return;
		}
		// load the configuration
		clientConfiguration = xStream.extFromXML(new FileInputStream(configurationFile), ClientConfiguration.class);
	}

	/**
	 * Persists the current configuration to the specified configuration file.
	 */
	public void storeConfiguration() throws Exception {
		XStream2 xStream = new XStream2();
		xStream.extToXML(clientConfiguration, new FileOutputStream(configurationFile));
	}

	/**
	 * Persists the current configuration
	 */

	/**
	 * Returns the current implementation of the {@linkplain HandlerFactory}.
	 * 
	 * @return the handler factory implementation
	 */
	@Override
	public HandlerFactory getHandlerFactory() {
		return handlerFactory;
	}

	/**
	 * Returns the current implementation of the
	 * {@link DataChangeListenerFactory}.
	 * 
	 * @return the data change listener factory implementation
	 */
	@Override
	public DataChangeListenerFactory getDataChangeListenerFactory() {
		return dataChangeFactory;
	}

	/**
	 * Returns the current {@link ClientConfiguration} instance that is attached
	 * to this configuration
	 * 
	 * @return the client configuration
	 */
	@Override
	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}

	@Override
	public File getConfigurationFile() {
		return configurationFile;
	}
}

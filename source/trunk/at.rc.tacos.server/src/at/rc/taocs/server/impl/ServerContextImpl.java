package at.rc.taocs.server.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.config.ServerConfiguration;
import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.net.handler.HandlerFactory;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.DbalServiceFactory;
import at.rc.tacos.platform.xstream.XStream2;
import at.rc.tacos.server.dbal.DataSourceImpl;
import at.rc.tacos.server.net.HandlerFactoryImpl;

/**
 * The server context manages and handles the resources for the application
 * 
 * @author Michael
 */
public class ServerContextImpl implements ServerContext {

	private Logger log = LoggerFactory.getLogger(ServerContextImpl.class);

	// the configuration file
	private File configurationFile;
	private ServerConfiguration serverConfiguration;

	// the handlers
	private HandlerFactory handlerFactory;
	private DbalServiceFactory serviceFactory;
	private DataSource dataSource;

	// the xstream instance to load and persist
	private XStream2 xStream = new XStream2();

	/**
	 * Default class constructor to create a new instance
	 */
	public ServerContextImpl(File configurationFile) {
		this.configurationFile = configurationFile;
		this.handlerFactory = new HandlerFactoryImpl();
		this.serviceFactory = new ServiceFactoryImpl();
		this.dataSource = new DataSourceImpl();
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
		serverConfiguration = xStream.extFromXML(new FileInputStream(configurationFile), ServerConfiguration.class);
	}

	/**
	 * Persists the current configuration to the specified configuration file.
	 */
	public void storeConfiguration() throws Exception {
		XStream2 xStream = new XStream2();
		xStream.extToXML(serverConfiguration, new FileOutputStream(configurationFile));
	}

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
	 * Returns the current implementation of the {@linkplain DbalServiceFactory}
	 * .
	 * 
	 * @return the service factory implementation.
	 */
	@Override
	public DbalServiceFactory getDbalServiceFactory() {
		return serviceFactory;
	}

	/**
	 * Returns the current implementation of the {@linkplain DataSource}.
	 * 
	 * @return the data source implementation
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public ServerConfiguration getServerConfiguration() {
		return serverConfiguration;
	}

	public void setServerConfiguration(ServerConfiguration serverConfiguration) {
		this.serverConfiguration = serverConfiguration;
	}

	@Override
	public File getConfigurationFile() {
		return configurationFile;
	}
}

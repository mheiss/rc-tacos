package at.rc.tacos.platform.log;

import org.apache.log4j.Logger;

/**
 * This class provides logging capabilities for other plugins
 * @author Michael
 */
public class LogFactory 
{
	/**
	 * Default class constructor
	 */
	private LogFactory() {
		//prevent instantiation
	}	
	
	/**
	 * Log a detailed message with log4j
	 */
	public static void trace(Object clazz,String message)
	{
		Logger logger = Logger.getLogger(clazz.getClass());
		logger.trace(message);
	}
	
	/**
	 * Log a debug message with log4j
	 */
	public static void debug(Object clazz,String message)
	{
		Logger logger = Logger.getLogger(clazz.getClass());
		logger.trace(message);
	}
	
	/**
	 * Log a information message with log4j
	 */
	public static void info(Object clazz,String message)
	{
		Logger logger = Logger.getLogger(clazz.getClass());
		logger.trace(message);
	}
	
	/**
	 * Log a error message with log4j
	 */
	public static void error(Object clazz,String message)
	{
		Logger logger = Logger.getLogger(clazz.getClass());
		logger.trace(message);
	}
}

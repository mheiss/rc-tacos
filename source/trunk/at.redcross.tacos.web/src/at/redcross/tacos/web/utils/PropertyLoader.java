package at.redcross.tacos.web.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The {@code PropertyLoader} provides convenient methods to load files from the
 * classpath.
 */
public class PropertyLoader {

    private final static Log logger = LogFactory.getLog(PropertyLoader.class);

    /**
     * Looks up a resource named 'name' in the classpath.
     * 
     * @param name
     *            classpath resource name
     * @return the resources as {@code Properties} object or {@code null} if the
     *         specified property is not existing or cannot be loaded.
     */
    public static Properties loadProperties(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The resource to load cannot be null");
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        InputStream in = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            in = loader.getResourceAsStream(name);
            if (in == null) {
                return null;
            }
            Properties result = new Properties();
            result.load(in);
            return result;
        }
        catch (Exception ex) {
            logger.error("Failed to load '" + name + "' ", ex);
            return null;
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }
}

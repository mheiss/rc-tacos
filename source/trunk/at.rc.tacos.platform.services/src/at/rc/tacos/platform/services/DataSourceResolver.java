package at.rc.tacos.platform.services;

import java.lang.annotation.Annotation;
import java.sql.Connection;

import javax.annotation.Resource;

/**
 * Provides methods to resolve the @link {@link Resource} annotations and use
 * reflection to set up the needed data source instance.
 * 
 * @author mheiss
 */
public class DataSourceResolver extends AnnotationResolver {

	private Connection connection;

	/**
	 * Default class constructor to setup a new resolver instance
	 * 
	 * @param connection
	 *            the connection to inject
	 */
	public DataSourceResolver(Connection connection) {
		super(Resource.class);
		this.connection = connection;
	}

	@Override
	protected Object annotationFound(Annotation annotation) throws Exception {
		if (!(annotation instanceof Resource))
			return null;
		return connection;
	}
}

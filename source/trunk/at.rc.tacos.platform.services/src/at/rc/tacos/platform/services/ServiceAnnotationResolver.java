package at.rc.tacos.platform.services;

import java.lang.annotation.Annotation;

import at.rc.tacos.platform.services.exception.NoSuchServiceException;

/**
 * Provides methods to resolve the needed resources
 * 
 * @author mheiss
 */
public class ServiceAnnotationResolver extends AnnotationResolver {

	private ServiceFactory factory;

	/**
	 * Default class constructor to handle @link {@link Service} annotations.
	 * The @link {@link ServiceFactory} will be used to resolve the services
	 * that are referenced by the annotations.
	 * 
	 * @param factory
	 *            the factory to resolve the annotation reference
	 */
	public ServiceAnnotationResolver(ServiceFactory factory) {
		super(Service.class);
		this.factory = factory;
	}

	@Override
	protected Object annotationFound(Annotation annotation) throws Exception {
		if (!(annotation instanceof Service)) {
			return null;
		}
		Service service = (Service) annotation;
		Class<?> nextClass = service.clazz();
		Object nextObject = factory.getService(nextClass.getName());
		if (nextObject == null)
			throw new NoSuchServiceException(nextClass.getName());
		return nextObject;
	}

}

package at.rc.tacos.platform.services;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The service annotation marks a service that is needed by the application. The
 * service can be identified by the fully qualified class name or by the class
 * itself.
 * 
 * @author Michael
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Service {

	public Class<?> clazz();
}

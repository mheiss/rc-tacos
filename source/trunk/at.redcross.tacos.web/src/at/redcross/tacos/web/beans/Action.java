package at.redcross.tacos.web.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for describing service layer security attributes.
 * <p>
 * The <code>Action</code> annotation is used to mark business methods so that
 * they are available for the resource management.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Action {

}

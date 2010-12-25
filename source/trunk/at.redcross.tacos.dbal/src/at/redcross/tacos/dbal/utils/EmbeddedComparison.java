package at.redcross.tacos.dbal.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a given field should be fully analyzed as part of the parent
 * element.
 * <p>
 * This is only a marker annotations.
 * </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface EmbeddedComparison {

}

package at.rc.tacos.platform.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches for the given annotation and try to recursively resolve the fields
 * that are labeled with the annotation.
 * 
 * @author Michael
 */

public abstract class AnnotationResolver {

	// the annotation
	private Class<? extends Annotation> annotation;

	/**
	 * Default class constructor defining the annotation to look for
	 */
	public AnnotationResolver(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	public List<Object> resolveAnnotations(Object... objects) throws Exception {
		List<Object> resolved = new ArrayList<Object>();
		for (Object obj : objects) {
			resolveAnnotation(resolved, obj);
		}
		return resolved;
	}

	private void resolveAnnotation(List<Object> resolved, Object currentObject) throws Exception {
		// add to the list of resolved objects
		if (currentObject != null) {
			resolved.add(currentObject);
		}

		// get the class for the object
		Class<?> clazz = currentObject.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			// check if the field has the annotation
			if (!field.isAnnotationPresent(annotation)) {
				continue;
			}
			field.setAccessible(true);
			// resolve the value of the field
			Object nextObject = annotationFound(field, field.getAnnotation(annotation), currentObject);
			if (nextObject == null)
				continue;
			// check for other dependend classes
			resolveAnnotation(resolved, nextObject);
		}
	}

	/**
	 * Processes the annotation that was found at the given field. If the
	 * returned values is not null then this object will also be checked for
	 * annotation.
	 * 
	 * @param field
	 *            the field where the annotation was found
	 * @param annotation
	 *            the annotation that was found
	 * @param currentInstance
	 *            the current instance of the class that is in progress
	 * @return the instance of the field object or null to abort the resolving
	 */
	protected abstract Object annotationFound(Field field, Annotation annotation, Object currentInstance) throws Exception;

}

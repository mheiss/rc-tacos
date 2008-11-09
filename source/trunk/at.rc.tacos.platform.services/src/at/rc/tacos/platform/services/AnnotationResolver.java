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

	public List<Object> resolveAnnotations(Object currentObject) throws Exception {
		List<Object> resolved = new ArrayList<Object>();
		resolveAnnotation(resolved, currentObject);
		return resolved;
	}

	private void resolveAnnotation(List<Object> resolved, Object currentObject) throws Exception {
		resolved.add(currentObject);
		// get the class for the object
		Class<?> clazz = currentObject.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			// check if the field has the annotation
			if (!field.isAnnotationPresent(annotation))
				continue;
			field.setAccessible(true);
			// resolve the value of the field
			Object nextObject = annotationFound(field.getAnnotation(annotation));
			// create a new instance of this class and pass to the object
			field.set(currentObject, nextObject);
			// check for other dependend classes
			resolveAnnotation(resolved, nextObject);
		}
	}

	/**
	 * Processes the annotation that was found at the given field. The returned
	 * value will be used to set the value of the field.
	 * 
	 * @param annotation
	 *            the annotation that was found
	 * @return the value for the field that was marked with the annotation
	 */
	protected abstract Object annotationFound(Annotation annotation) throws Exception;

}

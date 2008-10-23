package my.example.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Provides methods to resolve the needed resources
 * 
 * @author mheiss 
 */
public class AnnotationResolver {

    // the annotation
    private Class<? extends Annotation> annotation;

    /**
     * Default class constructor defining the annotation to look for
     */
    public AnnotationResolver(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public void resolveAnnotation(List<Object> resolved,Object currentObject) throws Exception {
        resolved.add(currentObject);
        // get the class for the object
        Class<?> clazz = currentObject.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            // check if the field has the annotation
            if (!field.isAnnotationPresent(annotation))
                continue;
            field.setAccessible(true);
            
            //get the required message class
            Message nextMessage = (Message)field.getAnnotation(annotation);
            Class<?> nextClass = nextMessage.clazz();
            Object nextObject = nextClass.newInstance();
            
            //create a new instance of this class and pass to the object
            System.out.println("setting the field "+field.getName()+" for "+currentObject.getClass().getName()+ " to "+nextObject.getClass().getName());
            field.set(currentObject,nextObject);
            
            //check for other dependend classes
            resolveAnnotation(resolved, nextObject);
        }
    }

}

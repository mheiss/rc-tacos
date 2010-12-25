package at.redcross.tacos.dbal.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class EntityUtils {

    /** Returns a collection with the given elements */
    public static Collection<Object> convert(EntityImpl... input) {
        Collection<Object> list = new ArrayList<Object>();
        for (EntityImpl entity : input) {
            list.add(entity);
        }
        return list;
    }

    /**
     * Returns the maximum allowed length of a given column.
     * 
     * @param clazz
     *            the class that contains the column definition
     * @param fieldName
     *            the name of the field that defines the column
     * @return the column length or -1
     */
    public static int getColumnLength(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                return -1;
            }
            return column.length();
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * Returns a list of all entity classes for the given package
     */
    public static List<Class<?>> listEntityClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(packageName + " (" + directory
                    + ") does not appear to be a valid package");
        }
        if (!directory.exists()) {
            throw new IllegalArgumentException("Package '" + packageName + "' is not existing");
        }

        String[] files = directory.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".class")) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < files.length; i++) {
            String clazzName = files[i].substring(0, files[i].length() - 6);
            Class<?> clazzForName = Class.forName(packageName + '.' + clazzName);
            if (!clazzForName.isAnnotationPresent(Table.class)) {
                continue;
            }
            classes.add(clazzForName);
        }
        return classes;
    }
}

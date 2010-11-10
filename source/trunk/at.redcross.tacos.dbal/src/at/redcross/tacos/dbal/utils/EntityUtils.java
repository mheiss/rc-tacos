package at.redcross.tacos.dbal.utils;

import java.lang.reflect.Field;

import javax.persistence.Column;

public class EntityUtils {

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
}

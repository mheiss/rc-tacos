package at.redcross.tacos.dbal.utils;

/** Contains common helper methods to work with strings */
public class StringHelper {

    /** Returns whether or not the string is empty or null */
    public static boolean isNullOrEmpty(String input) {
        if (input == null) {
            return true;
        }
        return input.trim().isEmpty();
    }

    /** Changes the common wildcard '*' with the HQL version '%' */
    public static String toHqlWildcard(String input) {
        String string = input.trim();
        if (string.endsWith("*")) {
            string = string.substring(0, string.length() - 1);
            string = string + "%";
        }
        return string;
    }

}

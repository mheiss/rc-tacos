package at.redcross.tacos.web.utils;

public class StringUtils {

    public static String saveString(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    public static boolean isEmpty(String input) {
        return saveString(input).isEmpty();
    }

}

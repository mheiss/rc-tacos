package at.rc.tacos.client.ui.utils;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.dialogs.IInputValidator;

/**
 * The <code>TimeValidator</code> checks if a given input string is a valid
 * time.
 * <p>
 * The accepted input patterns are <b>HH:mm</b> or <b>HHmm</b>
 * </p>
 * 
 * @author Michael
 */
public class TimeValidator implements IInputValidator {

	// the patterns to validate the input
	private final static String pattern1 = "HH:mm";
	private final static String pattern2 = "HHmm";

	/**
	 * Validates the input string and returns whether or not this is a valid
	 * time.
	 * 
	 * @param input
	 *            the string to validate
	 * @return null if the {@code input} is a valid date or an error message
	 */
	@Override
	public String isValid(String input) {
		// try to validate the date utils
		try {
			DateUtils.parseDate(input, new String[] { pattern1, pattern2 });
		}
		catch (Exception e) {
			return "Keine gültige Uhrzeit.\n Bitte im Format HH:mm oder HHmm eingeben";
		}

		// this date is valid
		return null;
	}
}

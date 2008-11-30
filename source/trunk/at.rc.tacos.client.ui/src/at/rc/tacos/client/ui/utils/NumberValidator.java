package at.rc.tacos.client.ui.utils;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * This class validates numbers so that only numeric values can be entered
 * 
 * @author Michael
 */
public class NumberValidator implements IInputValidator {

	// the patterns to check
	private final static String pattern1 = "\\d{4,5}-\\d{4,8}";
	private final static String pattern2 = "\\d{4,5}-\\d{4,9}";

	@Override
	public String isValid(String input) {
		// validate the phone number
		if (input.matches(pattern1) || input.matches(pattern2)) {
			return null;
		}
		return "Dies ist keine gültige Telefonnummer\n"
				+ "Bitte die Vorwahl und die Nummer mit einem Bindestrich getrennt eingeben. Bsp: 0699-12345678";
	}
}

package at.rc.tacos.client.ui.validators;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * The <code>IntegerValidator</code> is a simple <code>IInputValidator</code>
 * that validates that the given text is a valid integer.
 * 
 * @author Michael
 */
public class IntegerValidator implements IInputValidator {

	@Override
	public String isValid(String newText) {
		try {
			Integer.parseInt(newText);
			return null;
		}
		catch (NumberFormatException nfe) {
			return "Bitte geben Sie einge gültige Zahl ein";
		}
	}
}

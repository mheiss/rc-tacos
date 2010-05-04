package at.rc.tacos.client.ui.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * The <code>StringValidator</code> is a simple <code>IInputValidator</code>
 * that validates that the given text only contains characters but not numbers.
 * 
 * @author Michael
 */
public class StringValidator implements IInputValidator {

	private final static Pattern pattern = Pattern.compile(".[0-9]+");

	@Override
	public String isValid(String newText) {
		if (newText == null) {
			return "Bitte geben Sie einen Text ein";
		}
		// remove the whitespace
		newText = newText.trim();
		// try to validate the text
		if (newText.isEmpty()) {
			return "Der eingegebene Text besteht nur aus Leerzeichen";
		}
		Matcher matcher = pattern.matcher(newText);
		if (matcher.matches()) {
			return "Bitte geben Sie nur Buchstaben und keine Zahlen ein";
		}
		// check
		return null;
	}
}

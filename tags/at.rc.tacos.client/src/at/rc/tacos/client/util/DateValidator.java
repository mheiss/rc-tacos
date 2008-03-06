package at.rc.tacos.client.util;

import org.eclipse.jface.dialogs.IInputValidator;


/**
 * This class validates a date. It makes sure that the given string
 * can be parsed into a valid date.
 * @author Michael
 */
public class DateValidator implements IInputValidator
{
	/**
	 * Validates the String. Returns null for no error, or an error message
	 * @param newDate the string to validate
	 * @return null if everything is ok, or the error message
	 */
	public String isValid(String newDate) 
	{
		if(Util.getTimestampFromTime(newDate) == -1)
			return "Dies ist keine gültige Uhrzeit\n" +
					"Bitte im Format hh:mm eingeben";

		// input is ok
		return null;
	}
}

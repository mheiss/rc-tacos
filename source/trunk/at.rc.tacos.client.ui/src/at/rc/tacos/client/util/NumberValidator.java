package at.rc.tacos.client.util;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * This class validates numbers so that only numeric values can be entered
 * @author Michael
 */
public class NumberValidator implements IInputValidator
{
	@Override
	public String isValid(String input) 
	{
		//validate the phone number
		String pattern1 = "\\d{4,5}-\\d{4,8}";
		String pattern2 = "\\d{4,5}-\\d{4,9}";
		if(input.matches(pattern1) || input.matches(pattern2))
		{
			return null;
		}
		else
		{
			return "Dies ist keine g�ltige Telefonnummer\n" +
			"Bitte die Vorwahl und die Nummer mit einem Bindestrich getrennt eingeben. Bsp: 0699-12345678";
		}
	}

}
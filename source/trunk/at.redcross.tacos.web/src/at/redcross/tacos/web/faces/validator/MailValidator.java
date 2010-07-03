package at.redcross.tacos.web.faces.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validator.mail")
public class MailValidator implements Validator {
	
	private final static Pattern PATTERN = Pattern.compile(".+@.+\\.[a-z]+");

    @Override
    public void validate(FacesContext context, UIComponent comp, Object value) throws ValidatorException {
        String enteredEmail = (String)value;
        Matcher m = PATTERN.matcher(enteredEmail);
        if (! m.matches()) {
        	FacesMessage message = new FacesMessage("Die eMail adresse ist nicht gültig.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

	
}

package at.redcross.tacos.web.faces.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validator.stringDate")
public class StringDateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent comp, Object value) throws ValidatorException {
        try {
            DateFormat format = new SimpleDateFormat("MM_yyyy");
            format.parse((String) value);
        }
        catch (ParseException ex) {
            FacesMessage message = new FacesMessage("Das eingegebene Datum ist nicht gültig");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}

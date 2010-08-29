package at.redcross.tacos.web.faces.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import at.redcross.tacos.web.faces.FacesUtils;

@FacesValidator("validator.passwordMatch")
public class PasswordMatchValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent comp, Object values) throws ValidatorException {
		final String parentId = comp.getNamingContainer().getId();

		// retrieve the values from the components
		String password = (String) FacesUtils.getComponentValue(context, parentId, "password");
		String password2 = (String) FacesUtils.getComponentValue(context, parentId, "password2");

		// we can only validate if we have all values
		if (password == null || password2 == null) {
			return;
		}
		if (!password.equals(password2)) {
			throw new ValidatorException(FacesUtils
					.createErrorMessage("Die Kennwörter stimmen nicht überein"));
		}
	}

}

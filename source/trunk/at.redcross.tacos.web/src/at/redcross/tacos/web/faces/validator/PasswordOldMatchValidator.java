package at.redcross.tacos.web.faces.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import at.redcross.tacos.web.faces.FacesUtils;

@FacesValidator("validator.passwordOldMatch")
public class PasswordOldMatchValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent comp, Object values) throws ValidatorException {
		final String parentId = comp.getNamingContainer().getId();

		// retrieve the values from the components
		String oldPassword = (String) FacesUtils.getComponentValue(context, parentId, "oldPassword");
		//TODO
		String passwordFromDatabase = null;

		// we can only validate if we have all values
		if (oldPassword == null || passwordFromDatabase == null) {
			return;
		}
		if (!oldPassword.equals(passwordFromDatabase)) {
			throw new ValidatorException(FacesUtils
					.createErrorMessage("Die Kennwörter stimmen nicht überein"));
		}
	}

}

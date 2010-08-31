package at.redcross.tacos.web.faces.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.web.beans.LocaleBean;
import at.redcross.tacos.web.beans.WebPermissionBean;
import at.redcross.tacos.web.faces.FacesUtils;

@FacesValidator("validator.futureDate")
public class FutureDateValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent comp, Object values) throws ValidatorException {
		final String parentId = comp.getNamingContainer().getId();

		// retrieve the values from the components
		Date startDate = (Date) FacesUtils.getComponentValue(context, parentId, "startDate");
		Date nowDate = FacesUtils.lookupBean(LocaleBean.class).getDate();
		if (startDate == null) {
			return;
		}

		// check if the user is able to create outdated entries
		WebPermissionBean webPermissionBean = FacesUtils.lookupBean(WebPermissionBean.class);
		if (webPermissionBean.isAuthorizedToCreateOutdatedRoster()) {
			return;
		}

		// the date must be in the future
		startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);
		nowDate = DateUtils.truncate(nowDate, Calendar.DAY_OF_MONTH);
		if (startDate.before(nowDate)) {
			FacesMessage message = new FacesMessage("Das Startdatum muss in der Zukunft liegen");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
}

package at.redcross.tacos.web.faces.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.utils.TacosDateUtils;

@FacesValidator("validator.fromTo")
public class FromToDateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent comp, Object values) throws ValidatorException {
        final String parentId = comp.getNamingContainer().getId();

        // retrieve the values from the components
        Date startTime = (Date) FacesUtils.getComponentValue(context, parentId, "startTime");
        Date startDate = (Date) FacesUtils.getComponentValue(context, parentId, "startDate");
        Date endTime = (Date) FacesUtils.getComponentValue(context, parentId, "endTime");
        Date endDate = (Date) FacesUtils.getComponentValue(context, parentId, "endDate");

        // we can only validate if we have all values
        if (startTime == null || startDate == null || endTime == null || endDate == null) {
            return;
        }

        Calendar startCal = TacosDateUtils.mergeDateAndTime(startDate, startTime);
        Calendar endCal = TacosDateUtils.mergeDateAndTime(endDate, endTime);
        if (startCal.after(endCal)) {
            FacesMessage message = new FacesMessage("Das Startdatum muss vor dem Enddatum liegen.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

}

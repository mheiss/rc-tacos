package at.redcross.tacos.web.faces.validator;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import at.redcross.tacos.web.utils.DateUtils;

@FacesValidator("validator.fromTo")
public class FromToValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent comp, Object values) throws ValidatorException {
        final String parentId = comp.getNamingContainer().getId();

        // retrieve the values from the components
        Date startTime = (Date) getValue(context, parentId, "startTime");
        Date startDate = (Date) getValue(context, parentId, "startDate");
        Date endTime = (Date) getValue(context, parentId, "endTime");
        Date endDate = (Date) getValue(context, parentId, "endDate");

        // we can only validate if we have all values
        if (startTime == null || startDate == null || endTime == null || endDate == null) {
            return;
        }

        Calendar startCal = DateUtils.mergeDateAndTime(startDate, startTime);
        Calendar endCal = DateUtils.mergeDateAndTime(endDate, endTime);
        if (startCal.after(endCal)) {
            FacesMessage message = new FacesMessage("Das Startdatum muss vor dem Enddatum liegen.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }

    /** Returns the value of the component */
    private Object getValue(FacesContext context, String formId, String componentId) {
        UIComponent component = context.getViewRoot().findComponent(formId + ":" + componentId);
        UIInput input = (UIInput) component;
        Object submittedValue = input.getSubmittedValue();
        return input.getConverter().getAsObject(context, component, (String) submittedValue);
    }
}

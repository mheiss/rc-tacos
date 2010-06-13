package at.redcross.tacos.web.faces.combo;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.richfaces.component.html.HtmlComboBox;

/**
 * The {@code DropDownValidator} is a custom {@linkplain Validator validator}
 * that validates whether or not a selected value is part of the predefined
 * values of the drop down composite.
 */
@FacesValidator(value = "validator.dropDown")
public class DropDownValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        HtmlComboBox box = (HtmlComboBox) component;
        for (UIComponent child : box.getChildren()) {
            UISelectItems childItem = (UISelectItems) child;

            @SuppressWarnings("unchecked")
            List<SelectItem> items = (List<SelectItem>) childItem.getValue();
            for (SelectItem item : items) {
                DropDownItem downItem = (DropDownItem) item.getValue();
                if (value.equals(downItem.getValue())) {
                    return;
                }
            }
        }

        FacesMessage message = new FacesMessage();
        message.setSummary("Das gewählte Element '" + value + "' existiert nicht");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(component.getId(), message);
        throw new ValidatorException(message);
    }

}

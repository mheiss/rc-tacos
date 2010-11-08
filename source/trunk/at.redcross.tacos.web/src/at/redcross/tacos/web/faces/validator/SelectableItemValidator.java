package at.redcross.tacos.web.faces.validator;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import at.redcross.tacos.web.model.SelectableItem;

@FacesValidator(value = "validator.selectableItem")
public class SelectableItemValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        UIComponentBase componentBase = (UIComponentBase) component;
        for (UIComponent child : componentBase.getChildren()) {
            UISelectItems childItem = (UISelectItems) child;

            @SuppressWarnings("unchecked")
            List<SelectItem> items = (List<SelectItem>) childItem.getValue();
            for (SelectItem item : items) {
                SelectableItem downItem = (SelectableItem) item.getValue();
                if (value.equals(downItem.getValue())) {
                    return;
                }
            }
        }
        FacesMessage message = new FacesMessage();
        message.setSummary("Das gewählte Element '" + value + "' existiert nicht");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
    }
}

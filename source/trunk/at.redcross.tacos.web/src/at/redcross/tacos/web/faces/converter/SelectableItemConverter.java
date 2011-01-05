package at.redcross.tacos.web.faces.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import at.redcross.tacos.web.model.SelectableItem;
import at.redcross.tacos.web.utils.StringUtils;

@FacesConverter(value = "converter.selectableItem")
public class SelectableItemConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        UIComponentBase componentBase = (UIComponentBase) component;
        value = StringUtils.saveString(value).trim();
        for (UIComponent child : componentBase.getChildren()) {
            UISelectItems childItem = (UISelectItems) child;

            @SuppressWarnings("unchecked")
            List<SelectItem> items = (List<SelectItem>) childItem.getValue();
            for (SelectItem item : items) {
                SelectableItem downItem = (SelectableItem) item.getValue();
                if (value.equals(downItem.getLabel())) {
                    return downItem.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        UIComponentBase componentBase = (UIComponentBase) component;
        for (UIComponent child : componentBase.getChildren()) {
            UISelectItems childItem = (UISelectItems) child;

            @SuppressWarnings("unchecked")
            List<SelectItem> items = (List<SelectItem>) childItem.getValue();
            for (SelectItem item : items) {
                SelectableItem downItem = (SelectableItem) item.getValue();
                if (downItem.equals(value)) {
                    return downItem.getLabel();
                }
                if (downItem.getValue().equals(value)) {
                    return downItem.getLabel();
                }
            }
        }
        return "";
    }

}

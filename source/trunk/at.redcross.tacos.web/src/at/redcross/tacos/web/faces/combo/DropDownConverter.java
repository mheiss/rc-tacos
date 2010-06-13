package at.redcross.tacos.web.faces.combo;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlComboBox;

import at.redcross.tacos.dbal.entity.EntityImpl;

@FacesConverter(value = "converter.dropDown")
public class DropDownConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        HtmlComboBox box = (HtmlComboBox) component;
        for (UIComponent child : box.getChildren()) {
            UISelectItems childItem = (UISelectItems) child;

            @SuppressWarnings("unchecked")
            List<SelectItem> items = (List<SelectItem>) childItem.getValue();
            for (SelectItem item : items) {
                DropDownItem downItem = (DropDownItem) item.getValue();
                if (value.equals(downItem.getLabel())) {
                    return downItem.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof EntityImpl) {
            EntityImpl entity = (EntityImpl) value;
            return entity.getDisplayString();
        }
        if (value instanceof DropDownItem) {
            DropDownItem item = (DropDownItem) value;
            return item.getLabel();
        }
        return value == null ? "" : value.toString();
    }

}

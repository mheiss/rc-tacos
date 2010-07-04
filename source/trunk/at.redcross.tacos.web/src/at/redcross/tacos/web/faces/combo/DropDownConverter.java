package at.redcross.tacos.web.faces.combo;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlComboBox;

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
		HtmlComboBox box = (HtmlComboBox) component;
		for (UIComponent child : box.getChildren()) {
			UISelectItems childItem = (UISelectItems) child;

			@SuppressWarnings("unchecked")
			List<SelectItem> items = (List<SelectItem>) childItem.getValue();
			for (SelectItem item : items) {
				DropDownItem downItem = (DropDownItem) item.getValue();
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

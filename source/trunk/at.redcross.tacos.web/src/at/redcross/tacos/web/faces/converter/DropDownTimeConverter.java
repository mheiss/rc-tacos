package at.redcross.tacos.web.faces.converter;

import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import at.redcross.tacos.web.model.SelectableItem;

@FacesConverter(value = "converter.dropDownTime")
public class DropDownTimeConverter implements Converter {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return timeFormat.parseObject(value);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        // format empty string value
        if (value == null) {
            return "";
        }
        // object is wrapped so unpack
        if (value instanceof SelectableItem) {
            SelectableItem dropDownItem = (SelectableItem) value;
            return timeFormat.format(dropDownItem.getValue());
        }
        // Date conversion
        return timeFormat.format(value);
    }
}

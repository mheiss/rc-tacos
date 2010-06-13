package at.redcross.tacos.web.faces.combo;

import java.io.Serializable;

import javax.faces.model.SelectItem;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class DropDownItem implements Serializable {

    private static final long serialVersionUID = 3574974379104269750L;

    private final String label;
    private final Object value;

    public DropDownItem(EntityImpl entity) {
        this(entity.getDisplayString(), entity);
    }

    public DropDownItem(String label, Object value) {
        this.label = label;
        this.value = value;
    }

    public SelectItem getItem() {
        return new SelectItem(this, getLabel());
    }

    public String getLabel() {
        return label;
    }

    public Object getValue() {
        return value;
    }
}

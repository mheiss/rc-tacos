package at.redcross.tacos.web.model;

import java.io.Serializable;

import javax.faces.model.SelectItem;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class SelectableItem implements Serializable {

    private static final long serialVersionUID = 3574974379104269750L;

    private final String label;
    private final Object value;

    public SelectableItem(EntityImpl entity) {
        this(entity.getDisplayString(), entity);
    }

    public SelectableItem(String label, Object value) {
        this.label = label;
        this.value = value;
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectableItem other = (SelectableItem) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		}
		else if (!label.equals(other.label))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
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

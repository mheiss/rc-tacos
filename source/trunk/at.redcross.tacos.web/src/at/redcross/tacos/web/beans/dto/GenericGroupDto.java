package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class GenericGroupDto<K extends EntityImpl, V extends EntityImpl> implements Serializable {

    private static final long serialVersionUID = -3064068916375006299L;

    /** the object to group by */
    protected final GenericDto<K> group;

    /** the list of elements for this group */
    protected final List<GenericDto<V>> elements;

    /**
     * Creates a new group by using the given parent
     */
    public GenericGroupDto(K group) {
        this.group = new GenericDto<K>(group);
        this.elements = new ArrayList<GenericDto<V>>();
    }

    /** Adds the given element to the list of elements of this group */
    public void addElement(V element) {
        if (elements.contains(element)) {
            return;
        }
        elements.add(new GenericDto<V>(element));
    }

    public GenericDto<K> getGroup() {
        return group;
    }

    public List<GenericDto<V>> getElements() {
        return elements;
    }
}

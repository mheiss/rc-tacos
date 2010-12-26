package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericGroupDto<K, V> implements Serializable {

    private static final long serialVersionUID = -3064068916375006299L;

    /** the object to group by */
    protected final K entity;

    /** the list of elements for this group */
    protected final List<V> elements;

    /**
     * Creates a new group by using the given parent
     */
    public GenericGroupDto(K entity) {
        this.entity = entity;
        this.elements = new ArrayList<V>();
    }

    /** Adds the given element to the list of elements of this group */
    public void addElement(V element) {
        if (elements.contains(element)) {
            return;
        }
        elements.add(element);
    }

    public K getEntity() {
        return entity;
    }

    public List<V> getElements() {
        return elements;
    }
}

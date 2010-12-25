package at.redcross.tacos.dbal.utils;

import java.util.Comparator;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class EntityComparator implements Comparator<EntityImpl> {

    @Override
    public int compare(EntityImpl o1, EntityImpl o2) {
        return o1.getDisplayString().compareTo(o2.getDisplayString());
    }

}

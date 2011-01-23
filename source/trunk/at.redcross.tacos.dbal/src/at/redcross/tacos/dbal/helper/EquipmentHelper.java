package at.redcross.tacos.dbal.helper;

import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.Equipment;

public class EquipmentHelper {

    public static List<Equipment> list(EntityManager manager) {
        StringBuilder builder = new StringBuilder();
        builder.append(" from Equipment e ");
        builder.append(" order by e.name asc ");
        return manager.createQuery(builder.toString(), Equipment.class).getResultList();
    }

}

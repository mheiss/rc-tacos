package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.redcross.tacos.dbal.entity.Equipment;

/**
 * The {@code EquipmentDto} is an extended {@linkplain GenericDto DTO} that
 * provides additional information about a equipment entry that is needed during
 * rendering
 */
public class EquipmentDto extends GenericDto<Equipment> {

    private static final long serialVersionUID = 5138746136027069422L;

    /** number threshold reached */
    private boolean numberThreshold;

    /** date threshold reached */
    private boolean dateThreshold;

    public EquipmentDto(Equipment entity) {
        super(entity);
        refresh();
    }

    /** Refresh the state of the equipment entities */
    public void refresh() {
        numberThreshold = hasNumberThreshold();
        dateThreshold = hasDateThreshold();
    }

    @Override
    public EntryState getState() {
        if (numberThreshold || dateThreshold) {
            return EntryState.WARN;
        }
        return super.getState();
    }

    /**
     * Converts the provided list of equipment entries into a list of equipment
     * DTO objects
     * 
     * @param entities
     *            the list of equipments to convert
     * @return the wrapped entries
     */
    public static List<EquipmentDto> fromList(List<Equipment> entities) {
        List<EquipmentDto> resultList = new ArrayList<EquipmentDto>();
        for (Equipment entity : entities) {
            resultList.add(new EquipmentDto(entity));
        }
        return resultList;
    }

    /** Returns the 'css-style-class' for the number of elements */
    public String getThresholdStyleClass() {
        return numberThreshold ? "equipmentNumber" : "";
    }

    /** Returns the 'css-style-class' for the date */
    public String getDateStyleClass() {
        return dateThreshold ? "equipmentDate" : "";
    }

    /** Returns whether or not the number threshold is reached */
    private boolean hasNumberThreshold() {
        return entity.getActualNumber() < entity.getTheoreticalNumber();
    }

    /** Returns whether or not the date threshold is reached */
    private boolean hasDateThreshold() {
        if (entity.getExpirationDate() == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        if (entity.getExpirationDate().before(calendar.getTime())) {
            return true;
        }
        return false;
    }

}

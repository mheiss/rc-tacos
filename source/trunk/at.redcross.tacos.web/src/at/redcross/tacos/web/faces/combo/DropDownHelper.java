package at.redcross.tacos.web.faces.combo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import at.redcross.tacos.dbal.entity.EntityImpl;

public class DropDownHelper {

    public static List<SelectItem> convertToItems(List<? extends EntityImpl> entityList) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (EntityImpl entity : entityList) {
            items.add(new DropDownItem(entity).getItem());
        }
        return items;
    }

}

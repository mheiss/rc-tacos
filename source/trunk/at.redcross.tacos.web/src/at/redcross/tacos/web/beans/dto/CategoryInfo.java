package at.redcross.tacos.web.beans.dto;

import java.io.Serializable;


import java.util.List;

import at.redcross.tacos.dbal.entity.Category;
import at.redcross.tacos.dbal.entity.Info;

public class CategoryInfo implements Serializable {

	private static final long serialVersionUID = -4678283779282068881L;
	private final Category category;
    private final List<Info> infos;

    public CategoryInfo(Category category, List<Info> infos) {
        this.category = category;
        this.infos = infos;
    }

    public Category getCategory() {
        return category;
    }

    public List<Info> getInfos() {
        return infos;
    }
}

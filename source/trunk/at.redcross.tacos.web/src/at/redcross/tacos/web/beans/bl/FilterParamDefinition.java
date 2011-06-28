package at.redcross.tacos.web.beans.bl;

/** A single parameter definition */
public class FilterParamDefinition {

    /** the unique id of the parameter */
    private final String id;

    /** the description of the parameter */
    private String description;

    /** Creates a new definition */
    public FilterParamDefinition(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}

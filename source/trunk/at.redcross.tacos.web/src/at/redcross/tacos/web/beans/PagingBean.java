package at.redcross.tacos.web.beans;

/** Extended bean that supports pagination */
public abstract class PagingBean extends BaseBean {

    private static final long serialVersionUID = -733057969726629090L;

    /** the current displayed page */
    protected int page = 1;

    /** the maximum number of records to display on one page */
    protected int maxResults = 30;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setPage(int page) {
        this.page = page;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public int getMaxResults() {
        return maxResults;
    }

    public int getPage() {
        return page;
    }

}

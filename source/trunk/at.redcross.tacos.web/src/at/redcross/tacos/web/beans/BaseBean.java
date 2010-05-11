package at.redcross.tacos.web.beans;

import java.io.Serializable;

import at.redcross.tacos.web.faces.FacesUtils;

/**
 * The {@code ManagedBean} is the base class for every bean. It provides default
 * Initialization and error handling.
 */
public abstract class BaseBean implements Serializable {

    private static final long serialVersionUID = 247522968676864407L;

    /**
     * Default initialization method that is called by pretty faces to create
     * the bean. If the initialization failed due to an exception then the
     * request is delegated to the default error handler.
     */
    public void prettyInit() {
        try {
            init();
        }
        catch (Exception ex) {
            FacesUtils.redirectError(ex);
        }
    }

    /**
     * Callback method to initialize the bean.
     * 
     * @throws Exception
     *             if the initialization fails
     */
    protected void init() throws Exception {
        // default do nothing
    }

}

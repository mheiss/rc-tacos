package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "errorBean")
public class ErrorBean extends BaseBean {

    private static final long serialVersionUID = -8228546063540602086L;

    // the injected URI
    private String cameFrom;

    // the injected error code;
    private String errorCode;

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setCameFrom(String cameFrom) {
        this.cameFrom = cameFrom;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getCameFrom() {
        return cameFrom;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

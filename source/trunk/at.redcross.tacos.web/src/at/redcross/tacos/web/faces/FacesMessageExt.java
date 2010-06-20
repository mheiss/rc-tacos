package at.redcross.tacos.web.faces;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

/**
 * The {@code FacesMessageExt} is an extended {@code FacesMessage} providing
 * multiple line support.
 */
public class FacesMessageExt extends FacesMessage {

    private static final long serialVersionUID = 3627925979309597669L;

    private List<String> details;

    /**
     * Creates a new {@code FacesMessageExt} instance using the given {@code
     * FacesMessage}
     * 
     * @param message
     *            the original message to wrap.
     */
    public FacesMessageExt(FacesMessage message) {
        setDetail(message.getDetail());
        setSummary(message.getSummary());
        setSeverity(message.getSeverity());
        initDetails();
    }

    public List<String> getDetails() {
        return details;
    }

    private void initDetails() {
        details = new ArrayList<String>();
        if (getDetail() == null) {
            return;
        }
        if (getDetail().isEmpty()) {
            return;
        }
        // now append each line separated by a new line
        for (String line : getDetail().split("\\r?\\n")) {
            details.add(line);
        }
    }
}

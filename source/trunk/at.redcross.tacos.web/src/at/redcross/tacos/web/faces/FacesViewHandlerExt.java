package at.redcross.tacos.web.faces;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.application.PrettyViewHandler;

/**
 * The {@code FacesViewHandlerExt} is an extended view handler that tries to
 * prevent the <tt>ViewExpiredException</tt> by creating a new view when the
 * previous could not be restored.
 */
public class FacesViewHandlerExt extends PrettyViewHandler {

    private final static Log logger = LogFactory.getLog(FacesViewHandlerExt.class);

    public FacesViewHandlerExt(ViewHandler viewHandler) {
        super(viewHandler);
    }

    @Override
    public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
        UIViewRoot viewRoot = parent.restoreView(facesContext, viewId);
        if (viewRoot == null) {
            logger.info("Failed to restore the view, creating new one");
            viewRoot = super.createView(facesContext, viewId);
        }
        return viewRoot;
    }

}

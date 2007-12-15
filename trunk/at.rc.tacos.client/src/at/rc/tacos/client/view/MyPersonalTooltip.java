package at.rc.tacos.client.view;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;

/**
 * This shows the tooltip for a roster entry.
 * @author Michael
 */
public class MyPersonalTooltip extends ColumnViewerToolTipSupport  
{
    /**
     * Default class constructor for a new tooltip
     * @param viewer the viewer to enable the tooltip support
     * @param style the style of the ttoltip
     * @param manualActivation if the activation is done manually
     */
    public MyPersonalTooltip(ColumnViewer viewer, int style,boolean manualActivation) 
    {
        super(viewer, style, manualActivation);
    }

    /**
     * Create the content of the window
     * @param event the triggered event
     * @param parent the parent frame
     * @return the create tooltip window
     */
    protected Composite createToolTipContentArea(Event event, Composite parent) 
    {
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        FormColors colors = toolkit.getColors();
        Color top = colors.getColor(IFormColors.H_GRADIENT_END);
        Color bot = colors.getColor(IFormColors.H_GRADIENT_START);
        //get the entry
        RosterEntry entry = (RosterEntry)getToolTipArea(event);

        // create the base form
        Form form = toolkit.createForm(parent);
        form.setText(entry.getStaffMember().getFirstName() + " " +entry.getStaffMember().getLastname());
        form.setTextBackground(new Color[] { top, bot }, new int[] { 100 }, true);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        form.getBody().setLayout(layout);

        // create the text for user information
        FormText text = toolkit.createFormText(form.getBody(), true);
        GridData td = new GridData();
        td.horizontalSpan = 2;
        td.heightHint = 100;
        td.widthHint = 200;
        text.setLayoutData(td);

        text.setText(
                "<form><p>snippet8</p><p>snippet8</p></form>", 
                true, 
                false);

        // create the picture representing the user
        td = new GridData();
        td.horizontalSpan = 1;
        td.heightHint = 100;
        td.widthHint = 64;
        FormText formImage = toolkit.createFormText(form.getBody(), false);
        formImage.setText("<form><p><img href=\"image\"/></p></form>", true, false);
        formImage.setLayoutData(td);
        Image image = ImageFactory.getInstance().getRegisteredImage("image.personal.user");
        formImage.setImage("image", image);
        return parent;
    }
}

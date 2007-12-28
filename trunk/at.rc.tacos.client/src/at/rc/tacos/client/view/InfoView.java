package at.rc.tacos.client.view;

import java.util.Calendar;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.SelectRosterDateAction;
import at.rc.tacos.client.util.CustomColors;

/**
 * A view showing custom informations
 * @author heissm
 */
public class InfoView extends ViewPart
{
    public static final String ID = "at.rc.tacos.client.view.info"; 
    
    private DateTime dateTime;
    private FormToolkit toolkit;
    private ScrolledForm form;
    
    /**
     * Creates the view.
     * @param parent the parent frame to insert the new content
     */
    public void createPartControl(Composite parent) 
    {
        //group filter
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Allgemeine Informationen");
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new GridLayout());
                

        final Composite composite = form.getBody();
        composite.setSize(494, 349);
        form.setContent(composite);

        final GridData gd_filterGroup = new GridData(SWT.FILL, SWT.TOP, true, false);
        gd_filterGroup.heightHint = 150;//for normal date field: "30"
        gd_filterGroup.widthHint = 993;
        composite.setLayoutData(gd_filterGroup);
        final GridLayout gridLayout_3 = new GridLayout();
        gridLayout_3.numColumns = 9;
        composite.setLayout(gridLayout_3);
            
        //Calendar field
        dateTime = new DateTime(composite, SWT.CALENDAR);
        dateTime.setToolTipText("Datum der anzuzeigenden Dienstplanübersicht auswählen");
        dateTime.setBounds(10, 43,180, 171);
        dateTime.setData("newKey", null);
        dateTime.addSelectionListener (new SelectionAdapter () 
        {
            public void widgetSelected (SelectionEvent e) 
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dateTime.getYear());
                cal.set(Calendar.MONTH, dateTime.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
                SelectRosterDateAction selectAction = new SelectRosterDateAction(cal.getTime());
                selectAction.run();
            }
        });
        
		final Text informationOfTheDay = new Text(composite, SWT.BORDER);
		informationOfTheDay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() { }
}

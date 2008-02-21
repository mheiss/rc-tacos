package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;


import org.eclipse.swt.*;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.SelectTransportDateAction;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.util.CustomColors;


/**
 * A view showing custom informations
 * @author b.thek
 */
public class FilterView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.filter"; 

    //the components
    private DateTime dateTime;
    private FormToolkit toolkit;
    private ScrolledForm form;
    //the main components
    private Section calendarSection;

    //labels for the view
    public final static String LABEL_NOTES = "Filterfunktion";
    public final static String LABEL_CALENDAR = "Kalender";
    public final static String LABEL_INFO = "Informationen";
    

    /**
     * Default class constructor
     */
    public FilterView()
    {
        SessionManager.getInstance().addPropertyChangeListener(this);
    }

    /**
     * Cleanup the view
     */
    @Override
    public void dispose() 
    {
        SessionManager.getInstance().removePropertyChangeListener(this);
        super.dispose();
    }

    /**
     * Creates the view.
     * @param parent the parent frame to insert the new content
     */
    public void createPartControl(Composite parent) 
    {
        //setup the form
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Filterfunktionen");
        toolkit.decorateFormHeading(form.getForm());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form.getBody().setLayout(gridLayout);
       
        //add the composites
        createCalendarSection(form.getBody());

        //info should span over two
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() { }
    

    
    /**
     * Helper method to create a composite
     * @param parent the parent control
     * @param col the number of cols
     * @return the returned composite
     */
    public Composite makeComposite(Composite parent, int col) 
    {
        Composite nameValueComp = toolkit.createComposite(parent);
        GridLayout layout = new GridLayout(col, false);
        layout.marginHeight = 3;
        nameValueComp.setLayout(layout);
        nameValueComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return nameValueComp;
    }

    /**
     * Creates the calendar section of the view.
     * @param parent the parent view to integrate
     */
    private void createCalendarSection(Composite parent)
    {
        //create the section
        calendarSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
        toolkit.createCompositeSeparator(calendarSection);
        calendarSection.setText(LABEL_CALENDAR);
        calendarSection.setExpanded(true);
        calendarSection.setLayout(new GridLayout());
        calendarSection.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));

        //create the container for the notes
        Composite calendar = toolkit.createComposite(calendarSection);
        calendarSection.setClient(calendar);
        calendar.setLayout(new GridLayout());

        //Calendar field
        dateTime = new DateTime(calendar, SWT.CALENDAR);
        dateTime.setToolTipText("Datum der anzuzeigenden Transporte auswählen");
        dateTime.addSelectionListener (new SelectionAdapter () 
        {
            public void widgetSelected (SelectionEvent e) 
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dateTime.getYear());
                cal.set(Calendar.MONTH, dateTime.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
                //run the action to query the transports for the selected date
                SelectTransportDateAction selectAction = new SelectTransportDateAction(cal);
                selectAction.run();
            }
        });
    }

    /**
     * Listens to events
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce)
    {    
    	//not needed
    }
}

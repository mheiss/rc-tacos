package at.rc.tacos.client.view;

import java.util.Calendar;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.SelectRosterDateAction;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.core.net.NetWrapper;

/**
 * A view showing custom informations
 * @author heissm
 */
public class InfoView extends ViewPart
{
    public static final String ID = "at.rc.tacos.client.view.info"; 

    //the components
    private DateTime dateTime;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TextViewer noteEditor;
    //the main components
    private Composite info;
    private Section calendarSection;
    private Section dayInfoSection;

    //labels for the view
    public final static String LABEL_NOTES = "Tagesinformationen";
    public final static String LABEL_CALENDAR = "Kalender";
    public final static String LABEL_INFO = "Informationen";
    //infos to display
    public final static String LABEL_NAME = "Angemeldet als ";
    public final static String LABEL_DATE = "Anmeldezeit: ";

    /**
     * Creates the view.
     * @param parent the parent frame to insert the new content
     */
    public void createPartControl(Composite parent) 
    {
        //setup the form
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Allgemeine Informationen");
        toolkit.decorateFormHeading(form.getForm());
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        form.getBody().setLayout(gridLayout);
        
        //add the composites
        createInfoSection(form.getBody());
        createCalendarSection(form.getBody());
        createNotesSection(form.getBody());
        
        //info should span over two
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        info.setLayoutData(data);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() { }
    
    /**
     * Creates the info section containing the user information
     * @param parent the parent view to integrate
     */
    private void createInfoSection(Composite parent)
    {
        //create the container for the notes
        info = toolkit.createComposite(parent);
        info.setLayout(new GridLayout());
        GridData calData = new GridData(GridData.FILL_BOTH);
        calData.grabExcessVerticalSpace = true;
        info.setLayoutData(calData);
        
        //the text
        StringBuffer buf = new StringBuffer();
        buf.append("<form>");
        buf.append("<p>");
        buf.append("<span font=\"header\">"+ LABEL_NAME);
        buf.append(NetWrapper.getDefault().getClientSession().getUsername()+"</span> ");
        buf.append("</p>");
        buf.append("<p>" + LABEL_DATE + " " + Util.formatTimeAndDate(Calendar.getInstance().getTimeInMillis())+"</p>");
        buf.append("</form>");
        
        //the labels
        FormText formText = toolkit.createFormText(info, true);
        formText.setWhitespaceNormalized(true);
        formText.setColor("header", toolkit.getColors().getColor(IFormColors.TITLE));
        formText.setFont("header", CustomColors.SUBHEADER_FONT);
        formText.setText(buf.toString(), true, false);
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
        
        //create the container for the notes
        Composite calendar = toolkit.createComposite(calendarSection);
        calendarSection.setClient(calendar);
        calendar.setLayout(new GridLayout());
        
        //Calendar field
        dateTime = new DateTime(calendar, SWT.CALENDAR);
        dateTime.setToolTipText("Datum der anzuzeigenden Dienstplanübersicht auswählen");
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
    }

    /**
     * Creates the notes section of the view.
     * @param parent the parent view to integrate
     */
    private void createNotesSection(Composite parent)
    {
        //create the section
        dayInfoSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
        toolkit.createCompositeSeparator(dayInfoSection);
        dayInfoSection.setText(LABEL_NOTES);
        dayInfoSection.setExpanded(true);
        dayInfoSection.setLayout(new GridLayout());
        dayInfoSection.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        //create the container for the notes
        Composite notesField = toolkit.createComposite(dayInfoSection);
        dayInfoSection.setClient(notesField);
        notesField.setLayout(new GridLayout());
        GridData notesData = new GridData(GridData.FILL_BOTH);
        notesData.grabExcessVerticalSpace = true;
        notesField.setLayoutData(notesData);

        noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        noteEditor.setDocument(new Document());
        noteEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        noteEditor.setEditable(true);
        noteEditor.addTextListener(new ITextListener() 
        {
            public void textChanged(TextEvent event) 
            {
                String updatedText = noteEditor.getTextWidget().getText();
                System.out.println(updatedText);
            }
        });
    }
}

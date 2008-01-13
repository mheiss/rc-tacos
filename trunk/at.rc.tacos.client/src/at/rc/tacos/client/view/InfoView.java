package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.SelectRosterDateAction;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.model.StaffMember;

/**
 * A view showing custom informations
 * @author heissm
 */
public class InfoView extends ViewPart implements PropertyChangeListener
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

    //controls
    private Label user;
    private Hyperlink logoutLink;
    private Label date;

    //labels for the view
    public final static String LABEL_NOTES = "Tagesinformationen";
    public final static String LABEL_CALENDAR = "Kalender";
    public final static String LABEL_INFO = "Informationen";
    //infos to display
    public final static String LABEL_NAME = "Angemeldet als: ";
    public final static String LABEL_LOGOUT = "(Abmelden)";
    public final static String LABEL_DATE = "Angemeldet seit: ";
    public final static String LABEL_NOT_CONNECTED = "<Keine Serververbindung>";

    /**
     * Default class constructor
     */
    public InfoView()
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
     * Updates the info section
     */
    private void updateInfoSection()
    {
        //the login object
        StaffMember loginInfo = SessionManager.getInstance().getLoginInformation().getUserInformation();
        
        user.setText(loginInfo.getFirstName() + " "+loginInfo.getLastName());
        date.setText(Util.formatTimeAndDate(new Date().getTime()));
        
        //redraw
        info.redraw();
        info.update();
        info.layout(true);
      }

    /**
     * Creates the info section containing the user information
     * @param parent the parent view to integrate
     */
    private void createInfoSection(Composite parent)
    {
        //create the container for the notes
        info = toolkit.createComposite(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        info.setLayout(layout);
        GridData calData = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
        calData.grabExcessVerticalSpace = true;
        info.setLayoutData(calData);

        Font userFont = new Font(null,"Arial",12,SWT.BOLD);
        
        //the labels
        Label userLabel = toolkit.createLabel(info, LABEL_NAME);
        userLabel.setFont(userFont);
        user = toolkit.createLabel(info, LABEL_NOT_CONNECTED);
        user.setFont(userFont);
        
        //logout link
        logoutLink = toolkit.createHyperlink(info, LABEL_LOGOUT, SWT.LEFT);
        logoutLink.addHyperlinkListener(new HyperlinkAdapter() 
        {
            public void linkActivated(HyperlinkEvent e) 
            {
                MessageBox dialog = new MessageBox(getSite().getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
                dialog.setText("Abmelden");
                dialog.setMessage("Wollen Sie sich wirklich abmelden?");
                //check the result
                if (dialog.open() != SWT.NO)
                {
                    PlatformUI.getWorkbench().restart();
                }
            }
        });
        
        //info about the login time
        Label dateLabel = toolkit.createLabel(info, LABEL_DATE);
        date = toolkit.createLabel(info, LABEL_NOT_CONNECTED);
        
        //layout
        GridData data = new GridData();
        data.widthHint = 150;
        userLabel.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        dateLabel.setLayoutData(data);
        data = new GridData();
        data.widthHint = 100;
        logoutLink.setLayoutData(data);
        //layout for the dynamic fields
        GridData data2 = new GridData();
        data2.widthHint = 100;
        user.setLayoutData(data2);
        data2 = new GridData();
        data2.widthHint = 100;
        date.setLayoutData(data2);
        
        //update the labels
        updateInfoSection();
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

    /**
     * Listens to login events and updates the labels
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce)
    {
        if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(pce.getPropertyName()))
        {
            Display.getDefault().syncExec(new Runnable ()    
            {
                public void run ()       
                {
                    updateInfoSection();
                }
            });
        }
        if("CONNECTION_LOST".equalsIgnoreCase(pce.getPropertyName()))
        {
            Display.getDefault().syncExec(new Runnable ()    
            {
                public void run ()       
                {
                    updateInfoSection();
                }
            });
        }
    }
}

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
import org.eclipse.swt.custom.CLabel;
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
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.PersonalUpdateDayInfoAction;
import at.rc.tacos.client.controller.SelectRosterDateAction;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.MyUtils;

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
    private TextViewer noteEditor;
    //the main components
    private Composite info;
    private Section calendarSection;
    private Section dayInfoSection;

    //controls
    private Label date;

    //labels for the view
    public final static String LABEL_NOTES = "Filterfunktion";
    public final static String LABEL_CALENDAR = "Kalender";
    public final static String LABEL_INFO = "Informationen";
    
    public final static String LABEL_DATE = "Angemeldet seit: ";

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
        createInfoSection(form.getBody());
        createCalendarSection(form.getBody());


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
        date.setText(Util.formatTimeAndDate(new Date().getTime()));
        
        //redraw
        info.redraw();
        info.update();
        info.layout(true);
      }
    
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
  
        
 
        
        //info about the login time
        Label dateLabel = toolkit.createLabel(info, LABEL_DATE);

        
        //layout
       
       
        //layout for the dynamic fields
        GridData data2 = new GridData();
        data2 = new GridData();
        data2.widthHint = 150;
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

//    /**
//     * Creates the notes section of the view.
//     * @param parent the parent view to integrate
//     */
//    private void createNotesSection(Composite parent)
//    {
//        //create the section
//        dayInfoSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
//        toolkit.createCompositeSeparator(dayInfoSection);
//        dayInfoSection.setText(LABEL_NOTES);
//        dayInfoSection.setExpanded(true);
//        dayInfoSection.setLayout(new GridLayout());
//        dayInfoSection.setLayoutData(new GridData(GridData.FILL_BOTH));
//        
//        //create the container for the notes
//        Composite notesField = toolkit.createComposite(dayInfoSection);
//        dayInfoSection.setClient(notesField);
//        notesField.setLayout(new GridLayout());
//        GridData notesData = new GridData(GridData.FILL_BOTH);
//        notesData.grabExcessVerticalSpace = true;
//        notesField.setLayoutData(notesData);
//        
//        //make a composite on the top of the input field
//        Composite controlls = makeComposite(notesField, 2);
//        
//        //update button
//        saveDayInfoLink = toolkit.createImageHyperlink(controlls,SWT.NONE);
//        saveDayInfoLink.setImage(ImageFactory.getInstance().getRegisteredImage("image.info.save.na"));
//        saveDayInfoLink.addHyperlinkListener(new HyperlinkAdapter()
//        {
//			@Override
//			public void linkActivated(HyperlinkEvent e) 
//			{
//                //set the last chane user
//                String user = SessionManager.getInstance().getLoginInformation().getUsername();
//                DayInfoMessage dayInfo = SessionManager.getInstance().getDayInfoMessage();
//                dayInfo.setMessage(noteEditor.getTextWidget().getText());
//                dayInfo.setLastChangedBy(user);
//                dayInfo.setDirty(true);
//                //send the update request
//                PersonalUpdateDayInfoAction updateAction = new PersonalUpdateDayInfoAction(dayInfo);
//                updateAction.run();
//			}
//        });
//        
//        dayInfoMessage = new CLabel(controlls,SWT.LEFT);
//        dayInfoMessage.setText("Zuletzt geändert von <nicht verfügbar>, <nicht verfügbar>");
//        dayInfoMessage.setImage(ImageFactory.getInstance().getRegisteredImage("image.info.warning"));
//
//        noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
//        noteEditor.setDocument(new Document());
//        noteEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
//        noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
//        noteEditor.setEditable(true);
//        noteEditor.addTextListener(new ITextListener()
//        {
//			@Override
//			public void textChanged(TextEvent te) 
//			{
//				//Get the text and update it
//				String text = noteEditor.getTextWidget().getText();
//				SessionManager.getInstance().updateLocalDayInfoMessage(text);
//			}
//        });
//
//    }

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
                	//TODO: finde solution to update
                    //updateInfoSection();
                }
            });
        }
        if("CONNECTION_LOST".equalsIgnoreCase(pce.getPropertyName()))
        {
            Display.getDefault().syncExec(new Runnable ()    
            {
                public void run ()       
                {
                	//TODO: find solution to update
                    //updateInfoSection();
                }
            });
        }
        if("DAY_INFO_CHANGED".equalsIgnoreCase(pce.getPropertyName()))
        {
        	noteEditor.setEditable(false);
        	//cast to a day info message
        	DayInfoMessage dayInfo = (DayInfoMessage)pce.getNewValue();
        	noteEditor.getTextWidget().setText(dayInfo.getMessage());
        	noteEditor.setEditable(true);
        	//update the labels
        	dayInfoSection.setText("Tagesinformationen für den "+MyUtils.formatDate(dayInfo.getTimestamp()));
        }
        if("DAY_INFO_LOCAL_CHANGED".equalsIgnoreCase(pce.getPropertyName()))
        {
        	//cast to a day info message
        	DayInfoMessage dayInfo = (DayInfoMessage)pce.getNewValue();
        	
        }
    }
}

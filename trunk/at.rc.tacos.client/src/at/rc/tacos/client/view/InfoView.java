package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
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
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.MyUtils;

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
    private ImageHyperlink saveDayInfoLink;
    private CLabel dayInfoMessage;

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
    	ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
        SessionManager.getInstance().addPropertyChangeListener(this);
    }

    /**
     * Cleanup the view
     */
    @Override
    public void dispose() 
    {
    	ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
        SessionManager.getInstance().removePropertyChangeListener(this);
        super.dispose();
    }

    /**
     * Creates the view.
     * @param parent the parent frame to insert the new content
     */
    @Override
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
        
        //load and init the data
        SessionManager.getInstance().initViews(this);

        //info should span over two
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        info.setLayoutData(data);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
	public void setFocus() { }
    
    /**
     * Updates the info section
     */
    private void updateInfoSection()
    {
        //the login object
        StaffMember loginInfo = SessionManager.getInstance().getLoginInformation().getUserInformation();
        
        user.setText(loginInfo.getFirstName() + " "+loginInfo.getLastName());
        date.setText(MyUtils.timestampToString(Calendar.getInstance().getTimeInMillis(),MyUtils.timeAndDateFormat));
        
        //redraw
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
        Label userLabel = toolkit.createLabel(info, LABEL_NAME);
        userLabel.setFont(userFont);
        user = toolkit.createLabel(info, LABEL_NOT_CONNECTED);
        user.setFont(userFont);
        
        //logout link
        logoutLink = toolkit.createHyperlink(info, LABEL_LOGOUT, SWT.LEFT);
        logoutLink.setFont(userFont);
        logoutLink.addHyperlinkListener(new HyperlinkAdapter() 
        {
            @Override
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
        data.widthHint = 150;
        dateLabel.setLayoutData(data);
        data = new GridData();
        data.widthHint = 150;
        logoutLink.setLayoutData(data);
        //layout for the dynamic fields
        GridData data2 = new GridData();
        data2.widthHint = 150;
        user.setLayoutData(data2);
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
            @Override
			public void widgetSelected (SelectionEvent e) 
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dateTime.getYear());
                cal.set(Calendar.MONTH, dateTime.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
                //run the change action to query the roster entries for the given date
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
        
        //make a composite on the top of the input field
        Composite controlls = makeComposite(notesField, 2);
        
        //update button
        saveDayInfoLink = toolkit.createImageHyperlink(controlls,SWT.NONE);
        saveDayInfoLink.setImage(ImageFactory.getInstance().getRegisteredImage("info.save.na"));
        saveDayInfoLink.addHyperlinkListener(new HyperlinkAdapter()
        {
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
                //set the last chane user
                String user = SessionManager.getInstance().getLoginInformation().getUsername();
                DayInfoMessage dayInfo = SessionManager.getInstance().getDayInfoMessage();
                dayInfo.setMessage(noteEditor.getTextWidget().getText());
                dayInfo.setLastChangedBy(user);
                dayInfo.setDirty(true);
                //send the update request
                PersonalUpdateDayInfoAction updateAction = new PersonalUpdateDayInfoAction(dayInfo);
                updateAction.run();
			}
        });
        
        dayInfoMessage = new CLabel(controlls,SWT.LEFT);
        dayInfoMessage.setText("Zuletzt geändert von <nicht verfügbar>");
        dayInfoMessage.setImage(ImageFactory.getInstance().getRegisteredImage("info.warning"));

        noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        noteEditor.setDocument(new Document());
        noteEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        noteEditor.setEditable(true);
        noteEditor.addTextListener(new ITextListener()
        {
			@Override
			public void textChanged(TextEvent te) 
			{
				//Get the text and update it
				String text = noteEditor.getTextWidget().getText();
				SessionManager.getInstance().updateLocalDayInfoMessage(text);
			}
        });
        
        //set the width
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        dayInfoMessage.setLayoutData(data);
    }

    /**
     * Listens to login events and updates the labels
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce)
    {
    	if("STAFF_UPDATE".equalsIgnoreCase(pce.getPropertyName()))
    	{
    		//get the updated member
    		StaffMember updatedMember = (StaffMember)pce.getNewValue();
    		StaffMember currentMember = SessionManager.getInstance().getLoginInformation().getUserInformation();
    		//is this staff member the current logged in member?
    		if(updatedMember.equals(currentMember))
    		{
    			SessionManager.getInstance().getLoginInformation().setUserInformation(updatedMember);
    			updateInfoSection();
    		}
    	}
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
        if("DAY_INFO_CHANGED".equalsIgnoreCase(pce.getPropertyName()))
        {
        	noteEditor.setEditable(false);
        	//cast to a day info message
        	DayInfoMessage dayInfo = (DayInfoMessage)pce.getNewValue();
        	noteEditor.getTextWidget().setText(dayInfo.getMessage());
        	noteEditor.setEditable(true);
        	//update the labels
        	saveDayInfoLink.setEnabled(false);
        	saveDayInfoLink.setImage(ImageFactory.getInstance().getRegisteredImage("info.save.na"));
        	dayInfoMessage.setImage(ImageFactory.getInstance().getRegisteredImage("info.ok"));
        	dayInfoMessage.setText("Zuletzt geändert von "+dayInfo.getLastChangedBy());
        	dayInfoSection.setText("Tagesinformationen für den "+MyUtils.timestampToString(dayInfo.getTimestamp(),MyUtils.dateFormat));
        	dayInfoSection.layout(true);
        }
        if("DAY_INFO_LOCAL_CHANGED".equalsIgnoreCase(pce.getPropertyName()))
        {
        	//cast to a day info message
        	DayInfoMessage dayInfo = (DayInfoMessage)pce.getNewValue();
        	if(dayInfo.isDirty())
        	{
        		saveDayInfoLink.setEnabled(true);
        		saveDayInfoLink.setImage(ImageFactory.getInstance().getRegisteredImage("info.save"));
        		dayInfoMessage.setImage(ImageFactory.getInstance().getRegisteredImage("info.warning"));
        		dayInfoMessage.setText("Bitte speichern sie ihre lokalen Änderungen");
        	}
        	else
        	{
            	saveDayInfoLink.setEnabled(false);
            	saveDayInfoLink.setImage(ImageFactory.getInstance().getRegisteredImage("info.save.na"));
            	dayInfoMessage.setImage(ImageFactory.getInstance().getRegisteredImage("info.ok"));
            	dayInfoMessage.setText("Zuletzt geändert von "+dayInfo.getLastChangedBy());
        	}
        }
    }
}

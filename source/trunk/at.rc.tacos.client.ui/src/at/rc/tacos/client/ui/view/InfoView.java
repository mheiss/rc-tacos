package at.rc.tacos.client.ui.view;

import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.DayInfoHandler;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.util.MyUtils;

/**
 * The <code>InfoView</code> shows the {@link DayInfoMessage} and provides a
 * {@link CDateTime} instance to switch the date.
 * 
 * @author heissm
 */
public class InfoView extends ViewPart implements DataChangeListener<Object> {

	public static final String ID = "at.rc.tacos.client.view.info";

	// the components
	private CDateTime dateTime;
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TextViewer noteEditor;
	// the main components
	private Section generalSection;
	private Section calendarSection;
	private Section dayInfoSection;

	// controls
	private Label user;
	private Hyperlink logoutLink;
	private Label date;
	private ImageHyperlink saveDayInfoLink;

	// labels for the view
	private final static String LABEL_NOTES = "Tagesinformationen";
	private final static String LABEL_CALENDAR = "Kalender";
	// infos to display
	private final static String LABEL_NAME = "Angemeldet als: ";
	private final static String LABEL_LOGOUT = "(Abmelden)";
	private final static String LABEL_DATE = "Angemeldet seit: ";
	private final static String LABEL_NOT_CONNECTED = "<Keine Serververbindung>";

	// the handler
	private DayInfoHandler dayInfoHandler = (DayInfoHandler) NetWrapper.getHandler(DayInfoMessage.class);
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	/**
	 * Creates and initializes the view.
	 * 
	 * @param parent
	 *            the parent frame to insert the new content
	 */
	@Override
	public void createPartControl(Composite parent) {
		// setup the form
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		form.setText("Allgemeine Informationen");
		toolkit.decorateFormHeading(form.getForm());
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		form.getBody().setLayout(gridLayout);

		// add the composites
		createGeneralSection(form.getBody());
		createCalendarSection(form.getBody());
		createNotesSection(form.getBody());

		// add listeners to keep in track
		NetWrapper.registerListener(this, Login.class);
		NetWrapper.registerListener(this, StaffMember.class);
		NetWrapper.registerListener(this, DayInfoMessage.class);

		// initialize the view with current data
		initView();
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, Login.class);
		NetWrapper.removeListener(this, StaffMember.class);
		NetWrapper.removeListener(this, DayInfoMessage.class);
		super.dispose();
	}

	/**
	 * Helper method to initialize the view
	 */
	private void initView() {
		// the current date
		Calendar date = DateUtils.truncate(Calendar.getInstance(), Calendar.DAY_OF_MONTH);
		// update the view
		updateInfoSection(NetWrapper.getSession());
		updateDayInfoSection(dayInfoHandler.getMessageByDate(date.getTime()));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * Updates the info section
	 */
	private void updateInfoSection(MessageIoSession session) {
		if (session != null && session.isLoggedIn()) {
			Login login = session.getLogin();
			StaffMember userInformation = login.getUserInformation();
			user.setText(userInformation.getFirstName() + " " + userInformation.getLastName());
			date.setText(MyUtils.timestampToString(session.getLoginTime(), MyUtils.timeAndDateFormat));
		}
		else {
			user.setText("Nicht angemeldet");
			date.setText("-");
		}
		// redraw
		generalSection.layout(true);
	}

	/**
	 * Updates the DayInfoMessage
	 */
	private void updateDayInfoSection(DayInfoMessage message) {
		// set the section header
		dayInfoSection.setText("Tagesinformationen für den " + MyUtils.timestampToString(message.getTimestamp(), MyUtils.dateFormat));

		// update the linkt to save
		saveDayInfoLink.setEnabled(false);
		saveDayInfoLink.setUnderlined(false);
		saveDayInfoLink.setForeground(CustomColors.COLOR_GREY);
		saveDayInfoLink.setImage(imageRegistry.get("info.ok"));
		saveDayInfoLink.setText("Zuletzt geändert von " + message.getLastChangedBy());
		saveDayInfoLink.layout(true);

		// set the text
		noteEditor.getTextWidget().setText(message.getMessage());
	}

	/**
	 * Creates the info section containing the user information
	 * 
	 * @param parent
	 *            the parent view to integrate
	 */
	private void createGeneralSection(Composite parent) {
		// create the section
		generalSection = toolkit.createSection(parent, ExpandableComposite.NO_TITLE);
		generalSection.setExpanded(true);
		generalSection.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		generalSection.setLayoutData(data);

		// create the container for the general section
		Composite generalComposite = toolkit.createComposite(generalSection);
		generalSection.setClient(generalComposite);
		generalComposite.setLayout(new GridLayout(2, false));

		Font userFont = new Font(null, "Arial", 12, SWT.BOLD);

		// the label for the user
		Label userLabel = toolkit.createLabel(generalComposite, LABEL_NAME);
		data = new GridData();
		data.widthHint = 185;
		userLabel.setLayoutData(data);
		userLabel.setFont(userFont);

		// create a subcomposite for the username and the logout link
		Composite userControlComposite = toolkit.createComposite(generalComposite);
		userControlComposite.setLayout(new FillLayout());
		user = toolkit.createLabel(userControlComposite, LABEL_NOT_CONNECTED);
		user.setFont(userFont);

		// logout link
		logoutLink = toolkit.createHyperlink(userControlComposite, LABEL_LOGOUT, SWT.LEFT);
		logoutLink.setFont(userFont);
		logoutLink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				MessageBox dialog = new MessageBox(getSite().getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				dialog.setText("Abmelden");
				dialog.setMessage("Wollen Sie sich wirklich abmelden?");
				// check the result
				if (dialog.open() != SWT.NO) {
					PlatformUI.getWorkbench().restart();
				}
			}
		});

		// info about the login time
		Label dateLabel = toolkit.createLabel(generalComposite, LABEL_DATE);
		data = new GridData();
		data.widthHint = 185;
		dateLabel.setLayoutData(data);
		date = toolkit.createLabel(generalComposite, LABEL_NOT_CONNECTED);
		date.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	/**
	 * Creates the calendar section of the view.
	 * 
	 * @param parent
	 *            the parent view to integrate
	 */
	private void createCalendarSection(Composite parent) {
		// create the section
		calendarSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		toolkit.createCompositeSeparator(calendarSection);
		calendarSection.setText(LABEL_CALENDAR);
		calendarSection.setExpanded(true);
		calendarSection.setLayout(new GridLayout());
		calendarSection.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));

		// create the container for the calendar
		Composite calendar = toolkit.createComposite(calendarSection);
		calendarSection.setClient(calendar);
		calendar.setLayout(new GridLayout());

		// Calendar field
		dateTime = new CDateTime(calendar, CDT.SIMPLE);
		dateTime.setLocale(Locale.GERMAN);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dateTime.setToolTipText("Datum der anzuzeigenden Dienstplanübersicht auswählen");
		dateTime.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateTime.getSelection());
				// query the roster entries and the dayInfoMessage
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String strDate = sdf.format(cal.getTime());

				// query the rosterEntry
				GetMessage<RosterEntry> getRosterMessage = new GetMessage<RosterEntry>(new RosterEntry());
				getRosterMessage.addParameter(IFilterTypes.DATE_FILTER, strDate);
				getRosterMessage.asnchronRequest(NetWrapper.getSession());

				// query the dayInfoMessage
				GetMessage<DayInfoMessage> getInfoMessage = new GetMessage<DayInfoMessage>(new DayInfoMessage());
				getInfoMessage.addParameter(IFilterTypes.DATE_FILTER, strDate);
				getInfoMessage.asnchronRequest(NetWrapper.getSession());

				// setup the property change event and inform the listeners
				Object source = dateTime;
				String propertyName = ListenerConstants.ROSTER_DATE_CHANGED;
				Object oldValue = null;
				Object newValue = cal;
				PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue, newValue);
				UiWrapper.getDefault().firePropertyChangeEvent(event);
			}
		});
	}

	/**
	 * Creates the notes section of the view.
	 * 
	 * @param parent
	 *            the parent view to integrate
	 */
	private void createNotesSection(Composite parent) {
		// create the section
		dayInfoSection = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR);
		toolkit.createCompositeSeparator(dayInfoSection);
		dayInfoSection.setText(LABEL_NOTES);
		dayInfoSection.setExpanded(true);
		dayInfoSection.setLayout(new GridLayout());
		dayInfoSection.setLayoutData(new GridData(GridData.FILL_BOTH));

		// create the container for the notes
		Composite notesField = toolkit.createComposite(dayInfoSection);
		dayInfoSection.setClient(notesField);
		notesField.setLayout(new GridLayout());
		GridData notesData = new GridData(GridData.FILL_BOTH);
		notesData.grabExcessVerticalSpace = true;
		notesField.setLayoutData(notesData);

		// update button
		saveDayInfoLink = toolkit.createImageHyperlink(notesField, SWT.NONE);
		saveDayInfoLink.setImage(imageRegistry.get("info.save.na"));
		saveDayInfoLink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				// setup the message and send the update
				String user = NetWrapper.getSession().getUsername();
				String message = noteEditor.getTextWidget().getText();
				long timestamp = dateTime.getSelection().getTime();
				DayInfoMessage dayInfo = new DayInfoMessage(message, timestamp, user);
				dayInfo.setDirty(true);
				UpdateMessage<DayInfoMessage> updateMessage = new UpdateMessage<DayInfoMessage>(dayInfo);
				updateMessage.asnchronRequest(NetWrapper.getSession());
			}
		});

		// the textviewer for the message
		noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		noteEditor.setDocument(new Document());
		noteEditor.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		noteEditor.setEditable(true);
		noteEditor.addTextListener(new ITextListener() {

			@Override
			public void textChanged(TextEvent te) {
				System.out.println("Changed");

				Date selectedDate = DateUtils.truncate(dateTime.getSelection(), Calendar.DAY_OF_MONTH);

				// get the currently cached message
				DayInfoMessage savedDayInfoMessage = dayInfoHandler.getMessageByDate(selectedDate);
				if (savedDayInfoMessage == null) {
					return;
				}

				// get the current text and the saved text
				String savedText = savedDayInfoMessage.getMessage();
				String changedText = noteEditor.getTextWidget().getText();

				// compare with the current text
				if (!savedText.equalsIgnoreCase(changedText)) {
					// update the link
					saveDayInfoLink.setText("Änderungen speichern");
					saveDayInfoLink.setEnabled(true);
					saveDayInfoLink.setUnderlined(true);
					saveDayInfoLink.setForeground(CustomColors.COLOR_BLUE);
					saveDayInfoLink.setImage(imageRegistry.get("info.warning"));
				}
				else {
					// update the linkt to save
					saveDayInfoLink.setEnabled(false);
					saveDayInfoLink.setUnderlined(false);
					saveDayInfoLink.setForeground(CustomColors.COLOR_GREY);
					saveDayInfoLink.setImage(imageRegistry.get("info.ok"));
					saveDayInfoLink.setText("Zuletzt geändert von " + savedDayInfoMessage.getLastChangedBy());
				}
			}
		});
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		Object object = message.getFirstElement();
		if (object instanceof StaffMember) {
			updateInfoSection(messageIoSession);
		}
		if (object instanceof Login) {
			updateInfoSection(messageIoSession);
		}
		if (object instanceof DayInfoMessage) {
			DayInfoMessage dayInfoMessage = (DayInfoMessage) object;
			updateDayInfoSection(dayInfoMessage);
		}
	}
}

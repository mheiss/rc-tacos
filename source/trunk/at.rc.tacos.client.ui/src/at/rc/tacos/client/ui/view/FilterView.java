package at.rc.tacos.client.ui.view;

import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.filters.TransportViewFilter;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.GetMessage;

/**
 * A view showing custom informations
 * 
 * @author b.thek
 */
public class FilterView extends ViewPart {

	public static final String ID = "at.rc.tacos.client.view.filter";

	// the components
	private CDateTime dateTime;
	private FormToolkit toolkit;
	private ScrolledForm form;
	private Composite calendar;
	private Composite filter;

	// text fields for the filter
	private Text from, patient, to, location, transportNumber, priority, vehicle, disease;
	// to apply the filter
	private ImageHyperlink applyFilter, resetFilter;

	// labels for the view
	public final static String LABEL_NOTES = "Filterfunktion";
	public final static String LABEL_CALENDAR = "Kalender";
	public final static String LABEL_INFO = "Informationen";

	/**
	 * Creates the view.
	 * 
	 * @param parent
	 *            the parent frame to insert the new content
	 */
	@Override
	public void createPartControl(Composite parent) {
		// the scrolled form
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		form.setText("Filterfunktionen");
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		form.getBody().setLayoutData(data);

		// add the composites
		createCalendarSection(form.getBody());
		createFilterSection(form.getBody());

		// reflow
		form.reflow(true);
		form.update();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Helper method to create a composite
	 * 
	 * @param parent
	 *            the parent control
	 * @param col
	 *            the number of cols
	 * @return the returned composite
	 */
	public Composite makeComposite(Composite parent, int col) {
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		layout.marginHeight = 3;
		nameValueComp.setLayout(layout);
		nameValueComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return nameValueComp;
	}

	/**
	 * Creates the calendar section of the view.
	 * 
	 * @param parent
	 *            the parent view to integrate
	 */
	private void createCalendarSection(Composite parent) {
		// create the section
		calendar = createSection(parent, "Datum der Transporte");

		// Calendar field
		dateTime = new CDateTime(calendar, CDT.SIMPLE);
		dateTime.setLocale(Locale.GERMAN);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		dateTime.setToolTipText("Datum der anzuzeigenden Transporte auswählen");
		dateTime.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateTime.getSelection());

				// query the filtered transports
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				GetMessage<Transport> getMessage = new GetMessage<Transport>(new Transport());
				getMessage.addParameter(IFilterTypes.DATE_FILTER, sdf.format(cal.getTime()));
				getMessage.asnchronRequest(NetWrapper.getSession());

				// setup the property change eventand inform the listeners
				Object source = dateTime;
				String propertyName = ListenerConstants.TRANSPORT_DATE_CHANGED;
				Object oldValue = null;
				Object newValue = cal;
				PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue, newValue);
				UiWrapper.getDefault().firePropertyChangeEvent(event);
			}
		});
	}

	/**
	 * Creates the filter section
	 * 
	 * @param parent
	 */
	private void createFilterSection(Composite parent) {
		filter = createSection(parent, "Filterfunktion");

		// create the input fields, from street
		final Label labelTransportNumber = toolkit.createLabel(filter, "Transportnummer");
		transportNumber = toolkit.createText(filter, "");

		// create the input fields, from street
		final Label labelFrom = toolkit.createLabel(filter, "von");
		from = toolkit.createText(filter, "");

		// the patient
		final Label labelPatient = toolkit.createLabel(filter, "Patient");
		patient = toolkit.createText(filter, "");

		// the to street
		final Label labelTo = toolkit.createLabel(filter, "nach");
		to = toolkit.createText(filter, "");

		// the location
		final Label labelLocation = toolkit.createLabel(filter, "Ortsstelle");
		location = toolkit.createText(filter, "");

		// the priority
		final Label labelPriority = toolkit.createLabel(filter, "Priorität");
		priority = toolkit.createText(filter, "");

		// the vehicle
		final Label labelVehicle = toolkit.createLabel(filter, "Fahrzeug");
		vehicle = toolkit.createText(filter, "");

		// the disease
		final Label labelDisease = toolkit.createLabel(filter, "Erkrankung");
		disease = toolkit.createText(filter, "");

		// Create the hyperlink to import the data
		applyFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		applyFilter.setText("Transporte filtern");
		applyFilter.setImage(UiWrapper.getDefault().getImageRegistry().get("resource.import"));
		applyFilter.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				calendar.setBackground(CustomColors.BACKGROUND_RED);
				applyFilter.setBackground(CustomColors.BACKGROUND_RED);

				// setup the filter and inform the listeners
				final String strTrNr = transportNumber.getText();
				final String strFrom = from.getText();
				final String strPat = patient.getText();
				final String strTo = to.getText();
				final String strLocation = location.getText();
				final String strPriority = priority.getText();
				final String strVehicle = vehicle.getText();
				final String strDisease = disease.getText();

				// setup the event and inform the listeners
				Object source = this;
				String propertyName = ListenerConstants.TRANSPORT_FILTER_CHANGED;
				Object oldValue = null;
				Object newValue = new TransportViewFilter(strTrNr, strFrom, strPat, strTo, strLocation, strPriority, strVehicle, strDisease);
				PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue, newValue);
				UiWrapper.getDefault().firePropertyChangeEvent(event);
			}
		});

		// create the hyperlink to add a new job
		resetFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		resetFilter.setText("Einschränkungen entfernen");
		resetFilter.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.addressAdd"));
		resetFilter.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				// reset the fields
				transportNumber.setText("");
				from.setText("");
				patient.setText("");
				to.setText("");
				location.setText("");
				priority.setText("");
				vehicle.setText("");
				disease.setText("");
				// reset the background color
				applyFilter.setBackground(CustomColors.SECTION_BACKGROUND);
				calendar.setBackground(CustomColors.SECTION_BACKGROUND);
				// apply the filter
				Object source = this;
				String propertyName = ListenerConstants.TRANSPORT_FILTER_CHANGED;
				Object oldValue = null;
				Object newValue = null;
				PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue, newValue);
				UiWrapper.getDefault().firePropertyChangeEvent(event);
			}
		});

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 90;
		labelTransportNumber.setLayoutData(data);
		data = new GridData();
		data.widthHint = 50;
		labelFrom.setLayoutData(data);
		data.widthHint = 50;
		labelPatient.setLayoutData(data);
		data.widthHint = 50;
		labelTo.setLayoutData(data);
		labelPatient.setLayoutData(data);
		data.widthHint = 70;
		labelLocation.setLayoutData(data);
		data.widthHint = 50;
		labelPriority.setLayoutData(data);
		data.widthHint = 50;
		labelVehicle.setLayoutData(data);
		data.widthHint = 90;
		labelDisease.setLayoutData(data);
		data.widthHint = 70;
		// layout for the text fields
		GridData data2 = new GridData();
		data2.widthHint = 120;
		transportNumber.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		from.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		patient.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		to.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		location.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		priority.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		vehicle.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 120;
		disease.setLayoutData(data2);
	}

	// Helper methods
	/**
	 * Creates and returns a section and a composite with two colums
	 * 
	 * @param parent
	 *            the parent composite
	 * @param sectionName
	 *            the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent, String sectionName) {
		// create the section
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		// composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		// layout
		client.setLayout(new GridLayout(1, false));
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		client.setLayoutData(clientDataLayout);

		return client;
	}
}

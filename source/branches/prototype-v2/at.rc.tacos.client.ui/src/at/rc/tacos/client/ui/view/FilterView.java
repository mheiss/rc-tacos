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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.filters.TransportViewFilter;
import at.rc.tacos.client.ui.utils.CompositeHelper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.GetMessage;

/**
 * The <code>FilterView</code> provides a date and other custom filters for the
 * variouse transport perspectives and views.
 * 
 * @author b.thek
 */
public class FilterView extends ViewPart {

	public static final String ID = "at.rc.tacos.client.view.filter";

	// the components
	private FormToolkit toolkit;
	private Form form;
	private Composite calendar;
	private Composite filter;

	// controls
	private CDateTime dateTime;
	private Text from, patient, to, location, transportNumber, priority, vehicle, disease;
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
		form = toolkit.createForm(parent);
		form.setText("Filterfunktionen");
		toolkit.decorateFormHeading(form);

		Composite client = form.getBody();
		client.setLayout(new GridLayout(1, false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		// add the composites
		createCalendarSection(client);
		createFilterSection(client);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Creates the calendar section of the view.
	 * 
	 * @param parent
	 *            the parent view to integrate
	 */
	private void createCalendarSection(Composite parent) {
		calendar = CompositeHelper.createSection(toolkit, parent, "Datum der Transporte");

		// Calendar field
		dateTime = new CDateTime(calendar, CDT.SIMPLE);
		dateTime.setLocale(Locale.GERMAN);
		dateTime.setLayoutData(new GridData(GridData.CENTER));
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
		filter = CompositeHelper.createSection(toolkit, parent, "Filterfunktion");

		// create the input fields, from street
		toolkit.createLabel(filter, "Transportnummer");
		transportNumber = toolkit.createText(filter, "");
		transportNumber.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create the input fields, from street
		toolkit.createLabel(filter, "von");
		from = toolkit.createText(filter, "");
		from.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the patient
		toolkit.createLabel(filter, "Patient");
		patient = toolkit.createText(filter, "");
		patient.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the to street
		toolkit.createLabel(filter, "nach");
		to = toolkit.createText(filter, "");
		to.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the location
		toolkit.createLabel(filter, "Ortsstelle");
		location = toolkit.createText(filter, "");
		location.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the priority
		toolkit.createLabel(filter, "Priorität");
		priority = toolkit.createText(filter, "");
		priority.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the vehicle
		toolkit.createLabel(filter, "Fahrzeug");
		vehicle = toolkit.createText(filter, "");
		vehicle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the disease
		toolkit.createLabel(filter, "Erkrankung");
		disease = toolkit.createText(filter, "");
		disease.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

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
				applyFilter.setBackground(CustomColors.COLOR_WHITE);
				calendar.setBackground(CustomColors.COLOR_WHITE);
				// apply the filter
				Object source = this;
				String propertyName = ListenerConstants.TRANSPORT_FILTER_CHANGED;
				Object oldValue = null;
				Object newValue = null;
				PropertyChangeEvent event = new PropertyChangeEvent(source, propertyName, oldValue, newValue);
				UiWrapper.getDefault().firePropertyChangeEvent(event);
			}
		});
	}
}

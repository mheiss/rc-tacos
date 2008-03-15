package at.rc.tacos.client.view;

import java.util.Calendar;

import org.eclipse.swt.*;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.*;

import at.rc.tacos.client.controller.SelectTransportDateAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;


/**
 * A view showing custom informations
 * @author b.thek
 */
public class FilterView extends ViewPart
{
	public static final String ID = "at.rc.tacos.client.view.filter"; 

	//the components
	private DateTime dateTime;
	private FormToolkit toolkit;
	private ScrolledForm form;

	//text fields for the filter
	private Text from,patient,to,location;
	//to apply the filter
	private ImageHyperlink applyFilter,resetFilter;

	//labels for the view
	public final static String LABEL_NOTES = "Filterfunktion";
	public final static String LABEL_CALENDAR = "Kalender";
	public final static String LABEL_INFO = "Informationen";


	/**
	 * Default class constructor
	 */
	public FilterView()
	{
	}

	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		super.dispose();
	}

	/**
	 * Creates the view.
	 * @param parent the parent frame to insert the new content
	 */
	public void createPartControl(Composite parent) 
	{
		//the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Filterfunktionen"); 
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		GridData data = new GridData();
		data.widthHint = 250;
		data.grabExcessHorizontalSpace = false;
		form.getBody().setLayoutData(data);

		//add the composites
		createCalendarSection(form.getBody());
		createFilterSection(form.getBody());

		//reflow
		form.reflow(true);
		form.update();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{ 
		form.setFocus();
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
	 * Creates the calendar section of the view.
	 * @param parent the parent view to integrate
	 */
	private void createCalendarSection(Composite parent)
	{
		//create the section
		Composite calendar = createSection(parent,"Datum der Transporte");

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
	 * Creates the filter section 
	 * @param parent
	 */
	private void createFilterSection(Composite parent)
	{
		Composite filter = createSection(parent,"Filterfunktion");

		//create the input fields, from street
		final Label labelFrom = toolkit.createLabel(filter, "von");
		from = toolkit.createText(filter, "");

		//the patient
		final Label labelPatient = toolkit.createLabel(filter, "Patient");
		patient = toolkit.createText(filter, "");

		//the to street
		final Label labelTo = toolkit.createLabel(filter, "nach");
		to = toolkit.createText(filter, "");
		
		//the location
		final Label labelLocation = toolkit.createLabel(filter, "Ortsstelle");
		location = toolkit.createText(filter, "");

		//Create the hyperlink to import the data
		applyFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		applyFilter.setText("Transporte filtern");
		applyFilter.setImage(ImageFactory.getInstance().getRegisteredImage("resource.import"));
		applyFilter.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				inputChanged();
			}
		});

		//create the hyperlink to add a new job
		resetFilter = toolkit.createImageHyperlink(filter, SWT.NONE);
		resetFilter.setText("Einschränkungen entfernen");
		resetFilter.setImage(ImageFactory.getInstance().getRegisteredImage("admin.addressAdd"));
		resetFilter.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				//reset the fields
				from.setText("");
				patient.setText("");
				to.setText("");
				location.setText("");
				//apply the filter
				inputChanged();
			}
		});

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 50;
		labelFrom.setLayoutData(data);
		data = new GridData();
		data.widthHint = 50;
		labelPatient.setLayoutData(data);
		data.widthHint = 50;
		labelTo.setLayoutData(data);
		labelPatient.setLayoutData(data);
		data.widthHint = 50;
		labelLocation.setLayoutData(data);
		data.widthHint = 50;
		//layout for the text fields
		GridData data2 = new GridData();
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
	}

	//Helper methods
	/**
	 * Creates and returns a section and a composite with two colums
	 * @param parent the parent composite
	 * @param sectionName the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent,String sectionName)
	{
		//create the section
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | Section.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		client.setLayoutData(clientDataLayout);

		return client;
	}

	/**
	 * Helper method to apply the filer
	 */
	private void inputChanged()
	{
		TransportManager manager = ModelFactory.getInstance().getTransportList();
		//get the values
		final String strFrom = from.getText();
		final String strPat = patient.getText();
		final String strTo = to.getText();
		final String strLocation = location.getText();
		//inform the viewer
		manager.fireTransportFilterChanged(new TransportViewFilter(strFrom,strPat,strTo,strLocation));
	}
}

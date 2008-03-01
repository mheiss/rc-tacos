package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;


public class SearchView extends ViewPart
{
	public static final String ID = "at.rc.tacos.client.view.searchView";  

	//properties
	private FormToolkit toolkit;
	private ScrolledForm form;
	//text fields for the filter
	private Text from,patient,to;

	//to apply the filter
	private ImageHyperlink applyFilter,resetFilter;

	/**
	 * Default class constructor
	 */
	public SearchView()
	{
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{
		//the scrolled form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Transporte filtern"); 
		toolkit.decorateFormHeading(form.getForm());
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		form.getBody().setLayout(layout);
		form.getBody().setLayoutData(new GridData());

		//create the section to hold the filter
		Composite filter = createSection(form.getBody(), "Filter") ;

		//create the input fields, from street
		final Label labelFrom = toolkit.createLabel(filter, "von");
		from = toolkit.createText(filter, "");

		//the patient
		final Label labelPatient = toolkit.createLabel(filter, "Patient");
		patient = toolkit.createText(filter, "");

		//the to street
		final Label labelTo = toolkit.createLabel(filter, "nach");
		to = toolkit.createText(filter, "");

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
				//apply the filter
				inputChanged();
			}
		});

		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 80;
		labelFrom.setLayoutData(data);
		data = new GridData();
		data.widthHint = 80;
		labelPatient.setLayoutData(data);
		data.widthHint = 80;
		labelTo.setLayoutData(data);
		labelPatient.setLayoutData(data);
		data.widthHint = 80;
		//layout for the text fields
		GridData data2 = new GridData();
		data2.widthHint = 150;
		from.setLayoutData(data2);
		data2 = new GridData();
		data2.widthHint = 150;
		patient.setLayoutData(data2);	
		data2 = new GridData();
		data2.widthHint = 150;
		to.setLayoutData(data2);	
		//reflow
		form.reflow(true);
		form.update();
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() 
	{ 
		form.setFocus();
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
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}

	/**
	 * Helper method to apply the filer
	 */
	public void inputChanged()
	{
		TransportManager manager = ModelFactory.getInstance().getTransportList();
		//get the values
		final String strFrom = from.getText();
		final String strPat = patient.getText();
		final String strTo = to.getText();
		//inform the viewer
		manager.fireTransportFilterChanged(new TransportViewFilter(strFrom,strPat,strTo));
	}
}
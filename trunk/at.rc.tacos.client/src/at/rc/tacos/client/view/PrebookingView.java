package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.CancelTransportAction;
import at.rc.tacos.client.controller.EditTransportAction;
import at.rc.tacos.client.controller.JournalMoveToRunningTransportsAction;
import at.rc.tacos.client.controller.MoveToOutstandingTransportsAction;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.JournalViewContentProvider;
import at.rc.tacos.client.providers.JournalViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.JournalViewTooltip;
import at.rc.tacos.client.view.sorterAndTooltip.PersonalViewSorter;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;

import at.rc.tacos.model.Transport;


public class PrebookingView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.prebooking_view";

	//the toolkit to use
	private FormToolkit toolkit;
	private ScrolledForm form;
	private TableViewer viewerGraz;
	private TableViewer viewerLeoben;
	private TableViewer viewerBruck;
	private TableViewer viewerMariazell;
	private TableViewer viewerWien;
	private TableViewer viewerKapfenberg;
	private JournalViewTooltip tooltipGraz;
	private JournalViewTooltip tooltipLeoben;
	private JournalViewTooltip tooltipBruck;
	private JournalViewTooltip tooltipMariazell;
	private JournalViewTooltip tooltipWien;
	private JournalViewTooltip tooltipKapfenberg;
	
	//the actions for the context menu
	private EditTransportAction editTransportAction;
	private CancelTransportAction cancelTransportAction;//!!
	private MoveToOutstandingTransportsAction moveToOutstandingTransportsAction;
	

	/**
	 * Constructs a new journal view.
	 */
	public PrebookingView()
	{
		// add listener to model to keep on track. 
		ModelFactory.getInstance().getTransportManager().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup the view
	 */
	@Override
	public void dispose() 
	{
		ModelFactory.getInstance().getTransportManager().removePropertyChangeListener(this);
	}

	/**
	 * Callback method to create the control and initialize them.
	 * @param parent the parent composite to add
	 */
	public void createPartControl(final Composite parent) 
	{
		// Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		form.setText("Vormerkungen");
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new FillLayout());

		final Composite composite = form.getBody();
	
		
		final Composite composite_1 = new Composite(composite, SWT.NONE);
//		final GridLayout gridLayout_2 = new GridLayout();
//		composite_1.setLayout(gridLayout_2);
//		vormerkungTabItem.setControl(composite_1);
		composite_1.setLayout(new FillLayout());

		
		
		final Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new FillLayout());
		final GridData gd_composite_3 = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd_composite_3.widthHint = 989;
		composite_3.setLayoutData(gd_composite_3);
		
		//groups
		final SashForm sashForm_8 = new SashForm(composite_3, SWT.VERTICAL);
		
		final Group richtungBruckGroup = new Group(sashForm_8, SWT.NONE);
		richtungBruckGroup.setLayout(new FillLayout());
		richtungBruckGroup.setText("Richtung Bruck");
		
		final SashForm sashForm_7 = new SashForm(sashForm_8, SWT.VERTICAL);

		final Group richtungKapfenbergGroup = new Group(sashForm_7, SWT.NONE);
		richtungKapfenbergGroup.setLayout(new FillLayout());
		richtungKapfenbergGroup.setText("Richtung Kapfenberg");
		
		
		final Group richtungMariazellGroup = new Group(sashForm_7, SWT.NONE);
		richtungMariazellGroup.setLayout(new FillLayout());
		richtungMariazellGroup.setText("Richtung Mariazell");
		
		final SashForm sashForm_9 = new SashForm(composite_3, SWT.VERTICAL);
		
		final Group richtungGrazGroup = new Group(sashForm_9, SWT.NONE);
		richtungGrazGroup.setLayout(new FillLayout());
		richtungGrazGroup.setText("Richtung Graz");
		
		final Group richtungLeobenGroup = new Group(sashForm_9, SWT.NONE);
		richtungLeobenGroup.setLayout(new FillLayout());
		richtungLeobenGroup.setText("Richtung Leoben");
		
		final SashForm sashForm_1 = new SashForm(sashForm_9, SWT.NONE);
		sashForm_9.setWeights(new int[] {212, 167, 91 });
		
		final Group richtungWienGroup = new Group(sashForm_1, SWT.NONE);
		richtungWienGroup.setLayout(new FillLayout());
		richtungWienGroup.setText("Richtung Wien");


		
		
		//viewers
		viewerLeoben = new TableViewer(richtungLeobenGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerLeoben.setContentProvider(new JournalViewContentProvider());
		viewerLeoben.setLabelProvider(new JournalViewLabelProvider());
		viewerLeoben.setInput(ModelFactory.getInstance().getTransportManager());
		viewerLeoben.getTable().setLinesVisible(true);
		
		
		viewerGraz = new TableViewer(richtungGrazGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerGraz.setContentProvider(new JournalViewContentProvider());
		viewerGraz.setLabelProvider(new JournalViewLabelProvider());
		viewerGraz.setInput(ModelFactory.getInstance().getTransportManager());
		viewerGraz.getTable().setLinesVisible(true);
		
		
		viewerKapfenberg = new TableViewer(richtungKapfenbergGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerKapfenberg.setContentProvider(new JournalViewContentProvider());
		viewerKapfenberg.setLabelProvider(new JournalViewLabelProvider());
		viewerKapfenberg.setInput(ModelFactory.getInstance().getTransportManager());
		viewerKapfenberg.getTable().setLinesVisible(true);
		
		
		viewerBruck = new TableViewer(richtungBruckGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerBruck.setContentProvider(new JournalViewContentProvider());
		viewerBruck.setLabelProvider(new JournalViewLabelProvider());
		viewerBruck.setInput(ModelFactory.getInstance().getTransportManager());
		viewerBruck.getTable().setLinesVisible(true);
		
		
		viewerWien = new TableViewer(richtungWienGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerWien.setContentProvider(new JournalViewContentProvider());
		viewerWien.setLabelProvider(new JournalViewLabelProvider());
		viewerWien.setInput(ModelFactory.getInstance().getTransportManager());
		viewerWien.getTable().setLinesVisible(true);
		
		
		viewerMariazell = new TableViewer(richtungMariazellGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerMariazell.setContentProvider(new JournalViewContentProvider());
		viewerMariazell.setLabelProvider(new JournalViewLabelProvider());
		viewerMariazell.setInput(ModelFactory.getInstance().getTransportManager());
		viewerMariazell.getTable().setLinesVisible(true);
		
		//set the tool tip
		tooltipLeoben = new JournalViewTooltip(viewerLeoben.getControl());
		tooltipGraz = new JournalViewTooltip(viewerGraz.getControl());
		tooltipKapfenberg = new JournalViewTooltip(viewerKapfenberg.getControl());
		tooltipBruck = new JournalViewTooltip(viewerBruck.getControl());
		tooltipWien = new JournalViewTooltip(viewerWien.getControl());
		tooltipMariazell = new JournalViewTooltip(viewerMariazell.getControl());
		
		
		//show the tool tip when the selection has changed
		viewerLeoben.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerLeoben.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipLeoben.show(new Point(bounds.x, bounds.y));
				}
			}
		});     
		
		viewerGraz.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerGraz.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipGraz.show(new Point(bounds.x, bounds.y));
				}
			}
		});    
		
		viewerKapfenberg.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerKapfenberg.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipKapfenberg.show(new Point(bounds.x, bounds.y));
				}
			}
		});    
		
		viewerBruck.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerBruck.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipBruck.show(new Point(bounds.x, bounds.y));
				}
			}
		});    
		
		viewerWien.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerWien.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipWien.show(new Point(bounds.x, bounds.y));
				}
			}
		});    
		
		viewerMariazell.addSelectionChangedListener(new ISelectionChangedListener() 
		{
			public void selectionChanged(SelectionChangedEvent event) 
			{
				TableItem[] selection = viewerMariazell.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltipMariazell.show(new Point(bounds.x, bounds.y));
				}
			}
		});    
		

		//sort the table by default
		viewerLeoben.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerGraz.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerKapfenberg.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerBruck.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerWien.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		viewerMariazell.setSorter(new TransportSorter(TransportSorter.ABF_SORTER,SWT.DOWN));
		

	

		

		//Richtung Bruck
		
		//create the table for the transports
		final Table tableBruck = viewerBruck.getTable();
		tableBruck.setLinesVisible(true);
		tableBruck.setHeaderVisible(true);

		final TableColumn bTableColumnOrtsstelle = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnOrtsstelle.setWidth(39);
		bTableColumnOrtsstelle.setText("OS");

		final TableColumn bTableColumnAbfahrt = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnAbfahrt.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		bTableColumnAbfahrt.setWidth(41);
		bTableColumnAbfahrt.setText("Abf");

		final TableColumn bTableColumnAnkunft = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnAnkunft.setToolTipText("Geplante Ankunft beim Patienten");
		bTableColumnAnkunft.setWidth(45);
		bTableColumnAnkunft.setText("Ank");

		final TableColumn bTableColumnTermin = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnTermin.setToolTipText("Termin am Zielort");
		bTableColumnTermin.setWidth(49);
		bTableColumnTermin.setText("Termin");

		final TableColumn bTableColumnTransportVon = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnTransportVon.setWidth(100);
		bTableColumnTransportVon.setText("Transport von");

		final TableColumn bTtableColumnPatient = new TableColumn(tableBruck, SWT.NONE);
		bTtableColumnPatient.setWidth(100);
		bTtableColumnPatient.setText("Patient");

		final TableColumn bTableColumnTransportNach = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnTransportNach.setWidth(100);
		bTableColumnTransportNach.setText("Transport nach");

		final TableColumn bTableColumnTA = new TableColumn(tableBruck, SWT.NONE);
		bTableColumnTA.setToolTipText("Transportart");
		bTableColumnTA.setWidth(33);
		bTableColumnTA.setText("T");

		//Richtung Kapfenberg
		
		final Table tableKapfenberg = viewerKapfenberg.getTable();
		tableKapfenberg.setLinesVisible(true);
		tableKapfenberg.setHeaderVisible(true);
		

		final TableColumn tableColumnOrtsstelle_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnOrtsstelle_2.setWidth(39);
		tableColumnOrtsstelle_2.setText("OS");

		final TableColumn tableColumnAbfahrt_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnAbfahrt_2.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_2.setWidth(41);
		tableColumnAbfahrt_2.setText("Abf");

		final TableColumn tableColumnAnkunft_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnAnkunft_2.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_2.setWidth(45);
		tableColumnAnkunft_2.setText("Ank");

		final TableColumn tableColumnTermin_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnTermin_2.setToolTipText("Termin am Zielort");
		tableColumnTermin_2.setWidth(49);
		tableColumnTermin_2.setText("Termin");

		final TableColumn tableColumnTransportVon_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnTransportVon_2.setWidth(100);
		tableColumnTransportVon_2.setText("Transport von");

		final TableColumn tableColumnPatient_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnPatient_2.setWidth(100);
		tableColumnPatient_2.setText("Patient");

		final TableColumn tableColumnTransportNach_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnTransportNach_2.setWidth(100);
		tableColumnTransportNach_2.setText("Transport nach");

		final TableColumn tableColumnTA_2 = new TableColumn(tableKapfenberg, SWT.NONE);
		tableColumnTA_2.setToolTipText("Transportart");
		tableColumnTA_2.setWidth(33);
		tableColumnTA_2.setText("T");

		
		//Richtung Mariazell
		
		final Table tableMariazell = viewerMariazell.getTable();
		tableMariazell.setLinesVisible(true);
		tableMariazell.setHeaderVisible(true);


		final TableColumn tableColumnOrtsstelle_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnOrtsstelle_3.setWidth(39);
		tableColumnOrtsstelle_3.setText("OS");

		final TableColumn tableColumnAbfahrt_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnAbfahrt_3.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_3.setWidth(41);
		tableColumnAbfahrt_3.setText("Abf");

		final TableColumn tableColumnAnkunft_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnAnkunft_3.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_3.setWidth(45);
		tableColumnAnkunft_3.setText("Ank");

		final TableColumn tableColumnTermin_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnTermin_3.setToolTipText("Termin am Zielort");
		tableColumnTermin_3.setWidth(49);
		tableColumnTermin_3.setText("Termin");

		final TableColumn tableColumnTransportVon_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnTransportVon_3.setWidth(100);
		tableColumnTransportVon_3.setText("Transport von");

		final TableColumn tableColumnPatient_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnPatient_3.setWidth(100);
		tableColumnPatient_3.setText("Patient");

		final TableColumn tableColumnTransportNach_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnTransportNach_3.setWidth(100);
		tableColumnTransportNach_3.setText("Transport nach");

		final TableColumn tableColumnTA_3 = new TableColumn(tableMariazell, SWT.NONE);
		tableColumnTA_3.setToolTipText("Transportart");
		tableColumnTA_3.setWidth(33);
		tableColumnTA_3.setText("T");

		
		sashForm_8.setWeights(new int[] {212, 261 });
		sashForm_7.setWeights(new int[] {96, 51 });

		

		//Richtung Graz
		final Table tableGraz= viewerGraz.getTable();
		tableGraz.setLinesVisible(true);
		tableGraz.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnOrtsstelle_4.setWidth(39);
		tableColumnOrtsstelle_4.setText("OS");

		final TableColumn tableColumnAbfahrt_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnAbfahrt_4.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_4.setWidth(41);
		tableColumnAbfahrt_4.setText("Abf");

		final TableColumn tableColumnAnkunft_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnAnkunft_4.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_4.setWidth(45);
		tableColumnAnkunft_4.setText("Ank");

		final TableColumn tableColumnTermin_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnTermin_4.setToolTipText("Termin am Zielort");
		tableColumnTermin_4.setWidth(49);
		tableColumnTermin_4.setText("Termin");

		final TableColumn tableColumnTransportVon_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnTransportVon_4.setWidth(100);
		tableColumnTransportVon_4.setText("Transport von");

		final TableColumn tableColumnPatient_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnPatient_4.setWidth(100);
		tableColumnPatient_4.setText("Patient");

		final TableColumn tableColumnTransportNach_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnTransportNach_4.setWidth(100);
		tableColumnTransportNach_4.setText("Transport nach");

		final TableColumn tableColumnTA_4 = new TableColumn(tableGraz, SWT.NONE);
		tableColumnTA_4.setToolTipText("Transportart");
		tableColumnTA_4.setWidth(33);
		tableColumnTA_4.setText("T");

		
		//Richtung Leoben
		final Table tableLeoben= viewerLeoben.getTable();
		tableLeoben.setLinesVisible(true);
		tableLeoben.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnOrtsstelle_5.setWidth(39);
		tableColumnOrtsstelle_5.setText("OS");

		final TableColumn tableColumnAbfahrt_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnAbfahrt_5.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_5.setWidth(41);
		tableColumnAbfahrt_5.setText("Abf");

		final TableColumn tableColumnAnkunft_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnAnkunft_5.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_5.setWidth(45);
		tableColumnAnkunft_5.setText("Ank");

		final TableColumn tableColumnTermin_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnTermin_5.setToolTipText("Termin am Zielort");
		tableColumnTermin_5.setWidth(49);
		tableColumnTermin_5.setText("Termin");

		final TableColumn tableColumnTransportVon_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnTransportVon_5.setWidth(100);
		tableColumnTransportVon_5.setText("Transport von");

		final TableColumn tableColumnPatient_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnPatient_5.setWidth(100);
		tableColumnPatient_5.setText("Patient");

		final TableColumn tableColumnTransportNach_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnTransportNach_5.setWidth(100);
		tableColumnTransportNach_5.setText("Transport nach");

		final TableColumn tableColumnTA_5 = new TableColumn(tableLeoben, SWT.NONE);
		tableColumnTA_5.setToolTipText("Transportart");
		tableColumnTA_5.setWidth(33);
		tableColumnTA_5.setText("T");

		

	
//		sashForm_1.setWeights(new int[] { 1 });

		//Richtung Wien
		final Table tableWien= viewerWien.getTable();
		tableWien.setLinesVisible(true);
		tableWien.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnOrtsstelle_1.setWidth(39);
		tableColumnOrtsstelle_1.setText("OS");

		final TableColumn tableColumnAbfahrt_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnAbfahrt_1.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_1.setWidth(41);
		tableColumnAbfahrt_1.setText("Abf");

		final TableColumn tableColumnAnkunft_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnAnkunft_1.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_1.setWidth(45);
		tableColumnAnkunft_1.setText("Ank");

		final TableColumn tableColumnTermin_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnTermin_1.setToolTipText("Termin am Zielort");
		tableColumnTermin_1.setWidth(49);
		tableColumnTermin_1.setText("Termin");

		final TableColumn tableColumnTransportVon_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnTransportVon_1.setWidth(100);
		tableColumnTransportVon_1.setText("Transport von");

		final TableColumn tableColumnPatient_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnPatient_1.setWidth(100);
		tableColumnPatient_1.setText("Patient");

		final TableColumn tableColumnTransportNach_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnTransportNach_1.setWidth(100);
		tableColumnTransportNach_1.setText("Transport nach");

		final TableColumn tableColumnTA_1 = new TableColumn(tableWien, SWT.NONE);
		tableColumnTA_1.setToolTipText("Transportart");
		tableColumnTA_1.setWidth(33);
		tableColumnTA_1.setText("T");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		


//		//make the columns sort able
//		Listener sortRichtungBruckListener = new Listener() 
//		{
//			public void handleEvent(Event e) 
//			{
//				// determine new sort column and direction
//				TableColumn sortColumn = viewer.getTable().getSortColumn();
//				TableColumn currentColumn = (TableColumn) e.widget;
//				int dir = viewer.getTable().getSortDirection();
//				//revert the sortorder if the column is the same
//				if (sortColumn == currentColumn) 
//				{
//					if(dir == SWT.UP)
//						dir = SWT.DOWN;
//					else
//						dir = SWT.UP;
//				} 
//				else 
//				{
//					viewer.getTable().setSortColumn(currentColumn);
//					dir = SWT.UP;
//				}
//				// sort the data based on column and direction
//				String sortIdentifier = null;
//				if (currentColumn == columnTNr) 
//					sortIdentifier = TransportSorter.TNR_SORTER;
//				if (currentColumn == columnPrioritaet) 
//					sortIdentifier = TransportSorter.PRIORITY_SORTER;
//				if (currentColumn == columnTransport) 
//					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
//				if (currentColumn == columnPatient) 
//					sortIdentifier = TransportSorter.PATIENT_SORTER;
//				if (currentColumn == columnTransport)
//					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
//
//				
//				
//				//apply the filter
//				viewer.getTable().setSortDirection(dir);
//				viewer.setSorter(new PersonalViewSorter(sortIdentifier,dir));
//			}
//		};
//
//		//attach the listener
//		columnTNrJournal.addListener(SWT.Selection, sortListener);
//		columnPrioritaetJournal.addListener(SWT.Selection, sortListener);
//		columnTransportVonJournal.addListener(SWT.Selection, sortListener);
//		columnPatientJournal.addListener(SWT.Selection, sortListener);
//		columnTransportNachJournal.addListener(SWT.Selection, sortListener);
//		columnErkrVerlJournal.addListener(SWT.Selection, sortListener);
//		columnAEJournal.addListener(SWT.Selection, sortListener);
//		columnS1Journal.addListener(SWT.Selection, sortListener);
//		columnS2Journal.addListener(SWT.Selection, sortListener);
//		columnS3Journal.addListener(SWT.Selection, sortListener);
//		columnS4Journal.addListener(SWT.Selection, sortListener);
//		columnS5Journal.addListener(SWT.Selection, sortListener);
//		columnS6Journal.addListener(SWT.Selection, sortListener);
//		columnFzgJournal.addListener(SWT.Selection, sortListener);
//		columnSaniIJournal.addListener(SWT.Selection, sortListener);
//		columnSaniIIJournal.addListener(SWT.Selection, sortListener);
//		columnAnruferJournal.addListener(SWT.Selection, sortListener);
//		
		//create the actions
		makeActions();
		hookContextMenu();
//		contributeToActionBars();

//		tabFolder.setSelection(1);
//		tabFolder.setSelection(0);
	}
	
	/**
	 * Creates the needed actions
	 */
	private void makeActions()
	{		
		editTransportAction = new EditTransportAction(this.viewerLeoben);
		moveToOutstandingTransportsAction = new MoveToOutstandingTransportsAction(this.viewerLeoben);
		cancelTransportAction = new CancelTransportAction(this.viewerLeoben);
	}
	
	/**
	 * Creates the context menue 
	 */
	private void hookContextMenu() 
	{
		MenuManager menuManager = new MenuManager("#PopupMenu");
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menuLeoben = menuManager.createContextMenu(viewerLeoben.getControl());
		viewerLeoben.getControl().setMenu(menuLeoben);
		getSite().registerContextMenu(menuManager, viewerLeoben);
	}
	
	/**
	 * Fills the context menu with the actions
	 */
	private void fillContextMenu(IMenuManager manager)
	{
		//get the selected object
		final Object firstSelectedObject = ((IStructuredSelection) viewerLeoben.getSelection()).getFirstElement();
			
		//cast to a Transport
		Transport transport = (Transport)firstSelectedObject;
		
		if(transport == null)
			return;
		
		//add the actions
		manager.add(editTransportAction);
		manager.add(new Separator());
		manager.add(moveToOutstandingTransportsAction);
		manager.add(cancelTransportAction);
	
	}
	
	


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }

	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName())) 
			this.viewerLeoben.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_REMOVE".equals(evt.getPropertyName())) 
			this.viewerLeoben.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_UPDATE".equals(evt.getPropertyName())) 
			this.viewerLeoben.refresh();
		// event on deletion --> also just refresh
		if ("TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
			this.viewerLeoben.refresh();
	}
}

package at.rc.tacos.client.view;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.controller.CreateDialysisTransportAction;
import at.rc.tacos.client.controller.UpdateDialysisTransportAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PrebookingViewContentProvider;
import at.rc.tacos.client.providers.PrebookingViewLabelProvider;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.Transport;

/**
 * GUI (form) to manage the details of a dialysis patient
 * @author b.thek
*/
public class MultiTransportForm2 implements IKindOfTransport
{
	private Text textFertig;
	private Label abfLabel_1;
	private Button abbrechenButton;
	private Button okButton;
	private Group transportdatenGroup;
	private Group patientenzustandGroup;
	private Group planungGroup;
	private Button begleitpersonButton;
	private Combo comboZustOrtsstelle;
	private Button button_stationary;
	private Button button;
	private Combo comboNachOrt;
	private Combo comboNachStrasse;
	private Combo comboVorname;
	private Combo comboNachname;
	private Combo comboVonOrt;
	private Combo comboVonStrasse;
	private Button sonntagButton;
	private Button samstagButton;
	private Button freitagButton;
	private Button donnerstagButton;
	private Button mittwochButton;
	private Button dienstagButton;
	private Button montagButton;
	private Text textAbfRT;
	private Text textTermin;
	private Text textBeiPat;
	private Text textAbf;
	protected Shell shell;
	private Combo combokindOfTransport;
	
	private Listener exitListener;
	
	private boolean createNew;
	
	private DialysisPatient dia;

	 /**
     * constructor used to create a 
     * a new dialysis transport entry.
     */
	public MultiTransportForm2()
	{
		createContents();
	}
	
	 /**
     * Open the window
     */
	public void open()
	{
		shell.open();
	}
	
	/**
     * used to edit an dialysis entry
     * @param dialysisPatient the dialysisPatient to edit
     */
	public MultiTransportForm2(Transport transport)
	{
		
		
		
		
		
		
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setLayout(new FormLayout());
		shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		shell.setSize(1083, 223);
		shell.setText("Mehrfachformerkung");
		
		
		final TableViewer viewer = new TableViewer(shell, SWT.CHECK |SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewer.setContentProvider(new PrebookingViewContentProvider());
		viewer.setLabelProvider(new PrebookingViewLabelProvider());
		viewer.setInput(ModelFactory.getInstance().getTransportManager().toArray());
		viewer.getTable().setLinesVisible(true);
		
		viewer.getTable().addMouseListener(new MouseAdapter() 
		{
			public void mouseDown(MouseEvent e) 
			{
				if( viewer.getTable().getItem(new Point(e.x,e.y))==null ) 
				{
					viewer.setSelection(new StructuredSelection());
				}
			}
		});

		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn blockColumn = new TableColumn(table, SWT.CHECK);
		blockColumn.setToolTipText("Transport abspeichern");
		blockColumn.setWidth(20);
		blockColumn.setText("S");
	

		final TableColumn bTableColumnOrtsstelle = new TableColumn(table, SWT.NONE);
		bTableColumnOrtsstelle.setWidth(27);
		bTableColumnOrtsstelle.setText("OS");

		final TableColumn bTableColumnAbfahrt = new TableColumn(table, SWT.NONE);
		bTableColumnAbfahrt.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		bTableColumnAbfahrt.setWidth(40);
		bTableColumnAbfahrt.setText("Abf");

		final TableColumn bTableColumnAnkunft = new TableColumn(table, SWT.NONE);
		bTableColumnAnkunft.setToolTipText("Geplante Ankunft beim Patienten");
		bTableColumnAnkunft.setWidth(40);
		bTableColumnAnkunft.setText("Ank");

		final TableColumn bTableColumnTermin = new TableColumn(table, SWT.NONE);
		bTableColumnTermin.setToolTipText("Termin am Zielort");
		bTableColumnTermin.setWidth(40);
		bTableColumnTermin.setText("Termin");

		final TableColumn bTableColumnTransportVon = new TableColumn(table, SWT.NONE);
		bTableColumnTransportVon.setWidth(190);
		bTableColumnTransportVon.setText("Transport von");

		final TableColumn bTtableColumnPatient = new TableColumn(table, SWT.NONE);
		bTtableColumnPatient.setWidth(160);
		bTtableColumnPatient.setText("Patient");

		final TableColumn bTableColumnTransportNach = new TableColumn(table, SWT.NONE);
		bTableColumnTransportNach.setWidth(190);
		bTableColumnTransportNach.setText("Transport nach");

		final TableColumn bTableColumnTA = new TableColumn(table, SWT.NONE);
		bTableColumnTA.setToolTipText("Transportart");
		bTableColumnTA.setWidth(20);
		bTableColumnTA.setText("T");

		Listener sortListener = new Listener() 
		{
			public void handleEvent(Event e) 
			{
				// determine new sort column and direction
				TableColumn sortColumn = viewer.getTable().getSortColumn();
				TableColumn currentColumn = (TableColumn) e.widget;
				int dir = viewer.getTable().getSortDirection();
				//revert the sort order if the column is the same
				if (sortColumn == currentColumn) 
				{
					if(dir == SWT.UP)
						dir = SWT.DOWN;
					else
						dir = SWT.UP;
				} 
				else 
				{
					viewer.getTable().setSortColumn(currentColumn);
					dir = SWT.UP;
				}
				// sort the data based on column and direction
				String sortIdentifier = null;
				if (currentColumn == bTableColumnOrtsstelle) 
					sortIdentifier = TransportSorter.RESP_STATION_SORTER;
				if (currentColumn == bTableColumnAbfahrt) 
					sortIdentifier = TransportSorter.ABF_SORTER;
				if (currentColumn == bTableColumnAnkunft) 
					sortIdentifier = TransportSorter.AT_PATIENT_SORTER;
				if (currentColumn == bTableColumnTermin)
					sortIdentifier = TransportSorter.TERM_SORTER;
				if (currentColumn == bTableColumnTransportVon)
					sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
				if(currentColumn == bTtableColumnPatient)
					sortIdentifier = TransportSorter.PATIENT_SORTER;
				if(currentColumn == bTableColumnTransportNach)
					sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
				if(currentColumn == bTableColumnTA)
					sortIdentifier = TransportSorter.TA_SORTER;
				//apply the filter
				viewer.getTable().setSortDirection(dir);
				viewer.setSorter(new TransportSorter(sortIdentifier,dir));
			}
		};

		//attach the listener
		bTableColumnOrtsstelle.addListener(SWT.Selection, sortListener);
		bTableColumnAbfahrt.addListener(SWT.Selection, sortListener);
		bTableColumnAnkunft.addListener(SWT.Selection, sortListener);
		bTableColumnTermin.addListener(SWT.Selection, sortListener);
		bTableColumnTransportVon.addListener(SWT.Selection, sortListener);
		bTtableColumnPatient.addListener(SWT.Selection, sortListener);
		bTableColumnTransportNach.addListener(SWT.Selection, sortListener);
		bTableColumnTA.addListener(SWT.Selection, sortListener);

		
	

		transportdatenGroup = new Group(shell, SWT.NONE);
		final FormData fd_transportdatenGroup = new FormData();
		fd_transportdatenGroup.right = new FormAttachment(0, 1067);
		fd_transportdatenGroup.left = new FormAttachment(0, 205);
		transportdatenGroup.setLayoutData(fd_transportdatenGroup);
		transportdatenGroup.setForeground(Util.getColor(128, 128, 128));
		transportdatenGroup.setText("Transportdaten");

		abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.top = new FormAttachment(patientenzustandGroup, -23, SWT.BOTTOM);
		fd_abbrechenButton.bottom = new FormAttachment(patientenzustandGroup, 0, SWT.BOTTOM);
		fd_abbrechenButton.left = new FormAttachment(transportdatenGroup, -96, SWT.RIGHT);
		fd_abbrechenButton.right = new FormAttachment(transportdatenGroup, 0, SWT.RIGHT);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
//		abbrechenButton.setImage(ImageFactory.getInstance().getRegisteredImage("icon.stop"));
		abbrechenButton.setText("Abbrechen");
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
				MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				dialog.setText("Abbrechen");
				dialog.setMessage("Wollen Sie wirklich abbrechen?");
				if (e.type == SWT.Close) 
					e.doit = false;
				if (dialog.open() != SWT.YES) 
					return;
				shell.dispose();
			}
		};
		
		abbrechenButton.addListener(SWT.Selection, exitListener);
		
		

		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.top = new FormAttachment(abbrechenButton, -23, SWT.BOTTOM);
		fd_okButton.bottom = new FormAttachment(abbrechenButton, 0, SWT.BOTTOM);
		fd_okButton.left = new FormAttachment(abbrechenButton, -101, SWT.LEFT);
		fd_okButton.right = new FormAttachment(abbrechenButton, -5, SWT.LEFT);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		
			
	}
}

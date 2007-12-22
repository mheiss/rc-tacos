package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * Main view, provides an overview about the transports
 * @author b.thek
 */

public class TransportMainView {

	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TransportMainView window = new TransportMainView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		//shell.setMaximized(true);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(TransportMainView.class, "/image/Tacos_LOGO.jpg"));
		shell.setLayout(new swing2swt.layout.GridLayout());
		shell.setText("Transporte");

		final TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		final TabItem dispositionTabItem = new TabItem(tabFolder, SWT.NONE);
		dispositionTabItem.setText("Disposition");

		final SashForm sashForm = new SashForm(tabFolder, SWT.VERTICAL);
		dispositionTabItem.setControl(sashForm);
		sashForm.setLayout(new FillLayout());

		final Group disponierteTransporteGroup = new Group(sashForm, SWT.NONE);
		disponierteTransporteGroup.setLayout(new FillLayout());
		disponierteTransporteGroup.setText("Disponierte Transporte");

		final Table table = new Table(disponierteTransporteGroup, SWT.BORDER | SWT.V_SCROLL);
		//table.setRedraw(true);
		
		//table.setSelection(null);
		table.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		//table.setColumnOrder(null);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnPrioritaetDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnPrioritaetDisponierteTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		columnPrioritaetDisponierteTransporte.setWidth(36);
		columnPrioritaetDisponierteTransporte.setText("Pr");

		final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
		newItemTableItem.setText(14, "Bm08");
		newItemTableItem.setText(7, "06:56");
		newItemTableItem.setImage(16, SWTResourceManager.getImage(TransportMainView.class, "/image/O_BeglPerson_Table.bmp"));
		newItemTableItem.setText(2, "13:30");
		newItemTableItem.setText(1, "12388");
		newItemTableItem.setText(0, "A");
		newItemTableItem.setText("New item");

		final TableColumn columnTransportNummerDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnTransportNummerDisponierteTransporte.setToolTipText("Ortsstellenabhängige Transportnummer");
		columnTransportNummerDisponierteTransporte.setMoveable(true);
		columnTransportNummerDisponierteTransporte.setWidth(54);
		columnTransportNummerDisponierteTransporte.setText("TNr");

		final TableColumn columnTerminDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnTerminDisponierteTransporte.setToolTipText("Termin am Zielort");
		columnTerminDisponierteTransporte.setMoveable(true);
		columnTerminDisponierteTransporte.setWidth(48);
		columnTerminDisponierteTransporte.setText("Termin");

		final TableColumn columnTransportVonDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnTransportVonDisponierteTransporte.setMoveable(true);
		columnTransportVonDisponierteTransporte.setWidth(118);
		columnTransportVonDisponierteTransporte.setText("Transport von");

		final TableColumn columnPatientDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnPatientDisponierteTransporte.setMoveable(true);
		columnPatientDisponierteTransporte.setWidth(100);
		columnPatientDisponierteTransporte.setText("Patient");

		final TableColumn columnTransportNachDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnTransportNachDisponierteTransporte.setWidth(100);
		columnTransportNachDisponierteTransporte.setText("Transport nach");

		final TableColumn columnAEDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnAEDisponierteTransporte.setToolTipText("Auftrag erteilt");
		columnAEDisponierteTransporte.setWidth(39);
		columnAEDisponierteTransporte.setText("AE");

		final TableColumn columnS1DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS1DisponierteTransporte.setToolTipText("Transportbeginn");
		columnS1DisponierteTransporte.setWidth(36);
		columnS1DisponierteTransporte.setText("S1");

		final TableColumn columnS2DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS2DisponierteTransporte.setToolTipText("Ankunft bei Patient");
		columnS2DisponierteTransporte.setWidth(38);
		columnS2DisponierteTransporte.setText("S2");

		final TableColumn columnS3DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS3DisponierteTransporte.setToolTipText("Abfahrt mit Patient");
		columnS3DisponierteTransporte.setWidth(40);
		columnS3DisponierteTransporte.setText("S3");

		final TableColumn columnS4DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS4DisponierteTransporte.setToolTipText("Ankunft Ziel");
		columnS4DisponierteTransporte.setWidth(36);
		columnS4DisponierteTransporte.setText("S4");

		final TableColumn columnS7DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS7DisponierteTransporte.setToolTipText("Einsatzgebiet verlassen");
		columnS7DisponierteTransporte.setWidth(40);
		columnS7DisponierteTransporte.setText("S7");

		final TableColumn columnS8DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS8DisponierteTransporte.setToolTipText("Zurück im Einsatzgebiet");
		columnS8DisponierteTransporte.setWidth(34);
		columnS8DisponierteTransporte.setText("S8");

		final TableColumn columnS9DisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnS9DisponierteTransporte.setToolTipText("Sonderstatus");
		columnS9DisponierteTransporte.setWidth(34);
		columnS9DisponierteTransporte.setText("S9");

		final TableColumn columnFzgDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnFzgDisponierteTransporte.setToolTipText("Fahrzeugkennzeichnung");
		columnFzgDisponierteTransporte.setWidth(52);
		columnFzgDisponierteTransporte.setText("Fzg");

		final TableColumn columnTDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnTDisponierteTransporte.setToolTipText("Transportart");
		columnTDisponierteTransporte.setWidth(53);
		columnTDisponierteTransporte.setText("T");
		
		
//		//TODO
//		//Für Tabelle "disponierte Transporte"
//		disponierteTransporteGroup.addControlListener(new ControlAdapter() 
//		{
//			public void controlResized(final ControlEvent e) 
//			{
//				System.out.println("----------- im Controlllistener der table, im controlResized-------------");
//				Rectangle area = disponierteTransporteGroup.getClientArea();
//				Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//				ScrollBar vBar = table.getVerticalBar();
//				int width = area.width - table.computeTrim(0,0,0,0).width - vBar.getSize().x;
//				if (size.y > area.height + table.getHeaderHeight()) 
//				{
//					// Subtract the scrollbar width from the total column width
//					// if a vertical scrollbar will be required
//					Point vBarSize = vBar.getSize();
//					width -= vBarSize.x;
//				}
//				
//				
//				Point oldSize = table.getSize();
//				if (oldSize.x > area.width) 
//				{
//					// table is getting smaller so make the columns 
//					// smaller first and then resize the table to
//					// match the client area width
//					columnPrioritaetDisponierteTransporte.setWidth(width/3);//Spalte "Pr"
//					columnTransportNummerDisponierteTransporte.setWidth(width - columnPrioritaetDisponierteTransporte.getWidth());
//				
//					
//					table.setSize(area.width, area.height);
//				} else 
//				{
//					// table is getting bigger so make the table 
//					// bigger first and then make the columns wider
//					// to match the client area width
//					table.setSize(area.width, area.height);
//					columnPrioritaetDisponierteTransporte.setWidth(width/3);
//					columnTransportNummerDisponierteTransporte.setWidth(width - columnPrioritaetDisponierteTransporte.getWidth());
//					//columnAbfahrt.setWidth(width - columnTransportNummer.getWidth());
//	
//					
//				}
//			}
//		});

		final TableColumn columnErkrankungVerletzungDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnErkrankungVerletzungDisponierteTransporte.setWidth(146);
		columnErkrankungVerletzungDisponierteTransporte.setText("Erkrankung/Verletzung");

		final TableColumn columnSymboleDisponierteTransporte = new TableColumn(table, SWT.NONE);
		columnSymboleDisponierteTransporte.setWidth(139);
		columnSymboleDisponierteTransporte.setText("Symbole");
		
		
		
		
		

		final TableItem newItemTableItem_1 = new TableItem(table, SWT.BORDER);
		newItemTableItem_1.setText(5, "LKH Bruck Unf. Amb.");
		newItemTableItem_1.setText(3, "Dr. Theodor Körnerstraße 19b/8600");
		newItemTableItem_1.setText(4, "Mustermann Max");
		newItemTableItem_1.setText(0, "B");
		newItemTableItem_1.setText(2, "06:00");
		newItemTableItem_1.setText(1, "14599");
		newItemTableItem_1.setText("New item");

		final TableItem newItemTableItem_2 = new TableItem(table, SWT.BORDER);
		newItemTableItem_2.setText(0, "C");
		newItemTableItem_2.setText("New item");

		final TableItem newItemTableItem_3 = new TableItem(table, SWT.BORDER);
		newItemTableItem_3.setText(0, "3");
		newItemTableItem_3.setText("New item");

		final TableItem newItemTableItem_4 = new TableItem(table, SWT.BORDER);
		newItemTableItem_4.setText("New item");

		final TableItem newItemTableItem_5 = new TableItem(table, SWT.BORDER);
		newItemTableItem_5.setText("New item");

		final TableItem newItemTableItem_6 = new TableItem(table, SWT.BORDER);
		newItemTableItem_6.setText("New item");

		final TableItem newItemTableItem_7 = new TableItem(table, SWT.BORDER);
		newItemTableItem_7.setText("New item");

		final TableItem newItemTableItem_9 = new TableItem(table, SWT.BORDER);
		newItemTableItem_9.setText(new String[] {"aafdsfads"});
		newItemTableItem_9.setGrayed(true);
		newItemTableItem_9.setText("New item");

		final Group offeneTransporteGroup = new Group(sashForm, SWT.NO_RADIO_GROUP | SWT.BORDER);
		offeneTransporteGroup.addControlListener(new ControlAdapter() {
			public void controlResized(final ControlEvent e) {
			}
		});
		
		
		offeneTransporteGroup.setLayout(new FillLayout());
		offeneTransporteGroup.setText("Offene Transporte");

		final Table table_1 = new Table(offeneTransporteGroup, SWT.BORDER);
		table_1.setRedraw(true);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);

		final TableColumn newColumnTableColumnIdOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnIdOffeneTransporte.setWidth(29);
		newColumnTableColumnIdOffeneTransporte.setText("Id");

		final TableColumn newColumnTableColumnPrioritaetOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnPrioritaetOffeneTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		newColumnTableColumnPrioritaetOffeneTransporte.setWidth(31);
		newColumnTableColumnPrioritaetOffeneTransporte.setText("Pr");

		final TableColumn newColumnTableColumnOSOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		newColumnTableColumnOSOffeneTransporte.setWidth(49);
		newColumnTableColumnOSOffeneTransporte.setText("OS");

		final TableColumn newColumnTableColumnAbfOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnAbfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		newColumnTableColumnAbfOffeneTransporte.setWidth(54);
		newColumnTableColumnAbfOffeneTransporte.setText("Abf");

		final TableColumn newColumnTableColumnAnkOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnAnkOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		newColumnTableColumnAnkOffeneTransporte.setWidth(53);
		newColumnTableColumnAnkOffeneTransporte.setText("Ank.");

		final TableColumn newColumnTableColumnTerminOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnTerminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		newColumnTableColumnTerminOffeneTransporte.setWidth(57);
		newColumnTableColumnTerminOffeneTransporte.setText("Termin");

		final TableColumn newColumnTableColumnTransportVonOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnTransportVonOffeneTransporte.setWidth(102);
		newColumnTableColumnTransportVonOffeneTransporte.setText("Transport von");

		final TableColumn newColumnTableColumnPatientOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnPatientOffeneTransporte.setWidth(100);
		newColumnTableColumnPatientOffeneTransporte.setText("Patient");

		final TableColumn newColumnTableColumnTransportNachOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnTransportNachOffeneTransporte.setWidth(100);
		newColumnTableColumnTransportNachOffeneTransporte.setText("Transport nach");

		final TableColumn newColumnTableColumnAufgOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnAufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		newColumnTableColumnAufgOffeneTransporte.setWidth(100);
		newColumnTableColumnAufgOffeneTransporte.setText("Aufg");

		final TableColumn newColumnTableColumnTOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnTOffeneTransporte.setToolTipText("Transportart");
		newColumnTableColumnTOffeneTransporte.setWidth(100);
		newColumnTableColumnTOffeneTransporte.setText("T");

		final TableColumn newColumnTableColumnErkrankungVerletzungOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setWidth(74);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");

		final TableColumn newColumnTableColumnAnmerkungOffeneTransporte = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnAnmerkungOffeneTransporte.setWidth(71);
		newColumnTableColumnAnmerkungOffeneTransporte.setText("Anmerkung");

		final TableColumn newColumnTableColumnSymbole = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumnSymbole.setMoveable(true);
		newColumnTableColumnSymbole.setWidth(58);
		newColumnTableColumnSymbole.setText("Symbole");

		final TableItem tableItem = new TableItem(table_1, SWT.BORDER);
		tableItem.setText(5, "3");
		tableItem.setText(4, "1");
		tableItem.setText("New item");

		final TableItem tableItem_1 = new TableItem(table_1, SWT.BORDER);
		tableItem_1.setText(5, "2");
		tableItem_1.setText(4, "2");
		tableItem_1.setText("New item");

		final TableItem tableItem_2 = new TableItem(table_1, SWT.BORDER);
		tableItem_2.setText(5, "1");
		tableItem_2.setText(4, "3");
		tableItem_2.setText("New item");

		final TableItem tableItem_3 = new TableItem(table_1, SWT.BORDER);
		tableItem_3.setText("New item");

		final TableItem tableItem_4 = new TableItem(table_1, SWT.BORDER);
		tableItem_4.setText("New item");
		sashForm.setWeights(new int[] {274, 132 });

		final TabItem vormerkungTabItem = new TabItem(tabFolder, SWT.NONE);
		vormerkungTabItem.setText("Vormerkung");

		final Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		composite_1.setLayout(gridLayout_2);
		vormerkungTabItem.setControl(composite_1);

		//TODO --> for the personal view
		//group filter
		final Group filterGroup = new Group(composite_1, SWT.NONE);
		filterGroup.setText("Filter");
		final GridData gd_filterGroup = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_filterGroup.heightHint = 30;
		gd_filterGroup.widthHint = 730;
		filterGroup.setLayoutData(gd_filterGroup);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 9;
		filterGroup.setLayout(gridLayout_3);

		final Label ortsstelleLabel_1 = new Label(filterGroup, SWT.NONE);
		ortsstelleLabel_1.setText("Ortsstelle:");

		final Combo comboOrtsstelle = new Combo(filterGroup, SWT.NONE);
		final GridData gd_comboOrtsstelle = new GridData();
		comboOrtsstelle.setLayoutData(gd_comboOrtsstelle);

		final Label datumLabel = new Label(filterGroup, SWT.NONE);
		datumLabel.setText("Datum:");

		final Label sucheLabel_1 = new Label(filterGroup, SWT.NONE);
		sucheLabel_1.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		sucheLabel_1.setText("Suche:");

		final Label nachSpalteLabel_1 = new Label(filterGroup, SWT.NONE);
		nachSpalteLabel_1.setText("nach Spalte:");

		final Combo comboNachSpalte = new Combo(filterGroup, SWT.NONE);
		final GridData gd_comboNachSpalte = new GridData(115, SWT.DEFAULT);
		comboNachSpalte.setLayoutData(gd_comboNachSpalte);

		final Label begriffLabel_1 = new Label(filterGroup, SWT.NONE);
		begriffLabel_1.setText("Begriff:");

		final Text textBegriff = new Text(filterGroup, SWT.BORDER);
		final GridData gd_textBegriff = new GridData(189, SWT.DEFAULT);
		textBegriff.setLayoutData(gd_textBegriff);

		final Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new FillLayout());
		final GridData gd_composite_3 = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd_composite_3.widthHint = 989;
		composite_3.setLayoutData(gd_composite_3);

		final SashForm sashForm_8 = new SashForm(composite_3, SWT.VERTICAL);

		final Group richtungBruckGroup_1 = new Group(sashForm_8, SWT.NONE);
		richtungBruckGroup_1.setLayout(new FillLayout());
		richtungBruckGroup_1.setText("Richtung Bruck");

		final Table table_4 = new Table(richtungBruckGroup_1, SWT.BORDER);
		table_4.setLinesVisible(true);
		table_4.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle = new TableColumn(table_4, SWT.NONE);
		tableColumnOrtsstelle.setWidth(39);
		tableColumnOrtsstelle.setText("OS");

		final TableColumn tableColumnAbfahrt = new TableColumn(table_4, SWT.NONE);
		tableColumnAbfahrt.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt.setWidth(41);
		tableColumnAbfahrt.setText("Abf");

		final TableColumn tableColumnAnkunft = new TableColumn(table_4, SWT.NONE);
		tableColumnAnkunft.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft.setWidth(45);
		tableColumnAnkunft.setText("Ank");

		final TableColumn tableColumnTermin = new TableColumn(table_4, SWT.NONE);
		tableColumnTermin.setToolTipText("Termin am Zielort");
		tableColumnTermin.setWidth(49);
		tableColumnTermin.setText("Termin");

		final TableColumn tableColumnTransportVon = new TableColumn(table_4, SWT.NONE);
		tableColumnTransportVon.setWidth(100);
		tableColumnTransportVon.setText("Transport von");

		final TableColumn tableColumnPatient = new TableColumn(table_4, SWT.NONE);
		tableColumnPatient.setWidth(100);
		tableColumnPatient.setText("Patient");

		final TableColumn tableColumnTransportNach = new TableColumn(table_4, SWT.NONE);
		tableColumnTransportNach.setWidth(100);
		tableColumnTransportNach.setText("Transport nach");

		final TableColumn tableColumnTA = new TableColumn(table_4, SWT.NONE);
		tableColumnTA.setToolTipText("Transportart");
		tableColumnTA.setWidth(33);
		tableColumnTA.setText("T");

		final TableItem tableItem_5 = new TableItem(table_4, SWT.BORDER);
		tableItem_5.setText("New item");

		final TableItem tableItem_6 = new TableItem(table_4, SWT.BORDER);
		tableItem_6.setText("New item");

		final TableItem tableItem_9 = new TableItem(table_4, SWT.BORDER);
		tableItem_9.setText("New item");

		final TableItem tableItem_7 = new TableItem(table_4, SWT.BORDER);
		tableItem_7.setText("New item");

		final SashForm sashForm_7 = new SashForm(sashForm_8, SWT.VERTICAL);

		final Group richtungKapfenbergGroup_1 = new Group(sashForm_7, SWT.NONE);
		richtungKapfenbergGroup_1.setLayout(new FillLayout());
		richtungKapfenbergGroup_1.setText("Richtung Kapfenberg");

		final Table table_4_2 = new Table(richtungKapfenbergGroup_1, SWT.BORDER);
		table_4_2.setLinesVisible(true);
		table_4_2.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnOrtsstelle_2.setWidth(39);
		tableColumnOrtsstelle_2.setText("OS");

		final TableColumn tableColumnAbfahrt_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnAbfahrt_2.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_2.setWidth(41);
		tableColumnAbfahrt_2.setText("Abf");

		final TableColumn tableColumnAnkunft_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnAnkunft_2.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_2.setWidth(45);
		tableColumnAnkunft_2.setText("Ank");

		final TableColumn tableColumnTermin_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnTermin_2.setToolTipText("Termin am Zielort");
		tableColumnTermin_2.setWidth(49);
		tableColumnTermin_2.setText("Termin");

		final TableColumn tableColumnTransportVon_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnTransportVon_2.setWidth(100);
		tableColumnTransportVon_2.setText("Transport von");

		final TableColumn tableColumnPatient_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnPatient_2.setWidth(100);
		tableColumnPatient_2.setText("Patient");

		final TableColumn tableColumnTransportNach_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnTransportNach_2.setWidth(100);
		tableColumnTransportNach_2.setText("Transport nach");

		final TableColumn tableColumnTA_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumnTA_2.setToolTipText("Transportart");
		tableColumnTA_2.setWidth(33);
		tableColumnTA_2.setText("T");

		final TableItem tableItem_5_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_5_2.setText("New item");

		final TableItem tableItem_6_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_6_2.setText("New item");

		final TableItem tableItem_9_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_9_2.setText("New item");

		final TableItem tableItem_7_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_7_2.setText("New item");

		final Group richtungAflenzGroup = new Group(sashForm_7, SWT.NONE);
		richtungAflenzGroup.setLayout(new FillLayout());
		richtungAflenzGroup.setText("Richtung Aflenz");

		final Table table_4_3 = new Table(richtungAflenzGroup, SWT.BORDER);
		table_4_3.setLinesVisible(true);
		table_4_3.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnOrtsstelle_3.setWidth(39);
		tableColumnOrtsstelle_3.setText("OS");

		final TableColumn tableColumnAbfahrt_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnAbfahrt_3.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_3.setWidth(41);
		tableColumnAbfahrt_3.setText("Abf");

		final TableColumn tableColumnAnkunft_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnAnkunft_3.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_3.setWidth(45);
		tableColumnAnkunft_3.setText("Ank");

		final TableColumn tableColumnTermin_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnTermin_3.setToolTipText("Termin am Zielort");
		tableColumnTermin_3.setWidth(49);
		tableColumnTermin_3.setText("Termin");

		final TableColumn tableColumnTransportVon_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnTransportVon_3.setWidth(100);
		tableColumnTransportVon_3.setText("Transport von");

		final TableColumn tableColumnPatient_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnPatient_3.setWidth(100);
		tableColumnPatient_3.setText("Patient");

		final TableColumn tableColumnTransportNach_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnTransportNach_3.setWidth(100);
		tableColumnTransportNach_3.setText("Transport nach");

		final TableColumn tableColumnTA_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumnTA_3.setToolTipText("Transportart");
		tableColumnTA_3.setWidth(33);
		tableColumnTA_3.setText("T");

		final TableItem tableItem_5_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_5_3.setText("New item");

		final TableItem tableItem_6_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_6_3.setText("New item");

		final TableItem tableItem_9_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_9_3.setText("New item");

		final TableItem tableItem_7_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_7_3.setText("New item");
		sashForm_8.setWeights(new int[] {212, 261 });
		sashForm_7.setWeights(new int[] {96, 51 });

		final SashForm sashForm_9 = new SashForm(composite_3, SWT.VERTICAL);

		final Group richtungGrazGroup_1 = new Group(sashForm_9, SWT.NONE);
		richtungGrazGroup_1.setLayout(new FillLayout());
		richtungGrazGroup_1.setText("Richtung Graz");

		final Table table_4_4 = new Table(richtungGrazGroup_1, SWT.BORDER);
		table_4_4.setLinesVisible(true);
		table_4_4.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnOrtsstelle_4.setWidth(39);
		tableColumnOrtsstelle_4.setText("OS");

		final TableColumn tableColumnAbfahrt_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnAbfahrt_4.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_4.setWidth(41);
		tableColumnAbfahrt_4.setText("Abf");

		final TableColumn tableColumnAnkunft_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnAnkunft_4.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_4.setWidth(45);
		tableColumnAnkunft_4.setText("Ank");

		final TableColumn tableColumnTermin_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnTermin_4.setToolTipText("Termin am Zielort");
		tableColumnTermin_4.setWidth(49);
		tableColumnTermin_4.setText("Termin");

		final TableColumn tableColumnTransportVon_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnTransportVon_4.setWidth(100);
		tableColumnTransportVon_4.setText("Transport von");

		final TableColumn tableColumnPatient_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnPatient_4.setWidth(100);
		tableColumnPatient_4.setText("Patient");

		final TableColumn tableColumnTransportNach_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnTransportNach_4.setWidth(100);
		tableColumnTransportNach_4.setText("Transport nach");

		final TableColumn tableColumnTA_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumnTA_4.setToolTipText("Transportart");
		tableColumnTA_4.setWidth(33);
		tableColumnTA_4.setText("T");

		final TableItem tableItem_5_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_5_4.setText("New item");

		final TableItem tableItem_6_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_6_4.setText("New item");

		final TableItem tableItem_9_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_9_4.setText("New item");

		final TableItem tableItem_7_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_7_4.setText("New item");

		final Group richtungLeobenGroup_1 = new Group(sashForm_9, SWT.NONE);
		richtungLeobenGroup_1.setLayout(new FillLayout());
		richtungLeobenGroup_1.setText("Richtung Leoben");

		final Table table_4_5 = new Table(richtungLeobenGroup_1, SWT.BORDER);
		table_4_5.setLinesVisible(true);
		table_4_5.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnOrtsstelle_5.setWidth(39);
		tableColumnOrtsstelle_5.setText("OS");

		final TableColumn tableColumnAbfahrt_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnAbfahrt_5.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_5.setWidth(41);
		tableColumnAbfahrt_5.setText("Abf");

		final TableColumn tableColumnAnkunft_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnAnkunft_5.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_5.setWidth(45);
		tableColumnAnkunft_5.setText("Ank");

		final TableColumn tableColumnTermin_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnTermin_5.setToolTipText("Termin am Zielort");
		tableColumnTermin_5.setWidth(49);
		tableColumnTermin_5.setText("Termin");

		final TableColumn tableColumnTransportVon_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnTransportVon_5.setWidth(100);
		tableColumnTransportVon_5.setText("Transport von");

		final TableColumn tableColumnPatient_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnPatient_5.setWidth(100);
		tableColumnPatient_5.setText("Patient");

		final TableColumn tableColumnTransportNach_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnTransportNach_5.setWidth(100);
		tableColumnTransportNach_5.setText("Transport nach");

		final TableColumn tableColumnTA_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumnTA_5.setToolTipText("Transportart");
		tableColumnTA_5.setWidth(33);
		tableColumnTA_5.setText("T");

		final TableItem tableItem_5_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_5_5.setText("New item");

		final TableItem tableItem_6_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_6_5.setText("New item");

		final TableItem tableItem_9_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_9_5.setText("New item");

		final TableItem tableItem_7_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_7_5.setText("New item");

		final SashForm sashForm_1 = new SashForm(sashForm_9, SWT.NONE);
		sashForm_9.setWeights(new int[] {212, 167, 91 });
//		sashForm_1.setWeights(new int[] { 1 });

		final Group group_9 = new Group(sashForm_1, SWT.NONE);
		group_9.setLayout(new FillLayout());
		group_9.setText("Richtung Mürzzuschlag");

		final Table table_4_1 = new Table(group_9, SWT.BORDER);
		table_4_1.setLinesVisible(true);
		table_4_1.setHeaderVisible(true);

		final TableColumn tableColumnOrtsstelle_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnOrtsstelle_1.setWidth(39);
		tableColumnOrtsstelle_1.setText("OS");

		final TableColumn tableColumnAbfahrt_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnAbfahrt_1.setToolTipText("Geplante Abfahrt an der Ortsstelle");
		tableColumnAbfahrt_1.setWidth(41);
		tableColumnAbfahrt_1.setText("Abf");

		final TableColumn tableColumnAnkunft_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnAnkunft_1.setToolTipText("Geplante Ankunft beim Patienten");
		tableColumnAnkunft_1.setWidth(45);
		tableColumnAnkunft_1.setText("Ank");

		final TableColumn tableColumnTermin_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnTermin_1.setToolTipText("Termin am Zielort");
		tableColumnTermin_1.setWidth(49);
		tableColumnTermin_1.setText("Termin");

		final TableColumn tableColumnTransportVon_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnTransportVon_1.setWidth(100);
		tableColumnTransportVon_1.setText("Transport von");

		final TableColumn tableColumnPatient_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnPatient_1.setWidth(100);
		tableColumnPatient_1.setText("Patient");

		final TableColumn tableColumnTransportNach_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnTransportNach_1.setWidth(100);
		tableColumnTransportNach_1.setText("Transport nach");

		final TableColumn tableColumnTA_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumnTA_1.setToolTipText("Transportart");
		tableColumnTA_1.setWidth(33);
		tableColumnTA_1.setText("T");

		final TableItem tableItem_5_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_5_1.setText("New item");

		final TableItem tableItem_6_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_6_1.setText("New item");

		final TableItem tableItem_9_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_9_1.setText("New item");

		final TableItem tableItem_7_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_7_1.setText("New item");

		final TabItem journalTabItem = new TabItem(tabFolder, SWT.NONE);
		journalTabItem.setText("Journal");

		final Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new GridLayout());
		journalTabItem.setControl(composite);

		final Group filterGroup_1 = new Group(composite, SWT.NONE);
		final GridData gd_filterGroup_1 = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_filterGroup_1.heightHint = 30;
		gd_filterGroup_1.widthHint = 993;
		filterGroup_1.setLayoutData(gd_filterGroup_1);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 9;
		filterGroup_1.setLayout(gridLayout);
		filterGroup_1.setText("Filter");

		final Label ortsstelleLabel_1_1 = new Label(filterGroup_1, SWT.NONE);
		ortsstelleLabel_1_1.setText("Ortsstelle:");

		final Combo comboOrtsstelleJournal = new Combo(filterGroup_1, SWT.NONE);
		final GridData gd_comboOrtsstelleJournal = new GridData();
		comboOrtsstelleJournal.setLayoutData(gd_comboOrtsstelleJournal);

		final Label datumLabel_1 = new Label(filterGroup_1, SWT.NONE);
		datumLabel_1.setText("Datum:");

		final Label sucheLabel = new Label(filterGroup_1, SWT.NONE);
		sucheLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		sucheLabel.setText("Suche:");

		final Label nachSpalteLabel = new Label(filterGroup_1, SWT.NONE);
		nachSpalteLabel.setText("nach Spalte:");

		final Combo comboNachSpalteJournal = new Combo(filterGroup_1, SWT.NONE);
		final GridData gd_comboNachSpalteJournal = new GridData(115, SWT.DEFAULT);
		comboNachSpalteJournal.setLayoutData(gd_comboNachSpalteJournal);

		final Label begriffLabel = new Label(filterGroup_1, SWT.NONE);
		begriffLabel.setText("Begriff:");

		final Text textBegriffJournal = new Text(filterGroup_1, SWT.BORDER);
		final GridData gd_textBegriffJournal = new GridData(189, SWT.DEFAULT);
		textBegriffJournal.setLayoutData(gd_textBegriffJournal);

		final Group group = new Group(composite, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setText("Durchgeführte Transporte");

		final Table table_5 = new Table(group, SWT.BORDER);
		table_5.setForeground(SWTResourceManager.getColor(0, 0, 0));
		table_5.setLinesVisible(true);
		table_5.setHeaderVisible(true);

		final TableColumn newColumnTableColumnPrioritaetJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnPrioritaetJournal.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		newColumnTableColumnPrioritaetJournal.setWidth(29);
		newColumnTableColumnPrioritaetJournal.setText("Pr");

		final TableItem newItemTableItem_15 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_15.setText("New item");

		final TableColumn newColumnTableColumnTNrJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTNrJournal.setMoveable(true);
		newColumnTableColumnTNrJournal.setWidth(36);
		newColumnTableColumnTNrJournal.setText("TNr");

		final TableColumn newColumnTableColumnOSJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnOSJournal.setWidth(40);
		newColumnTableColumnOSJournal.setText("OS");

		final TableColumn newColumnTableColumnDatumJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnDatumJournal.setWidth(18);
		newColumnTableColumnDatumJournal.setText("Datum");

		final TableColumn newColumnTableColumnAbfJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAbfJournal.setMoveable(true);
		newColumnTableColumnAbfJournal.setWidth(43);
		newColumnTableColumnAbfJournal.setText("Abf");

		final TableColumn newColumnTableColumnAnkJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAnkJournal.setMoveable(true);
		newColumnTableColumnAnkJournal.setWidth(46);
		newColumnTableColumnAnkJournal.setText("Ank");

		final TableColumn newColumnTableColumnTerminJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTerminJournal.setMoveable(true);
		newColumnTableColumnTerminJournal.setWidth(44);
		newColumnTableColumnTerminJournal.setText("Termin");

		final TableColumn newColumnTableColumnTransportVonJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTransportVonJournal.setMoveable(true);
		newColumnTableColumnTransportVonJournal.setWidth(39);
		newColumnTableColumnTransportVonJournal.setText("Transport von");

		final TableColumn newColumnTableColumnPatient = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnPatient.setMoveable(true);
		newColumnTableColumnPatient.setWidth(38);
		newColumnTableColumnPatient.setText("Patient");

		final TableColumn newColumnTableColumnTransportNachJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTransportNachJournal.setWidth(30);
		newColumnTableColumnTransportNachJournal.setText("Transport nach");

		final TableColumn newColumnTableColumnErkrVerlJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnErkrVerlJournal.setWidth(23);
		newColumnTableColumnErkrVerlJournal.setText("Erkr/Verl");

		final TableColumn newColumnTableColumnAnmerkungenJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAnmerkungenJournal.setWidth(24);
		newColumnTableColumnAnmerkungenJournal.setText("Anmerkungen");

		final TableColumn newColumnTableColumnAlarmiuerungJouranl = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAlarmiuerungJouranl.setWidth(36);
		newColumnTableColumnAlarmiuerungJouranl.setText("Alarmierung");

		final TableColumn newColumnTableColumnAufgJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAufgJournal.setWidth(27);
		newColumnTableColumnAufgJournal.setText("Aufg");

		final TableColumn newColumnTableColumnAEJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnAEJournal.setToolTipText("Auftrag erteilt");
		newColumnTableColumnAEJournal.setWidth(39);
		newColumnTableColumnAEJournal.setText("AE");

		final TableColumn newColumnTableColumnS1Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS1Journal.setToolTipText("Transportbeginn");
		newColumnTableColumnS1Journal.setWidth(36);
		newColumnTableColumnS1Journal.setText("S1");

		final TableColumn newColumnTableColumnS2Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS2Journal.setToolTipText("Bei Patient");
		newColumnTableColumnS2Journal.setWidth(38);
		newColumnTableColumnS2Journal.setText("S2");

		final TableColumn newColumnTableColumnS3Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS3Journal.setToolTipText("Abfahrt mit Patient");
		newColumnTableColumnS3Journal.setWidth(40);
		newColumnTableColumnS3Journal.setText("S3");

		final TableColumn newColumnTableColumnS4Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS4Journal.setToolTipText("Ankunft Zielort");
		newColumnTableColumnS4Journal.setWidth(36);
		newColumnTableColumnS4Journal.setText("S4");

		final TableColumn newColumnTableColumnS5Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS5Journal.setToolTipText("Ziel frei");
		newColumnTableColumnS5Journal.setWidth(46);
		newColumnTableColumnS5Journal.setText("S5");

		final TableColumn newColumnTableColumnS6Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS6Journal.setToolTipText("Fahrzeug eingerückt");
		newColumnTableColumnS6Journal.setWidth(36);
		newColumnTableColumnS6Journal.setText("S6");

		final TableColumn newColumnTableColumnS7Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS7Journal.setToolTipText("Einsatzgebiet verlassen");
		newColumnTableColumnS7Journal.setWidth(40);
		newColumnTableColumnS7Journal.setText("S7");

		final TableColumn newColumnTableColumnS8Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS8Journal.setToolTipText("Zurück im Einsatzgebiet");
		newColumnTableColumnS8Journal.setWidth(34);
		newColumnTableColumnS8Journal.setText("S8");

		final TableColumn newColumnTableColumnS9Journal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnS9Journal.setToolTipText("Sonderstatus");
		newColumnTableColumnS9Journal.setWidth(34);
		newColumnTableColumnS9Journal.setText("S9");

		final TableColumn newColumnTableColumnFzgJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnFzgJournal.setWidth(52);
		newColumnTableColumnFzgJournal.setText("Fzg");

		final TableColumn newColumnTableColumnFahrerJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnFahrerJournal.setWidth(19);
		newColumnTableColumnFahrerJournal.setText("Fahrer");

		final TableColumn newColumnTableColumnSaniIJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnSaniIJournal.setWidth(24);
		newColumnTableColumnSaniIJournal.setText("Sanitäter I");

		final TableColumn newColumnTableColumnSaniIIJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnSaniIIJournal.setWidth(35);
		newColumnTableColumnSaniIIJournal.setText("Sanitäter II");

		final TableColumn newColumnTableColumnRueckmeldungJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnRueckmeldungJournal.setWidth(36);
		newColumnTableColumnRueckmeldungJournal.setText("Rückmeldung");

		final TableColumn newColumnTableColumnTJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTJournal.setToolTipText("Transportart");
		newColumnTableColumnTJournal.setWidth(27);
		newColumnTableColumnTJournal.setText("T");

		final TableColumn newColumnTableColumnanruferJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnanruferJournal.setWidth(23);
		newColumnTableColumnanruferJournal.setText("Anrufer");

		final TableColumn newColumnTableColumnTelAnruferJournal = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumnTelAnruferJournal.setWidth(29);
		newColumnTableColumnTelAnruferJournal.setText("Telefon Anrufer");

		final TableItem newItemTableItem_1_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_1_1.setText("New item");

		final TableItem newItemTableItem_2_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_2_1.setText("New item");

		final TableItem newItemTableItem_3_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_3_1.setText("New item");

		final TableItem newItemTableItem_4_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_4_1.setText("New item");

		final TableItem newItemTableItem_5_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_5_1.setText("New item");

		final TableItem newItemTableItem_6_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_6_1.setText("New item");

		final TableItem newItemTableItem_7_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_7_1.setText("New item");

		final TableItem newItemTableItem_8_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_8_1.setData("newKey", "the new key");
		newItemTableItem_8_1.setBackground(SWTResourceManager.getColor(255, 255, 128));
		newItemTableItem_8_1.setText("New item");

		final TableItem newItemTableItem_9_1 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_9_1.setGrayed(true);
		newItemTableItem_9_1.setText("New item");

		final TabItem dialyseTabItem = new TabItem(tabFolder, SWT.NONE);
		dialyseTabItem.setText("Dialyse");

		final Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		composite_2.setLayout(new FillLayout());
		dialyseTabItem.setControl(composite_2);

		final Group dialysepatientenGroup = new Group(composite_2, SWT.NONE);
		dialysepatientenGroup.setText("Dialysepatienten");
		dialysepatientenGroup.setLayout(new FillLayout());

		final Table table_2 = new Table(dialysepatientenGroup, SWT.BORDER);
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);

		final TableColumn newColumnTableColumnIdDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnIdDialyse.setWidth(34);
		newColumnTableColumnIdDialyse.setText("Id");

		final TableColumn newColumnTableColumnAbfDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbfDialyse.setToolTipText("Geplante Abfahrt an Ortsstelle");
		newColumnTableColumnAbfDialyse.setWidth(68);
		newColumnTableColumnAbfDialyse.setText("Abf");

		final TableColumn newColumnTableColumnAnkDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAnkDialyse.setToolTipText("Geplante Ankunft beim Patienten");
		newColumnTableColumnAnkDialyse.setWidth(65);
		newColumnTableColumnAnkDialyse.setText("Ank");

		final TableColumn newColumnTableColumnTerminDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnTerminDialyse.setToolTipText("Termin auf der Dialyse");
		newColumnTableColumnTerminDialyse.setWidth(68);
		newColumnTableColumnTerminDialyse.setText("Termin");

		final TableColumn newColumnTableColumnRTAbfahrtDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnRTAbfahrtDialyse.setToolTipText("Abfahrt an der Ortsstelle");
		newColumnTableColumnRTAbfahrtDialyse.setWidth(84);
		newColumnTableColumnRTAbfahrtDialyse.setText("RT Abfahrt");

		final TableColumn newColumnTableColumnAbholbereitDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAbholbereitDialyse.setToolTipText("Patient ist mit Dialyse fertig, abholbereit im LKH");
		newColumnTableColumnAbholbereitDialyse.setWidth(100);
		newColumnTableColumnAbholbereitDialyse.setText("Abholbereit");

		final TableColumn newColumnTableColumnWohnortDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnWohnortDialyse.setWidth(167);
		newColumnTableColumnWohnortDialyse.setText("Wohnort");

		final TableColumn newColumnTableColumnNameDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnNameDialyse.setWidth(133);
		newColumnTableColumnNameDialyse.setText("Name");

		final TableColumn newColumnTableColumnMontag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMontag.setData("newKey", null);
		newColumnTableColumnMontag.setWidth(40);
		newColumnTableColumnMontag.setText("Mo");

		final TableColumn newColumnTableColumnDienstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDienstag.setWidth(37);
		newColumnTableColumnDienstag.setText("Di");

		final TableColumn newColumnTableColumnMittwoch = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnMittwoch.setWidth(36);
		newColumnTableColumnMittwoch.setText("Mi");

		final TableColumn newColumnTableColumnDonnerstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnDonnerstag.setWidth(35);
		newColumnTableColumnDonnerstag.setText("Do");

		final TableColumn newColumnTableColumnFreitag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnFreitag.setWidth(38);
		newColumnTableColumnFreitag.setText("Fr");

		final TableColumn newColumnTableColumnSamstag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSamstag.setWidth(42);
		newColumnTableColumnSamstag.setText("Sa");

		final TableColumn newColumnTableColumnSonntag = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnSonntag.setWidth(43);
		newColumnTableColumnSonntag.setText("So");

		final TableColumn newColumnTableColumnStationaer = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnStationaer.setToolTipText("Patient wird derzeit nicht transportiert");
		newColumnTableColumnStationaer.setWidth(49);
		newColumnTableColumnStationaer.setText("Stat");

		final TableItem newItemTableItem_10 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_10.setText("New item");

		final TableColumn newColumnTableColumnAnmerkungenDialyse = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumnAnmerkungenDialyse.setToolTipText("Informationen zum Patienten und seinem Transportstatus");
		newColumnTableColumnAnmerkungenDialyse.setWidth(100);
		newColumnTableColumnAnmerkungenDialyse.setText("Anmerkungen");

		final TableItem newItemTableItem_11 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_11.setText("New item");

		final TableItem newItemTableItem_12 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_12.setText("New item");

		final TableItem newItemTableItem_13 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_13.setText("New item");

		final TableItem newItemTableItem_14 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_14.setText("New item");

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Rücktransporte");

		final Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		composite_4.setLayout(new FillLayout());
		tabItem.setControl(composite_4);

		final Group group_1 = new Group(composite_4, SWT.NONE);
		group_1.setText("Mögliche Rücktransporte");
		group_1.setLayout(new FillLayout());

		final Table table_3 = new Table(group_1, SWT.BORDER);
		table_3.setLinesVisible(true);
		table_3.setHeaderVisible(true);

		final TableColumn newColumnTableColumnOSRuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumnOSRuecktransporte.setWidth(44);
		newColumnTableColumnOSRuecktransporte.setText("OS");

		final TableColumn newColumnTableColumnFzgRuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumnFzgRuecktransporte.setWidth(100);
		newColumnTableColumnFzgRuecktransporte.setText("Fzg");

		final TableColumn newColumnTableColumTransportVonRuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumTransportVonRuecktransporte.setWidth(160);
		newColumnTableColumTransportVonRuecktransporte.setText("Transport von");

		final TableColumn newColumnTableColumnPatientRuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumnPatientRuecktransporte.setWidth(200);
		newColumnTableColumnPatientRuecktransporte.setText("Patient");

		final TableColumn newColumnTableColumnTransportNachRuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumnTransportNachRuecktransporte.setWidth(195);
		newColumnTableColumnTransportNachRuecktransporte.setText("Transport nach");

		final TableColumn newColumnTableColumnTARuecktransporte = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumnTARuecktransporte.setWidth(52);
		newColumnTableColumnTARuecktransporte.setText("TA");

		final TabItem zusatzfunktionenTabItem = new TabItem(tabFolder, SWT.NONE);
		zusatzfunktionenTabItem.setText("Zusatzfunktionen");

		final Composite composite_5 = new Composite(tabFolder, SWT.NONE);
		composite_5.setLayout(new FillLayout(SWT.VERTICAL));
		zusatzfunktionenTabItem.setControl(composite_5);

		final Group group_3 = new Group(composite_5, SWT.NONE);
		group_3.setText("Links");
		group_3.setLayout(new FillLayout());

		final Group group_2 = new Group(composite_5, SWT.NONE);
		group_2.setText("Druckfunktionen");
		group_2.setLayout(new FillLayout());
		
		
		//
	}

}

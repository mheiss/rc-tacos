package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.*;


public class TransportsView {

	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TransportsView window = new TransportsView();
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
		shell.setMaximized(true);
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
		shell.setImage(SWTResourceManager.getImage(TransportsView.class, "image/Tacos_Login.bmp"));
		shell.setLayout(new GridLayout());
		shell.setSize(1013, 571);
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
		table.setColumnOrder(null);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn newColumnTableColumn = new TableColumn(table, SWT.NONE);
		newColumnTableColumn.setWidth(29);
		newColumnTableColumn.setText("Pr");

		final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
		newItemTableItem.setText(2, "13:30");
		newItemTableItem.setText(1, "12388");
		newItemTableItem.setText(0, "1");
		newItemTableItem.setText("New item");

		final TableColumn newColumnTableColumn_1 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_1.setMoveable(true);
		newColumnTableColumn_1.setWidth(69);
		newColumnTableColumn_1.setText("TNr.");

		final TableColumn newColumnTableColumn_2 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_2.setMoveable(true);
		newColumnTableColumn_2.setWidth(60);
		newColumnTableColumn_2.setText("Abfahrt");

		final TableColumn newColumnTableColumn_3 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_3.setMoveable(true);
		newColumnTableColumn_3.setWidth(67);
		newColumnTableColumn_3.setText("Ankunft");

		final TableColumn newColumnTableColumn_4 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_4.setMoveable(true);
		newColumnTableColumn_4.setWidth(55);
		newColumnTableColumn_4.setText("Termin");

		final TableColumn newColumnTableColumn_5 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_5.setMoveable(true);
		newColumnTableColumn_5.setWidth(100);
		newColumnTableColumn_5.setText("Transport von");

		final TableColumn newColumnTableColumn_6 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_6.setMoveable(true);
		newColumnTableColumn_6.setWidth(100);
		newColumnTableColumn_6.setText("Patient");

		final TableColumn newColumnTableColumn_7 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_7.setWidth(100);
		newColumnTableColumn_7.setText("Transport nach");

		final TableColumn newColumnTableColumn_8 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_8.setWidth(39);
		newColumnTableColumn_8.setText("AE");

		final TableColumn newColumnTableColumn_9 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_9.setWidth(36);
		newColumnTableColumn_9.setText("S1");

		final TableColumn newColumnTableColumn_10 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_10.setWidth(38);
		newColumnTableColumn_10.setText("S2");

		final TableColumn newColumnTableColumn_11 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_11.setWidth(40);
		newColumnTableColumn_11.setText("S3");

		final TableColumn newColumnTableColumn_12 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_12.setWidth(36);
		newColumnTableColumn_12.setText("S4");

		final TableColumn newColumnTableColumn_13 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_13.setWidth(40);
		newColumnTableColumn_13.setText("S7");

		final TableColumn newColumnTableColumn_14 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_14.setWidth(34);
		newColumnTableColumn_14.setText("S8");

		final TableColumn newColumnTableColumn_15 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_15.setWidth(34);
		newColumnTableColumn_15.setText("S9");

		final TableColumn newColumnTableColumn_16 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_16.setWidth(52);
		newColumnTableColumn_16.setText("Fzg.");

		final TableColumn newColumnTableColumn_17 = new TableColumn(table, SWT.NONE);
		newColumnTableColumn_17.setWidth(53);
		newColumnTableColumn_17.setText("TA");
		
		
		//TODO
		//Für Tabelle "disponierte Transporte"
		disponierteTransporteGroup.addControlListener(new ControlAdapter() 
		{
			public void controlResized(final ControlEvent e) 
			{
				System.out.println("----------- im Controlllistener der table, im controlResized-------------");
				Rectangle area = disponierteTransporteGroup.getClientArea();
				Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				ScrollBar vBar = table.getVerticalBar();
				int width = area.width - table.computeTrim(0,0,0,0).width - vBar.getSize().x;
				if (size.y > area.height + table.getHeaderHeight()) 
				{
					// Subtract the scrollbar width from the total column width
					// if a vertical scrollbar will be required
					Point vBarSize = vBar.getSize();
					width -= vBarSize.x;
				}
				
				
				Point oldSize = table.getSize();
				if (oldSize.x > area.width) 
				{
					// table is getting smaller so make the columns 
					// smaller first and then resize the table to
					// match the client area width
					newColumnTableColumn.setWidth(width/3);//Spalte "Pr"
					newColumnTableColumn_1.setWidth(width - newColumnTableColumn.getWidth());
				
					
					table.setSize(area.width, area.height);
				} else 
				{
					// table is getting bigger so make the table 
					// bigger first and then make the columns wider
					// to match the client area width
					table.setSize(area.width, area.height);
					newColumnTableColumn.setWidth(width/3);
					newColumnTableColumn_1.setWidth(width - newColumnTableColumn.getWidth());
					newColumnTableColumn_2.setWidth(width - newColumnTableColumn_1.getWidth());
	
					
				}
			}
		});
		
		
		
		
		

		final TableItem newItemTableItem_1 = new TableItem(table, SWT.BORDER);
		newItemTableItem_1.setText(5, "Narzissenweg 5 /Oberaich");
		newItemTableItem_1.setText(3, "06:15");
		newItemTableItem_1.setText(4, "07:30");
		newItemTableItem_1.setText(0, "2");
		newItemTableItem_1.setText(2, "06:00");
		newItemTableItem_1.setText(1, "14599");
		newItemTableItem_1.setText("New item");

		final TableItem newItemTableItem_2 = new TableItem(table, SWT.BORDER);
		newItemTableItem_2.setText(0, "3");
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

		final TableItem newItemTableItem_8 = new TableItem(table, SWT.BORDER);
		newItemTableItem_8.setBackground(0, SWTResourceManager.getColor(255, 128, 255));
		newItemTableItem_8.setData("newKey", null);
		newItemTableItem_8.setData("newKey", "the new key");
		newItemTableItem_8.setBackground(SWTResourceManager.getColor(255, 255, 128));
		newItemTableItem_8.setImage(SWTResourceManager.getImage(TransportsView.class, "image/LAN Warning.ico"));
		newItemTableItem_8.setText("New item");

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

		final TableColumn newColumnTableColumn_19 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_19.setWidth(29);
		newColumnTableColumn_19.setText("Id");

		final TableColumn newColumnTableColumn_20 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_20.setWidth(31);
		newColumnTableColumn_20.setText("Pr");

		final TableColumn newColumnTableColumn_21 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_21.setWidth(49);
		newColumnTableColumn_21.setText("DS");

		final TableColumn newColumnTableColumn_22 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_22.setWidth(54);
		newColumnTableColumn_22.setText("Abf");

		final TableColumn newColumnTableColumn_23 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_23.setWidth(53);
		newColumnTableColumn_23.setText("Ank.");

		final TableColumn newColumnTableColumn_24 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_24.setWidth(57);
		newColumnTableColumn_24.setText("Termin");

		final TableColumn newColumnTableColumn_25 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_25.setWidth(102);
		newColumnTableColumn_25.setText("Transport von");

		final TableColumn newColumnTableColumn_26 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_26.setWidth(100);
		newColumnTableColumn_26.setText("Patient");

		final TableColumn newColumnTableColumn_27 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_27.setWidth(100);
		newColumnTableColumn_27.setText("Transport nach");

		final TableColumn newColumnTableColumn_28 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_28.setWidth(100);
		newColumnTableColumn_28.setText("Aufg.");

		final TableColumn newColumnTableColumn_29 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_29.setWidth(100);
		newColumnTableColumn_29.setText("TA");

		final TableColumn newColumnTableColumn_30 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_30.setWidth(74);
		newColumnTableColumn_30.setText("Erkrankung/Verletzung");

		final TableColumn newColumnTableColumn_31 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_31.setWidth(71);
		newColumnTableColumn_31.setText("Anmerkung");

		final TableColumn newColumnTableColumn_32 = new TableColumn(table_1, SWT.NONE);
		newColumnTableColumn_32.setMoveable(true);
		newColumnTableColumn_32.setWidth(58);
		newColumnTableColumn_32.setText("BP");

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

		final Group filterGroup = new Group(composite_1, SWT.NONE);
		filterGroup.setText("Filter");
		final GridData gd_filterGroup = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_filterGroup.heightHint = 30;
		gd_filterGroup.widthHint = 993;
		filterGroup.setLayoutData(gd_filterGroup);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 4;
		filterGroup.setLayout(gridLayout_3);

		final Label ortsstelleLabel_1 = new Label(filterGroup, SWT.NONE);
		ortsstelleLabel_1.setText("Ortsstelle:");

		final Combo combo_1 = new Combo(filterGroup, SWT.NONE);
		combo_1.setLayoutData(new GridData());

		final Label datumLabel = new Label(filterGroup, SWT.NONE);
		datumLabel.setText("Datum:");

		new DateTime(filterGroup, SWT.NONE);

		final Composite composite_3 = new Composite(composite_1, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.VERTICAL));
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

		final TableColumn tableColumn = new TableColumn(table_4, SWT.NONE);
		tableColumn.setWidth(32);
		tableColumn.setText("Id");

		final TableColumn tableColumn_1 = new TableColumn(table_4, SWT.NONE);
		tableColumn_1.setWidth(31);
		tableColumn_1.setText("Pr");

		final TableColumn tableColumn_2 = new TableColumn(table_4, SWT.NONE);
		tableColumn_2.setWidth(39);
		tableColumn_2.setText("OS");

		final TableColumn tableColumn_3 = new TableColumn(table_4, SWT.NONE);
		tableColumn_3.setWidth(41);
		tableColumn_3.setText("Abf");

		final TableColumn tableColumn_11 = new TableColumn(table_4, SWT.NONE);
		tableColumn_11.setWidth(45);
		tableColumn_11.setText("Ank");

		final TableColumn tableColumn_10 = new TableColumn(table_4, SWT.NONE);
		tableColumn_10.setWidth(49);
		tableColumn_10.setText("Termin");

		final TableColumn tableColumn_9 = new TableColumn(table_4, SWT.NONE);
		tableColumn_9.setWidth(100);
		tableColumn_9.setText("Transport von");

		final TableColumn tableColumn_8 = new TableColumn(table_4, SWT.NONE);
		tableColumn_8.setWidth(100);
		tableColumn_8.setText("Patient");

		final TableColumn tableColumn_7 = new TableColumn(table_4, SWT.NONE);
		tableColumn_7.setWidth(100);
		tableColumn_7.setText("Transport nach");

		final TableColumn tableColumn_6 = new TableColumn(table_4, SWT.NONE);
		tableColumn_6.setWidth(33);
		tableColumn_6.setText("T");

		final TableColumn tableColumn_5 = new TableColumn(table_4, SWT.NONE);
		tableColumn_5.setWidth(126);
		tableColumn_5.setText("Erkr/Verl");

		final TableColumn tableColumn_4 = new TableColumn(table_4, SWT.NONE);
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("Anmerkung");

		final TableColumn tableColumn_12 = new TableColumn(table_4, SWT.NONE);
		tableColumn_12.setWidth(33);
		tableColumn_12.setText("BP");

		final TableColumn tableColumn_13 = new TableColumn(table_4, SWT.NONE);
		tableColumn_13.setWidth(146);
		tableColumn_13.setText("FF");

		final TableItem tableItem_5 = new TableItem(table_4, SWT.BORDER);
		tableItem_5.setText("New item");

		final TableItem tableItem_6 = new TableItem(table_4, SWT.BORDER);
		tableItem_6.setText("New item");

		final TableItem tableItem_9 = new TableItem(table_4, SWT.BORDER);
		tableItem_9.setText("New item");

		final TableItem tableItem_8 = new TableItem(table_4, SWT.BORDER);
		tableItem_8.setText("New item");

		final TableItem tableItem_7 = new TableItem(table_4, SWT.BORDER);
		tableItem_7.setText("New item");

		final Group group_9 = new Group(sashForm_8, SWT.NONE);
		group_9.setLayout(new FillLayout());
		group_9.setText("Richtung Mürzzuschlag");

		final Table table_4_1 = new Table(group_9, SWT.BORDER);
		table_4_1.setLinesVisible(true);
		table_4_1.setHeaderVisible(true);

		final TableColumn idTableColumn = new TableColumn(table_4_1, SWT.NONE);
		idTableColumn.setWidth(32);
		idTableColumn.setText("Id");

		final TableColumn prTableColumn = new TableColumn(table_4_1, SWT.NONE);
		prTableColumn.setWidth(31);
		prTableColumn.setText("Pr");

		final TableColumn osTableColumn = new TableColumn(table_4_1, SWT.NONE);
		osTableColumn.setWidth(39);
		osTableColumn.setText("OS");

		final TableColumn abfTableColumn = new TableColumn(table_4_1, SWT.NONE);
		abfTableColumn.setWidth(41);
		abfTableColumn.setText("Abf");

		final TableColumn ankTableColumn = new TableColumn(table_4_1, SWT.NONE);
		ankTableColumn.setWidth(45);
		ankTableColumn.setText("Ank");

		final TableColumn terminTableColumn = new TableColumn(table_4_1, SWT.NONE);
		terminTableColumn.setWidth(49);
		terminTableColumn.setText("Termin");

		final TableColumn transportVonTableColumn = new TableColumn(table_4_1, SWT.NONE);
		transportVonTableColumn.setWidth(100);
		transportVonTableColumn.setText("Transport von");

		final TableColumn patientTableColumn = new TableColumn(table_4_1, SWT.NONE);
		patientTableColumn.setWidth(100);
		patientTableColumn.setText("Patient");

		final TableColumn transportNachTableColumn = new TableColumn(table_4_1, SWT.NONE);
		transportNachTableColumn.setWidth(100);
		transportNachTableColumn.setText("Transport nach");

		final TableColumn tableColumn_6_1 = new TableColumn(table_4_1, SWT.NONE);
		tableColumn_6_1.setWidth(33);
		tableColumn_6_1.setText("T");

		final TableColumn erkrverlTableColumn = new TableColumn(table_4_1, SWT.NONE);
		erkrverlTableColumn.setWidth(126);
		erkrverlTableColumn.setText("Erkr/Verl");

		final TableColumn anmerkungTableColumn = new TableColumn(table_4_1, SWT.NONE);
		anmerkungTableColumn.setWidth(100);
		anmerkungTableColumn.setText("Anmerkung");

		final TableColumn bpTableColumn = new TableColumn(table_4_1, SWT.NONE);
		bpTableColumn.setWidth(33);
		bpTableColumn.setText("BP");

		final TableColumn ffTableColumn = new TableColumn(table_4_1, SWT.NONE);
		ffTableColumn.setWidth(146);
		ffTableColumn.setText("FF");

		final TableItem tableItem_5_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_5_1.setText("New item");

		final TableItem tableItem_6_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_6_1.setText("New item");

		final TableItem tableItem_9_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_9_1.setText("New item");

		final TableItem tableItem_8_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_8_1.setText("New item");

		final TableItem tableItem_7_1 = new TableItem(table_4_1, SWT.BORDER);
		tableItem_7_1.setText("New item");
		sashForm_8.setWeights(new int[] {92, 55 });

		final SashForm sashForm_7 = new SashForm(composite_3, SWT.VERTICAL);

		final Group richtungKapfenbergGroup_1 = new Group(sashForm_7, SWT.NONE);
		richtungKapfenbergGroup_1.setLayout(new FillLayout());
		richtungKapfenbergGroup_1.setText("Richtung Kapfenberg");

		final Table table_4_2 = new Table(richtungKapfenbergGroup_1, SWT.BORDER);
		table_4_2.setLinesVisible(true);
		table_4_2.setHeaderVisible(true);

		final TableColumn idTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		idTableColumn_1.setWidth(32);
		idTableColumn_1.setText("Id");

		final TableColumn prTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		prTableColumn_1.setWidth(31);
		prTableColumn_1.setText("Pr");

		final TableColumn osTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		osTableColumn_1.setWidth(39);
		osTableColumn_1.setText("OS");

		final TableColumn abfTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		abfTableColumn_1.setWidth(41);
		abfTableColumn_1.setText("Abf");

		final TableColumn ankTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		ankTableColumn_1.setWidth(45);
		ankTableColumn_1.setText("Ank");

		final TableColumn terminTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		terminTableColumn_1.setWidth(49);
		terminTableColumn_1.setText("Termin");

		final TableColumn transportVonTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		transportVonTableColumn_1.setWidth(100);
		transportVonTableColumn_1.setText("Transport von");

		final TableColumn patientTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		patientTableColumn_1.setWidth(100);
		patientTableColumn_1.setText("Patient");

		final TableColumn transportNachTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		transportNachTableColumn_1.setWidth(100);
		transportNachTableColumn_1.setText("Transport nach");

		final TableColumn tableColumn_6_2 = new TableColumn(table_4_2, SWT.NONE);
		tableColumn_6_2.setWidth(33);
		tableColumn_6_2.setText("T");

		final TableColumn erkrverlTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		erkrverlTableColumn_1.setWidth(126);
		erkrverlTableColumn_1.setText("Erkr/Verl");

		final TableColumn anmerkungTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		anmerkungTableColumn_1.setWidth(100);
		anmerkungTableColumn_1.setText("Anmerkung");

		final TableColumn bpTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		bpTableColumn_1.setWidth(33);
		bpTableColumn_1.setText("BP");

		final TableColumn ffTableColumn_1 = new TableColumn(table_4_2, SWT.NONE);
		ffTableColumn_1.setWidth(146);
		ffTableColumn_1.setText("FF");

		final TableItem tableItem_5_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_5_2.setText("New item");

		final TableItem tableItem_6_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_6_2.setText("New item");

		final TableItem tableItem_9_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_9_2.setText("New item");

		final TableItem tableItem_8_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_8_2.setText("New item");

		final TableItem tableItem_7_2 = new TableItem(table_4_2, SWT.BORDER);
		tableItem_7_2.setText("New item");

		final SashForm sashForm_9 = new SashForm(composite_3, SWT.VERTICAL);

		final Group richtungGrazGroup_1 = new Group(sashForm_9, SWT.NONE);
		richtungGrazGroup_1.setLayout(new FillLayout());
		richtungGrazGroup_1.setText("Richtung Graz");

		final Table table_4_4 = new Table(richtungGrazGroup_1, SWT.BORDER);
		table_4_4.setLinesVisible(true);
		table_4_4.setHeaderVisible(true);

		final TableColumn idTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		idTableColumn_3.setWidth(32);
		idTableColumn_3.setText("Id");

		final TableColumn prTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		prTableColumn_3.setWidth(31);
		prTableColumn_3.setText("Pr");

		final TableColumn osTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		osTableColumn_3.setWidth(39);
		osTableColumn_3.setText("OS");

		final TableColumn abfTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		abfTableColumn_3.setWidth(41);
		abfTableColumn_3.setText("Abf");

		final TableColumn ankTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		ankTableColumn_3.setWidth(45);
		ankTableColumn_3.setText("Ank");

		final TableColumn terminTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		terminTableColumn_3.setWidth(49);
		terminTableColumn_3.setText("Termin");

		final TableColumn transportVonTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		transportVonTableColumn_3.setWidth(100);
		transportVonTableColumn_3.setText("Transport von");

		final TableColumn patientTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		patientTableColumn_3.setWidth(100);
		patientTableColumn_3.setText("Patient");

		final TableColumn transportNachTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		transportNachTableColumn_3.setWidth(100);
		transportNachTableColumn_3.setText("Transport nach");

		final TableColumn tableColumn_6_4 = new TableColumn(table_4_4, SWT.NONE);
		tableColumn_6_4.setWidth(33);
		tableColumn_6_4.setText("T");

		final TableColumn erkrverlTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		erkrverlTableColumn_3.setWidth(126);
		erkrverlTableColumn_3.setText("Erkr/Verl");

		final TableColumn anmerkungTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		anmerkungTableColumn_3.setWidth(100);
		anmerkungTableColumn_3.setText("Anmerkung");

		final TableColumn bpTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		bpTableColumn_3.setWidth(33);
		bpTableColumn_3.setText("BP");

		final TableColumn ffTableColumn_3 = new TableColumn(table_4_4, SWT.NONE);
		ffTableColumn_3.setWidth(146);
		ffTableColumn_3.setText("FF");

		final TableItem tableItem_5_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_5_4.setText("New item");

		final TableItem tableItem_6_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_6_4.setText("New item");

		final TableItem tableItem_9_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_9_4.setText("New item");

		final TableItem tableItem_8_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_8_4.setText("New item");

		final TableItem tableItem_7_4 = new TableItem(table_4_4, SWT.BORDER);
		tableItem_7_4.setText("New item");

		final Group richtungLeobenGroup_1 = new Group(sashForm_9, SWT.NONE);
		richtungLeobenGroup_1.setLayout(new FillLayout());
		richtungLeobenGroup_1.setText("Richtung Leoben");

		final Table table_4_5 = new Table(richtungLeobenGroup_1, SWT.BORDER);
		table_4_5.setLinesVisible(true);
		table_4_5.setHeaderVisible(true);

		final TableColumn idTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		idTableColumn_4.setWidth(32);
		idTableColumn_4.setText("Id");

		final TableColumn prTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		prTableColumn_4.setWidth(31);
		prTableColumn_4.setText("Pr");

		final TableColumn osTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		osTableColumn_4.setWidth(39);
		osTableColumn_4.setText("OS");

		final TableColumn abfTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		abfTableColumn_4.setWidth(41);
		abfTableColumn_4.setText("Abf");

		final TableColumn ankTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		ankTableColumn_4.setWidth(45);
		ankTableColumn_4.setText("Ank");

		final TableColumn terminTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		terminTableColumn_4.setWidth(49);
		terminTableColumn_4.setText("Termin");

		final TableColumn transportVonTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		transportVonTableColumn_4.setWidth(100);
		transportVonTableColumn_4.setText("Transport von");

		final TableColumn patientTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		patientTableColumn_4.setWidth(100);
		patientTableColumn_4.setText("Patient");

		final TableColumn transportNachTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		transportNachTableColumn_4.setWidth(100);
		transportNachTableColumn_4.setText("Transport nach");

		final TableColumn tableColumn_6_5 = new TableColumn(table_4_5, SWT.NONE);
		tableColumn_6_5.setWidth(33);
		tableColumn_6_5.setText("T");

		final TableColumn erkrverlTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		erkrverlTableColumn_4.setWidth(126);
		erkrverlTableColumn_4.setText("Erkr/Verl");

		final TableColumn anmerkungTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		anmerkungTableColumn_4.setWidth(100);
		anmerkungTableColumn_4.setText("Anmerkung");

		final TableColumn bpTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		bpTableColumn_4.setWidth(33);
		bpTableColumn_4.setText("BP");

		final TableColumn ffTableColumn_4 = new TableColumn(table_4_5, SWT.NONE);
		ffTableColumn_4.setWidth(146);
		ffTableColumn_4.setText("FF");

		final TableItem tableItem_5_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_5_5.setText("New item");

		final TableItem tableItem_6_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_6_5.setText("New item");

		final TableItem tableItem_9_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_9_5.setText("New item");

		final TableItem tableItem_8_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_8_5.setText("New item");

		final TableItem tableItem_7_5 = new TableItem(table_4_5, SWT.BORDER);
		tableItem_7_5.setText("New item");
		sashForm_9.setWeights(new int[] {1, 1 });

		final Group richtungAflenzGroup = new Group(sashForm_7, SWT.NONE);
		richtungAflenzGroup.setLayout(new FillLayout());
		richtungAflenzGroup.setText("Richtung Aflenz");

		final Table table_4_3 = new Table(richtungAflenzGroup, SWT.BORDER);
		table_4_3.setLinesVisible(true);
		table_4_3.setHeaderVisible(true);

		final TableColumn idTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		idTableColumn_2.setWidth(32);
		idTableColumn_2.setText("Id");

		final TableColumn prTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		prTableColumn_2.setWidth(31);
		prTableColumn_2.setText("Pr");

		final TableColumn osTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		osTableColumn_2.setWidth(39);
		osTableColumn_2.setText("OS");

		final TableColumn abfTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		abfTableColumn_2.setWidth(41);
		abfTableColumn_2.setText("Abf");

		final TableColumn ankTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		ankTableColumn_2.setWidth(45);
		ankTableColumn_2.setText("Ank");

		final TableColumn terminTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		terminTableColumn_2.setWidth(49);
		terminTableColumn_2.setText("Termin");

		final TableColumn transportVonTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		transportVonTableColumn_2.setWidth(100);
		transportVonTableColumn_2.setText("Transport von");

		final TableColumn patientTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		patientTableColumn_2.setWidth(100);
		patientTableColumn_2.setText("Patient");

		final TableColumn transportNachTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		transportNachTableColumn_2.setWidth(100);
		transportNachTableColumn_2.setText("Transport nach");

		final TableColumn tableColumn_6_3 = new TableColumn(table_4_3, SWT.NONE);
		tableColumn_6_3.setWidth(33);
		tableColumn_6_3.setText("T");

		final TableColumn erkrverlTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		erkrverlTableColumn_2.setWidth(126);
		erkrverlTableColumn_2.setText("Erkr/Verl");

		final TableColumn anmerkungTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		anmerkungTableColumn_2.setWidth(100);
		anmerkungTableColumn_2.setText("Anmerkung");

		final TableColumn bpTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		bpTableColumn_2.setWidth(33);
		bpTableColumn_2.setText("BP");

		final TableColumn ffTableColumn_2 = new TableColumn(table_4_3, SWT.NONE);
		ffTableColumn_2.setWidth(146);
		ffTableColumn_2.setText("FF");

		final TableItem tableItem_5_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_5_3.setText("New item");

		final TableItem tableItem_6_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_6_3.setText("New item");

		final TableItem tableItem_9_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_9_3.setText("New item");

		final TableItem tableItem_8_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_8_3.setText("New item");

		final TableItem tableItem_7_3 = new TableItem(table_4_3, SWT.BORDER);
		tableItem_7_3.setText("New item");
		sashForm_7.setWeights(new int[] {96, 51 });

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

		final Combo combo_1_1 = new Combo(filterGroup_1, SWT.NONE);
		final GridData gd_combo_1_1 = new GridData();
		combo_1_1.setLayoutData(gd_combo_1_1);

		final Label datumLabel_1 = new Label(filterGroup_1, SWT.NONE);
		datumLabel_1.setText("Datum:");

		new DateTime(filterGroup_1, SWT.NONE);

		final Label sucheLabel = new Label(filterGroup_1, SWT.NONE);
		sucheLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		sucheLabel.setText("Suche:");

		final Label nachSpalteLabel = new Label(filterGroup_1, SWT.NONE);
		nachSpalteLabel.setText("nach Spalte:");

		final Combo combo = new Combo(filterGroup_1, SWT.NONE);
		combo.setLayoutData(new GridData(115, SWT.DEFAULT));

		final Label begriffLabel = new Label(filterGroup_1, SWT.NONE);
		begriffLabel.setText("Begriff:");

		final Text text = new Text(filterGroup_1, SWT.BORDER);
		text.setLayoutData(new GridData(189, SWT.DEFAULT));

		final Group group = new Group(composite, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setText("Durchgeführte Transporte");

		final Table table_5 = new Table(group, SWT.BORDER);
		table_5.setForeground(SWTResourceManager.getColor(0, 0, 0));
		table_5.setLinesVisible(true);
		table_5.setHeaderVisible(true);

		final TableColumn newColumnTableColumn_52 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_52.setWidth(29);
		newColumnTableColumn_52.setText("Pr");

		final TableItem newItemTableItem_15 = new TableItem(table_5, SWT.BORDER);
		newItemTableItem_15.setText("New item");

		final TableColumn newColumnTableColumn_1_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_1_1.setMoveable(true);
		newColumnTableColumn_1_1.setWidth(36);
		newColumnTableColumn_1_1.setText("TNr.");

		final TableColumn newColumnTableColumn_53 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_53.setWidth(40);
		newColumnTableColumn_53.setText("OS");

		final TableColumn newColumnTableColumn_57 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_57.setWidth(18);
		newColumnTableColumn_57.setText("Datum");

		final TableColumn newColumnTableColumn_2_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_2_1.setMoveable(true);
		newColumnTableColumn_2_1.setWidth(43);
		newColumnTableColumn_2_1.setText("Abfahrt");

		final TableColumn newColumnTableColumn_3_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_3_1.setMoveable(true);
		newColumnTableColumn_3_1.setWidth(46);
		newColumnTableColumn_3_1.setText("Ankunft");

		final TableColumn newColumnTableColumn_4_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_4_1.setMoveable(true);
		newColumnTableColumn_4_1.setWidth(44);
		newColumnTableColumn_4_1.setText("Termin");

		final TableColumn newColumnTableColumn_5_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_5_1.setMoveable(true);
		newColumnTableColumn_5_1.setWidth(39);
		newColumnTableColumn_5_1.setText("Transport von");

		final TableColumn newColumnTableColumn_6_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_6_1.setMoveable(true);
		newColumnTableColumn_6_1.setWidth(38);
		newColumnTableColumn_6_1.setText("Patient");

		final TableColumn newColumnTableColumn_7_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_7_1.setWidth(30);
		newColumnTableColumn_7_1.setText("Transport nach");

		final TableColumn newColumnTableColumn_60 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_60.setWidth(23);
		newColumnTableColumn_60.setText("Erkr.Verl");

		final TableColumn newColumnTableColumn_61 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_61.setWidth(24);
		newColumnTableColumn_61.setText("Anmerkungen");

		final TableColumn newColumnTableColumn_63 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_63.setWidth(36);
		newColumnTableColumn_63.setText("Alarmierung");

		final TableColumn newColumnTableColumn_67 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_67.setWidth(27);
		newColumnTableColumn_67.setText("Aufg");

		final TableColumn newColumnTableColumn_8_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_8_1.setWidth(39);
		newColumnTableColumn_8_1.setText("AE");

		final TableColumn newColumnTableColumn_9_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_9_1.setWidth(36);
		newColumnTableColumn_9_1.setText("S1");

		final TableColumn newColumnTableColumn_10_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_10_1.setWidth(38);
		newColumnTableColumn_10_1.setText("S2");

		final TableColumn newColumnTableColumn_11_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_11_1.setWidth(40);
		newColumnTableColumn_11_1.setText("S3");

		final TableColumn newColumnTableColumn_12_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_12_1.setWidth(36);
		newColumnTableColumn_12_1.setText("S4");

		final TableColumn newColumnTableColumn_54 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_54.setWidth(46);
		newColumnTableColumn_54.setText("S5");

		final TableColumn newColumnTableColumn_55 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_55.setWidth(36);
		newColumnTableColumn_55.setText("S6");

		final TableColumn newColumnTableColumn_13_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_13_1.setWidth(40);
		newColumnTableColumn_13_1.setText("S7");

		final TableColumn newColumnTableColumn_14_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_14_1.setWidth(34);
		newColumnTableColumn_14_1.setText("S8");

		final TableColumn newColumnTableColumn_15_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_15_1.setWidth(34);
		newColumnTableColumn_15_1.setText("S9");

		final TableColumn newColumnTableColumn_16_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_16_1.setWidth(52);
		newColumnTableColumn_16_1.setText("Fzg.");

		final TableColumn newColumnTableColumn_64 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_64.setWidth(19);
		newColumnTableColumn_64.setText("Fahrer");

		final TableColumn newColumnTableColumn_65 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_65.setWidth(24);
		newColumnTableColumn_65.setText("Sanitäter I");

		final TableColumn newColumnTableColumn_66 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_66.setWidth(35);
		newColumnTableColumn_66.setText("Sanitäter II");

		final TableColumn newColumnTableColumn_62 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_62.setWidth(36);
		newColumnTableColumn_62.setText("Rückmeldung");

		final TableColumn newColumnTableColumn_17_1 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_17_1.setWidth(27);
		newColumnTableColumn_17_1.setText("TA");

		final TableColumn newColumnTableColumn_56 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_56.setWidth(20);
		newColumnTableColumn_56.setText("BP");

		final TableColumn newColumnTableColumn_58 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_58.setWidth(23);
		newColumnTableColumn_58.setText("Anrufer");

		final TableColumn newColumnTableColumn_59 = new TableColumn(table_5, SWT.NONE);
		newColumnTableColumn_59.setWidth(29);
		newColumnTableColumn_59.setText("Tel. Anrufer");

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

		final TableColumn newColumnTableColumn_18 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_18.setWidth(34);
		newColumnTableColumn_18.setText("Id");

		final TableColumn newColumnTableColumn_33 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_33.setWidth(68);
		newColumnTableColumn_33.setText("Abf");

		final TableColumn newColumnTableColumn_34 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_34.setWidth(65);
		newColumnTableColumn_34.setText("Ank");

		final TableColumn newColumnTableColumn_35 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_35.setWidth(68);
		newColumnTableColumn_35.setText("Termin");

		final TableColumn newColumnTableColumn_37 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_37.setWidth(50);
		newColumnTableColumn_37.setText("RT");

		final TableColumn newColumnTableColumn_36 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_36.setWidth(167);
		newColumnTableColumn_36.setText("Wohnort");

		final TableColumn newColumnTableColumn_38 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_38.setWidth(133);
		newColumnTableColumn_38.setText("Name");

		final TableColumn newColumnTableColumn_39 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_39.setData("newKey", null);
		newColumnTableColumn_39.setWidth(40);
		newColumnTableColumn_39.setText("Mo");

		final TableColumn newColumnTableColumn_40 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_40.setWidth(37);
		newColumnTableColumn_40.setText("Di");

		final TableColumn newColumnTableColumn_41 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_41.setWidth(36);
		newColumnTableColumn_41.setText("Mi");

		final TableColumn newColumnTableColumn_42 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_42.setWidth(35);
		newColumnTableColumn_42.setText("Do");

		final TableColumn newColumnTableColumn_43 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_43.setWidth(38);
		newColumnTableColumn_43.setText("Fr");

		final TableColumn newColumnTableColumn_44 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_44.setWidth(42);
		newColumnTableColumn_44.setText("Sa");

		final TableColumn newColumnTableColumn_45 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_45.setWidth(43);
		newColumnTableColumn_45.setText("So");

		final TableColumn newColumnTableColumn_46 = new TableColumn(table_2, SWT.NONE);
		newColumnTableColumn_46.setWidth(49);
		newColumnTableColumn_46.setText("Stat");

		final TableItem newItemTableItem_10 = new TableItem(table_2, SWT.BORDER);
		newItemTableItem_10.setText("New item");

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

		final TableColumn newColumnTableColumn_47 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_47.setWidth(44);
		newColumnTableColumn_47.setText("OS");

		final TableColumn newColumnTableColumn_68 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_68.setWidth(100);
		newColumnTableColumn_68.setText("Fzg.");

		final TableColumn newColumnTableColumn_48 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_48.setWidth(160);
		newColumnTableColumn_48.setText("Transport von");

		final TableColumn newColumnTableColumn_49 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_49.setWidth(200);
		newColumnTableColumn_49.setText("Patient");

		final TableColumn newColumnTableColumn_50 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_50.setWidth(195);
		newColumnTableColumn_50.setText("Transport nach");

		final TableColumn newColumnTableColumn_51 = new TableColumn(table_3, SWT.NONE);
		newColumnTableColumn_51.setWidth(52);
		newColumnTableColumn_51.setText("TA");

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

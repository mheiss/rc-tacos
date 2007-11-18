package at.rc.tacos.client.view;


import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.grouplayout.LayoutStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;


import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

import org.eclipse.ui.part.*;
//client
import at.rc.tacos.client.modelManager.CarCompositeManager;
import at.rc.tacos.client.view.composite.CarComposite;

/**
 * GUI to get an overview about the ambulances
 * create CarComposite's using the CarCompositeManager
 * @author b.thek
 */

//TODO: use staffMember instead of String for staff at the ambulance

public class VehiclesView extends ViewPart
{

	public static final String ID = "at.rc.tacos.client.view.ressources_view";


	/**
	 * Create contents of the window
	 */
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout());


		//final SashForm sashForm = new SashForm(parent, SWT.NONE);

		final Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(255, 255, 255));

		
		/**
		 * groups
		 */
		
		//group Bruck/Mur
		Group groupBruckMur;
		groupBruckMur = new Group(composite_1, SWT.NONE);
		groupBruckMur.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_1 = new RowLayout();
		rowLayout_1.marginRight = 5;
		rowLayout_1.spacing = 10;
		groupBruckMur.setLayout(rowLayout_1);
		groupBruckMur.setText("Bruck an der Mur");

		
		//group Kapfenberg
		Group groupKapfenberg;
		groupKapfenberg = new Group(composite_1, SWT.NONE);
		groupKapfenberg.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_2 = new RowLayout();
		rowLayout_2.spacing = 10;
		groupKapfenberg.setLayout(rowLayout_2);
		groupKapfenberg.setText("Kapfenberg");

		
		//group St. Marein
		Group groupMarein;
		groupMarein = new Group(composite_1, SWT.NONE);
		groupMarein.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_3 = new RowLayout();
		rowLayout_3.spacing = 10;
		groupMarein.setLayout(rowLayout_3);
		groupMarein.setText("St. Marein");

		
		//group Thörl
		Group groupThörl;
		groupThörl = new Group(composite_1, SWT.NONE);
		groupThörl.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_4 = new RowLayout();
		rowLayout_4.spacing = 10;
		groupThörl.setLayout(rowLayout_4);
		groupThörl.setText("Thörl");

		
		//group Turnau
		Group groupTurnau;
		groupTurnau = new Group(composite_1, SWT.NONE);
		groupTurnau.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final RowLayout rowLayout_5 = new RowLayout();
		rowLayout_5.spacing = 10;
		groupTurnau.setLayout(rowLayout_5);
		groupTurnau.setText("Turnau");

	
		//group Breitenau
		Group groupBreitenau;
		groupBreitenau = new Group(composite_1, SWT.NONE);
		groupBreitenau.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_6 = new RowLayout();
		rowLayout_6.spacing = 10;
		groupBreitenau.setLayout(rowLayout_6);
		groupBreitenau.setText("Breitenau");

		
		//group Bezirk
		Group groupBezirk;
		groupBezirk = new Group(composite_1, SWT.NONE);
		groupBezirk.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_7 = new RowLayout();
		rowLayout_7.spacing = 10;
		groupBezirk.setLayout(rowLayout_7);
		groupBezirk.setText("Bezirk");
		
		
		/**
		 * cars
		 */
		
		//cars of Bruck
		//TODO: get the ArrayList from the database
		MobilePhoneDetail mp = new MobilePhoneDetail("Bm01","0664/1234567");
		VehicleDetail v1 = new VehicleDetail(1,"Bm01","RTW","a.schw","p.anze","j.made",mp, "the notes 1", "BM", "BM", true,false,1);
		VehicleDetail v2 = new VehicleDetail(1,"Bm02","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "KA", false,false,2);
		VehicleDetail v3 = new VehicleDetail(1,"Bm03","BKTW","r.hart","s.krau","p.stri",mp, "     ", "BM", "BM", false,false,3);
		VehicleDetail v4 = new VehicleDetail(1,"Bm04","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,3);
		VehicleDetail v5 = new VehicleDetail(1,"Bm05","BKTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,2);
		VehicleDetail v6 = new VehicleDetail(1,"Bm06","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,3);
		VehicleDetail v7 = new VehicleDetail(1,"Bm07","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,1);
		VehicleDetail v8 = new VehicleDetail(1,"Bm08","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,2);
		VehicleDetail v9 = new VehicleDetail(1,"Bm09","KTW","r.hart","s.krau","p.stri",mp, "", "BM", "KA", true,false,3);
		VehicleDetail v10 = new VehicleDetail(1,"Bm10","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "KA", false,false,2);
		VehicleDetail v11 = new VehicleDetail(1,"Bm11","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", true,false,2);
		VehicleDetail v12 = new VehicleDetail(1,"Bm12","RTW","r.hart","s.krau","p.stri",mp, "", "BM", "BM", false,false,3);
		ArrayList<VehicleDetail> vehicleList = new ArrayList<VehicleDetail>(Arrays.asList(v1,v2,v3,v4,v5,v6,v8,v9,v10,v11,v12));
		
		CarCompositeManager ccm = new CarCompositeManager();
		for(VehicleDetail vehicle: vehicleList)
		{
			final CarComposite cc = ccm.getCarComposite(groupBruckMur, vehicle);
			cc.setParent(groupBruckMur);
		}
		
		

		/**
		 * Layout
		 */
		final GroupLayout groupLayout = new GroupLayout(composite_1);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.TRAILING)
				.add(groupLayout.createSequentialGroup()
					.addContainerGap()
					.add(groupLayout.createParallelGroup(GroupLayout.TRAILING)
						.add(GroupLayout.LEADING, groupBezirk, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupBreitenau, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupThörl, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupTurnau, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupMarein, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupKapfenberg, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupBruckMur, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE))
						.addContainerGap())
		);

		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(groupBruckMur, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupKapfenberg, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupMarein, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupThörl, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupTurnau, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupBreitenau, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(groupBezirk, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(94, Short.MAX_VALUE))
		);

		composite_1.setLayout(groupLayout);

		
		
		
//regular car composite
		
//		//One car of Turnau
//		final Composite compositeACar_4_8 = new Composite(groupTurnau, SWT.NONE);
//		final RowData rd_compositeACar_4_8 = new RowData();
//		rd_compositeACar_4_8.width = 136;
//		rd_compositeACar_4_8.height = 61;
//		compositeACar_4_8.setLayoutData(rd_compositeACar_4_8);
//		compositeACar_4_8.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6_9_8 = new Composite(compositeACar_4_8, SWT.NONE);
//		composite_6_9_8.setLayout(new FillLayout());
//
//		final Label bm02Label_3_8 = new Label(composite_6_9_8, SWT.NONE);
//		bm02Label_3_8.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label_3_8.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bm02Label_3_8.setText("Tu18");
//
//		final Composite composite_9_4_8 = new Composite(composite_6_9_8, SWT.NONE);
//		composite_9_4_8.setLayout(new FormLayout());
//		composite_9_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label bktwLabel_3_8 = new Label(composite_9_4_8, SWT.CENTER);
//		final FormData fd_label_3_4_8 = new FormData();
//		fd_label_3_4_8.bottom = new FormAttachment(0, 15);
//		fd_label_3_4_8.top = new FormAttachment(0, 0);
//		fd_label_3_4_8.right = new FormAttachment(0, 68);
//		fd_label_3_4_8.left = new FormAttachment(0, 15);
//		bktwLabel_3_8.setLayoutData(fd_label_3_4_8);
//		bktwLabel_3_8.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel_3_8.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel_3_8.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel_3_8.setText("RTW");
//
//		final Composite composite_5_9_8 = new Composite(compositeACar_4_8, SWT.NONE);
//		composite_5_9_8.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_4_8 = new Composite(composite_5_9_8, SWT.NONE);
//		composite_8_4_8.setLayout(new FillLayout());
//
//		final Label label_5_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_5_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/OK2.gif"));
//		label_5_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_8_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_8_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/Handy.gif"));
//		label_8_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_9_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_9_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/Haus.gif"));
//		label_9_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_7_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_7_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/Reparatur.gif"));
//		label_7_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_6_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_6_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/TXT.gif"));
//		label_6_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_4_4_8 = new Label(composite_8_4_8, SWT.NONE);
//		label_4_4_8.setImage(SWTResourceManager.getImage(VehiclesView.class, "/image/Ampel_grün.gif"));
//		label_4_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Composite composite_7_4_8 = new Composite(composite_5_9_8, SWT.NONE);
//		composite_7_4_8.setLayout(new FillLayout());
//
//		final Label label_11_4_8 = new Label(composite_7_4_8, SWT.NONE);
//		label_11_4_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_4_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11_4_8.setText("m.heiß");
//
//		final Label wlohmLabel_3_8 = new Label(composite_7_4_8, SWT.NONE);
//		wlohmLabel_3_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel_3_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		wlohmLabel_3_8.setText("w.lohm");
//
//		final Label bthekLabel_3_8 = new Label(composite_7_4_8, SWT.NONE);
//		bthekLabel_3_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel_3_8.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bthekLabel_3_8.setText("b.thek");
//
//	
		
		
		
		
		

		//gridLayout.makeColumnsEqualWidth = true;

		//---> PersonalView
//		final Composite composite = new Composite(sashForm, SWT.NONE);
//		composite.setLayout(new FillLayout());
//
//		final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
//
//		final TabItem bezirkTabItem = new TabItem(tabFolder, SWT.NONE);
//		bezirkTabItem.setText("Bezirk");
//
//		final SashForm sashForm_1 = new SashForm(tabFolder, SWT.VERTICAL);
//
//		final Group personalImDienstGroup = new Group(sashForm_1, SWT.NONE);
//		personalImDienstGroup.setLayout(new FillLayout());
//		personalImDienstGroup.setText("Personal im Dienst");
//
//		final Table table = new Table(personalImDienstGroup, SWT.BORDER);
//		table.setLinesVisible(true);
//		table.setHeaderVisible(true);
//
//		final TableColumn newColumnTableColumnBereitschaftBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnBereitschaftBezirkImDienst.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
//		newColumnTableColumnBereitschaftBezirkImDienst.setWidth(23);
//		newColumnTableColumnBereitschaftBezirkImDienst.setText("B");
//
//		final TableColumn newColumnTableColumAnmerkungBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumAnmerkungBezirkImDienst.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
//		newColumnTableColumAnmerkungBezirkImDienst.setWidth(26);
//		newColumnTableColumAnmerkungBezirkImDienst.setText("A");
//
//		final TableColumn newColumnTableColumnNameBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnNameBezirkImDienst.setWidth(98);
//		newColumnTableColumnNameBezirkImDienst.setText("Name");
//
//		final TableColumn newColumnTableColumnDienstbezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnDienstbezirkImDienst.setToolTipText("Dienst lt. Dienstplan");
//		newColumnTableColumnDienstbezirkImDienst.setWidth(73);
//		newColumnTableColumnDienstbezirkImDienst.setText("Dienst");
//
//		final TableColumn newColumnTableColumnAnmBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnAnmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Anmeldung");
//		newColumnTableColumnAnmBezirkImDienst.setWidth(41);
//		newColumnTableColumnAnmBezirkImDienst.setText("Anm");
//
//		final TableColumn newColumnTableColumnAbmBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnAbmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Abmeldung");
//		newColumnTableColumnAbmBezirkImDienst.setWidth(41);
//		newColumnTableColumnAbmBezirkImDienst.setText("Abm");
//
//		final TableColumn newColumnTableColumnDVBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnDVBezirkImDienst.setToolTipText("Dienstverhältnis");
//		newColumnTableColumnDVBezirkImDienst.setWidth(31);
//		newColumnTableColumnDVBezirkImDienst.setText("DV");
//
//		final TableColumn newColumnTableColumnVBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnVBezirkImDienst.setToolTipText("Verwendung");
//		newColumnTableColumnVBezirkImDienst.setWidth(30);
//		newColumnTableColumnVBezirkImDienst.setText("V");
//
//		final TableColumn newColumnTableColumnOSBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnOSBezirkImDienst.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
//		newColumnTableColumnOSBezirkImDienst.setWidth(22);
//		newColumnTableColumnOSBezirkImDienst.setText("OS");
//
//		final TableColumn newColumnTableColumnFzgBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnFzgBezirkImDienst.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
//		newColumnTableColumnFzgBezirkImDienst.setWidth(36);
//		newColumnTableColumnFzgBezirkImDienst.setText("Fzg");
//
//		final TableItem newTabItem = new TableItem(table, SWT.BORDER);
//		newTabItem.setText("New item");
//
//		final TableItem newItemTableItem_1 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_1.setText("New item");
//
//		final TableItem newItemTableItem_2 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_2.setText("New item");
//
//		final TableItem newItemTableItem_3 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_3.setText("New item");
//
//		final TableItem newItemTableItem_4 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_4.setText("New item");
//
//		final TableItem newItemTableItem_8 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_8.setText("New item");
//
//		final TableItem newItemTableItem_5 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_5.setText("New item");
//
//		final TableItem newItemTableItem_9 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_9.setText("New item");
//
//		final TableItem newItemTableItem_6 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_6.setText("New item");
//
//		final TableItem newItemTableItem_7 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_7.setText("New item");
//
//		final Menu menu_10 = new Menu(table);
//		table.setMenu(menu_10);
//
//		final MenuItem menuItem_28 = new MenuItem(menu_10, SWT.CASCADE);
//		menuItem_28.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_28.setText("Menu Item");
//
//		final Menu menu_11 = new Menu(menuItem_28);
//		menuItem_28.setMenu(menu_11);
//
//		final MenuItem menuItem_29 = new MenuItem(menu_11, SWT.NONE);
//		menuItem_29.setText("Menu Item");
//
//		final MenuItem menuItem_30 = new MenuItem(menu_11, SWT.NONE);
//		menuItem_30.setText("Menu Item");
//
//		final MenuItem menuItem_31 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_31.setText("Abmelden");
//
//		final MenuItem menuItem_32 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_32.setText("Anmeldung aufheben");
//
//		final MenuItem menuItem_33 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_33.setText("Fahrzeug zuweisen");
//
//		final MenuItem menuItem_34 = new MenuItem(menu_10, SWT.CASCADE);
//		menuItem_34.setText("Fahrzeug zuweisen");
//
//		final Menu menu_12 = new Menu(menuItem_34);
//		menuItem_34.setMenu(menu_12);
//
//		final MenuItem menuItem_35 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_35.setText("Bm02");
//
//		final Menu menu_13 = new Menu(menuItem_35);
//		menuItem_35.setMenu(menu_13);
//
//		final MenuItem menuItem_36 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_36.setText("Fahrer");
//
//		final MenuItem menuItem_38 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_38.setText("Sanitäter I");
//
//		final MenuItem menuItem_37 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_37.setText("Sanitäter II");
//
//		final MenuItem menuItem_39 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_39.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_39.setText("Bm18");
//
//		final Menu menu_14 = new Menu(menuItem_39);
//		menuItem_39.setMenu(menu_14);
//
//		final MenuItem menuItem_43 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_43.setText("Fahrer");
//
//		final MenuItem menuItem_44 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_44.setText("Sanitäter I");
//
//		final MenuItem menuItem_45 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_45.setText("Sanitäter II");
//
//		new MenuItem(menu_12, SWT.SEPARATOR);
//
//		final MenuItem menuItem_40 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_40.setText("Ka04");
//
//		final Menu menu_15 = new Menu(menuItem_40);
//		menuItem_40.setMenu(menu_15);
//
//		final MenuItem menuItem_46 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_46.setText("Fahrer");
//
//		final MenuItem menuItem_47 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_47.setText("Sanitäter I");
//
//		final MenuItem menuItem_48 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_48.setText("Sanitäter II");
//
//		final MenuItem menuItem_41 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_41.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_41.setText("Ka07");
//
//		final Menu menu_16 = new Menu(menuItem_41);
//		menuItem_41.setMenu(menu_16);
//
//		final MenuItem menuItem_49 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_49.setText("Fahrer");
//
//		final MenuItem menuItem_51 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_51.setText("Sanitäter I");
//
//		final MenuItem menuItem_50 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_50.setText("Sanitäter II");
//
//		new MenuItem(menu_12, SWT.SEPARATOR);
//
//		final MenuItem menuItem_42 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_42.setText("Th16");
//
//		final Menu menu_17 = new Menu(menuItem_42);
//		menuItem_42.setMenu(menu_17);
//
//		final MenuItem menuItem_52 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_52.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_52.setText("Fahrer");
//
//		final MenuItem menuItem_54 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_54.setText("Sanitäter I");
//
//		final MenuItem menuItem_53 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_53.setText("Sanitäter II");
//		bezirkTabItem.setControl(sashForm_1);
//
//		final Group personalLtDienstplanGroup = new Group(sashForm_1, SWT.NONE);
//		personalLtDienstplanGroup.setLayout(new FillLayout());
//		personalLtDienstplanGroup.setText("Personal laut Dienstplan");
//
//		final Table table_1 = new Table(personalLtDienstplanGroup, SWT.BORDER);
//		table_1.setLinesVisible(true);
//		table_1.setHeaderVisible(true);
//
//		final TableColumn newColumnTableColumnBereitschaftBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setWidth(23);
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setText("B");
//
//		final TableColumn newColumnTableColumnAnmerkungBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setWidth(26);
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setText("A");
//
//		final TableColumn newColumnTableColumnNameBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnNameBezirkLautDienstplan.setWidth(98);
//		newColumnTableColumnNameBezirkLautDienstplan.setText("Name");
//
//		final TableColumn newColumnTableColumnDienstBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnDienstBezirkLautDienstplan.setToolTipText("Dienst lt. Dienstplan");
//		newColumnTableColumnDienstBezirkLautDienstplan.setWidth(73);
//		newColumnTableColumnDienstBezirkLautDienstplan.setText("Dienst");
//
//		final TableColumn newColumnTableColumnAnmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAnmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Anmeldung");
//		newColumnTableColumnAnmBezirkLautDienstplan.setWidth(41);
//		newColumnTableColumnAnmBezirkLautDienstplan.setText("Anm");
//
//		final TableColumn newColumnTableColumnAbmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAbmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Abmeldung");
//		newColumnTableColumnAbmBezirkLautDienstplan.setWidth(41);
//		newColumnTableColumnAbmBezirkLautDienstplan.setText("Abm");
//
//		final TableColumn newColumnTableColumnDVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnDVBezirkLautDienstplan.setToolTipText("Dienstverhältnis");
//		newColumnTableColumnDVBezirkLautDienstplan.setWidth(31);
//		newColumnTableColumnDVBezirkLautDienstplan.setText("DV");
//
//		final TableColumn newColumnTableColumnVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnVBezirkLautDienstplan.setToolTipText("Verwendung");
//		newColumnTableColumnVBezirkLautDienstplan.setWidth(30);
//		newColumnTableColumnVBezirkLautDienstplan.setText("V");
//
//		final TableColumn newColumnTableColumnOSBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnOSBezirkLautDienstplan.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
//		newColumnTableColumnOSBezirkLautDienstplan.setWidth(22);
//		newColumnTableColumnOSBezirkLautDienstplan.setText("OS");
//
//		final TableColumn newColumnTableColumnFzgBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnFzgBezirkLautDienstplan.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
//		newColumnTableColumnFzgBezirkLautDienstplan.setWidth(36);
//		newColumnTableColumnFzgBezirkLautDienstplan.setText("Fzg");
//
//		final TableItem newTabItem_1 = new TableItem(table_1, SWT.BORDER);
//		newTabItem_1.setText("New item");
//
//		final TableItem newItemTableItem_1_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_1_1.setText("New item");
//
//		final TableItem newItemTableItem_2_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_2_1.setText("New item");
//
//		final TableItem newItemTableItem_3_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_3_1.setText("New item");
//
//		final TableItem newItemTableItem_4_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_4_1.setText("New item");
//
//		final TableItem newItemTableItem_8_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_8_1.setText("New item");
//
//		final TableItem newItemTableItem_5_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_5_1.setText("New item");
//
//		final TableItem newItemTableItem_9_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_9_1.setText("New item");
//
//		final TableItem newItemTableItem_6_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_6_1.setText("New item");
//
//		final TableItem newItemTableItem_7_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_7_1.setText("New item");
//		sashForm_1.setWeights(new int[] {1, 1 });
//
//		final TabItem bruckmurTabItem = new TabItem(tabFolder, SWT.NONE);
//		bruckmurTabItem.setText("Bruck/Mur");
//
//		final TabItem kapfenbergTabItem = new TabItem(tabFolder, SWT.NONE);
//		kapfenbergTabItem.setText("Kapfenberg");
//
//		final TabItem stMareinTabItem = new TabItem(tabFolder, SWT.NONE);
//		stMareinTabItem.setText("St. Marein");
//
//		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
//		tabItem.setText("Thörl");
//
//		final TabItem turnauTabItem = new TabItem(tabFolder, SWT.NONE);
//		turnauTabItem.setText("Turnau");
//
//		final TabItem tagesinformationTabItem = new TabItem(tabFolder, SWT.NONE);
//		tagesinformationTabItem.setText("Breitenau");
//
//		final Group bruckAnDergroupBruckMur = new Group(tabFolder, SWT.NONE);
//		final RowLayout rowLayout = new RowLayout();
//		rowLayout.spacing = 5;
//		rowLayout.pack = false;
//		bruckAnDergroupBruckMur.setLayout(rowLayout);
//		bruckAnDergroupBruckMur.setText("Bruck an der Mur");
//		tagesinformationTabItem.setControl(bruckAnDergroupBruckMur);
//
//		final Composite compositeACar_1 = new Composite(bruckAnDergroupBruckMur, SWT.NONE);
//		final RowData rd_compositeACar_1 = new RowData();
//		rd_compositeACar_1.width = 136;
//		rd_compositeACar_1.height = 61;
//		compositeACar_1.setLayoutData(rd_compositeACar_1);
//		compositeACar_1.setLayout(new FillLayout(SWT.VERTICAL));
//		compositeACar_1.setVisible(false);
//		compositeACar_1.setToolTipText("Detailinformationen zu Bm02");
//
//		final Composite composite_6_6 = new Composite(compositeACar_1, SWT.NONE);
//		composite_6_6.setLayout(new FillLayout());
//
//		final Label bm02Label = new Label(composite_6_6, SWT.NONE);
//		bm02Label.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bm02Label.setText("Bm02");
//
//		final Composite composite_9_1 = new Composite(composite_6_6, SWT.NONE);
//		composite_9_1.setLayout(new FormLayout());
//		composite_9_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label bktwLabel = new Label(composite_9_1, SWT.CENTER);
//		final FormData fd_label_3_1 = new FormData();
//		fd_label_3_1.bottom = new FormAttachment(0, 15);
//		fd_label_3_1.top = new FormAttachment(0, 0);
//		fd_label_3_1.right = new FormAttachment(0, 68);
//		fd_label_3_1.left = new FormAttachment(0, 15);
//		bktwLabel.setLayoutData(fd_label_3_1);
//		bktwLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel.setText("RTW");
//
//		final Composite composite_5_6 = new Composite(compositeACar_1, SWT.NONE);
//		composite_5_6.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_1 = new Composite(composite_5_6, SWT.NONE);
//		composite_8_1.setLayout(new FillLayout());
//
//		final Label label_5_1 = new Label(composite_8_1, SWT.NONE);
//		label_5_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5_1.setText("Label");
//
//		final Label label_8_1 = new Label(composite_8_1, SWT.NONE);
//		label_8_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8_1.setText("Label");
//
//		final Label label_9_1 = new Label(composite_8_1, SWT.NONE);
//		label_9_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9_1.setText("Label");
//
//		final Label label_7_1 = new Label(composite_8_1, SWT.NONE);
//		label_7_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7_1.setText("Label");
//
//		final Label label_6_1 = new Label(composite_8_1, SWT.NONE);
//		label_6_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6_1.setText("Label");
//
//		final Label label_4_1 = new Label(composite_8_1, SWT.NONE);
//		label_4_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4_1.setText("Label");
//
//		final Composite composite_7_1 = new Composite(composite_5_6, SWT.NONE);
//		composite_7_1.setLayout(new FillLayout());
//
//		final Label label_11_1 = new Label(composite_7_1, SWT.NONE);
//		label_11_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11_1.setText("m.heiß");
//
//		final Label wlohmLabel = new Label(composite_7_1, SWT.NONE);
//		wlohmLabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		wlohmLabel.setText("w.lohm");
//
//		final Label bthekLabel = new Label(composite_7_1, SWT.NONE);
//		bthekLabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bthekLabel.setText("b.thek");
//
//		final Group group = new Group(bruckAnDergroupBruckMur, SWT.NONE);
//		group.setLayoutData(new RowData());
//		group.setLayout(new RowLayout());
//		group.setText("Thörl");
//
//		final Composite compositeACar = new Composite(group, SWT.NONE);
//		final RowData rd_compositeACar = new RowData();
//		rd_compositeACar.width = 136;
//		rd_compositeACar.height = 61;
//		compositeACar.setLayoutData(rd_compositeACar);
//		compositeACar.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6 = new Composite(compositeACar, SWT.NONE);
//		composite_6.setLayout(new FillLayout());
//
//		final Label th16Label = new Label(composite_6, SWT.NONE);
//		th16Label.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		th16Label.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		th16Label.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		th16Label.setText("Th16");
//
//		final Composite composite_9 = new Composite(composite_6, SWT.NONE);
//		composite_9.setLayout(new FormLayout());
//		composite_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_3 = new Label(composite_9, SWT.CENTER);
//		final FormData fd_label_3 = new FormData();
//		fd_label_3.bottom = new FormAttachment(0, 15);
//		fd_label_3.top = new FormAttachment(0, 0);
//		fd_label_3.right = new FormAttachment(0, 68);
//		fd_label_3.left = new FormAttachment(0, 15);
//		label_3.setLayoutData(fd_label_3);
//		label_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		label_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		label_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_3.setText("BKTW");
//
//		final Composite composite_5 = new Composite(compositeACar, SWT.NONE);
//		composite_5.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8 = new Composite(composite_5, SWT.NONE);
//		composite_8.setLayout(new FillLayout());
//
//		final Label label_5 = new Label(composite_8, SWT.NONE);
//		label_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5.setText("Label");
//
//		final Label label_8 = new Label(composite_8, SWT.NONE);
//		label_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8.setText("Label");
//
//		final Label label_9 = new Label(composite_8, SWT.NONE);
//		label_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9.setText("Label");
//
//		final Label label_7 = new Label(composite_8, SWT.NONE);
//		label_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7.setText("Label");
//
//		final Label label_6 = new Label(composite_8, SWT.NONE);
//		label_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6.setText("Label");
//
//		final Label label_4 = new Label(composite_8, SWT.NONE);
//		label_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4.setText("Label");
//
//		final Composite composite_7 = new Composite(composite_5, SWT.NONE);
//		composite_7.setLayout(new FillLayout());
//
//		final Label label_11 = new Label(composite_7, SWT.NONE);
//		label_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11.setText("m.heiß");
//
//		final Label label_12 = new Label(composite_7, SWT.NONE);
//		label_12.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_12.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_12.setText("w.lohm");
//
//		final Label label_10 = new Label(composite_7, SWT.NONE);
//		label_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_10.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		label_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_10.setText("b.thek");
//
//		final Composite compositeACar_2 = new Composite(bruckAnDergroupBruckMur, SWT.NONE);
//		final RowData rd_compositeACar_2 = new RowData();
//		rd_compositeACar_2.width = 136;
//		rd_compositeACar_2.height = 61;
//		compositeACar_2.setLayoutData(rd_compositeACar_2);
//		compositeACar_2.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6_7 = new Composite(compositeACar_2, SWT.NONE);
//		composite_6_7.setLayout(new FillLayout());
//
//		final Label bm02Label_1 = new Label(composite_6_7, SWT.NONE);
//		bm02Label_1.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label_1.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bm02Label_1.setText("Bm03");
//
//		final Composite composite_9_2 = new Composite(composite_6_7, SWT.NONE);
//		composite_9_2.setLayout(new FormLayout());
//		composite_9_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//
//		final Label bktwLabel_1 = new Label(composite_9_2, SWT.CENTER);
//		final FormData fd_label_3_2 = new FormData();
//		fd_label_3_2.bottom = new FormAttachment(0, 15);
//		fd_label_3_2.top = new FormAttachment(0, 0);
//		fd_label_3_2.right = new FormAttachment(0, 68);
//		fd_label_3_2.left = new FormAttachment(0, 15);
//		bktwLabel_1.setLayoutData(fd_label_3_2);
//		bktwLabel_1.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel_1.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel_1.setText("RTW");
//
//		final Composite composite_5_7 = new Composite(compositeACar_2, SWT.NONE);
//		composite_5_7.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_2 = new Composite(composite_5_7, SWT.NONE);
//		composite_8_2.setLayout(new FillLayout());
//
//		final Label label_5_2 = new Label(composite_8_2, SWT.NONE);
//		label_5_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_5_2.setText("Label");
//
//		final Label label_8_2 = new Label(composite_8_2, SWT.NONE);
//		label_8_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_8_2.setText("Label");
//
//		final Label label_9_2 = new Label(composite_8_2, SWT.NONE);
//		label_9_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_9_2.setText("Label");
//
//		final Label label_7_2 = new Label(composite_8_2, SWT.NONE);
//		label_7_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_7_2.setText("Label");
//
//		final Label label_6_2 = new Label(composite_8_2, SWT.NONE);
//		label_6_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_6_2.setText("Label");
//
//		final Label label_4_2 = new Label(composite_8_2, SWT.NONE);
//		label_4_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_4_2.setText("Label");
//
//		final Composite composite_7_2 = new Composite(composite_5_7, SWT.NONE);
//		composite_7_2.setLayout(new FillLayout());
//
//		final Label label_11_2 = new Label(composite_7_2, SWT.NONE);
//		label_11_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_11_2.setText("Label");
//
//		final Label wlohmLabel_1 = new Label(composite_7_2, SWT.NONE);
//		wlohmLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		wlohmLabel_1.setText("Label");
//
//		final Label bthekLabel_1 = new Label(composite_7_2, SWT.NONE);
//		bthekLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel_1.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bthekLabel_1.setText("Label");
//
//		final Composite compositeACar_4 = new Composite(bruckAnDergroupBruckMur, SWT.NONE);
//		final RowData rd_compositeACar_4 = new RowData();
//		rd_compositeACar_4.width = 136;
//		rd_compositeACar_4.height = 61;
//		compositeACar_4.setLayoutData(rd_compositeACar_4);
//		compositeACar_4.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6_9 = new Composite(compositeACar_4, SWT.NONE);
//		composite_6_9.setLayout(new FillLayout());
//
//		final Label bm02Label_3 = new Label(composite_6_9, SWT.NONE);
//		bm02Label_3.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label_3.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bm02Label_3.setText("Bm05");
//
//		final Composite composite_9_4 = new Composite(composite_6_9, SWT.NONE);
//		composite_9_4.setLayout(new FormLayout());
//		composite_9_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label bktwLabel_3 = new Label(composite_9_4, SWT.CENTER);
//		final FormData fd_label_3_4 = new FormData();
//		fd_label_3_4.bottom = new FormAttachment(0, 15);
//		fd_label_3_4.top = new FormAttachment(0, 0);
//		fd_label_3_4.right = new FormAttachment(0, 68);
//		fd_label_3_4.left = new FormAttachment(0, 15);
//		bktwLabel_3.setLayoutData(fd_label_3_4);
//		bktwLabel_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel_3.setText("RTW");
//
//		final Composite composite_5_9 = new Composite(compositeACar_4, SWT.NONE);
//		composite_5_9.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_4 = new Composite(composite_5_9, SWT.NONE);
//		composite_8_4.setLayout(new FillLayout());
//
//		final Label label_5_4 = new Label(composite_8_4, SWT.NONE);
//		label_5_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5_4.setText("Label");
//
//		final Label label_8_4 = new Label(composite_8_4, SWT.NONE);
//		label_8_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8_4.setText("Label");
//
//		final Label label_9_4 = new Label(composite_8_4, SWT.NONE);
//		label_9_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9_4.setText("Label");
//
//		final Label label_7_4 = new Label(composite_8_4, SWT.NONE);
//		label_7_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7_4.setText("Label");
//
//		final Label label_6_4 = new Label(composite_8_4, SWT.NONE);
//		label_6_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6_4.setText("Label");
//
//		final Label label_4_4 = new Label(composite_8_4, SWT.NONE);
//		label_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4_4.setText("Label");
//
//		final Composite composite_7_4 = new Composite(composite_5_9, SWT.NONE);
//		composite_7_4.setLayout(new FillLayout());
//
//		final Label label_11_4 = new Label(composite_7_4, SWT.NONE);
//		label_11_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11_4.setText("m.heiß");
//
//		final Label wlohmLabel_3 = new Label(composite_7_4, SWT.NONE);
//		wlohmLabel_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		wlohmLabel_3.setText("w.lohm");
//
//		final Label bthekLabel_3 = new Label(composite_7_4, SWT.NONE);
//		bthekLabel_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel_3.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bthekLabel_3.setText("b.thek");
//
//		final TabItem tagesinformationTabItem_1 = new TabItem(tabFolder, SWT.NONE);
//		tagesinformationTabItem_1.setText("Info");

//		final Menu menu = new Menu(shell, SWT.BAR);
//		menu.setDefaultItem(null);
//		shell.setMenuBar(menu);
//
//		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
//		menuItem.setText("Datei");
//
//		final Menu menu_1 = new Menu(menuItem);
//		menuItem.setMenu(menu_1);
//
//		final MenuItem menuItem_4 = new MenuItem(menu_1, SWT.CASCADE);
//		menuItem_4.setText("Drucken");
//
//		final Menu menu_2 = new Menu(menuItem_4);
//		menuItem_4.setMenu(menu_2);
//
//		final MenuItem menuItem_7 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_7.setText("Journalblatt");
//
//		final MenuItem menuItem_5 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_5.setText("Vormerklisten");
//
//		final MenuItem menuItem_8 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_8.setText("Tagesdienstlisten");
//
//		final MenuItem menuItem_6 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_6.setText("Backup");
//
//		new MenuItem(menu_1, SWT.SEPARATOR);
//
//		final MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
//		menuItem_1.setText("Benutzer wechseln");
//
//		new MenuItem(menu_1, SWT.SEPARATOR);
//
//		final MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
//		menuItem_3.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_3.setText("Beenden");
//
//		final MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_2.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_2.setText("Auftrag");
//
//		final Menu menu_3 = new Menu(menuItem_2);
//		menuItem_2.setMenu(menu_3);
//
//		final MenuItem menuItem_16 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_16.setText("Notfall");
//
//		final MenuItem menuItem_17 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_17.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_17.setText("Vormerkung");
//
//		final MenuItem menuItem_18 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_18.setText("Auftrag (neutral)");
//
//		final MenuItem menuItem_9 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_9.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_9.setText("Personal");
//
//		final Menu menu_4 = new Menu(menuItem_9);
//		menuItem_9.setMenu(menu_4);
//
//		final MenuItem menuItem_19 = new MenuItem(menu_4, SWT.NONE);
//		menuItem_19.setText("Neuen Dienstplaneintrag");
//
//		final MenuItem menuItem_20 = new MenuItem(menu_4, SWT.NONE);
//		menuItem_20.setText("Menu Item");
//
//		final MenuItem menuItem_10 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_10.setText("Fahrzeug");
//
//		final Menu menu_5 = new Menu(menuItem_10);
//		menuItem_10.setMenu(menu_5);
//
//		final MenuItem menuItem_21 = new MenuItem(menu_5, SWT.CASCADE);
//		menuItem_21.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_21.setText("Fahrzeuge hervorheben");
//
//		final Menu menu_9 = new Menu(menuItem_21);
//		menuItem_21.setMenu(menu_9);
//
//		final MenuItem menuItem_24 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_24.setText("RTW's");
//
//		final MenuItem menuItem_22 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_22.setText("KTW's");
//
//		final MenuItem menuItem_23 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_23.setText("BKTW's");
//
//		new MenuItem(menu_9, SWT.SEPARATOR);
//
//		final MenuItem menuItem_25 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_25.setText("Hervorhebung aufheben");
//
//		final MenuItem menuItem_11 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_11.setText("Verwaltung");
//
//		final Menu menu_6 = new Menu(menuItem_11);
//		menuItem_11.setMenu(menu_6);
//
//		final MenuItem menuItem_12 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_12.setText("Sonstiges");
//
//		final Menu menu_7 = new Menu(menuItem_12);
//		menuItem_12.setMenu(menu_7);
//
//		final MenuItem menuItem_26 = new MenuItem(menu_7, SWT.CHECK);
//		menuItem_26.setText("Sachen gibt's ;-)");
//
//		final MenuItem menuItem_27 = new MenuItem(menu_7, SWT.RADIO);
//		menuItem_27.setText("RadioButton");
//
//		final MenuItem menuItem_13 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_13.setText("Hilfe");
//
//		final Menu menu_8 = new Menu(menuItem_13);
//		menuItem_13.setMenu(menu_8);
//
//		final MenuItem menuItem_15 = new MenuItem(menu_8, SWT.NONE);
//		menuItem_15.setText("Direkthilfe");
//
//		final MenuItem menuItem_14 = new MenuItem(menu_8, SWT.NONE);
//		menuItem_14.setText("Hotti's (neuer) Pager");
		//
	}
	

	/**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() 
    {
        //this.idText.setFocus();
    }

	
}


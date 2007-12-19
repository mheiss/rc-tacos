package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.controller.RemoveRosterEntryAction;
import at.rc.tacos.client.controller.UpdateRosterEntryAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PersonalViewContentProvider;
import at.rc.tacos.client.providers.PersonalViewLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.Constants;
import at.rc.tacos.model.RosterEntry;

public class PersonalView extends ViewPart implements PropertyChangeListener
{
    public static final String ID = "at.rc.tacos.client.view.personal_view";

    //the toolkit to use
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableViewer viewer;
    private Listener listener;
    private MyPersonalTooltip tooltip;

    public void createPartControl(final Composite parent) 
    {
        // add listener to model to keep on track. 
        ModelFactory.getInstance().getRosterManager().addPropertyChangeListener(this);

        // Create the scrolled parent component
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Personalübersicht");
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new FillLayout());

        final Composite composite = form.getBody();
        //'Dienstplan'
        final Group group = new Group(composite, SWT.NONE);
        group.setLayout(new FillLayout());
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setText("Dienstplan");


        //tab folder "Bruck - Kapfenberg"
        final TabFolder tabFolder = new TabFolder(group, SWT.NONE);
        tabFolder.addSelectionListener(new SelectionListener() 
        {
            public void widgetSelected(SelectionEvent e) 
            {
                TabItem item = tabFolder.getItem(tabFolder.getSelectionIndex());
                ModelFactory.getInstance().getRosterManager().setActiveStation(item.getText());
            }
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        viewer = new TableViewer(tabFolder, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
        viewer.setContentProvider(new PersonalViewContentProvider());
        viewer.setLabelProvider(new PersonalViewLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getRosterManager());
        viewer.getTable().setLinesVisible(true);
        //set the tooltip
        tooltip = new MyPersonalTooltip(viewer.getControl());
        
        viewer.addSelectionChangedListener(new ISelectionChangedListener() 
        {
			public void selectionChanged(SelectionChangedEvent event) 
			{
		        TableItem[] selection = viewer.getTable().getSelection();
				if (selection != null && selection.length > 0) 
				{
					Rectangle bounds = selection[0].getBounds();
					tooltip.show(new Point(bounds.x, bounds.y));
				}
			}
		});

        hookContextMenu();

        //create the table for the roster entries 
        final Table table = viewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        final TableColumn lockColumn = new TableColumn(table, SWT.NONE);
        lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
        lockColumn.setWidth(30);
        lockColumn.setText("L");
        
//        //TableColumn[] lines = table.getColumns();
//        for (int i = 0;i<=table.getColumnCount();i++)
//        {
//        	table.getItem(i).setForeground(SWTResourceManager.getColor(245, 245, 245));
//        }
//        

        final TableColumn newColumnTableColumnBereitschaftBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnBereitschaftBezirkImDienst.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
        newColumnTableColumnBereitschaftBezirkImDienst.setWidth(30);
        newColumnTableColumnBereitschaftBezirkImDienst.setText("B");

        final TableColumn newColumnTableColumAnmerkungBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumAnmerkungBezirkImDienst.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
        newColumnTableColumAnmerkungBezirkImDienst.setWidth(30);
        newColumnTableColumAnmerkungBezirkImDienst.setText("A");

        final TableColumn newColumnTableColumnNameBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnNameBezirkImDienst.setWidth(130);
        newColumnTableColumnNameBezirkImDienst.setText("Name");

        final TableColumn newColumnTableColumnDienstbezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnDienstbezirkImDienst.setToolTipText("Dienst lt. Dienstplan");
        newColumnTableColumnDienstbezirkImDienst.setWidth(120);
        newColumnTableColumnDienstbezirkImDienst.setText("Dienst");

        final TableColumn newColumnTableColumnAnmBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnAnmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Anmeldung");
        newColumnTableColumnAnmBezirkImDienst.setWidth(70);
        newColumnTableColumnAnmBezirkImDienst.setText("Anm");

        final TableColumn newColumnTableColumnAbmBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnAbmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Abmeldung");
        newColumnTableColumnAbmBezirkImDienst.setWidth(70);
        newColumnTableColumnAbmBezirkImDienst.setText("Abm");

        final TableColumn newColumnTableColumnDVBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnDVBezirkImDienst.setToolTipText("Dienstverhältnis");
        newColumnTableColumnDVBezirkImDienst.setWidth(75);
        newColumnTableColumnDVBezirkImDienst.setText("DV");

        final TableColumn newColumnTableColumnVBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnVBezirkImDienst.setToolTipText("Verwendung");
        newColumnTableColumnVBezirkImDienst.setWidth(65);
        newColumnTableColumnVBezirkImDienst.setText("V");

        final TableColumn newColumnTableColumnOSBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnOSBezirkImDienst.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
        newColumnTableColumnOSBezirkImDienst.setWidth(75);
        newColumnTableColumnOSBezirkImDienst.setText("OS");

        final TableColumn newColumnTableColumnFzgBezirkImDienst = new TableColumn(table, SWT.NONE);
        newColumnTableColumnFzgBezirkImDienst.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
        newColumnTableColumnFzgBezirkImDienst.setWidth(40);
        newColumnTableColumnFzgBezirkImDienst.setText("Fzg");

        //create the tab items for the personal overview
        final TabItem bezirkTabItem = new TabItem(tabFolder, SWT.NONE);
        bezirkTabItem.setText(Constants.STATION_BEZIRK);
        bezirkTabItem.setControl(table);

        final TabItem bruckmurTabItem = new TabItem(tabFolder, SWT.NONE);
        bruckmurTabItem.setText(Constants.STATION_BRUCK);
        bruckmurTabItem.setControl(table);

        final TabItem kapfenbergTabItem = new TabItem(tabFolder, SWT.NONE);
        kapfenbergTabItem.setText(Constants.STATION_KAPFENBERG);
        kapfenbergTabItem.setControl(table);

        final TabItem stMareinTabItem = new TabItem(tabFolder, SWT.NONE);
        stMareinTabItem.setText(Constants.STATION_MAREIN);
        stMareinTabItem.setControl(table);

        final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        tabItem.setText(Constants.STATION_THOERL);
        tabItem.setControl(table);

        final TabItem turnauTabItem = new TabItem(tabFolder, SWT.NONE);
        turnauTabItem.setText(Constants.STATION_TURNAU);
        turnauTabItem.setControl(table);

        final TabItem breitenauTabItem = new TabItem(tabFolder, SWT.NONE);
        breitenauTabItem.setText(Constants.STATION_BREITENAU);
        breitenauTabItem.setControl(table);

        /**
         * context menu
         */
        final Menu contextMenu = new Menu(table);
        table.setMenu(contextMenu);

        //item check in
        final MenuItem menuItemCheckIn = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemCheckIn.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
                int index = table.getSelectionIndex();
                TableItem ti = table.getItem(index);
                RosterEntry re = (RosterEntry)ti.getData();
                TimeForm tf = new TimeForm(re,"Anmeldung");
                tf.open();
            }
        });
        menuItemCheckIn.setText("Anmelden");

        //item check out
        final MenuItem menuItemCheckOut = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemCheckOut.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
            	//TODO move
//                for (int i = 0;i<=table.getColumnCount();i++)
//                {
//                	if(table.getItem(i).getText(5).equalsIgnoreCase(""))
//                	table.getItem(i).setForeground(SWTResourceManager.getColor(185, 185, 185));
//                }
                
                
                int index = table.getSelectionIndex();
                TableItem ti = table.getItem(index);
                RosterEntry re = (RosterEntry)ti.getData();
                TimeForm tf = new TimeForm(re,"Abmeldung");
                tf.open();
            }
        });
        menuItemCheckOut.setText("Abmelden");
        
        
        menuItemCheckOut.setEnabled(false);//TODO change-----------

        new MenuItem(contextMenu, SWT.SEPARATOR);


        //item edit roster entry
        final MenuItem menuItemEditEntry = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemEditEntry.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
                int index = table.getSelectionIndex();
                TableItem ti = table.getItem(index);
                RosterEntry re = (RosterEntry)ti.getData();
                RosterEntryForm window = new RosterEntryForm(re);
                window.open();

            }
        });
        menuItemEditEntry.setText("Eintrag bearbeiten");

        //item delete roster entry
        final MenuItem menuItemDeleteEntry = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemDeleteEntry.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
            	  int index = table.getSelectionIndex();
           				
	              MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_WARNING);
	              messageBox.setText("Dienstplaneintrag löschen");
	              messageBox.setMessage("Möchten Sie den Dienstplaneintrag wirlich löschen?");
	              if (messageBox.open() == SWT.YES)
	              {
		              TableItem ti = table.getItem(index);
		              RosterEntry re = (RosterEntry)ti.getData();
		              RemoveRosterEntryAction action = new RemoveRosterEntryAction(re);
		              action.run();
	              }
	              else
	              return;
            }
        });
        menuItemDeleteEntry.setText("Eintrag löschen");
        new MenuItem(contextMenu, SWT.SEPARATOR);

        //item annul check in
        final MenuItem menuItemAnnulCheckIn = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemAnnulCheckIn.addSelectionListener(new SelectionAdapter() 
        {
        	
            public void widgetSelected(final SelectionEvent e) 
            {
            	int index = table.getSelectionIndex();
            	
            	 MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_WARNING);
	             messageBox.setText("Anmeldung aufheben");
	             messageBox.setMessage("Möchten Sie die Anmeldung wirklich aufheben?");
	             
	             if (messageBox.open() == SWT.YES)
	             {
		                TableItem ti = table.getItem(index);
		                RosterEntry re = (RosterEntry)ti.getData();
		                re.setRealStartOfWork(0);
		                UpdateRosterEntryAction action = new UpdateRosterEntryAction(re);
		                action.run();
	             }
            }
        });
        menuItemAnnulCheckIn.setText("Anmeldung aufheben");

        //item annul check out
        final MenuItem menuItemAnnulCheckOut = new MenuItem(contextMenu, SWT.CASCADE);
        menuItemAnnulCheckOut.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
            	int index = table.getSelectionIndex();
            	
           	 MessageBox messageBox = new MessageBox(parent.getShell(), SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_WARNING);
	             messageBox.setText("Abmeldung aufheben");
	             messageBox.setMessage("Möchten Sie die Abmeldung wirklich aufheben?");
	             
	             if (messageBox.open() == SWT.YES)
	             {
		                TableItem ti = table.getItem(index);
		                RosterEntry re = (RosterEntry)ti.getData();
		                re.setRealEndOfWork(0);
		                UpdateRosterEntryAction action = new UpdateRosterEntryAction(re);
		                action.run();
	             }
            }
        });
        menuItemAnnulCheckOut.setText("Abmeldung aufheben");

//      //resize table with composite
//      tabFolder.addControlListener(new ControlAdapter() 
//      {
//      public void controlResized(ControlEvent e) 
//      {
//      Rectangle area = tabFolder.getClientArea();
//      Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
//      int width = area.width - 2*table.getBorderWidth();
//      if (preferredSize.y > area.height + table.getHeaderHeight()) 
//      {
//      // Subtract the scrollbar width from the total column width
//      // if a vertical scrollbar will be required
//      Point vBarSize = table.getVerticalBar().getSize();
//      width -= vBarSize.x;
//      }
//      Point oldSize = table.getSize();
//      if (oldSize.x > area.width) {
//      // table is getting smaller so make the columns 
//      // smaller first and then resize the table to
//      // match the client area width
//      newColumnTableColumnBereitschaftBezirkImDienst.setWidth(width/28);
//      newColumnTableColumAnmerkungBezirkImDienst.setWidth(width/28);
//      newColumnTableColumnNameBezirkImDienst.setWidth(width/28*9);
//      newColumnTableColumnDienstbezirkImDienst.setWidth(width/28*5);
//      newColumnTableColumnAnmBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnAbmBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnDVBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnVBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnOSBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnFzgBezirkImDienst.setWidth(width/28*2);
//      table.setSize(area.width, area.height);
//      } else {
//      // table is getting bigger so make the table 
//      // bigger first and then make the columns wider
//      // to match the client area width
//      table.setSize(area.width, area.height);
//      newColumnTableColumnBereitschaftBezirkImDienst.setWidth(width/28);
//      newColumnTableColumAnmerkungBezirkImDienst.setWidth(width/28);
//      newColumnTableColumnNameBezirkImDienst.setWidth(width/28*9);
//      newColumnTableColumnDienstbezirkImDienst.setWidth(width/28*5);
//      newColumnTableColumnAnmBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnAbmBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnDVBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnVBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnOSBezirkImDienst.setWidth(width/28*2);
//      newColumnTableColumnFzgBezirkImDienst.setWidth(width/28*2);
//      }
//      }
//      });

//      final Menu menu_11 = new Menu(menuItemCheckIn);
//      menuItemCheckIn.setMenu(menu_11);

//      final MenuItem menuItem_29 = new MenuItem(menu_11, SWT.NONE);
//      menuItem_29.setText("Menu Item");

//      final MenuItem menuItem_30 = new MenuItem(menu_11, SWT.NONE);
//      menuItem_30.setText("Menu Item");

//      final MenuItem menuItem_31 = new MenuItem(contextMenu, SWT.NONE);
//      menuItem_31.setText("Abmelden");

//      final MenuItem menuItem_32 = new MenuItem(contextMenu, SWT.NONE);
//      menuItem_32.setText("Anmeldung aufheben");

//      final MenuItem menuItem_33 = new MenuItem(contextMenu, SWT.NONE);
//      menuItem_33.setText("Fahrzeug zuweisen");

//      final MenuItem menuItem_34 = new MenuItem(contextMenu, SWT.CASCADE);
//      menuItem_34.setText("Fahrzeug zuweisen");

//      final Menu menu_12 = new Menu(menuItem_34);
//      menuItem_34.setMenu(menu_12);

//      final MenuItem menuItem_35 = new MenuItem(menu_12, SWT.CASCADE);
//      menuItem_35.setText("Bm02");

//      final Menu menu_13 = new Menu(menuItem_35);
//      menuItem_35.setMenu(menu_13);

//      final MenuItem menuItem_36 = new MenuItem(menu_13, SWT.NONE);
//      menuItem_36.setText("Fahrer");

//      final MenuItem menuItem_38 = new MenuItem(menu_13, SWT.NONE);
//      menuItem_38.setText("Sanitäter I");

//      final MenuItem menuItem_37 = new MenuItem(menu_13, SWT.NONE);
//      menuItem_37.setText("Sanitäter II");

//      final MenuItem menuItem_39 = new MenuItem(menu_12, SWT.CASCADE);
//      menuItem_39.addSelectionListener(new SelectionAdapter() 
//      {
//      public void widgetSelected(final SelectionEvent e) 
//      {

//      }
//      });
//      menuItem_39.setText("Bm18");

//      final Menu menu_14 = new Menu(menuItem_39);
//      menuItem_39.setMenu(menu_14);

//      final MenuItem menuItem_43 = new MenuItem(menu_14, SWT.NONE);
//      menuItem_43.setText("Fahrer");

//      final MenuItem menuItem_44 = new MenuItem(menu_14, SWT.NONE);
//      menuItem_44.setText("Sanitäter I");

//      final MenuItem menuItem_45 = new MenuItem(menu_14, SWT.NONE);
//      menuItem_45.setText("Sanitäter II");

//      new MenuItem(menu_12, SWT.SEPARATOR);

//      final MenuItem menuItem_40 = new MenuItem(menu_12, SWT.CASCADE);
//      menuItem_40.setText("Ka04");

//      final Menu menu_15 = new Menu(menuItem_40);
//      menuItem_40.setMenu(menu_15);

//      final MenuItem menuItem_46 = new MenuItem(menu_15, SWT.NONE);
//      menuItem_46.setText("Fahrer");

//      final MenuItem menuItem_47 = new MenuItem(menu_15, SWT.NONE);
//      menuItem_47.setText("Sanitäter I");

//      final MenuItem menuItem_48 = new MenuItem(menu_15, SWT.NONE);
//      menuItem_48.setText("Sanitäter II");

//      final MenuItem menuItem_41 = new MenuItem(menu_12, SWT.CASCADE);
//      menuItem_41.addSelectionListener(new SelectionAdapter() 
//      {
//      public void widgetSelected(final SelectionEvent e) 
//      {

//      }
//      });
//      menuItem_41.setText("Ka07");

//      final Menu menu_16 = new Menu(menuItem_41);
//      menuItem_41.setMenu(menu_16);

//      final MenuItem menuItem_49 = new MenuItem(menu_16, SWT.NONE);
//      menuItem_49.setText("Fahrer");

//      final MenuItem menuItem_51 = new MenuItem(menu_16, SWT.NONE);
//      menuItem_51.setText("Sanitäter I");

//      final MenuItem menuItem_50 = new MenuItem(menu_16, SWT.NONE);
//      menuItem_50.setText("Sanitäter II");

//      new MenuItem(menu_12, SWT.SEPARATOR);

//      final MenuItem menuItem_42 = new MenuItem(menu_12, SWT.CASCADE);
//      menuItem_42.setText("Th16");

//      final Menu menu_17 = new Menu(menuItem_42);
//      menuItem_42.setMenu(menu_17);

//      final MenuItem menuItem_52 = new MenuItem(menu_17, SWT.NONE);
//      menuItem_52.addSelectionListener(new SelectionAdapter() 
//      {
//      public void widgetSelected(final SelectionEvent e) 
//      {

//      }
//      });
//      menuItem_52.setText("Fahrer");

//      final MenuItem menuItem_54 = new MenuItem(menu_17, SWT.NONE);
//      menuItem_54.setText("Sanitäter I");

//      final MenuItem menuItem_53 = new MenuItem(menu_17, SWT.NONE);
//      menuItem_53.setText("Sanitäter II");


//      //personal at roster sash part
//      final Group personalLtDienstplanGroup = new Group(sashForm_1, SWT.NONE);
//      personalLtDienstplanGroup.setLayout(new FillLayout());
//      personalLtDienstplanGroup.setText("Personal laut Dienstplan");

//      final Table table_1 = new Table(personalLtDienstplanGroup, SWT.BORDER);
//      table_1.setLinesVisible(true);
//      table_1.setHeaderVisible(true);

//      final TableColumn newColumnTableColumnBereitschaftBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnBereitschaftBezirkLautDienstplan.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
//      newColumnTableColumnBereitschaftBezirkLautDienstplan.setWidth(23);
//      newColumnTableColumnBereitschaftBezirkLautDienstplan.setText("B");

//      final TableColumn newColumnTableColumnAnmerkungBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnAnmerkungBezirkLautDienstplan.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
//      newColumnTableColumnAnmerkungBezirkLautDienstplan.setWidth(26);
//      newColumnTableColumnAnmerkungBezirkLautDienstplan.setText("A");

//      final TableColumn newColumnTableColumnNameBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnNameBezirkLautDienstplan.setWidth(98);
//      newColumnTableColumnNameBezirkLautDienstplan.setText("Name");

//      final TableColumn newColumnTableColumnDienstBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnDienstBezirkLautDienstplan.setToolTipText("Dienst lt. Dienstplan");
//      newColumnTableColumnDienstBezirkLautDienstplan.setWidth(73);
//      newColumnTableColumnDienstBezirkLautDienstplan.setText("Dienst");

//      final TableColumn newColumnTableColumnAnmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnAnmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Anmeldung");
//      newColumnTableColumnAnmBezirkLautDienstplan.setWidth(41);
//      newColumnTableColumnAnmBezirkLautDienstplan.setText("Anm");

//      final TableColumn newColumnTableColumnAbmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnAbmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Abmeldung");
//      newColumnTableColumnAbmBezirkLautDienstplan.setWidth(41);
//      newColumnTableColumnAbmBezirkLautDienstplan.setText("Abm");

//      final TableColumn newColumnTableColumnDVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnDVBezirkLautDienstplan.setToolTipText("Dienstverhältnis");
//      newColumnTableColumnDVBezirkLautDienstplan.setWidth(31);
//      newColumnTableColumnDVBezirkLautDienstplan.setText("DV");

//      final TableColumn newColumnTableColumnVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnVBezirkLautDienstplan.setToolTipText("Verwendung");
//      newColumnTableColumnVBezirkLautDienstplan.setWidth(30);
//      newColumnTableColumnVBezirkLautDienstplan.setText("V");

//      final TableColumn newColumnTableColumnOSBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnOSBezirkLautDienstplan.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
//      newColumnTableColumnOSBezirkLautDienstplan.setWidth(22);
//      newColumnTableColumnOSBezirkLautDienstplan.setText("OS");

//      final TableColumn newColumnTableColumnFzgBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//      newColumnTableColumnFzgBezirkLautDienstplan.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
//      newColumnTableColumnFzgBezirkLautDienstplan.setWidth(36);
//      newColumnTableColumnFzgBezirkLautDienstplan.setText("Fzg");

//      final TableItem newTabItem_1 = new TableItem(table_1, SWT.BORDER);
//      newTabItem_1.setText("New item");

//      final TableItem newItemTableItem_1_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_1_1.setText("New item");

//      final TableItem newItemTableItem_2_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_2_1.setText("New item");

//      final TableItem newItemTableItem_3_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_3_1.setText("New item");

//      final TableItem newItemTableItem_4_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_4_1.setText("New item");

//      final TableItem newItemTableItem_8_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_8_1.setText("New item");

//      final TableItem newItemTableItem_5_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_5_1.setText("New item");

//      final TableItem newItemTableItem_9_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_9_1.setText("New item");

//      final TableItem newItemTableItem_6_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_6_1.setText("New item");

//      final TableItem newItemTableItem_7_1 = new TableItem(table_1, SWT.BORDER);
//      newItemTableItem_7_1.setText("New item");
//      sashForm_1.setWeights(new int[] {1, 1 });

        tabFolder.setSelection(1);
        tabFolder.setSelection(0);

        //listener
        listener = new Listener() {
            public void handleEvent(Event e) 
            {
//              MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
//              dialog.setText("Abbrechen");
//              dialog.setMessage("Wollen Sie wirklich abbrechen?");
//              if (e.type == SWT.Close) 
//              e.doit = false;
//              if (dialog.open() != SWT.YES) 
//              return;
//              shell.dispose();
            }
        };
    }

    //context menu
    private void hookContextMenu() 
    {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() 
        {
            public void menuAboutToShow(IMenuManager manager) 
            {
                // Adding controller to context-menu
                //manager.add(new DeleteItemAction((Item) ((IStructuredSelection) PersonalView.this.viewer.getSelection()).getFirstElement()));
            }
        });
        Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
        this.viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, this.viewer);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus()  { }

    public void propertyChange(PropertyChangeEvent evt) 
    {
        // the viewer represents simple model. refresh should be enough.
        if ("ROSTERENTRY_ADD".equals(evt.getPropertyName())) 
        { 
            this.viewer.refresh();
        }
        // event on deletion --> also just refresh
        if ("ROSTERENTRY_REMOVE".equals(evt.getPropertyName())) 
        { 
            this.viewer.refresh();
        }
        // event on deletion --> also just refresh
        if ("ROSTERENTRY_UPDATE".equals(evt.getPropertyName())) 
        { 
            this.viewer.refresh();
        }
        // event on deletion --> also just refresh
        if ("ROSTERENTRY_CLEARED".equals(evt.getPropertyName())) 
        { 
            this.viewer.refresh();
        }

    }
}

package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.Activator;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.swtdesigner.SWTResourceManager;

public class PersonalView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.personal_view";
	
	private TableViewer viewer;
	//define the columns
	public static final int COLUMN_STANDBY = 0;
	public static final int COLUMN_NOTES = 1;
	public static final int COLUMN_NAME = 2;
	public static final int COLUMN_PLANED_WORK_TIME = 3;
	public static final int COLUMN_CHECK_IN = 4;
	public static final int COLUMN_CHECK_OUT = 5;
	public static final int COLUMN_SERVICE_TYPE = 6;
	public static final int COLUMN_COMPETENCE = 7;
	public static final int COLUMN_STATION = 8;
	public static final int COLUMN_VEHICLE = 9;
	
	
	class PersonalContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
            // do nothing
		}

		public void dispose() {
            // do nothing
		}

		public Object[] getElements(Object parent) {
			return Activator.getDefault().getRosterEntryList().toArray();
		}
	}

	class PersonalLabelProvider implements ITableLabelProvider 
	{
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) 
		{
			RosterEntry entry = (RosterEntry)element;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			
			switch(columnIndex)
			{
			case COLUMN_STANDBY: return String.valueOf(entry.getStandby()); 
			case COLUMN_NOTES: return String.valueOf(entry.getRosterNotes().isEmpty()); 
			case COLUMN_NAME: return entry.getStaffMember().getLastname()+ " " + entry.getStaffMember().getFirstName();
			case COLUMN_PLANED_WORK_TIME: return sdf.format(entry.getTimePlannedStartOfWork() + " - " + sdf.format(entry.getTimePlannedEndOfWork()));
			case COLUMN_CHECK_IN: return sdf.format(entry.getRealStartOfWork());
			case COLUMN_CHECK_OUT: return sdf.format(entry.getRealEndOfWork());
			case COLUMN_SERVICE_TYPE: return entry.getServicetype();
			case COLUMN_COMPETENCE: return entry.getCompetence();
			case COLUMN_STATION: return entry.getStation();
			case COLUMN_VEHICLE: return "Auto";
			default: return null;
			}
		}

		/**
		   * Adds a listener
		   * @param listener the listener
		   */
		@Override
		public void addListener(ILabelProviderListener listener) {
			//ignore
			
		}

		/**
		* Disposes any created resources
		*/
		@Override
		public void dispose() {
			//nothing to dispose
		}

		/**
		   * Returns whether altering this property on this element will affect the
		   * label
		   * @param element the element
		   * @param property the property
		   * @return boolean
		*/
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/**
		 * Removes a listener.
		 * @param listener the listener to remove
		 */
		@Override
		public void removeListener(ILabelProviderListener listener) {
			//ignore it
		}
	}
	
	public void createPartControl(Composite parent) 
	{
        // add listener to model to keep on track. 
        Activator.getDefault().getRosterEntryList().addPropertyChangeListener(this);

		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());	
		
		//group filter
		final Composite composite_1 = new Composite(composite, SWT.NONE);
		final GridLayout gridLayout_2 = new GridLayout();
		composite_1.setLayout(gridLayout_2);
		
		
		final Group filterGroup = new Group(composite_1, SWT.NONE);
		filterGroup.setText("Filter");
		final GridData gd_filterGroup = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd_filterGroup.heightHint = 150;//for normal date field: "30"
		gd_filterGroup.widthHint = 993;
		filterGroup.setLayoutData(gd_filterGroup);
		final GridLayout gridLayout_3 = new GridLayout();
		gridLayout_3.numColumns = 9;
		filterGroup.setLayout(gridLayout_3);

		final DateTime comboDate = new DateTime(filterGroup, SWT.CALENDAR);
		
		final Group group = new Group(composite_1, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group.setText("Dienstplan");
		
		
		//tab folder "Bezirk"
		final TabFolder tabFolder = new TabFolder(group, SWT.NONE);
		
		//table viewer
		this.viewer = new TableViewer(tabFolder, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.viewer.setContentProvider(new PersonalContentProvider());
		this.viewer.setLabelProvider(new PersonalLabelProvider());
		this.viewer.setInput(Activator.getDefault().getRosterEntryList());
        hookContextMenu();
        
		//table
		final Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
	
		final TableColumn newColumnTableColumnBereitschaftBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnBereitschaftBezirkImDienst.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
		newColumnTableColumnBereitschaftBezirkImDienst.setWidth(23);
		newColumnTableColumnBereitschaftBezirkImDienst.setText("B");
	
		final TableColumn newColumnTableColumAnmerkungBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumAnmerkungBezirkImDienst.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
		newColumnTableColumAnmerkungBezirkImDienst.setWidth(26);
		newColumnTableColumAnmerkungBezirkImDienst.setText("A");
	
		final TableColumn newColumnTableColumnNameBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnNameBezirkImDienst.setWidth(98);
		newColumnTableColumnNameBezirkImDienst.setText("Name");
	
		final TableColumn newColumnTableColumnDienstbezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnDienstbezirkImDienst.setToolTipText("Dienst lt. Dienstplan");
		newColumnTableColumnDienstbezirkImDienst.setWidth(73);
		newColumnTableColumnDienstbezirkImDienst.setText("Dienst");
	
		final TableColumn newColumnTableColumnAnmBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnAnmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Anmeldung");
		newColumnTableColumnAnmBezirkImDienst.setWidth(41);
		newColumnTableColumnAnmBezirkImDienst.setText("Anm");
	
		final TableColumn newColumnTableColumnAbmBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnAbmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Abmeldung");
		newColumnTableColumnAbmBezirkImDienst.setWidth(41);
		newColumnTableColumnAbmBezirkImDienst.setText("Abm");
	
		final TableColumn newColumnTableColumnDVBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnDVBezirkImDienst.setToolTipText("Dienstverhältnis");
		newColumnTableColumnDVBezirkImDienst.setWidth(31);
		newColumnTableColumnDVBezirkImDienst.setText("DV");
	
		final TableColumn newColumnTableColumnVBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnVBezirkImDienst.setToolTipText("Verwendung");
		newColumnTableColumnVBezirkImDienst.setWidth(30);
		newColumnTableColumnVBezirkImDienst.setText("V");
	
		final TableColumn newColumnTableColumnOSBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnOSBezirkImDienst.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
		newColumnTableColumnOSBezirkImDienst.setWidth(22);
		newColumnTableColumnOSBezirkImDienst.setText("OS");
	
		final TableColumn newColumnTableColumnFzgBezirkImDienst = new TableColumn(table, SWT.NONE);
		newColumnTableColumnFzgBezirkImDienst.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
		newColumnTableColumnFzgBezirkImDienst.setWidth(36);
		newColumnTableColumnFzgBezirkImDienst.setText("Fzg");
	
		//create the tab items for the table
		final TabItem bezirkTabItem = new TabItem(tabFolder, SWT.NONE);
		bezirkTabItem.setText("Bezirk");
		bezirkTabItem.setControl(table);
		
		final TabItem bruckmurTabItem = new TabItem(tabFolder, SWT.NONE);
		bruckmurTabItem.setText("Bruck/Mur");
		bruckmurTabItem.setControl(table);
	
		final TabItem kapfenbergTabItem = new TabItem(tabFolder, SWT.NONE);
		kapfenbergTabItem.setText("Kapfenberg");
		kapfenbergTabItem.setControl(table);
	
		final TabItem stMareinTabItem = new TabItem(tabFolder, SWT.NONE);
		stMareinTabItem.setText("St. Marein");
		stMareinTabItem.setControl(table);
	
		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Thörl");
		tabItem.setControl(table);
	
		final TabItem turnauTabItem = new TabItem(tabFolder, SWT.NONE);
		turnauTabItem.setText("Turnau");
		turnauTabItem.setControl(table);
	
		final TabItem breitenauTabItem = new TabItem(tabFolder, SWT.NONE);
		breitenauTabItem.setText("Breitenau");
		breitenauTabItem.setControl(table);
	
		final TabItem tagesinformationTabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tagesinformationTabItem_1.setText("Info");	
	
		//context menu
		final Menu menu_10 = new Menu(table);
		table.setMenu(menu_10);
	
		final MenuItem menuItem_28 = new MenuItem(menu_10, SWT.CASCADE);
		menuItem_28.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		menuItem_28.setText("Menu Item");
	
		final Menu menu_11 = new Menu(menuItem_28);
		menuItem_28.setMenu(menu_11);
	
		final MenuItem menuItem_29 = new MenuItem(menu_11, SWT.NONE);
		menuItem_29.setText("Menu Item");
	
		final MenuItem menuItem_30 = new MenuItem(menu_11, SWT.NONE);
		menuItem_30.setText("Menu Item");
	
		final MenuItem menuItem_31 = new MenuItem(menu_10, SWT.NONE);
		menuItem_31.setText("Abmelden");
	
		final MenuItem menuItem_32 = new MenuItem(menu_10, SWT.NONE);
		menuItem_32.setText("Anmeldung aufheben");
	
		final MenuItem menuItem_33 = new MenuItem(menu_10, SWT.NONE);
		menuItem_33.setText("Fahrzeug zuweisen");
	
		final MenuItem menuItem_34 = new MenuItem(menu_10, SWT.CASCADE);
		menuItem_34.setText("Fahrzeug zuweisen");
	
		final Menu menu_12 = new Menu(menuItem_34);
		menuItem_34.setMenu(menu_12);
	
		final MenuItem menuItem_35 = new MenuItem(menu_12, SWT.CASCADE);
		menuItem_35.setText("Bm02");
	
		final Menu menu_13 = new Menu(menuItem_35);
		menuItem_35.setMenu(menu_13);
	
		final MenuItem menuItem_36 = new MenuItem(menu_13, SWT.NONE);
		menuItem_36.setText("Fahrer");
	
		final MenuItem menuItem_38 = new MenuItem(menu_13, SWT.NONE);
		menuItem_38.setText("Sanitäter I");
	
		final MenuItem menuItem_37 = new MenuItem(menu_13, SWT.NONE);
		menuItem_37.setText("Sanitäter II");
	
		final MenuItem menuItem_39 = new MenuItem(menu_12, SWT.CASCADE);
		menuItem_39.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		menuItem_39.setText("Bm18");
	
		final Menu menu_14 = new Menu(menuItem_39);
		menuItem_39.setMenu(menu_14);
	
		final MenuItem menuItem_43 = new MenuItem(menu_14, SWT.NONE);
		menuItem_43.setText("Fahrer");
	
		final MenuItem menuItem_44 = new MenuItem(menu_14, SWT.NONE);
		menuItem_44.setText("Sanitäter I");
	
		final MenuItem menuItem_45 = new MenuItem(menu_14, SWT.NONE);
		menuItem_45.setText("Sanitäter II");
	
		new MenuItem(menu_12, SWT.SEPARATOR);
	
		final MenuItem menuItem_40 = new MenuItem(menu_12, SWT.CASCADE);
		menuItem_40.setText("Ka04");
	
		final Menu menu_15 = new Menu(menuItem_40);
		menuItem_40.setMenu(menu_15);
	
		final MenuItem menuItem_46 = new MenuItem(menu_15, SWT.NONE);
		menuItem_46.setText("Fahrer");
	
		final MenuItem menuItem_47 = new MenuItem(menu_15, SWT.NONE);
		menuItem_47.setText("Sanitäter I");
	
		final MenuItem menuItem_48 = new MenuItem(menu_15, SWT.NONE);
		menuItem_48.setText("Sanitäter II");
	
		final MenuItem menuItem_41 = new MenuItem(menu_12, SWT.CASCADE);
		menuItem_41.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		menuItem_41.setText("Ka07");
	
		final Menu menu_16 = new Menu(menuItem_41);
		menuItem_41.setMenu(menu_16);
	
		final MenuItem menuItem_49 = new MenuItem(menu_16, SWT.NONE);
		menuItem_49.setText("Fahrer");
	
		final MenuItem menuItem_51 = new MenuItem(menu_16, SWT.NONE);
		menuItem_51.setText("Sanitäter I");
	
		final MenuItem menuItem_50 = new MenuItem(menu_16, SWT.NONE);
		menuItem_50.setText("Sanitäter II");
	
		new MenuItem(menu_12, SWT.SEPARATOR);
	
		final MenuItem menuItem_42 = new MenuItem(menu_12, SWT.CASCADE);
		menuItem_42.setText("Th16");
	
		final Menu menu_17 = new Menu(menuItem_42);
		menuItem_42.setMenu(menu_17);
	
		final MenuItem menuItem_52 = new MenuItem(menu_17, SWT.NONE);
		menuItem_52.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		menuItem_52.setText("Fahrer");
	
		final MenuItem menuItem_54 = new MenuItem(menu_17, SWT.NONE);
		menuItem_54.setText("Sanitäter I");
	
		final MenuItem menuItem_53 = new MenuItem(menu_17, SWT.NONE);
		menuItem_53.setText("Sanitäter II");
//	
//		
//		//personal at roster sash part
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
	}
	
	//context menu
	private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
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
    public void setFocus() 
    {
        //this.idText.setFocus();
    }
    
    public void propertyChange(PropertyChangeEvent evt) 
    {
    	System.out.println("change");
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
        
        // 
    }
}

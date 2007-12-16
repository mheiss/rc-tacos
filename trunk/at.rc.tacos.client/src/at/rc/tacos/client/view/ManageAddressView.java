package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.model.RosterEntry;

/**
 * view to manage address entries for the transports
 * @author b.thek
 *
 */
public class ManageAddressView {

	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ManageAddressView window = new ManageAddressView();
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
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setLayout(new FillLayout());
		shell.setSize(520, 511);
		shell.setText("Adressverwaltung");

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());

		final Table tableStreets = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
		tableStreets.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(final MenuDetectEvent e) {
			}
		});
		
		final FormData fd_tableStreets = new FormData();
		fd_tableStreets.top = new FormAttachment(0, 46);
		fd_tableStreets.bottom = new FormAttachment(0, 444);
		fd_tableStreets.right = new FormAttachment(0, 241);
		fd_tableStreets.left = new FormAttachment(0, 26);
		tableStreets.setLayoutData(fd_tableStreets);
		tableStreets.setLinesVisible(true);
		tableStreets.setHeaderVisible(true);

		final TableColumn newColumnTableColumn = new TableColumn(tableStreets, SWT.NONE);
		tableStreets.setSortColumn(newColumnTableColumn);
		newColumnTableColumn.setWidth(100);
		newColumnTableColumn.setText("Straﬂe");

		final Table tableCities = new Table(composite, SWT.FULL_SELECTION | SWT.BORDER);
		final FormData fd_tableCities = new FormData();
		fd_tableCities.right = new FormAttachment(0, 467);
		fd_tableCities.top = new FormAttachment(0, 46);
		fd_tableCities.bottom = new FormAttachment(0, 442);
		fd_tableCities.left = new FormAttachment(0, 270);
		tableCities.setLayoutData(fd_tableCities);
		tableCities.setLinesVisible(true);
		tableCities.setHeaderVisible(true);

		final TableColumn newColumnTableColumn_1 = new TableColumn(tableCities, SWT.NONE);
		newColumnTableColumn_1.setWidth(100);
		newColumnTableColumn_1.setText("Ort");

		final Label label = new Label(composite, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 22);
		fd_label.left = new FormAttachment(0, 27);
		label.setLayoutData(fd_label);
		label.setText("Straﬂen:");
		
		

        
        
		final Label orteLabel = new Label(composite, SWT.NONE);
		final FormData fd_orteLabel = new FormData();
		fd_orteLabel.top = new FormAttachment(label, 0, SWT.TOP);
		fd_orteLabel.left = new FormAttachment(0, 275);
		orteLabel.setLayoutData(fd_orteLabel);
		orteLabel.setText("Orte:");
		
		
		
		/**
         * context menu
         */
		//city
        final Menu contextMenuCity = new Menu(tableCities);
        tableCities.setMenu(contextMenuCity);

        //create new entry
        final MenuItem menuItemNewEntry = new MenuItem(contextMenuCity, SWT.CASCADE);
        menuItemNewEntry.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
                AddressForm af = new AddressForm("Ort");
                af.open();
            }
        });
        menuItemNewEntry.setText("Neuer Eintrag");
        
      //edit entry
        final MenuItem menuItemEditEntry = new MenuItem(contextMenuCity, SWT.CASCADE);
        menuItemEditEntry.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(final SelectionEvent e) 
            {
            	 int index = tableCities.getSelectionIndex();
                 TableItem ti = tableCities.getItem(index);
                String value = ti.getText();
                AddressForm af = new AddressForm(value,"Ort");
                af.open();
            }
        });
        menuItemEditEntry.setText("Eintrag Bearbeiten");
	
	
	
	
		//street
	    final Menu contextMenuStreet = new Menu(tableStreets);
	    tableStreets.setMenu(contextMenuStreet);
	
	    //create new entry
	    final MenuItem menuItemNewEntry2 = new MenuItem(contextMenuStreet, SWT.CASCADE);
	    menuItemNewEntry2.addSelectionListener(new SelectionAdapter() 
	    {
	        public void widgetSelected(final SelectionEvent e) 
	        {
	            AddressForm af = new AddressForm("Straﬂe");
	            af.open();
	        }
	    });
	    menuItemNewEntry2.setText("Neuer Eintrag");
	    
	  //edit entry
	    final MenuItem menuItemEditEntry2 = new MenuItem(contextMenuStreet, SWT.CASCADE);
	    menuItemEditEntry2.addSelectionListener(new SelectionAdapter() 
	    {
	        public void widgetSelected(final SelectionEvent e) 
	        {
	        	 int index = tableStreets.getSelectionIndex();
	             TableItem ti = tableStreets.getItem(index);
	            String value = ti.getText();
	            AddressForm af = new AddressForm(value,"Straﬂe");
	            af.open();
	        }
	    });
	    menuItemEditEntry2.setText("Eintrag Bearbeiten");
	}
}
	

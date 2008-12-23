package at.rc.tacos.client.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.custom.FieldEntry;
import at.rc.tacos.client.ui.providers.LoginTableLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.ui.validators.IntegerValidator;
import at.rc.tacos.client.ui.validators.StringValidator;
import at.rc.tacos.platform.model.ServerInfo;

/**
 * The <code>ServerManageDialog</code> is responsible for the management of the
 * available servers.
 * <p>
 * After the dialog is closed the new server list can be retrieved #getSe
 * 
 * @author Michael
 */
public class ServerManageDialog extends TitleAreaDialog implements ISelectionChangedListener {

	// the controls
	private TableViewer viewer;
	private FieldEntry hostEntry;
	private FieldEntry portEntry;
	private FieldEntry descEntry;
	// the hyperlinks
	private ImageHyperlink addHyperlink;
	private ImageHyperlink removeHyperlink;
	private ImageHyperlink defaultHyperlink;

	// the images to use
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	// the list of servers
	private List<ServerInfo> servers;

	/**
	 * Default class constructor to create a new dialog instance
	 * 
	 * @param parentShell
	 *            the parent shell for the dialog
	 */
	public ServerManageDialog(Shell parentShell, List<ServerInfo> servers) {
		super(parentShell);
		this.servers = servers;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Serververwaltung");
		newShell.setImage(imageRegistry.get("server.document"));
		newShell.setSize(650, 550);
		// center the dialog
		Rectangle parentSize = Display.getCurrent().getActiveShell().getBounds();
		Rectangle mySize = newShell.getBounds();
		int locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
		int locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
		newShell.setLocation(new Point(locationX, locationY));
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite contents = (Composite) super.createContents(parent);
		setTitle("Serververwaltung");
		setMessage("Hier können Sie die Server verwalten die im Programm zur Verfügung stehen.", IMessageProvider.INFORMATION);
		setTitleImage(imageRegistry.get("server.document"));
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout(1, false));
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		contents.setBackground(CustomColors.COLOR_WHITE);
		contents.setBackgroundMode(SWT.INHERIT_FORCE);

		// create the overview table
		createOverview(contents);
		createControls(contents);
		createDetail(contents);

		return contents;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object firstElement = selection.getFirstElement();
		if (!(firstElement instanceof ServerInfo)) {
			// disable the hyperlinks
			removeHyperlink.setEnabled(false);
			removeHyperlink.setForeground(CustomColors.COLOR_GREY);
			defaultHyperlink.setEnabled(false);
			defaultHyperlink.setForeground(CustomColors.COLOR_GREY);
			return;
		}
		ServerInfo info = (ServerInfo) firstElement;
		hostEntry.setText(info.getHostName());
		portEntry.setText(String.valueOf(info.getPort()));
		descEntry.setText(info.getDescription());
		// enable the hyperlinks
		removeHyperlink.setEnabled(true);
		removeHyperlink.setForeground(CustomColors.COLOR_BLUE);
		defaultHyperlink.setEnabled(true);
		defaultHyperlink.setForeground(CustomColors.COLOR_BLUE);
	}

	/**
	 * Helper method to create the overview table
	 */
	private void createOverview(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayout(new GridLayout());
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// default column
		TableColumn defColumn = new TableColumn(table, SWT.NONE);
		defColumn.setText("");
		defColumn.setToolTipText("Standard Server");
		defColumn.setWidth(32);

		// host column
		TableColumn hostColumn = new TableColumn(table, SWT.NONE);
		hostColumn.setText("Hostname");
		hostColumn.setWidth(100);
		// port column
		TableColumn portColumn = new TableColumn(table, SWT.NONE);
		portColumn.setText("Portnummer");
		portColumn.setWidth(100);
		// description column
		TableColumn descColumn = new TableColumn(table, SWT.NONE);
		descColumn.setText("Beschreibung");
		descColumn.setWidth(250);

		// create and setup the viewer
		viewer = new TableViewer(table);
		viewer.setLabelProvider(new LoginTableLabelProvider());
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(servers);
		viewer.addSelectionChangedListener(this);
		viewer.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (viewer.getTable().getItem(new Point(e.x, e.y)) == null) {
					viewer.setSelection(new StructuredSelection());
				}
			}
		});
	}

	/**
	 * Helper methods to create the controls
	 */
	private void createControls(Composite parent) {
		Composite client = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 5;
		layout.spacing = 10;
		client.setLayout(layout);
		client.setBackground(CustomColors.COLOR_WHITE);
		client.setBackgroundMode(SWT.INHERIT_FORCE);

		// create the add hyperlink
		addHyperlink = new ImageHyperlink(client, SWT.NONE);
		addHyperlink.setImage(imageRegistry.get("server.add"));
		addHyperlink.setText("Server hinzufügen");
		addHyperlink.setToolTipText("Fügt einen neuen Server mit den angegebenen Daten hinzu");
		addHyperlink.setUnderlined(true);
		addHyperlink.setForeground(CustomColors.COLOR_BLUE);
		addHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				// validate the input
				if (!validateInput()) {
					return;
				}
				String host = hostEntry.getText();
				String port = portEntry.getText();
				String desc = descEntry.getText();
				// create the new entry and update the viewer
				ServerInfo serverInfo = new ServerInfo(host, Integer.parseInt(port), desc);
				if (servers.contains(serverInfo)) {
					Display.getDefault().beep();
					setErrorMessage("Der Eintrag für '" + host + ":" + port + "' existiert bereits");
					return;
				}
				servers.add(serverInfo);
				viewer.refresh();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
				addHyperlink.setUnderlined(false);
				addHyperlink.setForeground(CustomColors.COLOR_LIGHT_BLUE);
			}

			@Override
			public void linkExited(HyperlinkEvent e) {
				addHyperlink.setUnderlined(true);
				addHyperlink.setForeground(CustomColors.COLOR_BLUE);
			}
		});

		// create the remove hyperlink
		removeHyperlink = new ImageHyperlink(client, SWT.NONE);
		removeHyperlink.setImage(imageRegistry.get("server.delete"));
		removeHyperlink.setText("Server entfernen");
		removeHyperlink.setToolTipText("Entfernt den selektierten Server aus der Liste");
		removeHyperlink.setUnderlined(true);
		removeHyperlink.setEnabled(false);
		removeHyperlink.setForeground(CustomColors.COLOR_GREY);
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				// assert valid selection
				if (viewer.getSelection().isEmpty()) {
					Display.getDefault().beep();
					setErrorMessage("Bitte wählen Sie einen Eintrag aus den Sie entfernen wollen");
					return;
				}
				// remove the selected entry
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				ServerInfo serverInfo = (ServerInfo) selection.getFirstElement();
				servers.remove(serverInfo);
				viewer.refresh();
				// clear the fields
				hostEntry.reset();
				portEntry.reset();
				descEntry.reset();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
				removeHyperlink.setUnderlined(false);
				removeHyperlink.setForeground(CustomColors.COLOR_LIGHT_BLUE);
			}

			@Override
			public void linkExited(HyperlinkEvent e) {
				removeHyperlink.setUnderlined(true);
				removeHyperlink.setForeground(CustomColors.COLOR_BLUE);
			}
		});

		// create the hyperlink that sets the default server
		defaultHyperlink = new ImageHyperlink(client, SWT.NONE);
		defaultHyperlink.setImage(imageRegistry.get("server.default"));
		defaultHyperlink.setText("Server als Standard definieren");
		defaultHyperlink.setToolTipText("Setzt den selektierten Server als Standard Server");
		defaultHyperlink.setUnderlined(true);
		defaultHyperlink.setEnabled(false);
		defaultHyperlink.setForeground(CustomColors.COLOR_GREY);
		defaultHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				// assert valid selection
				if (viewer.getSelection().isEmpty()) {
					Display.getDefault().beep();
					setErrorMessage("Bitte wählen Sie einen Eintrag aus den Sie als Standard definieren wollen");
					return;
				}
				// set the selected entry as default
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				ServerInfo selectedEntry = (ServerInfo) selection.getFirstElement();
				// update the server list
				for (ServerInfo currentEntry : servers) {
					if (selectedEntry.equals(currentEntry)) {
						currentEntry.setDefaultServer(true);
					}
					else {
						currentEntry.setDefaultServer(false);
					}
				}
				viewer.refresh();
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
				defaultHyperlink.setUnderlined(false);
				defaultHyperlink.setForeground(CustomColors.COLOR_LIGHT_BLUE);
			}

			@Override
			public void linkExited(HyperlinkEvent e) {
				defaultHyperlink.setUnderlined(true);
				defaultHyperlink.setForeground(CustomColors.COLOR_BLUE);
			}
		});
	}

	/**
	 * Helper method to create the detail section
	 */
	private void createDetail(Composite parent) {
		Composite client = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 15;
		client.setLayout(layout);
		client.setBackground(CustomColors.COLOR_WHITE);
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// the error decorators
		FieldDecoration decHostError = new FieldDecoration(FieldEntry.DECORATOR_ERROR, "Bitte geben Sie einen gültigen Hostnamen ein");
		FieldDecoration decPortError = new FieldDecoration(FieldEntry.DECORATOR_ERROR, "Bitte geben Sie eine gültige Portnummer ein");
		FieldDecoration decDescError = new FieldDecoration(FieldEntry.DECORATOR_ERROR, "Bitte geben Sie eine gültige Beschreibung ein");

		// create the host entry
		hostEntry = new FieldEntry(client, new StringValidator(), decHostError);
		hostEntry.setRequired();
		hostEntry.setLabel("Hostname");
		hostEntry.formatLabel(SWT.NONE, 100);
		hostEntry.formatText(GridData.FILL_HORIZONTAL, SWT.DEFAULT);

		// create the port entry
		portEntry = new FieldEntry(client, new IntegerValidator(), decPortError);
		portEntry.setRequired();
		portEntry.setLabel("Portnummer");
		portEntry.formatLabel(SWT.NONE, 100);
		portEntry.formatText(GridData.FILL_HORIZONTAL, SWT.DEFAULT);

		descEntry = new FieldEntry(client, new StringValidator(), decDescError);
		descEntry.setRequired();
		descEntry.setLabel("Beschreibung");
		descEntry.formatLabel(SWT.NONE, 100);
		descEntry.formatText(GridData.FILL_HORIZONTAL, SWT.DEFAULT);
	}

	/**
	 * Helper method to validate the input.
	 * 
	 * @return true if all inputs are valid or false in case of an validation
	 *         error
	 */
	private boolean validateInput() {
		// reset the error message
		setErrorMessage(null);

		// validate host
		if (!hostEntry.isValid()) {
			setErrorMessage(hostEntry.getValidationMessage());
			return false;
		}
		// validate port
		if (!portEntry.isValid()) {
			setErrorMessage(portEntry.getValidationMessage());
			return false;
		}
		// validate description
		if (!descEntry.isValid()) {
			setErrorMessage(descEntry.getValidationMessage());
			return false;
		}

		// input is valid
		return true;
	}

	/**
	 * Returns the current list of servers. Please note that this list should
	 * only be processced if the dialog was closed with a
	 * {@link #getReturnCode()} of <code>OK</code>.
	 * 
	 * @return the current list of servers
	 */
	public List<ServerInfo> getServers() {
		return servers;
	}
}

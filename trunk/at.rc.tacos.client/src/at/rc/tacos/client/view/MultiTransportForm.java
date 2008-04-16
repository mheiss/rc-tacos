package at.rc.tacos.client.view;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.PrebookingViewContentProvider;
import at.rc.tacos.client.providers.PrebookingViewLabelProvider;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * The gui to display one multi transport session
 * @author Birgit
 */
public class MultiTransportForm extends Dialog
{
	//properties
	private Table table;
	

	//transport
	Transport transport;
	// description text
	public final static String FORM_DESCRIPTION = "Zeigt die aufgenommenen Transporte an";

	/**
	 * Default class constructor for the MultiTransportForm
	 * @param parentShell the parent shell
	 */
	public MultiTransportForm(Shell parentShell)
	{
		super(parentShell);	
		setBlockOnOpen(false);
	}

	/**
	 * Default class constructor for the MultiTransportForm to add a transport
	 * @param parentShell the parent shell
	 */
	public MultiTransportForm(Shell parentShell,Transport transport)
	{
		super(parentShell);
		this.transport = transport;
	}

	/**
	 * Creates the dialog's contents
	 * @param parent the parent composite
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) 
	{
		
		Control contents = super.createContents(parent);
//		setTitle("Mehrfachtransport");
//		setMessage(FORM_DESCRIPTION, IMessageProvider.INFORMATION);
//		setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		//force a redraw
		getShell().setSize(600, 700);
		getShell().pack(true);
				
		return contents;
	}

	/**
	 * Create contents of the window
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		//setup the composite
		Composite composite = (Composite) super.createDialogArea(parent);

		//create the sections
		createDetailSection(composite);
		
//
//		//init if we have a vehicle
//		if(vehicleDetail != null)
//		{
//			vehicleComboViewer.setSelection(new StructuredSelection(vehicleDetail));
//			mobilePhoneComboViewer.setSelection(new StructuredSelection(vehicleDetail.getMobilePhone()));
//			stationComboViewer.setSelection(new StructuredSelection(vehicleDetail.getCurrentStation()));
//			if(vehicleDetail.getDriver() != null)
//				driverComboViewer.setSelection(new StructuredSelection(vehicleDetail.getDriver()));
//			if(vehicleDetail.getFirstParamedic() != null)
//				medic1ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getFirstParamedic()));
//			if(vehicleDetail.getSecondParamedic() != null)
//				medic2ComboViewer.setSelection(new StructuredSelection(vehicleDetail.getSecondParamedic()));
//			readyButton.setSelection(vehicleDetail.isReadyForAction());
//			outOfOrder.setSelection(vehicleDetail.isOutOfOrder());
//			noteEditor.getDocument().set(vehicleDetail.getVehicleNotes());
//		}
//		checkRequiredFields();
		return composite;
	}

	/**
	 * The user pressed the cancel button
	 */
	@Override
	protected void cancelPressed()
	{
		MessageBox dialog = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		dialog.setText("Abbrechen");
		dialog.setMessage("Wollen Sie wirklich abbrechen?");
		//check the result
		if (dialog.open() != SWT.NO)
			getShell().close();
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed()
	{
//		//driver
//		int index = driverComboViewer.getCombo().getSelectionIndex();
//		vehicleDetail.setDriver((StaffMember)driverComboViewer.getElementAt(index));
//		//medic
//		index = medic1ComboViewer.getCombo().getSelectionIndex();
//		vehicleDetail.setFirstParamedic((StaffMember)medic1ComboViewer.getElementAt(index));
//		//medic1
//		index = medic2ComboViewer.getCombo().getSelectionIndex();
//		vehicleDetail.setSecondParamedic((StaffMember)medic2ComboViewer.getElementAt(index));
//		//notes
//		vehicleDetail.setVehicleNotes(noteEditor.getTextWidget().getText());
//		//status
//		vehicleDetail.setOutOfOrder(outOfOrder.getSelection());
//		if(vehicleDetail.isOutOfOrder())
//			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
//		vehicleDetail.setReadyForAction(readyButton.getSelection());
//		//if the vehicle was out of order -> set the vehicle image to green
//		if(vehicleDetail.isReadyForAction())
//			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
//		//phone
//		index = mobilePhoneComboViewer.getCombo().getSelectionIndex();
//		vehicleDetail.setMobilPhone((MobilePhoneDetail)mobilePhoneComboViewer.getElementAt(index));
//		//station
//		index = stationComboViewer.getCombo().getSelectionIndex();
//		vehicleDetail.setCurrentStation((Location)stationComboViewer.getElementAt(index));
//
//		//check the status of the vehicle (red,yellow, green)
//		if(driverComboViewer.getCombo().getSelectionIndex() == -1 &&
//				medic1ComboViewer.getCombo().getSelectionIndex() == -1 &&
//				medic2ComboViewer.getCombo().getSelectionIndex() == -1)
//			vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
//		//Send the update message
		
		
		//für jeden Transport
		NetWrapper.getDefault().sendAddMessage(Transport.ID, transport);
		getShell().close();
	}

	/**
	 * Creates the section for the transports
	 * @parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{	
		// Create the scrolled parent component
		
		final TableViewer viewer = new TableViewer(parent, SWT.CHECK |SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
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

		table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn blockColumn = new TableColumn(table, SWT.NONE);
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
	}
	
	public void addTransport(Transport transport)
	{
		final TableItem newItemTableItem = new TableItem(table, SWT.BORDER);
		newItemTableItem.setText(1, transport.getPlanedLocation().getLocationName());
		newItemTableItem.setText(2, MyUtils.timestampToString(transport.getPlannedStartOfTransport(), MyUtils.timeFormat));
		newItemTableItem.setText(3, MyUtils.timestampToString(transport.getPlannedTimeAtPatient(), MyUtils.timeFormat));
		newItemTableItem.setText(4, MyUtils.timestampToString(transport.getAppointmentTimeAtDestination(), MyUtils.timeFormat));
		newItemTableItem.setText(5, transport.getFromStreet() +"/" +transport.getFromCity());
		newItemTableItem.setText(6, transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname());
		newItemTableItem.setText(7, transport.getToStreet() +"/" +transport.getToCity());
		newItemTableItem.setText(8, transport.getKindOfTransport());
		newItemTableItem.setText("speichern");
	}
}

package at.rc.tacos.client.ui.dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.Activator;
import at.rc.tacos.client.ui.custom.FieldEntry;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.ui.utils.TimeValidator;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

public class TransportStatusDialog extends TitleAreaDialog {

	private Logger log = LoggerFactory.getLogger(TransportStatusDialog.class);

	private FieldEntry aufgenommen;
	private FieldEntry auftragErteilt;
	private FieldEntry textS1;
	private FieldEntry textS2;
	private FieldEntry textS3;
	private FieldEntry textS4;

	// the edited transport
	private Transport transport;

	// common helper classes
	private ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();

	/**
	 * Creates a new instance of a <code>TransportStatiForm</code>.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param transport
	 *            the transport to edit the stati
	 */
	public TransportStatusDialog(Shell parentShell, Transport transport) {
		super(parentShell);
		this.transport = transport;
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param parent
	 *            the parent composite
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Transportstati bearbeiten");
		setMessage("Hier können sie die einzelnen Statusmeldungen eines Transports bearbeiten", IMessageProvider.INFORMATION);
		setTitleImage(imageRegistry.get("application.logo"));
		return contents;
	}

	@Override
	public boolean close() {
		// check if the user wants to close the window
		if (getReturnCode() == CANCEL) {
			boolean exit = MessageDialog.openQuestion(getShell(), "Abbrechen", "Wollen Sie wirklich abbrechen?");
			if (!exit) {
				return false;
			}
		}
		// remove the lock from the transport
		Lock lock = new Lock(transport.getTransportId(), Transport.class, "");
		RemoveMessage<Lock> removeMessage = new RemoveMessage<Lock>(lock);
		NetWrapper.sendMessage(removeMessage);
		return super.close();
	}

	/**
	 * Create contents of the window
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// setup the composite
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 30;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		composite.setBackground(CustomColors.SECTION_BACKGROUND);

		// the decorator to show in case of an error
		FieldDecoration decError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		decError.setDescription("Bitte geben sie die Zeit im Format HHmm oder HH:mm ein");
		// the validator to use
		TimeValidator validator = new TimeValidator();

		aufgenommen = new FieldEntry(composite, validator, decError);
		aufgenommen.setLabel("Aufgenommen");
		aufgenommen.formatLabel(SWT.NONE, 100);
		aufgenommen.formatText(SWT.NONE, 100);
		aufgenommen.setDisabled();

		auftragErteilt = new FieldEntry(composite, validator, decError);
		auftragErteilt.setLabel("Auftrag erteilt");
		auftragErteilt.formatLabel(SWT.NONE, 100);
		auftragErteilt.formatText(SWT.NONE, 100);
		auftragErteilt.setDisabled();

		textS1 = new FieldEntry(composite, validator, decError);
		textS1.setLabel("S1");
		textS1.formatLabel(SWT.NONE, 100);
		textS1.formatText(SWT.FILL, 100);

		textS2 = new FieldEntry(composite, validator, decError);
		textS2.setLabel("S2");
		textS2.formatLabel(SWT.NONE, 100);
		textS2.formatText(SWT.FILL, 100);

		textS3 = new FieldEntry(composite, validator, decError);
		textS3.setLabel("S3");
		textS3.formatLabel(SWT.NONE, 100);
		textS3.formatText(SWT.FILL, 100);

		textS4 = new FieldEntry(composite, validator, decError);
		textS4.setLabel("S4");
		textS4.formatLabel(SWT.NONE, 100);
		textS4.formatText(SWT.FILL, 100);

		// initialize the data
		initData();

		return composite;
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed() {
		// validate the fields
		if (!checkRequiredFields()) {
			return;
		}

		// apply or remove s1
		if (textS1.containsText()) {
			long timestamp = convertToLong(textS1.getText());
			transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY, timestamp);
		}
		else {
			transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY);
		}

		// apply or remove s2
		if (textS2.containsText()) {
			long timestamp = convertToLong(textS2.getText());
			transport.addStatus(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT, timestamp);
		}
		else {
			transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT);
		}

		// apply or remove s3
		if (textS3.containsText()) {
			long timestamp = convertToLong(textS3.getText());
			transport.addStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT, timestamp);
		}
		else {
			transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT);
		}

		// apply or remove s4
		if (textS4.containsText()) {
			long timestamp = convertToLong(textS4.getText());
			transport.addStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION, timestamp);
		}
		else {
			transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
		}

		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		NetWrapper.sendMessage(updateMessage);

		// closes the sehll
		super.okPressed();
	}

	/**
	 * Helper method to determine wheter all fields are valid
	 * 
	 * @return true if all fields are valid, otherwise false
	 */
	private boolean checkRequiredFields() {
		setErrorMessage(null);
		// check the required fields
		if (!textS1.isValid()) {
			setErrorMessage(textS1.getValidationMessage());
			return false;
		}
		if (!textS2.isValid()) {
			setErrorMessage(textS2.getValidationMessage());
			return false;
		}
		if (!textS3.isValid()) {
			setErrorMessage(textS3.getValidationMessage());
			return false;
		}
		if (!textS4.isValid()) {
			setErrorMessage(textS4.getValidationMessage());
			return false;
		}
		return true;
	}

	/**
	 * Helper method to convert a string into a long value
	 * 
	 * @param input
	 *            the text to parse
	 * @return the long value of the input or null in case of an error
	 */
	private long convertToLong(String input) {
		// the patterns that are valid
		String[] patterns = new String[] { "HH:mm", "HHmm" };
		try {
			Date date = DateUtils.parseDate(input, patterns);
			return date.getTime();
		}
		catch (Exception e) {
			log.error("Failed to convert the input '" + input + "' into a date");
			return 0;
		}
	}

	public void initData() {
		// formatter for the date and time
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();

		// received time
		cal.setTimeInMillis(transport.getCreationTime());
		aufgenommen.setText(sdf.format(cal.getTime()));

		if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED)) {
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
			auftragErteilt.setText(sdf.format(cal.getTime()));
		}

		// Status 1
		if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY)) {
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
			textS1.setText(sdf.format(cal.getTime()));
		}

		// Status 2
		if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT)) {
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
			textS2.setText(sdf.format(cal.getTime()));
		}

		// Status 3
		if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)) {
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
			textS3.setText(sdf.format(cal.getTime()));
		}

		// Status 4
		if (transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION)) {
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
			textS4.setText(sdf.format(cal.getTime()));
		}
	}
}

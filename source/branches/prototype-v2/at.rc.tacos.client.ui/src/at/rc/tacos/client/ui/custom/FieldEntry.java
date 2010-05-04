package at.rc.tacos.client.ui.custom;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.ui.utils.FontUtils;

/**
 * Helper class for creating fields with label and text. With the attached
 * {@link IInputValidator} the content of the field can be verify. Validation
 * errors are displayed using {@link ControlDecoration}.
 * <p>
 * The content of the field will be validated when the focus is lost. The
 * validation status can be obtained using {@link #isValid()} and
 * {@link #getValidationMessage()}
 * </p>
 * <p>
 * Please not that setting a layout or layout data will have no effect because
 * this values are managed internal.
 * 
 * @author Michael
 */
public class FieldEntry extends Composite implements FocusListener, ModifyListener {

	// the color for the invalid field
	private final static Color COLOR_BACKGROUND = new Color(Display.getCurrent(), 255, 255, 255);
	private final static Color COLOR_VALIDATION_ERROR = new Color(Display.getCurrent(), 255, 230, 230);

	/**
	 * The error decorator image that can be used for the field. <b>DO NOT
	 * DISPOSE THIS IMAGE</b>.
	 */
	public final static Image DECORATOR_ERROR = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();

	// validators
	private IInputValidator validator;
	private ControlDecoration errorDecorator;
	private ControlDecoration requiredDecorator;
	private FieldDecoration fieldDecoration;

	// the controls
	private Label label;
	private Text text;

	// required indicator
	private boolean required;

	// validation message
	private String lastValidationMessage = null;

	/**
	 * Create a new <code>FieldEntry</code> instance.
	 * 
	 * @param parent
	 *            the parent composite for this control
	 * @param fieldDecoration
	 *            the descriptor for the decoration
	 */
	public FieldEntry(Composite parent, IInputValidator validator, FieldDecoration fieldDecoration) {
		super(parent, SWT.NONE);
		this.validator = validator;
		this.fieldDecoration = fieldDecoration;
		init(parent);
	}

	/**
	 * Initializes the fields and setup the validators and the listeners
	 */
	private void init(Composite parent) {
		super.setLayout(new GridLayout(2, false));
		super.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		setBackgroundMode(SWT.INHERIT_FORCE);
		setBackground(COLOR_BACKGROUND);
		// create the content
		label = new Label(this, SWT.NONE);
		text = new Text(this, SWT.BORDER);
		text.addFocusListener(this);
		// the decorator
		errorDecorator = new ControlDecoration(text, SWT.RIGHT);
		errorDecorator.setDescriptionText(fieldDecoration.getDescription());
		errorDecorator.setImage(fieldDecoration.getImage());
		errorDecorator.hide();
	}

	/**
	 * Sets the text that should be displayed in the label.
	 * 
	 * @param initialValue
	 *            the text to display
	 */
	public void setLabel(String initialValue) {
		label.setText(initialValue);
	}

	/**
	 * Sets the values that will be taken to setup a {@link GridData} instance
	 * to layout the label.
	 * 
	 * @param styleFlag
	 *            the style flag
	 * @param width
	 *            the width of the label
	 */
	public void formatLabel(int styleFlag, int width) {
		GridData data = new GridData(styleFlag);
		data.widthHint = width;
		label.setLayoutData(data);
	}

	/**
	 * Sets the text that should be displayed in the textfield.
	 * 
	 * @param initialValue
	 *            the text to display
	 */
	public void setText(String initialValue) {
		text.setText(initialValue);
	}

	/**
	 * Sets the values that will be taken to setup a {@link GridData} instance
	 * to layout the textfield.
	 * 
	 * @param styleFlag
	 *            the style flag
	 * @param width
	 *            the width of the textfield
	 */
	public void formatText(int styleFlag, int width) {
		GridData data = new GridData(styleFlag);
		data.widthHint = width;
		text.setLayoutData(data);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// remove the key listener if the field is valud
		if (isValid()) {
			text.removeModifyListener(this);
			return;
		}
		text.addModifyListener(this);
	}

	@Override
	public void focusLost(FocusEvent e) {
		// if the field is required then empty text is not allowed
		if (isRequired()) {
			doValidate();
			return;
		}
		if (!containsText()) {
			doReset();
		}
	}

	@Override
	public void modifyText(ModifyEvent e) {
		doValidate();
	}

	/**
	 * Helper method to perform validation.
	 */
	private void doValidate() {
		lastValidationMessage = validator.isValid(text.getText());
		if (lastValidationMessage != null) {
			errorDecorator.show();
			// hide the decorator when we show an error
			if (requiredDecorator != null) {
				requiredDecorator.hide();
			}
			text.setBackground(COLOR_VALIDATION_ERROR);
			return;
		}
		// field is valid
		doReset();
	}

	/**
	 * Helper method to remove the decorators
	 */
	private void doReset() {
		errorDecorator.hide();
		text.setBackground(null);
		// show the decorator again
		if (requiredDecorator != null) {
			requiredDecorator.show();
		}
	}

	/**
	 * Resets the content of the field and removes the error decorators.
	 */
	public void reset() {
		text.setText("");
		doReset();
	}

	/**
	 * Returns whether or not the current content of the field is valid
	 * according to the {@link IInputValidator} that was set.
	 * <p>
	 * The last validation message can be retrived with
	 * {@link #getValidationMessage}
	 * 
	 * @see #getValidationMessage
	 * @return true if the content is valid otherwise false
	 */
	public boolean isValid() {
		doValidate();
		return lastValidationMessage == null ? true : false;
	}

	/**
	 * Returns the last validation message for the current input. The validation
	 * message will be updated when the field lost the focus.
	 * <p>
	 * The returned value will be null if the current content is valid.
	 * </p>
	 * 
	 * @see #isValid
	 * @return the last validation message or null if the content is valid
	 */
	public String getValidationMessage() {
		return lastValidationMessage;
	}

	/**
	 * Sets this field as required field and sets the required decorator.
	 */
	public void setRequired() {
		this.required = true;
		// create the decorator
		FieldDecoration decRequired = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED);
		decRequired.setDescription("Eingabe erforderlich");
		// add the required decorator
		requiredDecorator = new ControlDecoration(text, SWT.RIGHT);
		requiredDecorator.setDescriptionText(decRequired.getDescription());
		requiredDecorator.setImage(decRequired.getImage());
		// set the label to bold
		label.setFont(FontUtils.getBold(label.getFont()));
	}

	public boolean isRequired() {
		return required;
	}

	/**
	 * Disables the textfield so that it cannot be edited
	 */
	public void setDisabled() {
		text.setEditable(false);
		text.setEnabled(false);
	}

	/**
	 * Returns the current content of the textfield
	 * 
	 * @return the content of the textfield
	 */
	public String getText() {
		return text.getText().trim();
	}

	/**
	 * Returns whether or not the textfield contains text.
	 * 
	 * @return true if the textfield contains content otherwise false
	 */
	public boolean containsText() {
		return !getText().isEmpty();
	}

	/**
	 * Setting the layout will have no effect since it is managed internal.
	 */
	@Override
	public void setLayout(Layout layout) {
		// managed by the component
	}

	/**
	 * Setting the layout data will have no effect since it is managed internal.
	 */
	@Override
	public void setLayoutData(Object layoutData) {
		// managed by the component
	}

	/**
	 * Sets the fous to the input field
	 */
	@Override
	public boolean setFocus() {
		boolean hasFocus = text.setFocus();
		doReset();
		return hasFocus;
	}
}

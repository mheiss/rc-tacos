/*******************************************************************************
 * Copyright (c) 2004, 2007 Mylyn project committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package at.rc.tacos.client.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * The DatePick component from the mylyn project.
 */
public class DatePicker extends Composite 
{

	public final static String TITLE_DIALOG = "Datum ausw�hlem";

	public static final String LABEL_CHOOSE = "<Datum ausw�hlen>";

	private Text dateText = null;

	private Button pickButton = null;

	private Calendar date = null;

	private List<SelectionListener> pickerListeners = new LinkedList<SelectionListener>();

	private SimpleDateFormat simpleDateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
			DateFormat.SHORT);

	private String initialText = "Datum ausw�hlen";

	public DatePicker(Composite parent, int style, String initialText) 
	{
		super(parent, style);
		this.initialText = initialText;
		initialize((style & SWT.FLAT) > 0 ? SWT.FLAT : 0);
	}

	public void setDatePattern(String pattern) 
	{
		simpleDateFormat.applyPattern(pattern);
	}

	private void initialize(int style) 
	{

		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		this.setLayout(gridLayout);

		dateText = new Text(this, style);
		GridData dateTextGridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		dateTextGridData.widthHint = 135;
		dateTextGridData.verticalIndent = 0;

		dateText.setLayoutData(dateTextGridData);
		dateText.setText(initialText);
		dateText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// key listener used because setting of date picker text causes
				// modify listener to fire which results in perpetual dirty
				// editor
				notifyPickerListeners();
			}
		});

		dateText.addFocusListener(new FocusAdapter() {
			Calendar calendar = Calendar.getInstance();

			@Override
			public void focusLost(FocusEvent e) {
				Date reminderDate;
				try {
					reminderDate = simpleDateFormat.parse(dateText.getText());
					calendar.setTime(reminderDate);
					date = calendar;
					updateDateText();
				} catch (ParseException e1) {
					updateDateText();
				}

			}
		});

		pickButton = new Button(this, style | SWT.ARROW | SWT.DOWN);
		GridData pickButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, true);
		pickButtonGridData.verticalIndent = 0;
		pickButton.setLayoutData(pickButtonGridData);
		pickButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				Calendar newCalendar = GregorianCalendar.getInstance();
				newCalendar.set(Calendar.HOUR_OF_DAY, 0);
				newCalendar.set(Calendar.MINUTE, 0);
				newCalendar.set(Calendar.SECOND, 0);
				newCalendar.set(Calendar.MILLISECOND, 0);
				if (date != null) 
				{
					newCalendar.setTime(date.getTime());
				}

				Shell shell = null;
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
					shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				} else {
					shell = new Shell(PlatformUI.getWorkbench().getDisplay());
				}
				DateSelectionDialog dialog = new DateSelectionDialog(shell, newCalendar, DatePicker.TITLE_DIALOG);
				pickButton.setEnabled(false);
				dateText.setEnabled(false);

				int dialogResponse = dialog.open();
				if (dialog.getDate() != null) 
					newCalendar.setTime(dialog.getDate());
				else 
					newCalendar = null;
				dateSelected(dialogResponse == Window.CANCEL, newCalendar);
			}
		});

		pack();
	}

	public void addPickerSelectionListener(SelectionListener listener) {
		pickerListeners.add(listener);
	}

	/**
	 * must check for null return value
	 */
	public Calendar getDate() {
		return date;
	}

	@Override
	public void setBackground(Color backgroundColor) {
		dateText.setBackground(backgroundColor);
		super.setBackground(backgroundColor);
	}

	public void setDate(Calendar date) {
		this.date = date;
		updateDateText();
	}
	
	/** Called when the user has selected a date */
	protected void dateSelected(boolean canceled, Calendar selectedDate) {

		if (!canceled) {
			this.date = selectedDate != null ? selectedDate : null;
			updateDateText();
			notifyPickerListeners();
		}

		pickButton.setEnabled(true);
		dateText.setEnabled(true);
	}

	private void notifyPickerListeners() {
		for (SelectionListener listener : pickerListeners) {
			listener.widgetSelected(null);
		}
	}

	private void updateDateText() {
		if (date != null) {
			Date currentDate = new Date(date.getTimeInMillis());
			dateText.setText(simpleDateFormat.format(currentDate));
		} else {
			dateText.setEnabled(false);
			dateText.setText(LABEL_CHOOSE);
			dateText.setEnabled(true);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		dateText.setEnabled(enabled);
		pickButton.setEnabled(enabled);
		super.setEnabled(enabled);
	}

}

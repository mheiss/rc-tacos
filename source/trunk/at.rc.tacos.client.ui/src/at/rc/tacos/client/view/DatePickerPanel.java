/*******************************************************************************
 * Copyright (c) 2004, 2007 Mylyn project committers and others. All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package at.rc.tacos.client.view;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

/**
 * The DatePickerPanel from the eclipse mylin projekt.
 * 
 * @author Bahadir Yagan
 * @author Mik Kersten
 * @author Rob Elves
 */
public class DatePickerPanel extends Composite implements KeyListener, ISelectionProvider {

	private org.eclipse.swt.widgets.List timeList = null;

	private ISelection selection = null;

	private Calendar date = null;

	private DateTime calendar = null;

	private List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();

	public DatePickerPanel(Composite parent, int style, Calendar initialDate) {
		super(parent, style);
		this.date = initialDate;
		initialize();
		setDate(date);
		// this.setBackground()
	}

	private void initialize() {
		if (date == null) {
			date = Calendar.getInstance();
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			date.set(Calendar.MILLISECOND, 0);
		}
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);
		calendar = new DateTime(this, SWT.CALENDAR);
		calendar.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				date.set(Calendar.YEAR, calendar.getYear());
				date.set(Calendar.MONTH, calendar.getMonth());
				date.set(Calendar.DAY_OF_MONTH, calendar.getDay());
				setSelection(new DateSelection(date));
				notifyListeners(new SelectionChangedEvent(DatePickerPanel.this, getSelection()));
			}
		});

		createTimeList(this);
	}

	/**
	 * This method initializes the month combo
	 */
	private void createTimeList(Composite composite) {

		DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		Calendar tempCalendar = Calendar.getInstance();
		tempCalendar.set(Calendar.MINUTE, 0);
		tempCalendar.set(Calendar.SECOND, 0);
		String[] times = new String[48];
		int pos = 0;
		for (int x = 0; x < 24; x++) {
			// hour
			tempCalendar.set(Calendar.MINUTE, 0);
			tempCalendar.set(Calendar.HOUR_OF_DAY, x);
			times[pos] = dateFormat.format(tempCalendar.getTime());
			// count up the position for the minute
			pos++;
			// minute
			tempCalendar.set(Calendar.MINUTE, 30);
			times[pos] = dateFormat.format(tempCalendar.getTime());
			// count up the position for the hour
			pos++;
		}

		ListViewer listViewer = new ListViewer(composite);

		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setInput(times);

		timeList = listViewer.getList();

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				// even index -> just hour
				if (timeList.getSelectionIndex() % 2 == 0) {
					date.set(Calendar.HOUR_OF_DAY, timeList.getSelectionIndex() / 2);
					date.set(Calendar.MINUTE, 0);
				}
				else {
					date.set(Calendar.HOUR_OF_DAY, timeList.getSelectionIndex() / 2);
					date.set(Calendar.MINUTE, 30);
				}
				setSelection(new DateSelection(date));
				notifyListeners(new SelectionChangedEvent(DatePickerPanel.this, getSelection()));
			}
		});

		GridDataFactory.fillDefaults().hint(SWT.DEFAULT, 150).grab(false, true).applyTo(timeList);
		if (date != null) {
			// if we have a minute -> select it
			if (date.get(Calendar.MINUTE) > 0)
				listViewer.setSelection(new StructuredSelection(times[date.get(Calendar.HOUR_OF_DAY) + 1]), true);
			else
				listViewer.setSelection(new StructuredSelection(times[date.get(Calendar.HOUR_OF_DAY)]), true);
		}
		else {
			listViewer.setSelection(new StructuredSelection(times[8]), true);
		}
		timeList.addKeyListener(this);
	}

	public void setDate(Calendar date) {
		this.date = date;
		calendar.setYear(date.get(Calendar.YEAR));
		calendar.setMonth(date.get(Calendar.MONTH));
		calendar.setDay(date.get(Calendar.DAY_OF_MONTH));
	}

	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.ESC) {
			SelectionChangedEvent changeEvent = new SelectionChangedEvent(this, new ISelection() {

				public boolean isEmpty() {
					return true;
				}
			});
			notifyListeners(changeEvent);
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	private void notifyListeners(SelectionChangedEvent event) {
		for (ISelectionChangedListener listener : selectionListeners) {
			listener.selectionChanged(event);
		}
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	public ISelection getSelection() {
		return selection;
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public class DateSelection implements ISelection {

		private Calendar date;

		public DateSelection(Calendar calendar) {
			date = calendar;
		}

		public boolean isEmpty() {
			return date == null;
		}

		public Calendar getDate() {
			return date;
		}
	}
}

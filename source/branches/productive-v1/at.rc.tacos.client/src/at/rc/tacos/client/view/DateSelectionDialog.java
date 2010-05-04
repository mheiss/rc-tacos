/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/

package at.rc.tacos.client.view;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.rc.tacos.client.view.DatePickerPanel.DateSelection;

/**
 * The date selection dialog from the clipse mylyn project. (c) is by the
 * eclipse mylyn project
 * 
 * @author Ken Sueda
 * @author Mik Kersten
 * @author Rob Elves
 */
public class DateSelectionDialog extends Dialog {

	private Date reminderDate = null;

	private String title = "Date Selection";

	private Calendar initialCalendar = Calendar.getInstance();

	private FormToolkit toolkit;

	public DateSelectionDialog(Shell parentShell, String title) {
		this(parentShell, Calendar.getInstance(), title);
	}

	public DateSelectionDialog(Shell parentShell, Calendar initialDate, String title) {
		super(parentShell);

		toolkit = new FormToolkit(parentShell.getDisplay());
		;
		if (title != null) {
			this.title = title;
		}
		if (initialDate != null) {
			this.initialCalendar.setTime(initialDate.getTime());
		}
		reminderDate = initialCalendar.getTime();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(title);
		DatePickerPanel datePanel = new DatePickerPanel(parent, SWT.NULL, initialCalendar);
		datePanel.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					DateSelection dateSelection = (DateSelection) event.getSelection();
					reminderDate = dateSelection.getDate().getTime();
				}
			}
		});
		datePanel.setBackground(toolkit.getColors().getBackground());
		return datePanel;
	}

	@Override
	public boolean close() {
		toolkit.dispose();
		return super.close();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(toolkit.getColors().getBackground());
		createButton(parent, IDialogConstants.CLIENT_ID + 1, "Clear", false);
		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
		if (buttonId == IDialogConstants.CLIENT_ID + 1) {
			reminderDate = null;
			okPressed();
		}
	}

	public Date getDate() {
		return reminderDate;
	}
}

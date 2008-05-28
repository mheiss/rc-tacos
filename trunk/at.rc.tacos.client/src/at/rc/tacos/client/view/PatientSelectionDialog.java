/*******************************************************************************
 * Copyright (c) 2004 - 2006 Mylar committers and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.SickPersonContentProvider;
import at.rc.tacos.client.providers.SickPersonLabelProvider;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SickPerson;

/**
 * Provides a selection dialog to choose a patient
 */
public class PatientSelectionDialog extends SelectionStatusDialog implements PropertyChangeListener
{
	private TableViewer viewer;

	/**
	 * Defaul class constructor to set up a new patient select dialog
	 * @param parent
	 */
	public PatientSelectionDialog(final Shell parent) 
	{
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		//listen to sick person updates
		ModelFactory.getInstance().getSickPersonManager().addPropertyChangeListener(this);
	}
	
	@Override
	public boolean close() 
	{
		//cleanup the listeners
		ModelFactory.getInstance().getSickPersonManager().removePropertyChangeListener(this);
		//colse the dialog
		return super.close();
	}

	@Override
    protected void configureShell(final Shell shell)
    {
        shell.setText("Patienten Suche"); 
        super.configureShell(shell);
    }

	@Override
	protected Control createDialogArea(final Composite parent) 
	{
		final Composite area = (Composite) super.createDialogArea(parent);

		final Label message = new Label(area, SWT.NONE);
		message.setText("&Bitte wählen Sie einen Patienten aus:\n");
		final Text filterText = new Text(area, SWT.SINGLE | SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));

		final Label matches = new Label(area, SWT.NONE);
		matches.setText("&Gefundene Patienten:"); 
		
		viewer = new TableViewer(area, SWT.SINGLE | SWT.BORDER);
		final Control control = this.viewer.getControl();
		final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(gd);
		gd.widthHint = 400;
		gd.heightHint = 200;
        
		viewer.setLabelProvider(new SickPersonLabelProvider());
		viewer.setContentProvider(new SickPersonContentProvider());
		viewer.setInput(ModelFactory.getInstance().getSickPersonManager().getSickPersons());

		final PatientSelectionDialog.TaskFilter filter = new PatientSelectionDialog.TaskFilter();
		viewer.addFilter(filter);

		//add a key listener to the filter text input box
		filterText.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(final KeyEvent e) 
			{
				if (e.keyCode == SWT.ARROW_DOWN) 
					PatientSelectionDialog.this.viewer.getControl().setFocus();
			}
		});
		
		filterText.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				//get the entered text
				String filterValue = filterText.getText().toLowerCase();
				
				if(filterValue.trim().length() < 1)
				{
					updateStatus(new Status(Status.WARNING,Activator.PLUGIN_ID,"Bitte geben sie mindestens ein Zeichen des Nachnamens ein"));
					return;
				}
				updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte wählen Sie einen Patienten aus"));
				
				//send a request to the server to list all matching patients
				NetWrapper.getDefault().requestListing(SickPerson.ID,new QueryFilter(IFilterTypes.SICK_PERSON_LASTNAME_FILTER,filterValue));
				//apply the filter
				filter.setFilterText(filterText.getText()); 
			}
		});
		
        viewer.addSelectionChangedListener(new ISelectionChangedListener() 
        {
            public void selectionChanged(final SelectionChangedEvent event) 
            {
                if (!event.getSelection().isEmpty()) 
                	updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID, ((SickPerson)((IStructuredSelection) event.getSelection()).getFirstElement()).getLastName() + " ausgewählt"));
                else 
                    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte wählen Sie einen Patienten aus"));      
            }
        });
        
        setStatusLineAboveButtons(true);
        
		return area;
	}

	@Override
	protected void computeResult() 
	{
		setResult(((IStructuredSelection) viewer.getSelection()).toList());
	}
	
	//SUBCLASS FOR A TASK FILTER TO PROVIDE SOME CUSTOM CHARS
	
	/**
	 * Implements a {@link ViewFilter} based on content typed in the filter
	 * field
	 */
	class TaskFilter extends ViewerFilter 
	{
		private Pattern pattern;

		public void setFilterText(final String filterText) 
		{
            String newText = filterText + "*"; 
			if (newText.trim().equals("")) 
				this.pattern = null;
			else 
			{
                newText = newText.replace("\\", "\\\\"); 
                newText = newText.replace(".", "\\.");
                newText = newText.replace("*", ".*");
                newText = newText.replace("?", ".?"); 
				this.pattern = Pattern.compile(newText, Pattern.CASE_INSENSITIVE);
			}
		}

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) 
		{
            boolean returnValue = true;
            if (pattern != null) 
                returnValue = pattern.matcher(((SickPerson)element).getLastName()).matches() || pattern.matcher(((SickPerson) element).getFirstName()).matches();
            return returnValue;
		}
	}

	/**
	 * This listener will be informed when the server sends new patients based on the entered text
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) 
	{
		//listen to listing responses
		if("SICKPERSON_ADD".equalsIgnoreCase((pce.getPropertyName())) || "SICKPERSON_UPDATE".equalsIgnoreCase((pce.getPropertyName())))
		{
			SickPerson person = (SickPerson)pce.getNewValue();
			viewer.refresh(true);
			final Object first = viewer.getElementAt(0);
			if (first != null) 
			{
				PatientSelectionDialog.this.viewer.setSelection(new StructuredSelection(first));
                updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID,((SickPerson)first).getLastName() + " ausgewählt")); 
			}
			else 
			    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte wählen Sie einen Patienten aus"));
		}
	}
}

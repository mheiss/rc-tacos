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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
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
import at.rc.tacos.client.jobs.FilterAddressJob;
import at.rc.tacos.client.jobs.FilterPatientJob;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.SickPersonContentProvider;
import at.rc.tacos.client.providers.SickPersonLabelProvider;
import at.rc.tacos.model.SickPerson;

/**
 * Provides a selection dialog to choose a patient
 */
public class PatientSelectionDialog extends SelectionStatusDialog implements PropertyChangeListener
{
	private TableViewer viewer;
	private Text filterText;
	private String initValue;
	
	
	/**
	 * The scheduler job to start the filter
	 */
	private FilterPatientJob filterJob;

	/**
	 * Defaul class constructor to set up a new patient select dialog
	 * @param parent
	 */
	public PatientSelectionDialog(String initValue,final Shell parent) 
	{
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.initValue = initValue;
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
		filterText = new Text(area, SWT.SINGLE | SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		filterText.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});

		final Label matches = new Label(area, SWT.NONE);
		matches.setText("&Gefundene Patienten:"); 
		
		viewer = new TableViewer(area, SWT.SINGLE | SWT.BORDER);
		final Control control = this.viewer.getControl();final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(gd);
		gd.widthHint = 400;
		gd.heightHint = 200;
        
		viewer.setLabelProvider(new SickPersonLabelProvider());
		viewer.setContentProvider(new SickPersonContentProvider());
		viewer.setInput(ModelFactory.getInstance().getSickPersonManager().getSickPersons());
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
        
        //setup the initial value
        filterText.setText(initValue);
        
		return area;
	}

	@Override
	protected void computeResult() 
	{
		setResult(((IStructuredSelection) viewer.getSelection()).toList());
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
	
	//PRIVATE METHODS
	/**
	 * Called when the input text of a filter is changes
	 */
	private void inputChanged()
	{
		if(filterJob == null)
			filterJob = new FilterPatientJob(viewer);
		
		//check the state
		if(filterJob.getState() == Job.RUNNING)
		{
			System.out.println("Job is currently running");
			return;
		}
		
		//pass the entered text
		filterJob.setSearchString(filterText.getText().toLowerCase());
		filterJob.schedule(FilterAddressJob.INTERVAL_KEY_PRESSED);
	}
}

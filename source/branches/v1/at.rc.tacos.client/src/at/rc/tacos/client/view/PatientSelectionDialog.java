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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
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
		message.setText("&Bitte w‰hlen Sie einen Patienten aus:\n");
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
		
		Table table = new Table(area, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(table);
		final Control control = this.viewer.getControl();final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		control.setLayoutData(gd);
		gd.widthHint = 400;
		gd.heightHint = 200;
        
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.setLabelProvider(new SickPersonLabelProvider());
		viewer.setContentProvider(new SickPersonContentProvider());
		viewer.setInput(ModelFactory.getInstance().getSickPersonManager().getSickPersons());
        viewer.addSelectionChangedListener(new ISelectionChangedListener() 
        {
            public void selectionChanged(final SelectionChangedEvent event) 
            {
                if (!event.getSelection().isEmpty()) 
                	updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID, ((SickPerson)((IStructuredSelection) event.getSelection()).getFirstElement()).getLastName() + " ausgew‰hlt"));
                else 
                    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte w‰hlen Sie einen Patienten aus"));      
            }
        });
        
        //create the columns
		final TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setToolTipText("Nachname");
		nameColumn.setWidth(80);
		nameColumn.setText("Nachname");

		final TableColumn firstNameColumn = new TableColumn(table, SWT.NONE);
		firstNameColumn.setToolTipText("Vorname");
		firstNameColumn.setWidth(80);
		firstNameColumn.setText("Vorname");
		
		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Straﬂe");
		streetColumn.setWidth(100);
		streetColumn.setText("Straﬂe");
		
		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Stadt");
		cityColumn.setWidth(70);
		cityColumn.setText("Stadt");

		final TableColumn svnrColumn = new TableColumn(table, SWT.NONE);
		svnrColumn.setToolTipText("Sozialversicherungsnummer");
		svnrColumn.setWidth(60);
		svnrColumn.setText("SVNR");
		
		final TableColumn notesColumn = new TableColumn(table, SWT.NONE);
		notesColumn.setToolTipText("Notizen");
		notesColumn.setWidth(80);
		notesColumn.setText("Notizen");

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
                updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID,((SickPerson)first).getLastName() + " ausgew‰hlt")); 
			}
			else 
			    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte w‰hlen Sie einen Patienten aus"));
		}
	}
	
	//PRIVATE METHODS
	/**
	 * Called when the input text of a filter is changes
	 */
	private void inputChanged()
	{
		//get the entered text
		String filterValue = filterText.getText().toLowerCase();
		if(filterValue.trim().length() < 1)
		{
			updateStatus(new Status(Status.WARNING,Activator.PLUGIN_ID,"Bitte geben sie mindestens ein Zeichen des Nachnamens ein"));
			return;
		}
		updateStatus(new Status(IStatus.INFO,Activator.PLUGIN_ID,"Bitte w‰hlen Sie einen Patienten aus"));
		
		if(filterJob == null)
			filterJob = new FilterPatientJob(viewer);
		
		//check the state
		if(filterJob.getState() == Job.RUNNING)
		{
			return;
		}
		
		//pass the entered text
		filterJob.setSearchString(filterValue);
		filterJob.schedule(FilterAddressJob.INTERVAL_KEY_PRESSED);
	}
}

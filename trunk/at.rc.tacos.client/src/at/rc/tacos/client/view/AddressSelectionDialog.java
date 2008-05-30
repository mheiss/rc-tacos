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
import org.eclipse.swt.layout.GridLayout;
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
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.AddressContentProvider;
import at.rc.tacos.client.providers.AddressLabelProvider;
import at.rc.tacos.model.Address;

/**
 * Provides a selection dialog to choose a address
 */
public class AddressSelectionDialog extends SelectionStatusDialog implements PropertyChangeListener
{
	private TableViewer viewer;
	private String initStreetValue,initCityValue;
	private Text filterStreet,filterCity;
	
	/**
	 * The scheduler job to start the filter
	 */
	private FilterAddressJob filterJob;

	/**
	 * Defaul class constructor to set up a new address select dialog
	 * @param parent
	 */
	public AddressSelectionDialog(String initStreetValue,String initCityValue,final Shell parent) 
	{
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.initStreetValue = initStreetValue;
		this.initCityValue = initCityValue;
		ModelFactory.getInstance().getAddressManager().addPropertyChangeListener(this);
	}
	
	@Override
	public boolean close() 
	{
		//cleanup the listeners
		ModelFactory.getInstance().getAddressManager().removePropertyChangeListener(this);
		//colse the dialog
		return super.close();
	}

	@Override
    protected void configureShell(final Shell shell)
    {
        shell.setText("Adresse Suche"); 
        super.configureShell(shell);
    }

	@Override
	protected Control createDialogArea(final Composite parent) 
	{
		final Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(2,false));
		area.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Label message = new Label(area, SWT.NONE);
		message.setText("&Bitte w‰hlen Sie eine Adresse aus:\n");
		
		final Label labelStreet = new Label(area,SWT.BOLD);
		labelStreet.setText("Straﬂe");
		filterStreet = new Text(area, SWT.SINGLE | SWT.BORDER);
		filterStreet.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
		
		final Label labelCity = new Label(area,SWT.BOLD);
		labelCity.setText("Stadt");
		filterCity = new Text(area, SWT.SINGLE | SWT.BORDER);
		filterCity.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));

		final Label matches = new Label(area, SWT.NONE);
		matches.setText("&Gefundene Adressen:"); 
		
		viewer = new TableViewer(area, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		final Control control = this.viewer.getControl();
		final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = 400;
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		control.setLayoutData(gd);
        
		viewer.setLabelProvider(new AddressLabelProvider());
		viewer.setContentProvider(new AddressContentProvider());
		viewer.setUseHashlookup(true);
		viewer.setInput(ModelFactory.getInstance().getAddressManager().getAddressList());
		Table table = viewer.getTable();
		
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		
		//create the columns
		final TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.setToolTipText("");
		imageColumn.setWidth(30);
		imageColumn.setText("");

		final TableColumn zipColumn = new TableColumn(table, SWT.NONE);
		zipColumn.setToolTipText("Gemeindekennzeichen");
		zipColumn.setWidth(60);
		zipColumn.setText("GKZ");

		final TableColumn cityColumn = new TableColumn(table, SWT.NONE);
		cityColumn.setToolTipText("Name der Stadt");
		cityColumn.setWidth(180);
		cityColumn.setText("Stadt");

		final TableColumn streetColumn = new TableColumn(table, SWT.NONE);
		streetColumn.setToolTipText("Name der Straﬂe");
		streetColumn.setWidth(180);
		streetColumn.setText("Straﬂe");
		
		filterStreet.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});
		filterCity.addModifyListener(new ModifyListener() 
		{
			public void modifyText(final ModifyEvent e) 
			{
				inputChanged();
			}
		});
		
        viewer.addSelectionChangedListener(new ISelectionChangedListener() 
        {
            public void selectionChanged(final SelectionChangedEvent event) 
            {
                if (!event.getSelection().isEmpty()) 
                	updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID, ((Address)((IStructuredSelection) event.getSelection()).getFirstElement()).toString() + " ausgew‰hlt"));
                else 
                    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte w‰hlen Sie eine Adresse aus"));      
            }
        });
        
        setStatusLineAboveButtons(true);
        
        //layout the components
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        message.setLayoutData(data);
        //the "matches" label
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.horizontalSpan = 2;
        matches.setLayoutData(data);
        //the input fields
        data = new GridData(GridData.FILL_HORIZONTAL);
        filterStreet.setLayoutData(data);
        data = new GridData(GridData.FILL_HORIZONTAL);
        filterCity.setLayoutData(data);
        //the labels
        data = new GridData();
        data.widthHint = 50;
        labelStreet.setLayoutData(data);
        data = new GridData();
        data.widthHint = 50;
        labelCity.setLayoutData(data);
        
        //setup the initial value
        filterCity.setText(initCityValue);
        filterStreet.setText(initStreetValue);
        
        viewer.refresh();
        
		return area;
	}

	@Override
	protected void computeResult() 
	{
		setResult(((IStructuredSelection) viewer.getSelection()).toList());
	}
	
	/**
	 * This listener will be informed when the server sends new address recoreds based on the entered text
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		String event = evt.getPropertyName();
		if("ADDRESS_ADD".equalsIgnoreCase(event) ||
				"ADDRESS_REMOVE".equalsIgnoreCase(event) ||
				"ADDRESS_UPDATE".equalsIgnoreCase(event) ||
				"ADDRESS_CLEARED".equalsIgnoreCase(event) ||
				"ADDRESS_ADD_ALL".equalsIgnoreCase(event))
		{
			viewer.refresh(true);
			final Object first = viewer.getElementAt(0);
			if (first != null) 
			{
				AddressSelectionDialog.this.viewer.setSelection(new StructuredSelection(first));
                updateStatus(new Status(IStatus.INFO, Activator.PLUGIN_ID,((Address)first).toString() + " ausgew‰hlt")); 
			}
			else 
			    updateStatus(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Bitte w‰hlen Sie eine Adresse aus."));
		}
	}
	
	//PRIVATE METHODS
	/**
	 * Called when the input text of a filter is changes
	 */
	private void inputChanged()
	{
		if(filterJob == null)
			filterJob = new FilterAddressJob(viewer);
		
		//check the state
		if(filterJob.getState() == Job.RUNNING)
		{
			System.out.println("Job is currently running");
			return;
		}
		
		//pass the entered text
		filterJob.setStrCity(filterCity.getText().toLowerCase());
		filterJob.setStrStreet(filterStreet.getText().toLowerCase());
		filterJob.setStrZip("");
		filterJob.schedule(FilterAddressJob.INTERVAL_KEY_PRESSED);
	}
}

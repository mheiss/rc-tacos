package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.DispositionViewDispContentProvider;
import at.rc.tacos.client.providers.DispositionViewDispLabelProvider;
import at.rc.tacos.client.providers.DispositionViewOffContentProvider;
import at.rc.tacos.client.providers.DispositionViewOffLabelProvider;
import at.rc.tacos.client.util.CustomColors;

/**
 * Main view, provides an overview about the transports
 * @author b.thek
 */

public class DispositionView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.view.disposition_view";
	
	private FormToolkit toolkit;
	private ScrolledForm formDisp;
	private ScrolledForm formOff;
	private TableViewer viewerDispTrans;
	private TableViewer viewerOffTrans;



	public DispositionView()
	{
		//add listener to model to keep on track
		ModelFactory.getInstance().getRosterManager().addPropertyChangeListener(this);
	}
	/**
	 * Call back method to create the control and initialize them
	 * Create contents of the window
	 */
	public void createPartControl(final Composite parent) 
	{
		
		//Create the scrolled parent component
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		formDisp = toolkit.createScrolledForm(parent);
		formDisp.setText("Transportübersicht");
		toolkit.decorateFormHeading(formDisp.getForm());
		formDisp.getBody().setLayout(new FillLayout());
		
		final Composite composite = formDisp.getBody();
		

		final SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
//		dispositionTabItem.setControl(sashForm);
		sashForm.setLayout(new FillLayout());

		final Group disponierteTransporteGroup = new Group(sashForm, SWT.NONE);
		disponierteTransporteGroup.setLayout(new FillLayout());
		disponierteTransporteGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		disponierteTransporteGroup.setText("Disponierte Transporte");

		/** tabFolder Selection Listener not needed? */
		viewerDispTrans = new TableViewer(disponierteTransporteGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerDispTrans.setContentProvider(new DispositionViewDispContentProvider());
		viewerDispTrans.setLabelProvider(new DispositionViewDispLabelProvider());
		viewerDispTrans.setInput(ModelFactory.getInstance().getTransportManager());
		viewerDispTrans.getTable().setLinesVisible(true);
		
		/** Tooltip */
		
		/** Sorter */
		
		
		
		final Table tableDisp = viewerDispTrans.getTable();
		tableDisp.setLinesVisible(true);
		tableDisp.setHeaderVisible(true);
		
	
		final TableColumn lockColumn = new TableColumn(tableDisp, SWT.NONE);
		lockColumn.setToolTipText("Eintrag wird gerade bearbeitet");
		lockColumn.setWidth(30);
		lockColumn.setText("L");
		
		
//		table.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		

		final TableColumn columnPrioritaetDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnPrioritaetDisponierteTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		columnPrioritaetDisponierteTransporte.setWidth(36);
		columnPrioritaetDisponierteTransporte.setText("Pr");

		final TableColumn columnTransportNummerDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportNummerDisponierteTransporte.setToolTipText("Ortsstellenabhängige Transportnummer");
		columnTransportNummerDisponierteTransporte.setMoveable(true);
		columnTransportNummerDisponierteTransporte.setWidth(54);
		columnTransportNummerDisponierteTransporte.setText("TNr");

		final TableColumn columnTerminDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTerminDisponierteTransporte.setToolTipText("Termin am Zielort");
		columnTerminDisponierteTransporte.setMoveable(true);
		columnTerminDisponierteTransporte.setWidth(48);
		columnTerminDisponierteTransporte.setText("Termin");

		final TableColumn columnTransportVonDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportVonDisponierteTransporte.setMoveable(true);
		columnTransportVonDisponierteTransporte.setWidth(118);
		columnTransportVonDisponierteTransporte.setText("Transport von");

		final TableColumn columnPatientDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnPatientDisponierteTransporte.setMoveable(true);
		columnPatientDisponierteTransporte.setWidth(100);
		columnPatientDisponierteTransporte.setText("Patient");

		final TableColumn columnTransportNachDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTransportNachDisponierteTransporte.setWidth(100);
		columnTransportNachDisponierteTransporte.setText("Transport nach");

		final TableColumn columnAEDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnAEDisponierteTransporte.setToolTipText("Auftrag erteilt");
		columnAEDisponierteTransporte.setWidth(39);
		columnAEDisponierteTransporte.setText("AE");

		final TableColumn columnS1DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS1DisponierteTransporte.setToolTipText("Transportbeginn");
		columnS1DisponierteTransporte.setWidth(36);
		columnS1DisponierteTransporte.setText("S1");

		final TableColumn columnS2DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS2DisponierteTransporte.setToolTipText("Ankunft bei Patient");
		columnS2DisponierteTransporte.setWidth(38);
		columnS2DisponierteTransporte.setText("S2");

		final TableColumn columnS3DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS3DisponierteTransporte.setToolTipText("Abfahrt mit Patient");
		columnS3DisponierteTransporte.setWidth(40);
		columnS3DisponierteTransporte.setText("S3");

		final TableColumn columnS4DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS4DisponierteTransporte.setToolTipText("Ankunft Ziel");
		columnS4DisponierteTransporte.setWidth(36);
		columnS4DisponierteTransporte.setText("S4");

		final TableColumn columnS7DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS7DisponierteTransporte.setToolTipText("Einsatzgebiet verlassen");
		columnS7DisponierteTransporte.setWidth(40);
		columnS7DisponierteTransporte.setText("S7");

		final TableColumn columnS8DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS8DisponierteTransporte.setToolTipText("Zurück im Einsatzgebiet");
		columnS8DisponierteTransporte.setWidth(34);
		columnS8DisponierteTransporte.setText("S8");

		final TableColumn columnS9DisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnS9DisponierteTransporte.setToolTipText("Sonderstatus");
		columnS9DisponierteTransporte.setWidth(34);
		columnS9DisponierteTransporte.setText("S9");

		final TableColumn columnFzgDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnFzgDisponierteTransporte.setToolTipText("Fahrzeugkennzeichnung");
		columnFzgDisponierteTransporte.setWidth(52);
		columnFzgDisponierteTransporte.setText("Fzg");

		final TableColumn columnTDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnTDisponierteTransporte.setToolTipText("Transportart");
		columnTDisponierteTransporte.setWidth(53);
		columnTDisponierteTransporte.setText("T");
		

		final TableColumn columnErkrankungVerletzungDisponierteTransporte = new TableColumn(tableDisp, SWT.NONE);
		columnErkrankungVerletzungDisponierteTransporte.setWidth(146);
		columnErkrankungVerletzungDisponierteTransporte.setText("Erkrankung/Verletzung");
		
		/** make columns sort able*/

		
		/**
		 * area of 'offene Transporte'
		 */
		final Group offeneTransporteGroup = new Group(sashForm, SWT.NO_RADIO_GROUP | SWT.BORDER);
		
		
		offeneTransporteGroup.setLayout(new FillLayout());
		offeneTransporteGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		offeneTransporteGroup.setText("Offene Transporte");

		
		/** tabFolder Selection Listener not needed? */
		viewerOffTrans = new TableViewer(offeneTransporteGroup, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL|SWT.FULL_SELECTION);
		viewerOffTrans.setContentProvider(new DispositionViewOffContentProvider());
		viewerOffTrans.setLabelProvider(new DispositionViewOffLabelProvider());
		viewerOffTrans.setInput(ModelFactory.getInstance().getTransportManager());
		viewerOffTrans.getTable().setLinesVisible(true);
		
		/** tool tip*/
		
		/** sorter*/
		
		
		
		final Table tableOff = viewerOffTrans.getTable();
		tableOff.setRedraw(true);
		tableOff.setLinesVisible(true);
		tableOff.setHeaderVisible(true);

		final TableColumn newColumnTableColumnIdOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnIdOffeneTransporte.setWidth(29);
		newColumnTableColumnIdOffeneTransporte.setText("Id");

		final TableColumn newColumnTableColumnPrioritaetOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnPrioritaetOffeneTransporte.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		newColumnTableColumnPrioritaetOffeneTransporte.setWidth(31);
		newColumnTableColumnPrioritaetOffeneTransporte.setText("Pr");

		final TableColumn newColumnTableColumnOSOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnOSOffeneTransporte.setToolTipText("Zuständige Ortsstelle");
		newColumnTableColumnOSOffeneTransporte.setWidth(49);
		newColumnTableColumnOSOffeneTransporte.setText("OS");

		final TableColumn newColumnTableColumnAbfOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAbfOffeneTransporte.setToolTipText("Abfahrt auf der Dienststelle");
		newColumnTableColumnAbfOffeneTransporte.setWidth(54);
		newColumnTableColumnAbfOffeneTransporte.setText("Abf");

		final TableColumn newColumnTableColumnAnkOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAnkOffeneTransporte.setToolTipText("Ankunft beim Patienten");
		newColumnTableColumnAnkOffeneTransporte.setWidth(53);
		newColumnTableColumnAnkOffeneTransporte.setText("Ank.");

		final TableColumn newColumnTableColumnTerminOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTerminOffeneTransporte.setToolTipText("Terminzeit am Zielort");
		newColumnTableColumnTerminOffeneTransporte.setWidth(57);
		newColumnTableColumnTerminOffeneTransporte.setText("Termin");

		final TableColumn newColumnTableColumnTransportVonOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTransportVonOffeneTransporte.setWidth(102);
		newColumnTableColumnTransportVonOffeneTransporte.setText("Transport von");

		final TableColumn newColumnTableColumnPatientOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnPatientOffeneTransporte.setWidth(100);
		newColumnTableColumnPatientOffeneTransporte.setText("Patient");

		final TableColumn newColumnTableColumnTransportNachOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTransportNachOffeneTransporte.setWidth(100);
		newColumnTableColumnTransportNachOffeneTransporte.setText("Transport nach");

		final TableColumn newColumnTableColumnAufgOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAufgOffeneTransporte.setToolTipText("Zeit zu der der Transport aufgenommen wurde");
		newColumnTableColumnAufgOffeneTransporte.setWidth(100);
		newColumnTableColumnAufgOffeneTransporte.setText("Aufg");

		final TableColumn newColumnTableColumnTOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnTOffeneTransporte.setToolTipText("Transportart");
		newColumnTableColumnTOffeneTransporte.setWidth(100);
		newColumnTableColumnTOffeneTransporte.setText("T");

		final TableColumn newColumnTableColumnErkrankungVerletzungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setWidth(74);
		newColumnTableColumnErkrankungVerletzungOffeneTransporte.setText("Erkrankung/Verletzung");

		final TableColumn newColumnTableColumnAnmerkungOffeneTransporte = new TableColumn(tableOff, SWT.NONE);
		newColumnTableColumnAnmerkungOffeneTransporte.setWidth(71);
		newColumnTableColumnAnmerkungOffeneTransporte.setText("Anmerkung");


		/** make the columns sort able*/

		//TODO --> for the personal view
		//group filter
		
		
		//
	}
	
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()  { }

	public void propertyChange(PropertyChangeEvent evt) 
	{
		// the viewer represents simple model. refresh should be enough.
		if ("TRANSPORT_ADD".equals(evt.getPropertyName())) 
		{ 
			this.viewerDispTrans.refresh();
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_REMOVE".equals(evt.getPropertyName())) 
		{ 
			this.viewerDispTrans.refresh();
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_UPDATE".equals(evt.getPropertyName())) 
		{ 
			this.viewerDispTrans.refresh();
			this.viewerOffTrans.refresh();
		}
		// event on deletion --> also just refresh
		if ("TRANSPORT_CLEARED".equals(evt.getPropertyName())) 
		{ 
			this.viewerDispTrans.refresh();
			this.viewerOffTrans.refresh();
		}
	}
}

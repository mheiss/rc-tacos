package at.rc.tacos.client.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.grouplayout.LayoutStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import at.rc.tacos.swtdesigner.SWTResourceManager;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;
//client
import at.rc.tacos.client.controller.CreateItemAction;

/**
 * GUI to get an overview about the resources (vehicles and staff)
 * @author b.thek
 */


public class RessourcesView extends ViewPart
{

	public static final String ID = "at.rc.tacos.client.view.ressources_view";
	//protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			RessourcesView window = new RessourcesView();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Open the window
	 */
//	public void open() {
//		final Display display = Display.getDefault();
//		createContents();
//		shell.open();
//		shell.layout();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//	}

	/**
	 * Create contents of the window
	 */
	public void createPartControl(Composite parent) {
		//shell = new Shell();
		//shell.setRegion(null);
		this.setTitle("der testtitel");//---------------------------------------------
		parent.setLayout(new FillLayout());
		//parent.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Tacos_LOGO.jpg"));
		//shell.setSize(1500, 1000);
		//shell.setText("Ressourcen");

		//final SashForm sashForm = new SashForm(parent, SWT.NONE);

		final Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(255, 255, 255));

		Group group_1;
		group_1 = new Group(composite_1, SWT.NONE);
		group_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_1 = new RowLayout();
		rowLayout_1.marginRight = 5;
		rowLayout_1.spacing = 10;
		group_1.setLayout(rowLayout_1);
		group_1.setText("Bruck an der Mur");

		final Composite compositeACar_4_1_6 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_6 = new RowData();
		rd_compositeACar_4_1_6.width = 136;
		rd_compositeACar_4_1_6.height = 61;
		compositeACar_4_1_6.setLayoutData(rd_compositeACar_4_1_6);
		compositeACar_4_1_6.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_6 = new Composite(compositeACar_4_1_6, SWT.NONE);
		composite_6_9_1_6.setLayout(new FillLayout());

		final Label bm02Label_3_1_6 = new Label(composite_6_9_1_6, SWT.NONE);
		bm02Label_3_1_6.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_6.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_6.setText("Bm02");

		final Composite composite_9_4_1_6 = new Composite(composite_6_9_1_6, SWT.NONE);
		composite_9_4_1_6.setLayout(new FormLayout());
		composite_9_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_6 = new Label(composite_9_4_1_6, SWT.CENTER);
		final FormData fd_label_3_4_1_6 = new FormData();
		fd_label_3_4_1_6.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_6.top = new FormAttachment(0, 0);
		fd_label_3_4_1_6.right = new FormAttachment(0, 68);
		fd_label_3_4_1_6.left = new FormAttachment(0, 15);
		bktwLabel_3_1_6.setLayoutData(fd_label_3_4_1_6);
		bktwLabel_3_1_6.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_6.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_6.setText("RTW");

		final Composite composite_5_9_1_6 = new Composite(compositeACar_4_1_6, SWT.NONE);
		composite_5_9_1_6.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_6 = new Composite(composite_5_9_1_6, SWT.NONE);
		composite_8_4_1_6.setLayout(new FillLayout());

		final Label label_5_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_5_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_8_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_9_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_7_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_6_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_6 = new Label(composite_8_4_1_6, SWT.NONE);
		label_4_4_1_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_6 = new Composite(composite_5_9_1_6, SWT.NONE);
		composite_7_4_1_6.setLayout(new FillLayout());

		final Label label_11_4_1_6 = new Label(composite_7_4_1_6, SWT.NONE);
		label_11_4_1_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_6.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_6 = new Label(composite_7_4_1_6, SWT.NONE);
		wlohmLabel_3_1_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_6.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_6 = new Label(composite_7_4_1_6, SWT.NONE);
		bthekLabel_3_1_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_6.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_6.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_1_7 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_7 = new RowData();
		rd_compositeACar_4_1_7.width = 136;
		rd_compositeACar_4_1_7.height = 61;
		compositeACar_4_1_7.setLayoutData(rd_compositeACar_4_1_7);
		compositeACar_4_1_7.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_7 = new Composite(compositeACar_4_1_7, SWT.NONE);
		composite_6_9_1_7.setLayout(new FillLayout());

		final Label bm02Label_3_1_7 = new Label(composite_6_9_1_7, SWT.NONE);
		bm02Label_3_1_7.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_7.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_7.setText("Bm03");

		final Composite composite_9_4_1_7 = new Composite(composite_6_9_1_7, SWT.NONE);
		composite_9_4_1_7.setLayout(new FormLayout());
		composite_9_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_7 = new Label(composite_9_4_1_7, SWT.CENTER);
		final FormData fd_label_3_4_1_7 = new FormData();
		fd_label_3_4_1_7.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_7.top = new FormAttachment(0, 0);
		fd_label_3_4_1_7.right = new FormAttachment(0, 68);
		fd_label_3_4_1_7.left = new FormAttachment(0, 15);
		bktwLabel_3_1_7.setLayoutData(fd_label_3_4_1_7);
		bktwLabel_3_1_7.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_7.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_7.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_7.setText("RTW");

		final Composite composite_5_9_1_7 = new Composite(compositeACar_4_1_7, SWT.NONE);
		composite_5_9_1_7.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_7 = new Composite(composite_5_9_1_7, SWT.NONE);
		composite_8_4_1_7.setLayout(new FillLayout());

		final Label label_5_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_5_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_8_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_9_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_7_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_6_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_7 = new Label(composite_8_4_1_7, SWT.NONE);
		label_4_4_1_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_7 = new Composite(composite_5_9_1_7, SWT.NONE);
		composite_7_4_1_7.setLayout(new FillLayout());

		final Label label_11_4_1_7 = new Label(composite_7_4_1_7, SWT.NONE);
		label_11_4_1_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_7.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_7.setText("m.heiß");

		final Label wlohmLabel_3_1_7 = new Label(composite_7_4_1_7, SWT.NONE);
		wlohmLabel_3_1_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_7.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_7.setText("w.lohm");

		final Label bthekLabel_3_1_7 = new Label(composite_7_4_1_7, SWT.NONE);
		bthekLabel_3_1_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_7.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_7.setText("b.thek");

		final Composite compositeACar_4_1_8 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_8 = new RowData();
		rd_compositeACar_4_1_8.width = 136;
		rd_compositeACar_4_1_8.height = 61;
		compositeACar_4_1_8.setLayoutData(rd_compositeACar_4_1_8);
		compositeACar_4_1_8.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_8 = new Composite(compositeACar_4_1_8, SWT.NONE);
		composite_6_9_1_8.setLayout(new FillLayout());

		final Label bm02Label_3_1_8 = new Label(composite_6_9_1_8, SWT.NONE);
		bm02Label_3_1_8.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_8.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_8.setText("Bm04");

		final Composite composite_9_4_1_8 = new Composite(composite_6_9_1_8, SWT.NONE);
		composite_9_4_1_8.setLayout(new FormLayout());
		composite_9_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_8 = new Label(composite_9_4_1_8, SWT.CENTER);
		final FormData fd_label_3_4_1_8 = new FormData();
		fd_label_3_4_1_8.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_8.top = new FormAttachment(0, 0);
		fd_label_3_4_1_8.right = new FormAttachment(0, 68);
		fd_label_3_4_1_8.left = new FormAttachment(0, 15);
		bktwLabel_3_1_8.setLayoutData(fd_label_3_4_1_8);
		bktwLabel_3_1_8.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_8.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_8.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_8.setText("KTW");

		final Composite composite_5_9_1_8 = new Composite(compositeACar_4_1_8, SWT.NONE);
		composite_5_9_1_8.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_8 = new Composite(composite_5_9_1_8, SWT.NONE);
		composite_8_4_1_8.setLayout(new FillLayout());

		final Label label_5_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_5_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_8_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_9_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_7_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_6_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_8 = new Label(composite_8_4_1_8, SWT.NONE);
		label_4_4_1_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_rot.gif"));
		label_4_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_8 = new Composite(composite_5_9_1_8, SWT.NONE);
		composite_7_4_1_8.setLayout(new FillLayout());

		final Label label_11_4_1_8 = new Label(composite_7_4_1_8, SWT.NONE);
		label_11_4_1_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_8.setText("m.heiß");

		final Label wlohmLabel_3_1_8 = new Label(composite_7_4_1_8, SWT.NONE);
		wlohmLabel_3_1_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_8.setText("w.lohm");

		final Label bthekLabel_3_1_8 = new Label(composite_7_4_1_8, SWT.NONE);
		bthekLabel_3_1_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_8.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_8.setText("b.thek");

		final Composite compositeACar_4_1_9 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_9 = new RowData();
		rd_compositeACar_4_1_9.width = 136;
		rd_compositeACar_4_1_9.height = 61;
		compositeACar_4_1_9.setLayoutData(rd_compositeACar_4_1_9);
		compositeACar_4_1_9.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_9 = new Composite(compositeACar_4_1_9, SWT.NONE);
		composite_6_9_1_9.setLayout(new FillLayout());

		final Label bm02Label_3_1_9 = new Label(composite_6_9_1_9, SWT.NONE);
		bm02Label_3_1_9.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_9.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_9.setText("Bm05");

		final Composite composite_9_4_1_9 = new Composite(composite_6_9_1_9, SWT.NONE);
		composite_9_4_1_9.setLayout(new FormLayout());
		composite_9_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_9 = new Label(composite_9_4_1_9, SWT.CENTER);
		final FormData fd_label_3_4_1_9 = new FormData();
		fd_label_3_4_1_9.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_9.top = new FormAttachment(0, 0);
		fd_label_3_4_1_9.right = new FormAttachment(0, 68);
		fd_label_3_4_1_9.left = new FormAttachment(0, 15);
		bktwLabel_3_1_9.setLayoutData(fd_label_3_4_1_9);
		bktwLabel_3_1_9.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_9.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_9.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_9.setText("RTW");

		final Composite composite_5_9_1_9 = new Composite(compositeACar_4_1_9, SWT.NONE);
		composite_5_9_1_9.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_9 = new Composite(composite_5_9_1_9, SWT.NONE);
		composite_8_4_1_9.setLayout(new FillLayout());

		final Label label_5_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_5_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_8_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_9_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_7_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_6_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_9 = new Label(composite_8_4_1_9, SWT.NONE);
		label_4_4_1_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_9 = new Composite(composite_5_9_1_9, SWT.NONE);
		composite_7_4_1_9.setLayout(new FillLayout());

		final Label label_11_4_1_9 = new Label(composite_7_4_1_9, SWT.NONE);
		label_11_4_1_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_9.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_9.setText("m.heiß");

		final Label wlohmLabel_3_1_9 = new Label(composite_7_4_1_9, SWT.NONE);
		wlohmLabel_3_1_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_9.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_9.setText("w.lohm");

		final Label bthekLabel_3_1_9 = new Label(composite_7_4_1_9, SWT.NONE);
		bthekLabel_3_1_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_9.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_9.setText("b.thek");

		final Composite compositeACar_4_1_10 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_10 = new RowData();
		rd_compositeACar_4_1_10.width = 136;
		rd_compositeACar_4_1_10.height = 61;
		compositeACar_4_1_10.setLayoutData(rd_compositeACar_4_1_10);
		compositeACar_4_1_10.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_10 = new Composite(compositeACar_4_1_10, SWT.NONE);
		composite_6_9_1_10.setLayout(new FillLayout());

		final Label bm02Label_3_1_10 = new Label(composite_6_9_1_10, SWT.NONE);
		bm02Label_3_1_10.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_10.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_10.setText("Bm06");

		final Composite composite_9_4_1_10 = new Composite(composite_6_9_1_10, SWT.NONE);
		composite_9_4_1_10.setLayout(new FormLayout());
		composite_9_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_10 = new Label(composite_9_4_1_10, SWT.CENTER);
		final FormData fd_label_3_4_1_10 = new FormData();
		fd_label_3_4_1_10.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_10.top = new FormAttachment(0, 0);
		fd_label_3_4_1_10.right = new FormAttachment(0, 68);
		fd_label_3_4_1_10.left = new FormAttachment(0, 15);
		bktwLabel_3_1_10.setLayoutData(fd_label_3_4_1_10);
		bktwLabel_3_1_10.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_10.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_10.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_10.setText("RTW");

		final Composite composite_5_9_1_10 = new Composite(compositeACar_4_1_10, SWT.NONE);
		composite_5_9_1_10.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_10 = new Composite(composite_5_9_1_10, SWT.NONE);
		composite_8_4_1_10.setLayout(new FillLayout());

		final Label label_5_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_5_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_8_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy2.gif"));
		label_8_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_9_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus2.gif"));
		label_9_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_7_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_6_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_10 = new Label(composite_8_4_1_10, SWT.NONE);
		label_4_4_1_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_rot.gif"));
		label_4_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_10 = new Composite(composite_5_9_1_10, SWT.NONE);
		composite_7_4_1_10.setLayout(new FillLayout());

		final Label label_11_4_1_10 = new Label(composite_7_4_1_10, SWT.NONE);
		label_11_4_1_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_10.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_10.setText("m.heiß");

		final Label wlohmLabel_3_1_10 = new Label(composite_7_4_1_10, SWT.NONE);
		wlohmLabel_3_1_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_10.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_10.setText("w.lohm");

		final Label bthekLabel_3_1_10 = new Label(composite_7_4_1_10, SWT.NONE);
		bthekLabel_3_1_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_10.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_10.setText("b.thek");

		final Composite compositeACar_4_1_11 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_11 = new RowData();
		rd_compositeACar_4_1_11.width = 136;
		rd_compositeACar_4_1_11.height = 61;
		compositeACar_4_1_11.setLayoutData(rd_compositeACar_4_1_11);
		compositeACar_4_1_11.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_11 = new Composite(compositeACar_4_1_11, SWT.NONE);
		composite_6_9_1_11.setLayout(new FillLayout());

		final Label bm02Label_3_1_11 = new Label(composite_6_9_1_11, SWT.NONE);
		bm02Label_3_1_11.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_11.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_11.setText("Bm08");

		final Composite composite_9_4_1_11 = new Composite(composite_6_9_1_11, SWT.NONE);
		composite_9_4_1_11.setLayout(new FormLayout());
		composite_9_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_11 = new Label(composite_9_4_1_11, SWT.CENTER);
		final FormData fd_label_3_4_1_11 = new FormData();
		fd_label_3_4_1_11.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_11.top = new FormAttachment(0, 0);
		fd_label_3_4_1_11.right = new FormAttachment(0, 68);
		fd_label_3_4_1_11.left = new FormAttachment(0, 15);
		bktwLabel_3_1_11.setLayoutData(fd_label_3_4_1_11);
		bktwLabel_3_1_11.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_11.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_11.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_11.setText("BKTW");

		final Composite composite_5_9_1_11 = new Composite(compositeACar_4_1_11, SWT.NONE);
		composite_5_9_1_11.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_11 = new Composite(composite_5_9_1_11, SWT.NONE);
		composite_8_4_1_11.setLayout(new FillLayout());

		final Label label_5_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_5_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_8_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_9_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_7_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_6_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_11 = new Label(composite_8_4_1_11, SWT.NONE);
		label_4_4_1_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_gelb.gif"));
		label_4_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_11 = new Composite(composite_5_9_1_11, SWT.NONE);
		composite_7_4_1_11.setLayout(new FillLayout());

		final Label label_11_4_1_11 = new Label(composite_7_4_1_11, SWT.NONE);
		label_11_4_1_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_11.setText("m.heiß");

		final Label wlohmLabel_3_1_11 = new Label(composite_7_4_1_11, SWT.NONE);
		wlohmLabel_3_1_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_11.setText("w.lohm");

		final Label bthekLabel_3_1_11 = new Label(composite_7_4_1_11, SWT.NONE);
		bthekLabel_3_1_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_11.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_11.setText("b.thek");

		final Composite compositeACar_4_1_12 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_12 = new RowData();
		rd_compositeACar_4_1_12.width = 136;
		rd_compositeACar_4_1_12.height = 61;
		compositeACar_4_1_12.setLayoutData(rd_compositeACar_4_1_12);
		compositeACar_4_1_12.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_12 = new Composite(compositeACar_4_1_12, SWT.NONE);
		composite_6_9_1_12.setLayout(new FillLayout());

		final Label bm02Label_3_1_12 = new Label(composite_6_9_1_12, SWT.NONE);
		bm02Label_3_1_12.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_12.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_12.setText("Bm09");

		final Composite composite_9_4_1_12 = new Composite(composite_6_9_1_12, SWT.NONE);
		composite_9_4_1_12.setLayout(new FormLayout());
		composite_9_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_12 = new Label(composite_9_4_1_12, SWT.CENTER);
		final FormData fd_label_3_4_1_12 = new FormData();
		fd_label_3_4_1_12.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_12.top = new FormAttachment(0, 0);
		fd_label_3_4_1_12.right = new FormAttachment(0, 68);
		fd_label_3_4_1_12.left = new FormAttachment(0, 15);
		bktwLabel_3_1_12.setLayoutData(fd_label_3_4_1_12);
		bktwLabel_3_1_12.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_12.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_12.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_12.setText("KTW");

		final Composite composite_5_9_1_12 = new Composite(compositeACar_4_1_12, SWT.NONE);
		composite_5_9_1_12.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_12 = new Composite(composite_5_9_1_12, SWT.NONE);
		composite_8_4_1_12.setLayout(new FillLayout());

		final Label label_5_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_5_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_8_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_9_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_7_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_6_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_12 = new Label(composite_8_4_1_12, SWT.NONE);
		label_4_4_1_12.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_12 = new Composite(composite_5_9_1_12, SWT.NONE);
		composite_7_4_1_12.setLayout(new FillLayout());

		final Label label_11_4_1_12 = new Label(composite_7_4_1_12, SWT.NONE);
		label_11_4_1_12.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_12.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_12.setText("m.heiß");

		final Label wlohmLabel_3_1_12 = new Label(composite_7_4_1_12, SWT.NONE);
		wlohmLabel_3_1_12.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_12.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_12.setText("w.lohm");

		final Label bthekLabel_3_1_12 = new Label(composite_7_4_1_12, SWT.NONE);
		bthekLabel_3_1_12.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_12.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_12.setText("b.thek");

		final Composite compositeACar_4_1_13 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_13 = new RowData();
		rd_compositeACar_4_1_13.width = 136;
		rd_compositeACar_4_1_13.height = 61;
		compositeACar_4_1_13.setLayoutData(rd_compositeACar_4_1_13);
		compositeACar_4_1_13.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_13 = new Composite(compositeACar_4_1_13, SWT.NONE);
		composite_6_9_1_13.setLayout(new FillLayout());

		final Label bm02Label_3_1_13 = new Label(composite_6_9_1_13, SWT.NONE);
		bm02Label_3_1_13.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_13.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_13.setText("Bm10");

		final Composite composite_9_4_1_13 = new Composite(composite_6_9_1_13, SWT.NONE);
		composite_9_4_1_13.setLayout(new FormLayout());
		composite_9_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_13 = new Label(composite_9_4_1_13, SWT.CENTER);
		final FormData fd_label_3_4_1_13 = new FormData();
		fd_label_3_4_1_13.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_13.top = new FormAttachment(0, 0);
		fd_label_3_4_1_13.right = new FormAttachment(0, 68);
		fd_label_3_4_1_13.left = new FormAttachment(0, 15);
		bktwLabel_3_1_13.setLayoutData(fd_label_3_4_1_13);
		bktwLabel_3_1_13.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_13.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_13.setText("BKTW");

		final Composite composite_5_9_1_13 = new Composite(compositeACar_4_1_13, SWT.NONE);
		composite_5_9_1_13.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_13 = new Composite(composite_5_9_1_13, SWT.NONE);
		composite_8_4_1_13.setLayout(new FillLayout());

		final Label label_5_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_5_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_8_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_9_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_7_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_6_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_13 = new Label(composite_8_4_1_13, SWT.NONE);
		label_4_4_1_13.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_13 = new Composite(composite_5_9_1_13, SWT.NONE);
		composite_7_4_1_13.setLayout(new FillLayout());

		final Label label_11_4_1_13 = new Label(composite_7_4_1_13, SWT.NONE);
		label_11_4_1_13.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_13.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_13 = new Label(composite_7_4_1_13, SWT.NONE);
		wlohmLabel_3_1_13.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_13.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_13 = new Label(composite_7_4_1_13, SWT.NONE);
		bthekLabel_3_1_13.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_13.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_13.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_1_14 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_14 = new RowData();
		rd_compositeACar_4_1_14.width = 136;
		rd_compositeACar_4_1_14.height = 61;
		compositeACar_4_1_14.setLayoutData(rd_compositeACar_4_1_14);
		compositeACar_4_1_14.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_14 = new Composite(compositeACar_4_1_14, SWT.NONE);
		composite_6_9_1_14.setLayout(new FillLayout());

		final Label bm02Label_3_1_14 = new Label(composite_6_9_1_14, SWT.NONE);
		bm02Label_3_1_14.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_14.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_14.setText("Bm14");

		final Composite composite_9_4_1_14 = new Composite(composite_6_9_1_14, SWT.NONE);
		composite_9_4_1_14.setLayout(new FormLayout());
		composite_9_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_14 = new Label(composite_9_4_1_14, SWT.CENTER);
		final FormData fd_label_3_4_1_14 = new FormData();
		fd_label_3_4_1_14.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_14.top = new FormAttachment(0, 0);
		fd_label_3_4_1_14.right = new FormAttachment(0, 68);
		fd_label_3_4_1_14.left = new FormAttachment(0, 15);
		bktwLabel_3_1_14.setLayoutData(fd_label_3_4_1_14);
		bktwLabel_3_1_14.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_14.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_14.setText("LKW");

		final Composite composite_5_9_1_14 = new Composite(compositeACar_4_1_14, SWT.NONE);
		composite_5_9_1_14.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_14 = new Composite(composite_5_9_1_14, SWT.NONE);
		composite_8_4_1_14.setLayout(new FillLayout());

		final Label label_5_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_5_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_8_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_9_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_7_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_6_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_14 = new Label(composite_8_4_1_14, SWT.NONE);
		label_4_4_1_14.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_14 = new Composite(composite_5_9_1_14, SWT.NONE);
		composite_7_4_1_14.setLayout(new FillLayout());

		final Label label_11_4_1_14 = new Label(composite_7_4_1_14, SWT.NONE);
		label_11_4_1_14.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_14.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_14 = new Label(composite_7_4_1_14, SWT.NONE);
		wlohmLabel_3_1_14.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_14.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_14 = new Label(composite_7_4_1_14, SWT.NONE);
		bthekLabel_3_1_14.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_14.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_14.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_1_15 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_15 = new RowData();
		rd_compositeACar_4_1_15.width = 136;
		rd_compositeACar_4_1_15.height = 61;
		compositeACar_4_1_15.setLayoutData(rd_compositeACar_4_1_15);
		compositeACar_4_1_15.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_15 = new Composite(compositeACar_4_1_15, SWT.NONE);
		composite_6_9_1_15.setLayout(new FillLayout());

		final Label bm02Label_3_1_15 = new Label(composite_6_9_1_15, SWT.NONE);
		bm02Label_3_1_15.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_15.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_15.setText("Bm15");

		final Composite composite_9_4_1_15 = new Composite(composite_6_9_1_15, SWT.NONE);
		composite_9_4_1_15.setLayout(new FormLayout());
		composite_9_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_15 = new Label(composite_9_4_1_15, SWT.CENTER);
		final FormData fd_label_3_4_1_15 = new FormData();
		fd_label_3_4_1_15.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_15.top = new FormAttachment(0, 0);
		fd_label_3_4_1_15.right = new FormAttachment(0, 68);
		fd_label_3_4_1_15.left = new FormAttachment(0, 15);
		bktwLabel_3_1_15.setLayoutData(fd_label_3_4_1_15);
		bktwLabel_3_1_15.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_15.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_15.setText("STW");

		final Composite composite_5_9_1_15 = new Composite(compositeACar_4_1_15, SWT.NONE);
		composite_5_9_1_15.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_15 = new Composite(composite_5_9_1_15, SWT.NONE);
		composite_8_4_1_15.setLayout(new FillLayout());

		final Label label_5_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_5_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_8_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_9_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_7_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_6_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_15 = new Label(composite_8_4_1_15, SWT.NONE);
		label_4_4_1_15.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_15 = new Composite(composite_5_9_1_15, SWT.NONE);
		composite_7_4_1_15.setLayout(new FillLayout());

		final Label label_11_4_1_15 = new Label(composite_7_4_1_15, SWT.NONE);
		label_11_4_1_15.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_15.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_15 = new Label(composite_7_4_1_15, SWT.NONE);
		wlohmLabel_3_1_15.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_15.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_15 = new Label(composite_7_4_1_15, SWT.NONE);
		bthekLabel_3_1_15.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_15.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_15.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_1_16 = new Composite(group_1, SWT.NONE);
		final RowData rd_compositeACar_4_1_16 = new RowData();
		rd_compositeACar_4_1_16.width = 136;
		rd_compositeACar_4_1_16.height = 61;
		compositeACar_4_1_16.setLayoutData(rd_compositeACar_4_1_16);
		compositeACar_4_1_16.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_16 = new Composite(compositeACar_4_1_16, SWT.NONE);
		composite_6_9_1_16.setLayout(new FillLayout());

		final Label bm02Label_3_1_16 = new Label(composite_6_9_1_16, SWT.NONE);
		bm02Label_3_1_16.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_16.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_16.setText("Bm16");

		final Composite composite_9_4_1_16 = new Composite(composite_6_9_1_16, SWT.NONE);
		composite_9_4_1_16.setLayout(new FormLayout());
		composite_9_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_16 = new Label(composite_9_4_1_16, SWT.CENTER);
		final FormData fd_label_3_4_1_16 = new FormData();
		fd_label_3_4_1_16.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_16.top = new FormAttachment(0, 0);
		fd_label_3_4_1_16.right = new FormAttachment(0, 68);
		fd_label_3_4_1_16.left = new FormAttachment(0, 15);
		bktwLabel_3_1_16.setLayoutData(fd_label_3_4_1_16);
		bktwLabel_3_1_16.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_16.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_16.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_16.setText("BKTW");

		final Composite composite_5_9_1_16 = new Composite(compositeACar_4_1_16, SWT.NONE);
		composite_5_9_1_16.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_16 = new Composite(composite_5_9_1_16, SWT.NONE);
		composite_8_4_1_16.setLayout(new FillLayout());

		final Label label_5_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_5_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_8_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_9_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_7_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_6_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_16 = new Label(composite_8_4_1_16, SWT.NONE);
		label_4_4_1_16.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_16 = new Composite(composite_5_9_1_16, SWT.NONE);
		composite_7_4_1_16.setLayout(new FillLayout());

		final Label label_11_4_1_16 = new Label(composite_7_4_1_16, SWT.NONE);
		label_11_4_1_16.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_16.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_16.setText("m.heiß");

		final Label wlohmLabel_3_1_16 = new Label(composite_7_4_1_16, SWT.NONE);
		wlohmLabel_3_1_16.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_16.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_16.setText("w.lohm");

		final Label bthekLabel_3_1_16 = new Label(composite_7_4_1_16, SWT.NONE);
		bthekLabel_3_1_16.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_16.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_16.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_16.setText("b.thek");

		Group group_2;
		group_2 = new Group(composite_1, SWT.NONE);
		group_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_2 = new RowLayout();
		rowLayout_2.spacing = 10;
		group_2.setLayout(rowLayout_2);
		group_2.setText("Kapfenberg");

		Group group_3;
		group_3 = new Group(composite_1, SWT.NONE);
		group_3.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_3 = new RowLayout();
		rowLayout_3.spacing = 10;
		group_3.setLayout(rowLayout_3);
		group_3.setText("St. Marein");

		final Composite compositeACar_4_4 = new Composite(group_3, SWT.NONE);
		final RowData rd_compositeACar_4_4 = new RowData();
		rd_compositeACar_4_4.width = 136;
		rd_compositeACar_4_4.height = 61;
		compositeACar_4_4.setLayoutData(rd_compositeACar_4_4);
		compositeACar_4_4.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_4 = new Composite(compositeACar_4_4, SWT.NONE);
		composite_6_9_4.setLayout(new FillLayout());

		final Label bm02Label_3_4 = new Label(composite_6_9_4, SWT.NONE);
		bm02Label_3_4.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_4.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_4.setText("Ma14");

		final Composite composite_9_4_4 = new Composite(composite_6_9_4, SWT.NONE);
		composite_9_4_4.setLayout(new FormLayout());
		composite_9_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_4 = new Label(composite_9_4_4, SWT.CENTER);
		final FormData fd_label_3_4_4 = new FormData();
		fd_label_3_4_4.bottom = new FormAttachment(0, 15);
		fd_label_3_4_4.top = new FormAttachment(0, 0);
		fd_label_3_4_4.right = new FormAttachment(0, 68);
		fd_label_3_4_4.left = new FormAttachment(0, 15);
		bktwLabel_3_4.setLayoutData(fd_label_3_4_4);
		bktwLabel_3_4.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_4.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_4.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_4.setText("RTW");

		final Composite composite_5_9_4 = new Composite(compositeACar_4_4, SWT.NONE);
		composite_5_9_4.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_4 = new Composite(composite_5_9_4, SWT.NONE);
		composite_8_4_4.setLayout(new FillLayout());

		final Label label_5_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_5_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_8_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_9_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_7_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_6_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_4 = new Label(composite_8_4_4, SWT.NONE);
		label_4_4_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_4 = new Composite(composite_5_9_4, SWT.NONE);
		composite_7_4_4.setLayout(new FillLayout());

		final Label label_11_4_4 = new Label(composite_7_4_4, SWT.NONE);
		label_11_4_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_4.setText("m.heiß");

		final Label wlohmLabel_3_4 = new Label(composite_7_4_4, SWT.NONE);
		wlohmLabel_3_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_4.setText("w.lohm");

		final Label bthekLabel_3_4 = new Label(composite_7_4_4, SWT.NONE);
		bthekLabel_3_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_4.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_4.setText("b.thek");

		final Composite compositeACar_4_5 = new Composite(group_3, SWT.NONE);
		final RowData rd_compositeACar_4_5 = new RowData();
		rd_compositeACar_4_5.width = 136;
		rd_compositeACar_4_5.height = 61;
		compositeACar_4_5.setLayoutData(rd_compositeACar_4_5);
		compositeACar_4_5.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_5 = new Composite(compositeACar_4_5, SWT.NONE);
		composite_6_9_5.setLayout(new FillLayout());

		final Label bm02Label_3_5 = new Label(composite_6_9_5, SWT.NONE);
		bm02Label_3_5.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_5.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_5.setText("Ma15");

		final Composite composite_9_4_5 = new Composite(composite_6_9_5, SWT.NONE);
		composite_9_4_5.setLayout(new FormLayout());
		composite_9_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_5 = new Label(composite_9_4_5, SWT.CENTER);
		final FormData fd_label_3_4_5 = new FormData();
		fd_label_3_4_5.bottom = new FormAttachment(0, 15);
		fd_label_3_4_5.top = new FormAttachment(0, 0);
		fd_label_3_4_5.right = new FormAttachment(0, 68);
		fd_label_3_4_5.left = new FormAttachment(0, 15);
		bktwLabel_3_5.setLayoutData(fd_label_3_4_5);
		bktwLabel_3_5.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_5.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_5.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_5.setText("RTW");

		final Composite composite_5_9_5 = new Composite(compositeACar_4_5, SWT.NONE);
		composite_5_9_5.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_5 = new Composite(composite_5_9_5, SWT.NONE);
		composite_8_4_5.setLayout(new FillLayout());

		final Label label_5_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_5_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_8_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_9_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_7_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_6_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_5 = new Label(composite_8_4_5, SWT.NONE);
		label_4_4_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_gelb.gif"));
		label_4_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_5 = new Composite(composite_5_9_5, SWT.NONE);
		composite_7_4_5.setLayout(new FillLayout());

		final Label label_11_4_5 = new Label(composite_7_4_5, SWT.NONE);
		label_11_4_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_5.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_5.setText("m.heiß");

		final Label wlohmLabel_3_5 = new Label(composite_7_4_5, SWT.NONE);
		wlohmLabel_3_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_5.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_5.setText("w.lohm");

		final Label bthekLabel_3_5 = new Label(composite_7_4_5, SWT.NONE);
		bthekLabel_3_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_5.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_5.setText("b.thek");

		Group group_4;
		group_4 = new Group(composite_1, SWT.NONE);
		group_4.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_4 = new RowLayout();
		rowLayout_4.spacing = 10;
		group_4.setLayout(rowLayout_4);
		group_4.setText("Thörl");

		final Composite compositeACar_4_6 = new Composite(group_4, SWT.NONE);
		final RowData rd_compositeACar_4_6 = new RowData();
		rd_compositeACar_4_6.width = 136;
		rd_compositeACar_4_6.height = 61;
		compositeACar_4_6.setLayoutData(rd_compositeACar_4_6);
		compositeACar_4_6.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_6 = new Composite(compositeACar_4_6, SWT.NONE);
		composite_6_9_6.setLayout(new FillLayout());

		final Label bm02Label_3_6 = new Label(composite_6_9_6, SWT.NONE);
		bm02Label_3_6.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_6.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_6.setText("Th16");

		final Composite composite_9_4_6 = new Composite(composite_6_9_6, SWT.NONE);
		composite_9_4_6.setLayout(new FormLayout());
		composite_9_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_6 = new Label(composite_9_4_6, SWT.CENTER);
		final FormData fd_label_3_4_6 = new FormData();
		fd_label_3_4_6.bottom = new FormAttachment(0, 15);
		fd_label_3_4_6.top = new FormAttachment(0, 0);
		fd_label_3_4_6.right = new FormAttachment(0, 68);
		fd_label_3_4_6.left = new FormAttachment(0, 15);
		bktwLabel_3_6.setLayoutData(fd_label_3_4_6);
		bktwLabel_3_6.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_6.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_6.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_6.setText("RTW");

		final Composite composite_5_9_6 = new Composite(compositeACar_4_6, SWT.NONE);
		composite_5_9_6.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_6 = new Composite(composite_5_9_6, SWT.NONE);
		composite_8_4_6.setLayout(new FillLayout());

		final Label label_5_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_5_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_8_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_9_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_7_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_6_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_6 = new Label(composite_8_4_6, SWT.NONE);
		label_4_4_6.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_6 = new Composite(composite_5_9_6, SWT.NONE);
		composite_7_4_6.setLayout(new FillLayout());

		final Label label_11_4_6 = new Label(composite_7_4_6, SWT.NONE);
		label_11_4_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_6.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_6.setText("m.heiß");

		final Label wlohmLabel_3_6 = new Label(composite_7_4_6, SWT.NONE);
		wlohmLabel_3_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_6.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_6.setText("w.lohm");

		final Label bthekLabel_3_6 = new Label(composite_7_4_6, SWT.NONE);
		bthekLabel_3_6.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_6.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_6.setText("b.thek");

		final Composite compositeACar_4_7 = new Composite(group_4, SWT.NONE);
		final RowData rd_compositeACar_4_7 = new RowData();
		rd_compositeACar_4_7.width = 136;
		rd_compositeACar_4_7.height = 61;
		compositeACar_4_7.setLayoutData(rd_compositeACar_4_7);
		compositeACar_4_7.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_7 = new Composite(compositeACar_4_7, SWT.NONE);
		composite_6_9_7.setLayout(new FillLayout());

		final Label bm02Label_3_7 = new Label(composite_6_9_7, SWT.NONE);
		bm02Label_3_7.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_7.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_7.setText("Th17");

		final Composite composite_9_4_7 = new Composite(composite_6_9_7, SWT.NONE);
		composite_9_4_7.setLayout(new FormLayout());
		composite_9_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_7 = new Label(composite_9_4_7, SWT.CENTER);
		final FormData fd_label_3_4_7 = new FormData();
		fd_label_3_4_7.bottom = new FormAttachment(0, 15);
		fd_label_3_4_7.top = new FormAttachment(0, 0);
		fd_label_3_4_7.right = new FormAttachment(0, 68);
		fd_label_3_4_7.left = new FormAttachment(0, 15);
		bktwLabel_3_7.setLayoutData(fd_label_3_4_7);
		bktwLabel_3_7.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_7.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_7.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_7.setText("BKTW");

		final Composite composite_5_9_7 = new Composite(compositeACar_4_7, SWT.NONE);
		composite_5_9_7.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_7 = new Composite(composite_5_9_7, SWT.NONE);
		composite_8_4_7.setLayout(new FillLayout());

		final Label label_5_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_5_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_8_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_9_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_7_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_6_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_7 = new Label(composite_8_4_7, SWT.NONE);
		label_4_4_7.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_rot.gif"));
		label_4_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_7 = new Composite(composite_5_9_7, SWT.NONE);
		composite_7_4_7.setLayout(new FillLayout());

		final Label label_11_4_7 = new Label(composite_7_4_7, SWT.NONE);
		label_11_4_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_7.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_7.setText("m.heiß");

		final Label wlohmLabel_3_7 = new Label(composite_7_4_7, SWT.NONE);
		wlohmLabel_3_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_7.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_7.setText("w.lohm");

		final Label bthekLabel_3_7 = new Label(composite_7_4_7, SWT.NONE);
		bthekLabel_3_7.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_7.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_7.setText("b.thek");

		Group group_5;
		group_5 = new Group(composite_1, SWT.NONE);
		group_5.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		final RowLayout rowLayout_5 = new RowLayout();
		rowLayout_5.spacing = 10;
		group_5.setLayout(rowLayout_5);
		group_5.setText("Turnau");

		final Composite compositeACar_4_8 = new Composite(group_5, SWT.NONE);
		final RowData rd_compositeACar_4_8 = new RowData();
		rd_compositeACar_4_8.width = 136;
		rd_compositeACar_4_8.height = 61;
		compositeACar_4_8.setLayoutData(rd_compositeACar_4_8);
		compositeACar_4_8.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_8 = new Composite(compositeACar_4_8, SWT.NONE);
		composite_6_9_8.setLayout(new FillLayout());

		final Label bm02Label_3_8 = new Label(composite_6_9_8, SWT.NONE);
		bm02Label_3_8.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_8.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_8.setText("Tu18");

		final Composite composite_9_4_8 = new Composite(composite_6_9_8, SWT.NONE);
		composite_9_4_8.setLayout(new FormLayout());
		composite_9_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_8 = new Label(composite_9_4_8, SWT.CENTER);
		final FormData fd_label_3_4_8 = new FormData();
		fd_label_3_4_8.bottom = new FormAttachment(0, 15);
		fd_label_3_4_8.top = new FormAttachment(0, 0);
		fd_label_3_4_8.right = new FormAttachment(0, 68);
		fd_label_3_4_8.left = new FormAttachment(0, 15);
		bktwLabel_3_8.setLayoutData(fd_label_3_4_8);
		bktwLabel_3_8.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_8.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_8.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_8.setText("RTW");

		final Composite composite_5_9_8 = new Composite(compositeACar_4_8, SWT.NONE);
		composite_5_9_8.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_8 = new Composite(composite_5_9_8, SWT.NONE);
		composite_8_4_8.setLayout(new FillLayout());

		final Label label_5_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_5_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_8_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_9_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_7_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_6_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_8 = new Label(composite_8_4_8, SWT.NONE);
		label_4_4_8.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_8 = new Composite(composite_5_9_8, SWT.NONE);
		composite_7_4_8.setLayout(new FillLayout());

		final Label label_11_4_8 = new Label(composite_7_4_8, SWT.NONE);
		label_11_4_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_8.setText("m.heiß");

		final Label wlohmLabel_3_8 = new Label(composite_7_4_8, SWT.NONE);
		wlohmLabel_3_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_8.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_8.setText("w.lohm");

		final Label bthekLabel_3_8 = new Label(composite_7_4_8, SWT.NONE);
		bthekLabel_3_8.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_8.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_8.setText("b.thek");

		Group group_6;
		group_6 = new Group(composite_1, SWT.NONE);
		group_6.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_6 = new RowLayout();
		rowLayout_6.spacing = 10;
		group_6.setLayout(rowLayout_6);
		group_6.setText("Breitenau");

		final Composite compositeACar_4_9 = new Composite(group_6, SWT.NONE);
		final RowData rd_compositeACar_4_9 = new RowData();
		rd_compositeACar_4_9.width = 136;
		rd_compositeACar_4_9.height = 61;
		compositeACar_4_9.setLayoutData(rd_compositeACar_4_9);
		compositeACar_4_9.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_9 = new Composite(compositeACar_4_9, SWT.NONE);
		composite_6_9_9.setLayout(new FillLayout());

		final Label bm02Label_3_9 = new Label(composite_6_9_9, SWT.NONE);
		bm02Label_3_9.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_9.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_9.setText("Br07");

		final Composite composite_9_4_9 = new Composite(composite_6_9_9, SWT.NONE);
		composite_9_4_9.setLayout(new FormLayout());
		composite_9_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_9 = new Label(composite_9_4_9, SWT.CENTER);
		final FormData fd_label_3_4_9 = new FormData();
		fd_label_3_4_9.bottom = new FormAttachment(0, 15);
		fd_label_3_4_9.top = new FormAttachment(0, 0);
		fd_label_3_4_9.right = new FormAttachment(0, 68);
		fd_label_3_4_9.left = new FormAttachment(0, 15);
		bktwLabel_3_9.setLayoutData(fd_label_3_4_9);
		bktwLabel_3_9.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_9.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_9.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_9.setText("RTW");

		final Composite composite_5_9_9 = new Composite(compositeACar_4_9, SWT.NONE);
		composite_5_9_9.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_9 = new Composite(composite_5_9_9, SWT.NONE);
		composite_8_4_9.setLayout(new FillLayout());

		final Label label_5_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_5_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_8_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_9_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_7_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_6_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_9 = new Label(composite_8_4_9, SWT.NONE);
		label_4_4_9.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_9 = new Composite(composite_5_9_9, SWT.NONE);
		composite_7_4_9.setLayout(new FillLayout());

		final Label label_11_4_9 = new Label(composite_7_4_9, SWT.NONE);
		label_11_4_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_9.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_9.setText("m.heiß");

		final Label wlohmLabel_3_9 = new Label(composite_7_4_9, SWT.NONE);
		wlohmLabel_3_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_9.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_9.setText("w.lohm");

		final Label bthekLabel_3_9 = new Label(composite_7_4_9, SWT.NONE);
		bthekLabel_3_9.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_9.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_9.setText("b.thek");

		Group group_7;
		group_7 = new Group(composite_1, SWT.NONE);
		group_7.setBackground(SWTResourceManager.getColor(255, 255, 255));
		final RowLayout rowLayout_7 = new RowLayout();
		rowLayout_7.spacing = 10;
		group_7.setLayout(rowLayout_7);
		group_7.setText("Bezirk");

		final Composite compositeACar_4_10 = new Composite(group_7, SWT.NONE);
		final RowData rd_compositeACar_4_10 = new RowData();
		rd_compositeACar_4_10.width = 136;
		rd_compositeACar_4_10.height = 61;
		compositeACar_4_10.setLayoutData(rd_compositeACar_4_10);
		compositeACar_4_10.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_10 = new Composite(compositeACar_4_10, SWT.NONE);
		composite_6_9_10.setLayout(new FillLayout());

		final Label bm02Label_3_10 = new Label(composite_6_9_10, SWT.NONE);
		bm02Label_3_10.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_10.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_10.setText("NEF");

		final Composite composite_9_4_10 = new Composite(composite_6_9_10, SWT.NONE);
		composite_9_4_10.setLayout(new FormLayout());
		composite_9_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_10 = new Label(composite_9_4_10, SWT.CENTER);
		final FormData fd_label_3_4_10 = new FormData();
		fd_label_3_4_10.bottom = new FormAttachment(0, 15);
		fd_label_3_4_10.top = new FormAttachment(0, 0);
		fd_label_3_4_10.right = new FormAttachment(0, 68);
		fd_label_3_4_10.left = new FormAttachment(0, 15);
		bktwLabel_3_10.setLayoutData(fd_label_3_4_10);
		bktwLabel_3_10.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_10.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_10.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_10.setText("NEF");

		final Composite composite_5_9_10 = new Composite(compositeACar_4_10, SWT.NONE);
		composite_5_9_10.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_10 = new Composite(composite_5_9_10, SWT.NONE);
		composite_8_4_10.setLayout(new FillLayout());

		final Label label_5_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_5_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_8_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_9_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_7_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_6_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_10 = new Label(composite_8_4_10, SWT.NONE);
		label_4_4_10.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_10 = new Composite(composite_5_9_10, SWT.NONE);
		composite_7_4_10.setLayout(new FillLayout());

		final Label label_11_4_10 = new Label(composite_7_4_10, SWT.NONE);
		label_11_4_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_10.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_10.setText("m.heiß");

		final Label wlohmLabel_3_10 = new Label(composite_7_4_10, SWT.NONE);
		wlohmLabel_3_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_10.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_10.setText("w.lohm");

		final Label bthekLabel_3_10 = new Label(composite_7_4_10, SWT.NONE);
		bthekLabel_3_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_10.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_10.setText("b.thek");

		final Composite compositeACar_4_11 = new Composite(group_7, SWT.NONE);
		final RowData rd_compositeACar_4_11 = new RowData();
		rd_compositeACar_4_11.width = 136;
		rd_compositeACar_4_11.height = 61;
		compositeACar_4_11.setLayoutData(rd_compositeACar_4_11);
		compositeACar_4_11.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_11 = new Composite(compositeACar_4_11, SWT.NONE);
		composite_6_9_11.setLayout(new FillLayout());

		final Label bm02Label_3_11 = new Label(composite_6_9_11, SWT.NONE);
		bm02Label_3_11.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_11.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_11.setText("KDO");

		final Composite composite_9_4_11 = new Composite(composite_6_9_11, SWT.NONE);
		composite_9_4_11.setLayout(new FormLayout());
		composite_9_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_11 = new Label(composite_9_4_11, SWT.CENTER);
		final FormData fd_label_3_4_11 = new FormData();
		fd_label_3_4_11.bottom = new FormAttachment(0, 15);
		fd_label_3_4_11.top = new FormAttachment(0, 0);
		fd_label_3_4_11.right = new FormAttachment(0, 68);
		fd_label_3_4_11.left = new FormAttachment(0, 15);
		bktwLabel_3_11.setLayoutData(fd_label_3_4_11);
		bktwLabel_3_11.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_11.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_11.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_11.setText("KDO");

		final Composite composite_5_9_11 = new Composite(compositeACar_4_11, SWT.NONE);
		composite_5_9_11.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_11 = new Composite(composite_5_9_11, SWT.NONE);
		composite_8_4_11.setLayout(new FillLayout());

		final Label label_5_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_5_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_8_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_9_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_7_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_6_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_11 = new Label(composite_8_4_11, SWT.NONE);
		label_4_4_11.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_11 = new Composite(composite_5_9_11, SWT.NONE);
		composite_7_4_11.setLayout(new FillLayout());

		final Label label_11_4_11 = new Label(composite_7_4_11, SWT.NONE);
		label_11_4_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_11.setText("m.heiß");

		final Label wlohmLabel_3_11 = new Label(composite_7_4_11, SWT.NONE);
		wlohmLabel_3_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_11.setText("w.lohm");

		final Label bthekLabel_3_11 = new Label(composite_7_4_11, SWT.NONE);
		bthekLabel_3_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_11.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_11.setText("b.thek");

		final Composite compositeACar_4_1_1 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1_1 = new RowData();
		rd_compositeACar_4_1_1.width = 136;
		rd_compositeACar_4_1_1.height = 61;
		compositeACar_4_1_1.setLayoutData(rd_compositeACar_4_1_1);
		compositeACar_4_1_1.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_1 = new Composite(compositeACar_4_1_1, SWT.NONE);
		composite_6_9_1_1.setLayout(new FillLayout());

		final Label bm02Label_3_1_1 = new Label(composite_6_9_1_1, SWT.NONE);
		bm02Label_3_1_1.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_1.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_1.setText("Ka02");

		final Composite composite_9_4_1_1 = new Composite(composite_6_9_1_1, SWT.NONE);
		composite_9_4_1_1.setLayout(new FormLayout());
		composite_9_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_1 = new Label(composite_9_4_1_1, SWT.CENTER);
		final FormData fd_label_3_4_1_1 = new FormData();
		fd_label_3_4_1_1.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_1.top = new FormAttachment(0, 0);
		fd_label_3_4_1_1.right = new FormAttachment(0, 68);
		fd_label_3_4_1_1.left = new FormAttachment(0, 15);
		bktwLabel_3_1_1.setLayoutData(fd_label_3_4_1_1);
		bktwLabel_3_1_1.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_1.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_1.setText("BKTW");

		final Composite composite_5_9_1_1 = new Composite(compositeACar_4_1_1, SWT.NONE);
		composite_5_9_1_1.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_1 = new Composite(composite_5_9_1_1, SWT.NONE);
		composite_8_4_1_1.setLayout(new FillLayout());

		final Label label_5_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_5_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_8_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_9_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_7_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_6_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_1 = new Label(composite_8_4_1_1, SWT.NONE);
		label_4_4_1_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_rot.gif"));
		label_4_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_1 = new Composite(composite_5_9_1_1, SWT.NONE);
		composite_7_4_1_1.setLayout(new FillLayout());

		final Label label_11_4_1_1 = new Label(composite_7_4_1_1, SWT.NONE);
		label_11_4_1_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_1.setText("m.heiß");

		final Label wlohmLabel_3_1_1 = new Label(composite_7_4_1_1, SWT.NONE);
		wlohmLabel_3_1_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_1.setText("w.lohm");

		final Label bthekLabel_3_1_1 = new Label(composite_7_4_1_1, SWT.NONE);
		bthekLabel_3_1_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_1.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_1.setText("b.thek");

		final Composite compositeACar_4_1 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1 = new RowData();
		rd_compositeACar_4_1.width = 136;
		rd_compositeACar_4_1.height = 61;
		compositeACar_4_1.setLayoutData(rd_compositeACar_4_1);
		compositeACar_4_1.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1 = new Composite(compositeACar_4_1, SWT.NONE);
		composite_6_9_1.setLayout(new FillLayout());

		final Label bm02Label_3_1 = new Label(composite_6_9_1, SWT.NONE);
		bm02Label_3_1.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1.setText("Ka03");

		final Composite composite_9_4_1 = new Composite(composite_6_9_1, SWT.NONE);
		composite_9_4_1.setLayout(new FormLayout());
		composite_9_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1 = new Label(composite_9_4_1, SWT.CENTER);
		final FormData fd_label_3_4_1 = new FormData();
		fd_label_3_4_1.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1.top = new FormAttachment(0, 0);
		fd_label_3_4_1.right = new FormAttachment(0, 68);
		fd_label_3_4_1.left = new FormAttachment(0, 15);
		bktwLabel_3_1.setLayoutData(fd_label_3_4_1);
		bktwLabel_3_1.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1.setText("BKTW");

		final Composite composite_5_9_1 = new Composite(compositeACar_4_1, SWT.NONE);
		composite_5_9_1.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1 = new Composite(composite_5_9_1, SWT.NONE);
		composite_8_4_1.setLayout(new FillLayout());

		final Label label_5_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_5_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_8_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_9_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_7_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur2.gif"));
		label_7_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_6_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT2.gif"));
		label_6_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1 = new Label(composite_8_4_1, SWT.NONE);
		label_4_4_1.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1 = new Composite(composite_5_9_1, SWT.NONE);
		composite_7_4_1.setLayout(new FillLayout());

		final Label label_11_4_1 = new Label(composite_7_4_1, SWT.NONE);
		label_11_4_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1 = new Label(composite_7_4_1, SWT.NONE);
		wlohmLabel_3_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1 = new Label(composite_7_4_1, SWT.NONE);
		bthekLabel_3_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_2 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_2 = new RowData();
		rd_compositeACar_4_2.width = 136;
		rd_compositeACar_4_2.height = 61;
		compositeACar_4_2.setLayoutData(rd_compositeACar_4_2);
		compositeACar_4_2.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_2 = new Composite(compositeACar_4_2, SWT.NONE);
		composite_6_9_2.setLayout(new FillLayout());

		final Label bm02Label_3_2 = new Label(composite_6_9_2, SWT.NONE);
		bm02Label_3_2.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_2.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_2.setText("Ka04");

		final Composite composite_9_4_2 = new Composite(composite_6_9_2, SWT.NONE);
		composite_9_4_2.setLayout(new FormLayout());
		composite_9_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_2 = new Label(composite_9_4_2, SWT.CENTER);
		final FormData fd_label_3_4_2 = new FormData();
		fd_label_3_4_2.bottom = new FormAttachment(0, 15);
		fd_label_3_4_2.top = new FormAttachment(0, 0);
		fd_label_3_4_2.right = new FormAttachment(0, 68);
		fd_label_3_4_2.left = new FormAttachment(0, 15);
		bktwLabel_3_2.setLayoutData(fd_label_3_4_2);
		bktwLabel_3_2.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_2.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_2.setText("RTW");

		final Composite composite_5_9_2 = new Composite(compositeACar_4_2, SWT.NONE);
		composite_5_9_2.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_2 = new Composite(composite_5_9_2, SWT.NONE);
		composite_8_4_2.setLayout(new FillLayout());

		final Label label_5_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_5_4_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_8_4_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_9_4_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_7_4_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_6_4_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_2 = new Label(composite_8_4_2, SWT.NONE);
		label_4_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_4_4_2.setText("Label");

		final Composite composite_7_4_2 = new Composite(composite_5_9_2, SWT.NONE);
		composite_7_4_2.setLayout(new FillLayout());

		final Label label_11_4_2 = new Label(composite_7_4_2, SWT.NONE);
		label_11_4_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_2.setText("m.heiß");

		final Label wlohmLabel_3_2 = new Label(composite_7_4_2, SWT.NONE);
		wlohmLabel_3_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_2.setText("w.lohm");

		final Label bthekLabel_3_2 = new Label(composite_7_4_2, SWT.NONE);
		bthekLabel_3_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_2.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_2.setText("b.thek");

		final Composite compositeACar_4_3 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_3 = new RowData();
		rd_compositeACar_4_3.width = 136;
		rd_compositeACar_4_3.height = 61;
		compositeACar_4_3.setLayoutData(rd_compositeACar_4_3);
		compositeACar_4_3.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_3 = new Composite(compositeACar_4_3, SWT.NONE);
		composite_6_9_3.setLayout(new FillLayout());

		final Label bm02Label_3_3 = new Label(composite_6_9_3, SWT.NONE);
		bm02Label_3_3.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_3.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_3.setText("Ka05");

		final Composite composite_9_4_3 = new Composite(composite_6_9_3, SWT.NONE);
		composite_9_4_3.setLayout(new FormLayout());
		composite_9_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_3 = new Label(composite_9_4_3, SWT.CENTER);
		final FormData fd_label_3_4_3 = new FormData();
		fd_label_3_4_3.bottom = new FormAttachment(0, 15);
		fd_label_3_4_3.top = new FormAttachment(0, 0);
		fd_label_3_4_3.right = new FormAttachment(0, 68);
		fd_label_3_4_3.left = new FormAttachment(0, 15);
		bktwLabel_3_3.setLayoutData(fd_label_3_4_3);
		bktwLabel_3_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_3.setText("RTW");

		final Composite composite_5_9_3 = new Composite(compositeACar_4_3, SWT.NONE);
		composite_5_9_3.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_3 = new Composite(composite_5_9_3, SWT.NONE);
		composite_8_4_3.setLayout(new FillLayout());

		final Label label_5_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_5_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_8_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_9_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_7_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_6_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_3 = new Label(composite_8_4_3, SWT.NONE);
		label_4_4_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_grün.gif"));
		label_4_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_3 = new Composite(composite_5_9_3, SWT.NONE);
		composite_7_4_3.setLayout(new FillLayout());

		final Label label_11_4_3 = new Label(composite_7_4_3, SWT.NONE);
		label_11_4_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_3.setText("m.heiß");

		final Label wlohmLabel_3_3 = new Label(composite_7_4_3, SWT.NONE);
		wlohmLabel_3_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_3.setText("w.lohm");

		final Label bthekLabel_3_3 = new Label(composite_7_4_3, SWT.NONE);
		bthekLabel_3_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_3.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_3.setText("b.thek");

		final Composite compositeACar_4_1_2 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1_2 = new RowData();
		rd_compositeACar_4_1_2.width = 136;
		rd_compositeACar_4_1_2.height = 61;
		compositeACar_4_1_2.setLayoutData(rd_compositeACar_4_1_2);
		compositeACar_4_1_2.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_2 = new Composite(compositeACar_4_1_2, SWT.NONE);
		composite_6_9_1_2.setLayout(new FillLayout());

		final Label bm02Label_3_1_2 = new Label(composite_6_9_1_2, SWT.NONE);
		bm02Label_3_1_2.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_2.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_2.setText("Ka06");

		final Composite composite_9_4_1_2 = new Composite(composite_6_9_1_2, SWT.NONE);
		composite_9_4_1_2.setLayout(new FormLayout());
		composite_9_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_2 = new Label(composite_9_4_1_2, SWT.CENTER);
		final FormData fd_label_3_4_1_2 = new FormData();
		fd_label_3_4_1_2.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_2.top = new FormAttachment(0, 0);
		fd_label_3_4_1_2.right = new FormAttachment(0, 68);
		fd_label_3_4_1_2.left = new FormAttachment(0, 15);
		bktwLabel_3_1_2.setLayoutData(fd_label_3_4_1_2);
		bktwLabel_3_1_2.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_2.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_2.setText("KTW");

		final Composite composite_5_9_1_2 = new Composite(compositeACar_4_1_2, SWT.NONE);
		composite_5_9_1_2.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_2 = new Composite(composite_5_9_1_2, SWT.NONE);
		composite_8_4_1_2.setLayout(new FillLayout());

		final Label label_5_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_5_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_8_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_9_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_7_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_6_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_2 = new Label(composite_8_4_1_2, SWT.NONE);
		label_4_4_1_2.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_rot.gif"));
		label_4_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_2 = new Composite(composite_5_9_1_2, SWT.NONE);
		composite_7_4_1_2.setLayout(new FillLayout());

		final Label label_11_4_1_2 = new Label(composite_7_4_1_2, SWT.NONE);
		label_11_4_1_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_2.setText("m.heiß");

		final Label wlohmLabel_3_1_2 = new Label(composite_7_4_1_2, SWT.NONE);
		wlohmLabel_3_1_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_2.setText("w.lohm");

		final Label bthekLabel_3_1_2 = new Label(composite_7_4_1_2, SWT.NONE);
		bthekLabel_3_1_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_2.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_2.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_2.setText("b.thek");

		final Composite compositeACar_4_1_3 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1_3 = new RowData();
		rd_compositeACar_4_1_3.width = 136;
		rd_compositeACar_4_1_3.height = 61;
		compositeACar_4_1_3.setLayoutData(rd_compositeACar_4_1_3);
		compositeACar_4_1_3.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_3 = new Composite(compositeACar_4_1_3, SWT.NONE);
		composite_6_9_1_3.setLayout(new FillLayout());

		final Label bm02Label_3_1_3 = new Label(composite_6_9_1_3, SWT.NONE);
		bm02Label_3_1_3.setForeground(SWTResourceManager.getColor(0, 0, 128));
		bm02Label_3_1_3.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bm02Label_3_1_3.setText("Ka07");

		final Composite composite_9_4_1_3 = new Composite(composite_6_9_1_3, SWT.NONE);
		composite_9_4_1_3.setLayout(new FormLayout());
		composite_9_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label bktwLabel_3_1_3 = new Label(composite_9_4_1_3, SWT.CENTER);
		final FormData fd_label_3_4_1_3 = new FormData();
		fd_label_3_4_1_3.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_3.top = new FormAttachment(0, 0);
		fd_label_3_4_1_3.right = new FormAttachment(0, 68);
		fd_label_3_4_1_3.left = new FormAttachment(0, 15);
		bktwLabel_3_1_3.setLayoutData(fd_label_3_4_1_3);
		bktwLabel_3_1_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_3.setText("RTW");

		final Composite composite_5_9_1_3 = new Composite(compositeACar_4_1_3, SWT.NONE);
		composite_5_9_1_3.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_3 = new Composite(composite_5_9_1_3, SWT.NONE);
		composite_8_4_1_3.setLayout(new FillLayout());

		final Label label_5_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_5_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK2.gif"));
		label_5_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_8_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_8_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_9_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_9_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_7_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_7_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_6_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_6_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Label label_4_4_1_3 = new Label(composite_8_4_1_3, SWT.NONE);
		label_4_4_1_3.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel_gelb.gif"));
		label_4_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));

		final Composite composite_7_4_1_3 = new Composite(composite_5_9_1_3, SWT.NONE);
		composite_7_4_1_3.setLayout(new FillLayout());

		final Label label_11_4_1_3 = new Label(composite_7_4_1_3, SWT.NONE);
		label_11_4_1_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		label_11_4_1_3.setText("m.heiß");

		final Label wlohmLabel_3_1_3 = new Label(composite_7_4_1_3, SWT.NONE);
		wlohmLabel_3_1_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		wlohmLabel_3_1_3.setText("w.lohm");

		final Label bthekLabel_3_1_3 = new Label(composite_7_4_1_3, SWT.NONE);
		bthekLabel_3_1_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_3.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
		bthekLabel_3_1_3.setText("b.thek");

		final Composite compositeACar_4_1_4 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1_4 = new RowData();
		rd_compositeACar_4_1_4.width = 136;
		rd_compositeACar_4_1_4.height = 61;
		compositeACar_4_1_4.setLayoutData(rd_compositeACar_4_1_4);
		compositeACar_4_1_4.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_4 = new Composite(compositeACar_4_1_4, SWT.NONE);
		composite_6_9_1_4.setLayout(new FillLayout());

		final Label bm02Label_3_1_4 = new Label(composite_6_9_1_4, SWT.NONE);
		bm02Label_3_1_4.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_4.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_4.setText("Ka08");

		final Composite composite_9_4_1_4 = new Composite(composite_6_9_1_4, SWT.NONE);
		composite_9_4_1_4.setLayout(new FormLayout());
		composite_9_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_4 = new Label(composite_9_4_1_4, SWT.CENTER);
		final FormData fd_label_3_4_1_4 = new FormData();
		fd_label_3_4_1_4.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_4.top = new FormAttachment(0, 0);
		fd_label_3_4_1_4.right = new FormAttachment(0, 68);
		fd_label_3_4_1_4.left = new FormAttachment(0, 15);
		bktwLabel_3_1_4.setLayoutData(fd_label_3_4_1_4);
		bktwLabel_3_1_4.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_4.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_4.setText("BKTW");

		final Composite composite_5_9_1_4 = new Composite(compositeACar_4_1_4, SWT.NONE);
		composite_5_9_1_4.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_4 = new Composite(composite_5_9_1_4, SWT.NONE);
		composite_8_4_1_4.setLayout(new FillLayout());

		final Label label_5_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_5_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_8_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_9_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_7_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_6_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_4 = new Label(composite_8_4_1_4, SWT.NONE);
		label_4_4_1_4.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_4 = new Composite(composite_5_9_1_4, SWT.NONE);
		composite_7_4_1_4.setLayout(new FillLayout());

		final Label label_11_4_1_4 = new Label(composite_7_4_1_4, SWT.NONE);
		label_11_4_1_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_4 = new Label(composite_7_4_1_4, SWT.NONE);
		wlohmLabel_3_1_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_4 = new Label(composite_7_4_1_4, SWT.NONE);
		bthekLabel_3_1_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_4.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_4.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite compositeACar_4_1_5 = new Composite(group_2, SWT.NONE);
		final RowData rd_compositeACar_4_1_5 = new RowData();
		rd_compositeACar_4_1_5.width = 136;
		rd_compositeACar_4_1_5.height = 61;
		compositeACar_4_1_5.setLayoutData(rd_compositeACar_4_1_5);
		compositeACar_4_1_5.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_6_9_1_5 = new Composite(compositeACar_4_1_5, SWT.NONE);
		composite_6_9_1_5.setLayout(new FillLayout());

		final Label bm02Label_3_1_5 = new Label(composite_6_9_1_5, SWT.NONE);
		bm02Label_3_1_5.setForeground(SWTResourceManager.getColor(207, 207, 230));
		bm02Label_3_1_5.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		bm02Label_3_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bm02Label_3_1_5.setText("Ka19");

		final Composite composite_9_4_1_5 = new Composite(composite_6_9_1_5, SWT.NONE);
		composite_9_4_1_5.setLayout(new FormLayout());
		composite_9_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bktwLabel_3_1_5 = new Label(composite_9_4_1_5, SWT.CENTER);
		final FormData fd_label_3_4_1_5 = new FormData();
		fd_label_3_4_1_5.bottom = new FormAttachment(0, 15);
		fd_label_3_4_1_5.top = new FormAttachment(0, 0);
		fd_label_3_4_1_5.right = new FormAttachment(0, 68);
		fd_label_3_4_1_5.left = new FormAttachment(0, 15);
		bktwLabel_3_1_5.setLayoutData(fd_label_3_4_1_5);
		bktwLabel_3_1_5.setForeground(SWTResourceManager.getColor(255, 255, 255));
		bktwLabel_3_1_5.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		bktwLabel_3_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));
		bktwLabel_3_1_5.setText("STW");

		final Composite composite_5_9_1_5 = new Composite(compositeACar_4_1_5, SWT.NONE);
		composite_5_9_1_5.setLayout(new FillLayout(SWT.VERTICAL));

		final Composite composite_8_4_1_5 = new Composite(composite_5_9_1_5, SWT.NONE);
		composite_8_4_1_5.setLayout(new FillLayout());

		final Label label_5_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_5_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/OK.gif"));
		label_5_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_8_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_8_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Handy.gif"));
		label_8_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_9_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_9_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Haus.gif"));
		label_9_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_7_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_7_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Reparatur.gif"));
		label_7_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_6_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_6_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/TXT.gif"));
		label_6_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label label_4_4_1_5 = new Label(composite_8_4_1_5, SWT.NONE);
		label_4_4_1_5.setImage(SWTResourceManager.getImage(RessourcesView.class, "/image/Ampel.gif"));
		label_4_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Composite composite_7_4_1_5 = new Composite(composite_5_9_1_5, SWT.NONE);
		composite_7_4_1_5.setLayout(new FillLayout());

		final Label label_11_4_1_5 = new Label(composite_7_4_1_5, SWT.NONE);
		label_11_4_1_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		label_11_4_1_5.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		label_11_4_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label wlohmLabel_3_1_5 = new Label(composite_7_4_1_5, SWT.NONE);
		wlohmLabel_3_1_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		wlohmLabel_3_1_5.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
		wlohmLabel_3_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));

		final Label bthekLabel_3_1_5 = new Label(composite_7_4_1_5, SWT.NONE);
		bthekLabel_3_1_5.setForeground(SWTResourceManager.getColor(0, 0, 102));
		bthekLabel_3_1_5.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
		bthekLabel_3_1_5.setBackground(SWTResourceManager.getColor(228, 236, 238));
		final GroupLayout groupLayout = new GroupLayout(composite_1);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.TRAILING)
				.add(groupLayout.createSequentialGroup()
					.addContainerGap()
					.add(groupLayout.createParallelGroup(GroupLayout.TRAILING)
						.add(GroupLayout.LEADING, group_7, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_6, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_4, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_5, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_3, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_2, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, group_1, GroupLayout.PREFERRED_SIZE, 447, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(group_1, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(group_2, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.RELATED)
					.add(group_3, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.add(19, 19, 19)
					.add(group_4, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.add(19, 19, 19)
					.add(group_5, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.add(19, 19, 19)
					.add(group_6, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.add(19, 19, 19)
					.add(group_7, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(94, Short.MAX_VALUE))
		);
		composite_1.setLayout(groupLayout);

		//gridLayout.makeColumnsEqualWidth = true;

		
		//---> PersonalView
//		final Composite composite = new Composite(sashForm, SWT.NONE);
//		composite.setLayout(new FillLayout());
//
//		final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
//
//		final TabItem bezirkTabItem = new TabItem(tabFolder, SWT.NONE);
//		bezirkTabItem.setText("Bezirk");
//
//		final SashForm sashForm_1 = new SashForm(tabFolder, SWT.VERTICAL);
//
//		final Group personalImDienstGroup = new Group(sashForm_1, SWT.NONE);
//		personalImDienstGroup.setLayout(new FillLayout());
//		personalImDienstGroup.setText("Personal im Dienst");
//
//		final Table table = new Table(personalImDienstGroup, SWT.BORDER);
//		table.setLinesVisible(true);
//		table.setHeaderVisible(true);
//
//		final TableColumn newColumnTableColumnBereitschaftBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnBereitschaftBezirkImDienst.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
//		newColumnTableColumnBereitschaftBezirkImDienst.setWidth(23);
//		newColumnTableColumnBereitschaftBezirkImDienst.setText("B");
//
//		final TableColumn newColumnTableColumAnmerkungBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumAnmerkungBezirkImDienst.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
//		newColumnTableColumAnmerkungBezirkImDienst.setWidth(26);
//		newColumnTableColumAnmerkungBezirkImDienst.setText("A");
//
//		final TableColumn newColumnTableColumnNameBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnNameBezirkImDienst.setWidth(98);
//		newColumnTableColumnNameBezirkImDienst.setText("Name");
//
//		final TableColumn newColumnTableColumnDienstbezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnDienstbezirkImDienst.setToolTipText("Dienst lt. Dienstplan");
//		newColumnTableColumnDienstbezirkImDienst.setWidth(73);
//		newColumnTableColumnDienstbezirkImDienst.setText("Dienst");
//
//		final TableColumn newColumnTableColumnAnmBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnAnmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Anmeldung");
//		newColumnTableColumnAnmBezirkImDienst.setWidth(41);
//		newColumnTableColumnAnmBezirkImDienst.setText("Anm");
//
//		final TableColumn newColumnTableColumnAbmBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnAbmBezirkImDienst.setToolTipText("Zeit der tatsächlichen Abmeldung");
//		newColumnTableColumnAbmBezirkImDienst.setWidth(41);
//		newColumnTableColumnAbmBezirkImDienst.setText("Abm");
//
//		final TableColumn newColumnTableColumnDVBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnDVBezirkImDienst.setToolTipText("Dienstverhältnis");
//		newColumnTableColumnDVBezirkImDienst.setWidth(31);
//		newColumnTableColumnDVBezirkImDienst.setText("DV");
//
//		final TableColumn newColumnTableColumnVBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnVBezirkImDienst.setToolTipText("Verwendung");
//		newColumnTableColumnVBezirkImDienst.setWidth(30);
//		newColumnTableColumnVBezirkImDienst.setText("V");
//
//		final TableColumn newColumnTableColumnOSBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnOSBezirkImDienst.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
//		newColumnTableColumnOSBezirkImDienst.setWidth(22);
//		newColumnTableColumnOSBezirkImDienst.setText("OS");
//
//		final TableColumn newColumnTableColumnFzgBezirkImDienst = new TableColumn(table, SWT.NONE);
//		newColumnTableColumnFzgBezirkImDienst.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
//		newColumnTableColumnFzgBezirkImDienst.setWidth(36);
//		newColumnTableColumnFzgBezirkImDienst.setText("Fzg");
//
//		final TableItem newTabItem = new TableItem(table, SWT.BORDER);
//		newTabItem.setText("New item");
//
//		final TableItem newItemTableItem_1 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_1.setText("New item");
//
//		final TableItem newItemTableItem_2 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_2.setText("New item");
//
//		final TableItem newItemTableItem_3 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_3.setText("New item");
//
//		final TableItem newItemTableItem_4 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_4.setText("New item");
//
//		final TableItem newItemTableItem_8 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_8.setText("New item");
//
//		final TableItem newItemTableItem_5 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_5.setText("New item");
//
//		final TableItem newItemTableItem_9 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_9.setText("New item");
//
//		final TableItem newItemTableItem_6 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_6.setText("New item");
//
//		final TableItem newItemTableItem_7 = new TableItem(table, SWT.BORDER);
//		newItemTableItem_7.setText("New item");
//
//		final Menu menu_10 = new Menu(table);
//		table.setMenu(menu_10);
//
//		final MenuItem menuItem_28 = new MenuItem(menu_10, SWT.CASCADE);
//		menuItem_28.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_28.setText("Menu Item");
//
//		final Menu menu_11 = new Menu(menuItem_28);
//		menuItem_28.setMenu(menu_11);
//
//		final MenuItem menuItem_29 = new MenuItem(menu_11, SWT.NONE);
//		menuItem_29.setText("Menu Item");
//
//		final MenuItem menuItem_30 = new MenuItem(menu_11, SWT.NONE);
//		menuItem_30.setText("Menu Item");
//
//		final MenuItem menuItem_31 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_31.setText("Abmelden");
//
//		final MenuItem menuItem_32 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_32.setText("Anmeldung aufheben");
//
//		final MenuItem menuItem_33 = new MenuItem(menu_10, SWT.NONE);
//		menuItem_33.setText("Fahrzeug zuweisen");
//
//		final MenuItem menuItem_34 = new MenuItem(menu_10, SWT.CASCADE);
//		menuItem_34.setText("Fahrzeug zuweisen");
//
//		final Menu menu_12 = new Menu(menuItem_34);
//		menuItem_34.setMenu(menu_12);
//
//		final MenuItem menuItem_35 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_35.setText("Bm02");
//
//		final Menu menu_13 = new Menu(menuItem_35);
//		menuItem_35.setMenu(menu_13);
//
//		final MenuItem menuItem_36 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_36.setText("Fahrer");
//
//		final MenuItem menuItem_38 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_38.setText("Sanitäter I");
//
//		final MenuItem menuItem_37 = new MenuItem(menu_13, SWT.NONE);
//		menuItem_37.setText("Sanitäter II");
//
//		final MenuItem menuItem_39 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_39.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_39.setText("Bm18");
//
//		final Menu menu_14 = new Menu(menuItem_39);
//		menuItem_39.setMenu(menu_14);
//
//		final MenuItem menuItem_43 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_43.setText("Fahrer");
//
//		final MenuItem menuItem_44 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_44.setText("Sanitäter I");
//
//		final MenuItem menuItem_45 = new MenuItem(menu_14, SWT.NONE);
//		menuItem_45.setText("Sanitäter II");
//
//		new MenuItem(menu_12, SWT.SEPARATOR);
//
//		final MenuItem menuItem_40 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_40.setText("Ka04");
//
//		final Menu menu_15 = new Menu(menuItem_40);
//		menuItem_40.setMenu(menu_15);
//
//		final MenuItem menuItem_46 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_46.setText("Fahrer");
//
//		final MenuItem menuItem_47 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_47.setText("Sanitäter I");
//
//		final MenuItem menuItem_48 = new MenuItem(menu_15, SWT.NONE);
//		menuItem_48.setText("Sanitäter II");
//
//		final MenuItem menuItem_41 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_41.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_41.setText("Ka07");
//
//		final Menu menu_16 = new Menu(menuItem_41);
//		menuItem_41.setMenu(menu_16);
//
//		final MenuItem menuItem_49 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_49.setText("Fahrer");
//
//		final MenuItem menuItem_51 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_51.setText("Sanitäter I");
//
//		final MenuItem menuItem_50 = new MenuItem(menu_16, SWT.NONE);
//		menuItem_50.setText("Sanitäter II");
//
//		new MenuItem(menu_12, SWT.SEPARATOR);
//
//		final MenuItem menuItem_42 = new MenuItem(menu_12, SWT.CASCADE);
//		menuItem_42.setText("Th16");
//
//		final Menu menu_17 = new Menu(menuItem_42);
//		menuItem_42.setMenu(menu_17);
//
//		final MenuItem menuItem_52 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_52.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_52.setText("Fahrer");
//
//		final MenuItem menuItem_54 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_54.setText("Sanitäter I");
//
//		final MenuItem menuItem_53 = new MenuItem(menu_17, SWT.NONE);
//		menuItem_53.setText("Sanitäter II");
//		bezirkTabItem.setControl(sashForm_1);
//
//		final Group personalLtDienstplanGroup = new Group(sashForm_1, SWT.NONE);
//		personalLtDienstplanGroup.setLayout(new FillLayout());
//		personalLtDienstplanGroup.setText("Personal laut Dienstplan");
//
//		final Table table_1 = new Table(personalLtDienstplanGroup, SWT.BORDER);
//		table_1.setLinesVisible(true);
//		table_1.setHeaderVisible(true);
//
//		final TableColumn newColumnTableColumnBereitschaftBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setToolTipText("Mitarbeiter auf Bereitschaft (Symbol, wenn der Fall)");
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setWidth(23);
//		newColumnTableColumnBereitschaftBezirkLautDienstplan.setText("B");
//
//		final TableColumn newColumnTableColumnAnmerkungBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setToolTipText("Anmerkung (Symbol, wenn Anmerkung vorhanden)");
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setWidth(26);
//		newColumnTableColumnAnmerkungBezirkLautDienstplan.setText("A");
//
//		final TableColumn newColumnTableColumnNameBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnNameBezirkLautDienstplan.setWidth(98);
//		newColumnTableColumnNameBezirkLautDienstplan.setText("Name");
//
//		final TableColumn newColumnTableColumnDienstBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnDienstBezirkLautDienstplan.setToolTipText("Dienst lt. Dienstplan");
//		newColumnTableColumnDienstBezirkLautDienstplan.setWidth(73);
//		newColumnTableColumnDienstBezirkLautDienstplan.setText("Dienst");
//
//		final TableColumn newColumnTableColumnAnmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAnmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Anmeldung");
//		newColumnTableColumnAnmBezirkLautDienstplan.setWidth(41);
//		newColumnTableColumnAnmBezirkLautDienstplan.setText("Anm");
//
//		final TableColumn newColumnTableColumnAbmBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnAbmBezirkLautDienstplan.setToolTipText("Zeit der tatsächlichen Abmeldung");
//		newColumnTableColumnAbmBezirkLautDienstplan.setWidth(41);
//		newColumnTableColumnAbmBezirkLautDienstplan.setText("Abm");
//
//		final TableColumn newColumnTableColumnDVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnDVBezirkLautDienstplan.setToolTipText("Dienstverhältnis");
//		newColumnTableColumnDVBezirkLautDienstplan.setWidth(31);
//		newColumnTableColumnDVBezirkLautDienstplan.setText("DV");
//
//		final TableColumn newColumnTableColumnVBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnVBezirkLautDienstplan.setToolTipText("Verwendung");
//		newColumnTableColumnVBezirkLautDienstplan.setWidth(30);
//		newColumnTableColumnVBezirkLautDienstplan.setText("V");
//
//		final TableColumn newColumnTableColumnOSBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnOSBezirkLautDienstplan.setToolTipText("Ortsstelle, an der der Mitarbeiter Dienst macht");
//		newColumnTableColumnOSBezirkLautDienstplan.setWidth(22);
//		newColumnTableColumnOSBezirkLautDienstplan.setText("OS");
//
//		final TableColumn newColumnTableColumnFzgBezirkLautDienstplan = new TableColumn(table_1, SWT.NONE);
//		newColumnTableColumnFzgBezirkLautDienstplan.setToolTipText("Fahrzeug, dem der Mitarbeiter zugewiesen ist");
//		newColumnTableColumnFzgBezirkLautDienstplan.setWidth(36);
//		newColumnTableColumnFzgBezirkLautDienstplan.setText("Fzg");
//
//		final TableItem newTabItem_1 = new TableItem(table_1, SWT.BORDER);
//		newTabItem_1.setText("New item");
//
//		final TableItem newItemTableItem_1_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_1_1.setText("New item");
//
//		final TableItem newItemTableItem_2_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_2_1.setText("New item");
//
//		final TableItem newItemTableItem_3_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_3_1.setText("New item");
//
//		final TableItem newItemTableItem_4_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_4_1.setText("New item");
//
//		final TableItem newItemTableItem_8_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_8_1.setText("New item");
//
//		final TableItem newItemTableItem_5_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_5_1.setText("New item");
//
//		final TableItem newItemTableItem_9_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_9_1.setText("New item");
//
//		final TableItem newItemTableItem_6_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_6_1.setText("New item");
//
//		final TableItem newItemTableItem_7_1 = new TableItem(table_1, SWT.BORDER);
//		newItemTableItem_7_1.setText("New item");
//		sashForm_1.setWeights(new int[] {1, 1 });
//
//		final TabItem bruckmurTabItem = new TabItem(tabFolder, SWT.NONE);
//		bruckmurTabItem.setText("Bruck/Mur");
//
//		final TabItem kapfenbergTabItem = new TabItem(tabFolder, SWT.NONE);
//		kapfenbergTabItem.setText("Kapfenberg");
//
//		final TabItem stMareinTabItem = new TabItem(tabFolder, SWT.NONE);
//		stMareinTabItem.setText("St. Marein");
//
//		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
//		tabItem.setText("Thörl");
//
//		final TabItem turnauTabItem = new TabItem(tabFolder, SWT.NONE);
//		turnauTabItem.setText("Turnau");
//
//		final TabItem tagesinformationTabItem = new TabItem(tabFolder, SWT.NONE);
//		tagesinformationTabItem.setText("Breitenau");
//
//		final Group bruckAnDerGroup_1 = new Group(tabFolder, SWT.NONE);
//		final RowLayout rowLayout = new RowLayout();
//		rowLayout.spacing = 5;
//		rowLayout.pack = false;
//		bruckAnDerGroup_1.setLayout(rowLayout);
//		bruckAnDerGroup_1.setText("Bruck an der Mur");
//		tagesinformationTabItem.setControl(bruckAnDerGroup_1);
//
//		final Composite compositeACar_1 = new Composite(bruckAnDerGroup_1, SWT.NONE);
//		final RowData rd_compositeACar_1 = new RowData();
//		rd_compositeACar_1.width = 136;
//		rd_compositeACar_1.height = 61;
//		compositeACar_1.setLayoutData(rd_compositeACar_1);
//		compositeACar_1.setLayout(new FillLayout(SWT.VERTICAL));
//		compositeACar_1.setVisible(false);
//		compositeACar_1.setToolTipText("Detailinformationen zu Bm02");
//
//		final Composite composite_6_6 = new Composite(compositeACar_1, SWT.NONE);
//		composite_6_6.setLayout(new FillLayout());
//
//		final Label bm02Label = new Label(composite_6_6, SWT.NONE);
//		bm02Label.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bm02Label.setText("Bm02");
//
//		final Composite composite_9_1 = new Composite(composite_6_6, SWT.NONE);
//		composite_9_1.setLayout(new FormLayout());
//		composite_9_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label bktwLabel = new Label(composite_9_1, SWT.CENTER);
//		final FormData fd_label_3_1 = new FormData();
//		fd_label_3_1.bottom = new FormAttachment(0, 15);
//		fd_label_3_1.top = new FormAttachment(0, 0);
//		fd_label_3_1.right = new FormAttachment(0, 68);
//		fd_label_3_1.left = new FormAttachment(0, 15);
//		bktwLabel.setLayoutData(fd_label_3_1);
//		bktwLabel.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel.setText("RTW");
//
//		final Composite composite_5_6 = new Composite(compositeACar_1, SWT.NONE);
//		composite_5_6.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_1 = new Composite(composite_5_6, SWT.NONE);
//		composite_8_1.setLayout(new FillLayout());
//
//		final Label label_5_1 = new Label(composite_8_1, SWT.NONE);
//		label_5_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5_1.setText("Label");
//
//		final Label label_8_1 = new Label(composite_8_1, SWT.NONE);
//		label_8_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8_1.setText("Label");
//
//		final Label label_9_1 = new Label(composite_8_1, SWT.NONE);
//		label_9_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9_1.setText("Label");
//
//		final Label label_7_1 = new Label(composite_8_1, SWT.NONE);
//		label_7_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7_1.setText("Label");
//
//		final Label label_6_1 = new Label(composite_8_1, SWT.NONE);
//		label_6_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6_1.setText("Label");
//
//		final Label label_4_1 = new Label(composite_8_1, SWT.NONE);
//		label_4_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4_1.setText("Label");
//
//		final Composite composite_7_1 = new Composite(composite_5_6, SWT.NONE);
//		composite_7_1.setLayout(new FillLayout());
//
//		final Label label_11_1 = new Label(composite_7_1, SWT.NONE);
//		label_11_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_1.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11_1.setText("m.heiß");
//
//		final Label wlohmLabel = new Label(composite_7_1, SWT.NONE);
//		wlohmLabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		wlohmLabel.setText("w.lohm");
//
//		final Label bthekLabel = new Label(composite_7_1, SWT.NONE);
//		bthekLabel.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bthekLabel.setText("b.thek");
//
//		final Group group = new Group(bruckAnDerGroup_1, SWT.NONE);
//		group.setLayoutData(new RowData());
//		group.setLayout(new RowLayout());
//		group.setText("Thörl");
//
//		final Composite compositeACar = new Composite(group, SWT.NONE);
//		final RowData rd_compositeACar = new RowData();
//		rd_compositeACar.width = 136;
//		rd_compositeACar.height = 61;
//		compositeACar.setLayoutData(rd_compositeACar);
//		compositeACar.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6 = new Composite(compositeACar, SWT.NONE);
//		composite_6.setLayout(new FillLayout());
//
//		final Label th16Label = new Label(composite_6, SWT.NONE);
//		th16Label.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		th16Label.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		th16Label.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		th16Label.setText("Th16");
//
//		final Composite composite_9 = new Composite(composite_6, SWT.NONE);
//		composite_9.setLayout(new FormLayout());
//		composite_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label label_3 = new Label(composite_9, SWT.CENTER);
//		final FormData fd_label_3 = new FormData();
//		fd_label_3.bottom = new FormAttachment(0, 15);
//		fd_label_3.top = new FormAttachment(0, 0);
//		fd_label_3.right = new FormAttachment(0, 68);
//		fd_label_3.left = new FormAttachment(0, 15);
//		label_3.setLayoutData(fd_label_3);
//		label_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		label_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		label_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_3.setText("BKTW");
//
//		final Composite composite_5 = new Composite(compositeACar, SWT.NONE);
//		composite_5.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8 = new Composite(composite_5, SWT.NONE);
//		composite_8.setLayout(new FillLayout());
//
//		final Label label_5 = new Label(composite_8, SWT.NONE);
//		label_5.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5.setText("Label");
//
//		final Label label_8 = new Label(composite_8, SWT.NONE);
//		label_8.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8.setText("Label");
//
//		final Label label_9 = new Label(composite_8, SWT.NONE);
//		label_9.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9.setText("Label");
//
//		final Label label_7 = new Label(composite_8, SWT.NONE);
//		label_7.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7.setText("Label");
//
//		final Label label_6 = new Label(composite_8, SWT.NONE);
//		label_6.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6.setText("Label");
//
//		final Label label_4 = new Label(composite_8, SWT.NONE);
//		label_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4.setText("Label");
//
//		final Composite composite_7 = new Composite(composite_5, SWT.NONE);
//		composite_7.setLayout(new FillLayout());
//
//		final Label label_11 = new Label(composite_7, SWT.NONE);
//		label_11.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11.setText("m.heiß");
//
//		final Label label_12 = new Label(composite_7, SWT.NONE);
//		label_12.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_12.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_12.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_12.setText("w.lohm");
//
//		final Label label_10 = new Label(composite_7, SWT.NONE);
//		label_10.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_10.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		label_10.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_10.setText("b.thek");
//
//		final Composite compositeACar_2 = new Composite(bruckAnDerGroup_1, SWT.NONE);
//		final RowData rd_compositeACar_2 = new RowData();
//		rd_compositeACar_2.width = 136;
//		rd_compositeACar_2.height = 61;
//		compositeACar_2.setLayoutData(rd_compositeACar_2);
//		compositeACar_2.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6_7 = new Composite(compositeACar_2, SWT.NONE);
//		composite_6_7.setLayout(new FillLayout());
//
//		final Label bm02Label_1 = new Label(composite_6_7, SWT.NONE);
//		bm02Label_1.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label_1.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bm02Label_1.setText("Bm03");
//
//		final Composite composite_9_2 = new Composite(composite_6_7, SWT.NONE);
//		composite_9_2.setLayout(new FormLayout());
//		composite_9_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//
//		final Label bktwLabel_1 = new Label(composite_9_2, SWT.CENTER);
//		final FormData fd_label_3_2 = new FormData();
//		fd_label_3_2.bottom = new FormAttachment(0, 15);
//		fd_label_3_2.top = new FormAttachment(0, 0);
//		fd_label_3_2.right = new FormAttachment(0, 68);
//		fd_label_3_2.left = new FormAttachment(0, 15);
//		bktwLabel_1.setLayoutData(fd_label_3_2);
//		bktwLabel_1.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel_1.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel_1.setText("RTW");
//
//		final Composite composite_5_7 = new Composite(compositeACar_2, SWT.NONE);
//		composite_5_7.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_2 = new Composite(composite_5_7, SWT.NONE);
//		composite_8_2.setLayout(new FillLayout());
//
//		final Label label_5_2 = new Label(composite_8_2, SWT.NONE);
//		label_5_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_5_2.setText("Label");
//
//		final Label label_8_2 = new Label(composite_8_2, SWT.NONE);
//		label_8_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_8_2.setText("Label");
//
//		final Label label_9_2 = new Label(composite_8_2, SWT.NONE);
//		label_9_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_9_2.setText("Label");
//
//		final Label label_7_2 = new Label(composite_8_2, SWT.NONE);
//		label_7_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_7_2.setText("Label");
//
//		final Label label_6_2 = new Label(composite_8_2, SWT.NONE);
//		label_6_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_6_2.setText("Label");
//
//		final Label label_4_2 = new Label(composite_8_2, SWT.NONE);
//		label_4_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_4_2.setText("Label");
//
//		final Composite composite_7_2 = new Composite(composite_5_7, SWT.NONE);
//		composite_7_2.setLayout(new FillLayout());
//
//		final Label label_11_2 = new Label(composite_7_2, SWT.NONE);
//		label_11_2.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_2.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_2.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		label_11_2.setText("Label");
//
//		final Label wlohmLabel_1 = new Label(composite_7_2, SWT.NONE);
//		wlohmLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel_1.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		wlohmLabel_1.setText("Label");
//
//		final Label bthekLabel_1 = new Label(composite_7_2, SWT.NONE);
//		bthekLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel_1.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel_1.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bthekLabel_1.setText("Label");
//
//		final Composite compositeACar_4 = new Composite(bruckAnDerGroup_1, SWT.NONE);
//		final RowData rd_compositeACar_4 = new RowData();
//		rd_compositeACar_4.width = 136;
//		rd_compositeACar_4.height = 61;
//		compositeACar_4.setLayoutData(rd_compositeACar_4);
//		compositeACar_4.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_6_9 = new Composite(compositeACar_4, SWT.NONE);
//		composite_6_9.setLayout(new FillLayout());
//
//		final Label bm02Label_3 = new Label(composite_6_9, SWT.NONE);
//		bm02Label_3.setForeground(SWTResourceManager.getColor(0, 0, 128));
//		bm02Label_3.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
//		bm02Label_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bm02Label_3.setText("Bm05");
//
//		final Composite composite_9_4 = new Composite(composite_6_9, SWT.NONE);
//		composite_9_4.setLayout(new FormLayout());
//		composite_9_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//
//		final Label bktwLabel_3 = new Label(composite_9_4, SWT.CENTER);
//		final FormData fd_label_3_4 = new FormData();
//		fd_label_3_4.bottom = new FormAttachment(0, 15);
//		fd_label_3_4.top = new FormAttachment(0, 0);
//		fd_label_3_4.right = new FormAttachment(0, 68);
//		fd_label_3_4.left = new FormAttachment(0, 15);
//		bktwLabel_3.setLayoutData(fd_label_3_4);
//		bktwLabel_3.setForeground(SWTResourceManager.getColor(255, 255, 255));
//		bktwLabel_3.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
//		bktwLabel_3.setBackground(SWTResourceManager.getColor(228, 236, 238));
//		bktwLabel_3.setText("RTW");
//
//		final Composite composite_5_9 = new Composite(compositeACar_4, SWT.NONE);
//		composite_5_9.setLayout(new FillLayout(SWT.VERTICAL));
//
//		final Composite composite_8_4 = new Composite(composite_5_9, SWT.NONE);
//		composite_8_4.setLayout(new FillLayout());
//
//		final Label label_5_4 = new Label(composite_8_4, SWT.NONE);
//		label_5_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_5_4.setText("Label");
//
//		final Label label_8_4 = new Label(composite_8_4, SWT.NONE);
//		label_8_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_8_4.setText("Label");
//
//		final Label label_9_4 = new Label(composite_8_4, SWT.NONE);
//		label_9_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_9_4.setText("Label");
//
//		final Label label_7_4 = new Label(composite_8_4, SWT.NONE);
//		label_7_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_7_4.setText("Label");
//
//		final Label label_6_4 = new Label(composite_8_4, SWT.NONE);
//		label_6_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_6_4.setText("Label");
//
//		final Label label_4_4 = new Label(composite_8_4, SWT.NONE);
//		label_4_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_4_4.setText("Label");
//
//		final Composite composite_7_4 = new Composite(composite_5_9, SWT.NONE);
//		composite_7_4.setLayout(new FillLayout());
//
//		final Label label_11_4 = new Label(composite_7_4, SWT.NONE);
//		label_11_4.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		label_11_4.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		label_11_4.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		label_11_4.setText("m.heiß");
//
//		final Label wlohmLabel_3 = new Label(composite_7_4, SWT.NONE);
//		wlohmLabel_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		wlohmLabel_3.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NONE));
//		wlohmLabel_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		wlohmLabel_3.setText("w.lohm");
//
//		final Label bthekLabel_3 = new Label(composite_7_4, SWT.NONE);
//		bthekLabel_3.setForeground(SWTResourceManager.getColor(0, 0, 102));
//		bthekLabel_3.setFont(SWTResourceManager.getFont("", 8, SWT.NONE));
//		bthekLabel_3.setBackground(SWTResourceManager.getColor(209, 229, 249));
//		bthekLabel_3.setText("b.thek");
//
//		final TabItem tagesinformationTabItem_1 = new TabItem(tabFolder, SWT.NONE);
//		tagesinformationTabItem_1.setText("Info");

//		final Menu menu = new Menu(shell, SWT.BAR);
//		menu.setDefaultItem(null);
//		shell.setMenuBar(menu);
//
//		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
//		menuItem.setText("Datei");
//
//		final Menu menu_1 = new Menu(menuItem);
//		menuItem.setMenu(menu_1);
//
//		final MenuItem menuItem_4 = new MenuItem(menu_1, SWT.CASCADE);
//		menuItem_4.setText("Drucken");
//
//		final Menu menu_2 = new Menu(menuItem_4);
//		menuItem_4.setMenu(menu_2);
//
//		final MenuItem menuItem_7 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_7.setText("Journalblatt");
//
//		final MenuItem menuItem_5 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_5.setText("Vormerklisten");
//
//		final MenuItem menuItem_8 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_8.setText("Tagesdienstlisten");
//
//		final MenuItem menuItem_6 = new MenuItem(menu_2, SWT.NONE);
//		menuItem_6.setText("Backup");
//
//		new MenuItem(menu_1, SWT.SEPARATOR);
//
//		final MenuItem menuItem_1 = new MenuItem(menu_1, SWT.NONE);
//		menuItem_1.setText("Benutzer wechseln");
//
//		new MenuItem(menu_1, SWT.SEPARATOR);
//
//		final MenuItem menuItem_3 = new MenuItem(menu_1, SWT.NONE);
//		menuItem_3.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_3.setText("Beenden");
//
//		final MenuItem menuItem_2 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_2.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_2.setText("Auftrag");
//
//		final Menu menu_3 = new Menu(menuItem_2);
//		menuItem_2.setMenu(menu_3);
//
//		final MenuItem menuItem_16 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_16.setText("Notfall");
//
//		final MenuItem menuItem_17 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_17.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_17.setText("Vormerkung");
//
//		final MenuItem menuItem_18 = new MenuItem(menu_3, SWT.NONE);
//		menuItem_18.setText("Auftrag (neutral)");
//
//		final MenuItem menuItem_9 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_9.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_9.setText("Personal");
//
//		final Menu menu_4 = new Menu(menuItem_9);
//		menuItem_9.setMenu(menu_4);
//
//		final MenuItem menuItem_19 = new MenuItem(menu_4, SWT.NONE);
//		menuItem_19.setText("Neuen Dienstplaneintrag");
//
//		final MenuItem menuItem_20 = new MenuItem(menu_4, SWT.NONE);
//		menuItem_20.setText("Menu Item");
//
//		final MenuItem menuItem_10 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_10.setText("Fahrzeug");
//
//		final Menu menu_5 = new Menu(menuItem_10);
//		menuItem_10.setMenu(menu_5);
//
//		final MenuItem menuItem_21 = new MenuItem(menu_5, SWT.CASCADE);
//		menuItem_21.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(final SelectionEvent e) {
//			}
//		});
//		menuItem_21.setText("Fahrzeuge hervorheben");
//
//		final Menu menu_9 = new Menu(menuItem_21);
//		menuItem_21.setMenu(menu_9);
//
//		final MenuItem menuItem_24 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_24.setText("RTW's");
//
//		final MenuItem menuItem_22 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_22.setText("KTW's");
//
//		final MenuItem menuItem_23 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_23.setText("BKTW's");
//
//		new MenuItem(menu_9, SWT.SEPARATOR);
//
//		final MenuItem menuItem_25 = new MenuItem(menu_9, SWT.NONE);
//		menuItem_25.setText("Hervorhebung aufheben");
//
//		final MenuItem menuItem_11 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_11.setText("Verwaltung");
//
//		final Menu menu_6 = new Menu(menuItem_11);
//		menuItem_11.setMenu(menu_6);
//
//		final MenuItem menuItem_12 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_12.setText("Sonstiges");
//
//		final Menu menu_7 = new Menu(menuItem_12);
//		menuItem_12.setMenu(menu_7);
//
//		final MenuItem menuItem_26 = new MenuItem(menu_7, SWT.CHECK);
//		menuItem_26.setText("Sachen gibt's ;-)");
//
//		final MenuItem menuItem_27 = new MenuItem(menu_7, SWT.RADIO);
//		menuItem_27.setText("RadioButton");
//
//		final MenuItem menuItem_13 = new MenuItem(menu, SWT.CASCADE);
//		menuItem_13.setText("Hilfe");
//
//		final Menu menu_8 = new Menu(menuItem_13);
//		menuItem_13.setMenu(menu_8);
//
//		final MenuItem menuItem_15 = new MenuItem(menu_8, SWT.NONE);
//		menuItem_15.setText("Direkthilfe");
//
//		final MenuItem menuItem_14 = new MenuItem(menu_8, SWT.NONE);
//		menuItem_14.setText("Hotti's (neuer) Pager");
		//
	}
	
	
	
	/**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() 
    {
        //this.idText.setFocus();
    }

}


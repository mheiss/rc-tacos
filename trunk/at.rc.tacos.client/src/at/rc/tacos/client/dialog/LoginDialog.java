package at.rc.tacos.client.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import at.rc.tacos.factory.ImageFactory;

public class LoginDialog 
{
    private static Text txt_Password;
    private static Text txt_Username;
    private Display display;

    public LoginDialog(Display display) 
    {
        this.display = display;
    }

    public void createContents() 
    {
        final Shell shell = new Shell(display, SWT.ON_TOP);
        final FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 1;
        shell.setLayout(fillLayout);

        final Composite composite = new Composite(shell, SWT.NONE);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        composite.setLayout(gridLayout);

        // Setting the background of the composite
        // with the image background for login dialog
        final Label img_Label = new Label(composite, SWT.NONE);
        img_Label.setLayoutData(new GridData(195, 181));
        img_Label.setImage(ImageFactory.getInstance().getRegisteredImage("application.splash"));

        // Creating the composite which will contain the login related widgets
        final Composite cmp_Login = new Composite(composite, SWT.NONE);
        final RowLayout rowLayout = new RowLayout();
        rowLayout.fill = true;
        cmp_Login.setLayout(rowLayout);
        final GridData gridData = new GridData(GridData.FILL, GridData.FILL, false, false);
        gridData.widthHint = 196;
        cmp_Login.setLayoutData(gridData);

        // Label for the heading
        final CLabel clbl_UserLogin = new CLabel(cmp_Login, SWT.NONE);
        final RowData rowData = new RowData();
        rowData.width = 180;
        clbl_UserLogin.setLayoutData(rowData);
        clbl_UserLogin.setText("User Login");

        //Label for the username
        final CLabel clbl_Username = new CLabel(cmp_Login, SWT.NONE);
        final RowData rowData_1 = new RowData();
        rowData_1.width = 180;
        clbl_Username.setLayoutData(rowData_1);
        clbl_Username.setText("Username");

        // Textfield for the username
        txt_Username = new Text(cmp_Login, SWT.BORDER);
        final RowData rowData_2 = new RowData();
        rowData_2.width = 170;
        txt_Username.setLayoutData(rowData_2);

        // Label for the password
        final CLabel clbl_Password = new CLabel(cmp_Login, SWT.NONE);
        final RowData rowData_3 = new RowData();
        rowData_3.width = 180;
        clbl_Password.setLayoutData(rowData_3);
        clbl_Password.setText("Password");

        // Textfield for the password
        txt_Password = new Text(cmp_Login, SWT.BORDER);
        final RowData rowData_4 = new RowData();
        rowData_4.width = 170;
        txt_Password.setLayoutData(rowData_4);
        txt_Password.setEchoChar('*');

        // Composite to hold button as I want the
        // button to be positioned to my choice.
        final Composite cmp_ButtonBar = new Composite(cmp_Login, SWT.NONE);
        final RowData rowData_5 = new RowData();
        rowData_5.height = 38;
        rowData_5.width = 185;
        cmp_ButtonBar.setLayoutData(rowData_5);
        cmp_ButtonBar.setLayout(new FormLayout());

        // Button for login
        final Button btn_login = new Button(cmp_ButtonBar, SWT.FLAT);
        final FormData formData = new FormData();
        formData.bottom = new FormAttachment(0, 28);
        formData.top = new FormAttachment(0, 5);
        formData.right = new FormAttachment(100, -3);
        formData.left = new FormAttachment(100, -40);
        btn_login.setLayoutData(formData);
        btn_login.setText("Login");

        // Adding CLOSE action to this button.
        btn_login.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) 
            {
                shell.close();
//              In your case, you might wish
//              to call the authentication method.
            }
        });

        // Label for copyright info
        final CLabel clbl_Message = new CLabel(cmp_Login, SWT.NONE);
        clbl_Message.setAlignment(SWT.RIGHT);
        final RowData rowData_6 = new RowData();
        rowData_6.width = 188;
        clbl_Message.setLayoutData(rowData_6);
        clbl_Message.setText("My Custom Login Screen");

        //  Drawing a region which will
        //  Form the base of the login
        Region region = new Region();
        Rectangle pixel = new Rectangle(1, 1, 388, 180);
        region.add(pixel);
        shell.setRegion(region);

//      Positioning in the center of the screen.
//      This for the 1024 resolution only. Later,
//      I plan to make generic so, that it takes
//      the resolution and finds the center of
//      the screen.
        shell.setLocation(320,290);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        region.dispose();
    }

}

package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Test extends Dialog
{

    protected Object result;

    protected Shell shell;

    /**
     * Create the dialog
     * @param parent
     * @param style
     */
    public Test(Shell parent, int style)
    {
        super(parent, style);
    }

    /**
     * Create the dialog
     * @param parent
     */
    public Test(Shell parent)
    {
        this(parent, SWT.NONE);
    }

    /**
     * Open the dialog
     * @return the result
     */
    public Object open()
    {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed())
        {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return result;
    }

    /**
     * Create contents of the dialog
     */
    protected void createContents()
    {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setSize(500, 375);
        shell.setText("SWT Dialog");
        //
    }

}

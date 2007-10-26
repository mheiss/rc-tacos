package at.rc.tacos.client.view;

//rcp
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;
//client
import at.rc.tacos.client.controller.CreateItemAction;

/**
 * A simple form to enter a id
 * @author tmseidel
 */
public class FormView extends ViewPart
{
    private Text idText;
    
    public static final String ID = "at.rc.tacos.client.view.item_form"; 

    /**
     * Creates the view
     */
    public void createPartControl(Composite parent) 
    {
        parent.setLayout(new GridLayout(2,false));
        Label idLabel = new Label(parent, SWT.NONE);
        idLabel.setText("New ID:"); 
        GridData gd = new GridData(SWT.BEGINNING,SWT.CENTER,false,false);
        gd.widthHint = 50;
        idLabel.setLayoutData(gd);
        
        this.idText = new Text(parent,SWT.BORDER);
        gd = new GridData(SWT.FILL,SWT.CENTER,true,false);
        this.idText.setLayoutData(gd);
        
        Button button = new Button(parent,SWT.PUSH);
        button.setText("Create"); 
        gd = new GridData(SWT.RIGHT,SWT.BEGINNING,true,false);
        gd.horizontalSpan = 2;
        button.setLayoutData(gd);
        
        // Adding the controller
        button.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event event) {
                new CreateItemAction(FormView.this.idText.getText()).run();
            }
        });
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() 
    {
        this.idText.setFocus();
    }
}

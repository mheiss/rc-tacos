package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.factory.ImageFactory;

public class LogHeaderView extends ViewPart
{
    public static final String ID = "at.rc.tacos.client.view.logHeader"; 
    
    @Override
    public void createPartControl(Composite parent)
    {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginLeft = 5;
        parent.setLayout(layout);
        parent.setBackground(Util.getColor(255, 255, 255));
                
        Label imageLabel = new Label(parent,SWT.NONE);
        imageLabel.setBackground(Util.getColor(255, 255, 255));
        imageLabel.setImage(ImageFactory.getInstance().getRegisteredImage("image.log"));
        
        Composite comp = new Composite(parent,SWT.NONE);
        comp.setLayout(new GridLayout());
        comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        comp.setBackground(CustomColors.HEADING_COLOR);
        
        Label header = new Label(comp,SWT.NONE);
        header.setText("TACOS - LoggingService");
        header.setForeground(CustomColors.RED_COLOR);
        header.setBackground(CustomColors.HEADING_COLOR);
        header.setFont(new Font(null,"Times New Roman",20,SWT.BOLD));
        
        Label description = new Label(comp,SWT.NONE);
        description.setText("Eine Übersicht über alle aufgezeichneten Ereignisse und Fehlermeldungen.");
        description.setBackground(CustomColors.HEADING_COLOR);
        description.setFont(new Font(null,"Times New Roman",12,SWT.BOLD));
    }

    @Override
    public void setFocus() { }
}

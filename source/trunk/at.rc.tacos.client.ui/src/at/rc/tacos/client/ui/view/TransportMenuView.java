package at.rc.tacos.client.ui.view;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.custom.MyToolbarManager;
import at.rc.tacos.client.ui.perspectives.SwitchToTransportDialysePerspective;
import at.rc.tacos.client.ui.perspectives.SwitchToTransportJournalPerspective;
import at.rc.tacos.client.ui.perspectives.SwitchToTransportPerspective;
import at.rc.tacos.client.ui.perspectives.SwitchToTransportPrebookingPerspective;
import at.rc.tacos.client.ui.perspectives.TransportPerspective;
import at.rc.tacos.client.ui.utils.CustomColors;

/**
 * The top navigation for the {@link TransportPerspective}
 * 
 * @author Michael
 */
public class TransportMenuView extends ViewPart {

	public static final String ID = "at.rc.tacos.client.view.transportMenu";

	// properties
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(5, false);
		parent.setLayout(layout);
		parent.setBackground(CustomColors.COLOR_WHITE);

		// create a group for the perspective switch
		final Group createGroup = new Group(parent, SWT.NONE);
		createGroup.setLayout(new GridLayout());
		createGroup.setLayoutData(new GridData());
		createGroup.setBackground(CustomColors.COLOR_WHITE);
		createGroup.setText("Transportübersicht");

		// Create the toolbar
		final MyToolbarManager tbmCreate = new MyToolbarManager(new ToolBar(createGroup, SWT.FLAT));
		tbmCreate.getControl().setBackground(CustomColors.COLOR_WHITE);
		tbmCreate.getControl().setLayout(new GridLayout());
		tbmCreate.getControl().setLayoutData(new GridData());
		tbmCreate.add(new SwitchToTransportPerspective());
		tbmCreate.add(new SwitchToTransportPrebookingPerspective());
		tbmCreate.add(new SwitchToTransportDialysePerspective());
		tbmCreate.add(new SwitchToTransportJournalPerspective());
		tbmCreate.update(true);

		Composite comp1 = new Composite(parent, SWT.NONE);
		comp1.setBackground(CustomColors.COLOR_WHITE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		comp1.setLayoutData(gd);

		Label headerImageLabel = new Label(parent, SWT.NONE);
		headerImageLabel.setImage(imageRegistry.get("toolbar.logo"));
		gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		headerImageLabel.setLayoutData(gd);
	}

	@Override
	public void setFocus() {
	}
}

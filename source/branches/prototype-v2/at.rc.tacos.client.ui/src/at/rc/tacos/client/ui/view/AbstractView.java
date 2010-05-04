package at.rc.tacos.client.ui.view;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.platform.net.listeners.DataChangeListener;

/**
 * The {@code AbstractView} is the base class for all views. It takes care of
 * the livecycle of a view and provides callbacks to add and remove listeners.
 * 
 * @author Michael
 */
public abstract class AbstractView extends ViewPart implements PropertyChangeListener, DataChangeListener<Object> {

	// utility classes
	protected FormToolkit toolkit;
	protected Form form;

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		addListeners();
	}

	@Override
	public final void createPartControl(Composite parent) {
		// prepare the parent form
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		Composite client = form.getBody();
		createPartBody(client);
	}

	@Override
	public void dispose() {
		if (toolkit != null) {
			toolkit.dispose();
		}
		removeListeners();
		super.dispose();
	}

	/**
	 * Sets the title text and the image for the view. The image can be {@code
	 * null} to show no image.
	 */
	public void decorateView(FieldDecoration decoration) {
		form.setText(decoration.getDescription());
		form.setImage(decoration.getImage());
	}

	/**
	 * Callback method to create the body of the control.
	 */
	public abstract void createPartBody(Composite parent);

	/**
	 * Callback method that allows listener registration.
	 */
	public abstract void addListeners();

	/**
	 * Callback method that allows listener removement.
	 */
	public abstract void removeListeners();

}

package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.platform.net.handler.Handler;

/**
 * This implementation of <code>IStructuredContentProvider</code> handles the
 * case where the viewer input is {@link Handler} instance.
 */
@SuppressWarnings("unchecked")
public class HandlerContentProvider implements IStructuredContentProvider {

	/**
	 * Returns the elements from the handler, which must be either an array or a
	 * <code>Collection</code>.
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof Handler)) {
			throw new IllegalArgumentException("The input for this content provider must be an handler");
		}
		return ((Handler) inputElement).toArray();
	}

	/**
	 * This implementation does nothing.
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing.
	}

	/**
	 * This implementation does nothing.
	 */
	@Override
	public void dispose() {
		// do nothing.
	}
}

package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>ArrayContentProvider</code> is a structured content provider that
 * takes an <code>Array</code> as input.
 * 
 * @author Michael
 */
public class ArrayContentProvider implements IStructuredContentProvider {

	private Logger log = LoggerFactory.getLogger(ArrayContentProvider.class);

	@Override
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof Object[])) {
			String message = "Expected '" + Object[].class.getName() + "' but was '" + inputElement.getClass().getName() + "'";
			log.error(message);
			throw new IllegalArgumentException(message);
		}
		Object[] objects = (Object[]) inputElement;
		return objects;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

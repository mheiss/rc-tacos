package at.rc.tacos.client.ui.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>ListContentProvider</code> is a structured content provider that
 * takes an <code>List</code> as input.
 * 
 * @author Michael
 */
@SuppressWarnings("unchecked")
public class ListContentProvider implements IStructuredContentProvider {

	private Logger log = LoggerFactory.getLogger(ArrayContentProvider.class);

	@Override
	public Object[] getElements(Object inputElement) {
		if (!(inputElement instanceof List)) {
			String message = "Expected '" + List.class.getName() + "' but was '" + inputElement.getClass().getName() + "'";
			log.error(message);
			throw new IllegalArgumentException(message);
		}
		List<?> list = (List) inputElement;
		return list.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

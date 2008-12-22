package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.ServerInfo;

public class LoginTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	// the columns of the table
	private static final int COLUMN_DEF = 0;
	private static final int COLUMN_HOST = 1;
	private static final int COLUMN_PORT = 2;
	private static final int COLUMN_DESC = 3;

	// the image registry to use
	private final ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public String getColumnText(Object element, int columnIndex) {
		ServerInfo info = (ServerInfo) element;
		switch (columnIndex) {
			case COLUMN_HOST:
				return info.getHostName();
			case COLUMN_PORT:
				return String.valueOf(info.getPort());
			case COLUMN_DESC:
				return info.getDescription();
			default:
				return new String();
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		ServerInfo info = (ServerInfo) element;
		switch (columnIndex) {
			case COLUMN_DEF:
				return info.isDefaultServer() ? imageRegistry.get("server.default") : imageRegistry.get("empty.image24");
			default:
				return null;
		}
	}
}

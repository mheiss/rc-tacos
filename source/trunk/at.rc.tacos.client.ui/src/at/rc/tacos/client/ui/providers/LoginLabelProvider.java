package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.ServerInfo;

public class LoginLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		ServerInfo serverInfo = (ServerInfo) element;
		return serverInfo.getDescription();
	}

}

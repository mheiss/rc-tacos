package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.MobilePhoneDetail;

public class MobilePhoneLabelProvider extends LabelProvider {

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		MobilePhoneDetail phone = (MobilePhoneDetail) object;
		return phone.getMobilePhoneName() + "-" + phone.getMobilePhoneNumber();
	}
}

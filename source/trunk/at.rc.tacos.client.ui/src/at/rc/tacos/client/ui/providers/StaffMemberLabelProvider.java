package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.StaffMember;

public class StaffMemberLabelProvider extends LabelProvider {

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		StaffMember member = (StaffMember) object;
		return member.getLastName() + " " + member.getFirstName();
	}
}

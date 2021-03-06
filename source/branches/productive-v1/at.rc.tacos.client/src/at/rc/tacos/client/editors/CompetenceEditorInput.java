/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import at.rc.tacos.model.Competence;

public class CompetenceEditorInput implements IEditorInput {

	// properties
	private Competence competence;
	private boolean isNew;

	/**
	 * Default class constructor for the competence editor.
	 * 
	 * @param competence
	 *            the competence to edit
	 * @param isNew
	 *            a flag to determine whether the competence is new
	 */
	public CompetenceEditorInput(Competence competence, boolean isNew) {
		this.competence = competence;
		this.isNew = isNew;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		if (isNew)
			return "Neue Kompetenz";
		return competence.getCompetenceName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Kompetenz: " + competence.getCompetenceName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class arg0) {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof CompetenceEditorInput) {
			CompetenceEditorInput otherEditor = (CompetenceEditorInput) other;
			return competence.equals(otherEditor.getCompetence());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return competence.hashCode();
	}

	/**
	 * Returns the competence managed by the editor
	 * 
	 * @return the managed competence
	 */
	public Competence getCompetence() {
		return competence;
	}

	/**
	 * Returns whether or not the competence is new.
	 * 
	 * @return true if the competence is created new
	 */
	public boolean isNew() {
		return isNew;
	}
}

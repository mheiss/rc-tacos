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
package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.model.Competence;

public class CompetenceLabelProvider extends LabelProvider {

	/**
	 * Returns the image to use for this element.
	 * 
	 * @param object
	 *            the object to get the image for
	 * @return the image to use
	 */
	@Override
	public Image getImage(Object object) {
		return null;
	}

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		Competence competence = (Competence) object;
		return competence.getCompetenceName();
	}
}

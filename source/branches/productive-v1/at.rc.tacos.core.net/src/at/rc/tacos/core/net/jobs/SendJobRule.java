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
package at.rc.tacos.core.net.jobs;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * This rule is for the send job so that the internal job manager ensures that
 * only one job of this type is running at the same time.
 * 
 * @author Michael
 */
public class SendJobRule implements ISchedulingRule {

	@Override
	public boolean contains(ISchedulingRule otherRule) {
		return otherRule.getClass() == SendJobRule.class;
	}

	@Override
	public boolean isConflicting(ISchedulingRule otherRule) {
		return otherRule.getClass() == SendJobRule.class;
	}
}

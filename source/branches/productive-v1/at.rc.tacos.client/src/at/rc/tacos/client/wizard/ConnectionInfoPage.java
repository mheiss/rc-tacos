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
package at.rc.tacos.client.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import at.rc.tacos.core.net.NetSource;
import at.rc.tacos.factory.ImageFactory;

public class ConnectionInfoPage extends WizardPage {

	// properties
	private Composite container;
	private CLabel infoText;
	private CLabel connectionStatus;

	/**
	 * Default class constructor
	 */
	public ConnectionInfoPage() {
		super("");
	}

	/**
	 * Callback method to create the page content and initialize it
	 */
	@Override
	public void createControl(Composite parent) {
		// the container
		container = new Composite(parent, SWT.NULL);
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		container.setLayout(fillLayout);

		// the status of the connection
		connectionStatus = new CLabel(container, SWT.LEFT);
		// the image to display
		if (NetSource.getInstance().getConnection() != null) {
			setTitle("Verbindung hergestellt");
			setDescription("Die Ausf�hrung des Assistenten ist nicht notwendig");
			// show true image
			connectionStatus.setText("Es besteht bereits eine Verbindung zum Server.\n" + "Die Ausf�hrung des Assistenten is nicht n�tig");
			connectionStatus.setImage(ImageFactory.getInstance().getRegisteredImage("wizars.server.connected"));
		}
		else {
			setTitle("Verbindung zum Server unterbrochen");
			setDescription("Mit diesem Assistenten k�nnen sie eine neue Verbindung aufbauen");
			// show true image
			connectionStatus.setText("Verbindung zum Server wurde unterbrochen.\n"
					+ "Mit diesem Wizard kann die Verbindung zum Server wiederhergstellt werden");
			connectionStatus.setImage(ImageFactory.getInstance().getRegisteredImage("wizard.server.disconnected"));

			// the label
			infoText = new CLabel(container, SWT.LEFT);
			infoText.setText("Klicken Sie auf weiter um einen neuen Server auszuw�hlen.");
		}
		// Required to avoid an error in the system
		setControl(container);
	}

	@Override
	public boolean canFlipToNextPage() {
		// only go to the next page, if we do not have a connection
		if (NetSource.getInstance().getConnection() != null)
			return false;

		return true;
	}

	/**
	 * Returns the top widget of the application.
	 * 
	 * @return the top widget
	 */
	@Override
	public Control getControl() {
		return container;
	}
}

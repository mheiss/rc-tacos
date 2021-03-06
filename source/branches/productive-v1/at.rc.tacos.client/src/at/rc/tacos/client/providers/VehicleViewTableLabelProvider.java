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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

public class VehicleViewTableLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport, ITableFontProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COLUMN_STATUS = 2;

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		VehicleDetail vehicle = (VehicleDetail) element;

		// determine the column and return a image if needed
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (lockManager.containsLock(VehicleDetail.ID, vehicle.getVehicleName()))
					return ImageFactory.getInstance().getRegisteredImage("resource.lock");
				else
					return ImageFactory.getInstance().getRegisteredImage("resource.nothing18");
			case COLUMN_STATUS:
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_GREEN)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.green");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_NA)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.na");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_YELLOW)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.yellow");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPROT_STATUS_RED)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.red");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_BLUE)
					return ImageFactory.getInstance().getRegisteredImage("vehicle.status.blue");
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		VehicleDetail detail = (VehicleDetail) element;

		switch (columnIndex) {
			case COLUMN_NAME:
				return detail.getVehicleName();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return CustomColors.VEHICLE_TABLE;
	}
}

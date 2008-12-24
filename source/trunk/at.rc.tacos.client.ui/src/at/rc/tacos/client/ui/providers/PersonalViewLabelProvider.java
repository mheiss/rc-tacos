package at.rc.tacos.client.ui.providers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.client.ui.ListenerConstants;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.ui.view.PrebookingView;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.VehicleDetail;

public class PersonalViewLabelProvider extends BaseLabelProvider implements PropertyChangeListener, ITableLabelProvider, ITableColorProvider, ITableFontProvider {

	private Logger log = LoggerFactory.getLogger(PrebookingView.class);

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_STANDBY = 1;
	public static final int COLUMN_NOTES = 2;
	public static final int COLUMN_NAME = 3;
	public static final int COLUMN_PLANED_WORK_TIME = 4;
	public static final int COLUMN_CHECK_IN = 5;
	public static final int COLUMN_CHECK_OUT = 6;
	public static final int COLUMN_SERVICE_TYPE = 7;
	public static final int COLUMN_JOB = 8;
	public static final int COLUMN_VEHICLE = 9;

	// the vehicle handler
	private VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	// the current display date
	private Calendar displayDate = Calendar.getInstance();

	/**
	 * Default class constructor to register the listener
	 */
	public PersonalViewLabelProvider() {
		UiWrapper.getDefault().registerListener(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		UiWrapper.getDefault().removeListener(this);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		RosterEntry entry = (RosterEntry) element;
		// determine the colum and return a image if needed
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (entry.isLocked())
					return imageRegistry.get("resource.lock");
				return imageRegistry.get("empty.image24");
				// show house symbol if the person is at home
			case COLUMN_STANDBY:
				if (entry.getStandby())
					return imageRegistry.get("resource.yes");
				return null;
				// show info symbol if there are notes
			case COLUMN_NOTES:
				if (entry.hasNotes())
					return imageRegistry.get("resource.info");
				return null;
				// show a symbol if the planned time is not handled
			case COLUMN_CHECK_IN:
				// user is not cheked in but he should
				if (entry.getRealStartOfWork() != 0)
					return null;
				if (entry.getPlannedStartOfWork() < new Date().getTime())
					return imageRegistry.get("resource.warning");
				return null;
			case COLUMN_CHECK_OUT:
				// user is not checked out but he should
				if (entry.getRealEndOfWork() != 0)
					return null;
				if (entry.getPlannedEndOfWork() < new Date().getTime())
					return imageRegistry.get("resource.warning");
				return null;
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		RosterEntry entry = (RosterEntry) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		// setup the current day and the next day
		Date thisDay = DateUtils.truncate(displayDate, Calendar.DAY_OF_MONTH).getTime();
		Date nextDay = DateUtils.addDays(thisDay, 1);

		String plannedStart;
		String plannedEnd;

		switch (columnIndex) {
			case COLUMN_LOCK:
				return null;
			case COLUMN_STANDBY:
				return null;
			case COLUMN_NOTES:
				return null;
			case COLUMN_NAME:
				return entry.getStaffMember().getLastName() + " " + entry.getStaffMember().getFirstName();

			case COLUMN_PLANED_WORK_TIME:
				if (entry.getPlannedStartOfWork() < thisDay.getTime())
					plannedStart = "00:00";
				else
					plannedStart = sdf.format(entry.getPlannedStartOfWork());
				if (entry.getPlannedEndOfWork() > nextDay.getTime())
					plannedEnd = "00:00";
				else
					plannedEnd = sdf.format(entry.getPlannedEndOfWork());
				return plannedStart + " - " + plannedEnd;
			case COLUMN_CHECK_IN:
				if (entry.getRealStartOfWork() != 0)
					return sdf.format(entry.getRealStartOfWork());
				else
					return "";
			case COLUMN_CHECK_OUT:
				if (entry.getRealEndOfWork() != 0)
					return sdf.format(entry.getRealEndOfWork());
				else
					return "";
			case COLUMN_SERVICE_TYPE:
				return entry.getServicetype().getServiceName();
			case COLUMN_JOB:
				return entry.getJob().getJobName();
			case COLUMN_VEHICLE:
				VehicleDetail detail = vehicleHandler.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId());
				if (detail != null && entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0) {
					return detail.getVehicleName();
				}
				return null;
			default:
				return null;
		}
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		RosterEntry entry = (RosterEntry) element;
		if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0
				&& vehicleHandler.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId()) == null)
			return null;// black
		if (entry.getRealStartOfWork() == 0 && entry.getRealEndOfWork() == 0)
			return CustomColors.DARK_GREY_COLOR;
		if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() != 0)
			return CustomColors.DARK_GREY_COLOR;
		if (entry.getRealStartOfWork() != 0 && entry.getRealEndOfWork() == 0
				&& vehicleHandler.getVehicleOfStaff(entry.getStaffMember().getStaffMemberId()) != null)
			return CustomColors.BACKGROUND_BLUE;
		return CustomColors.BACKGROUND_RED;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return CustomColors.VEHICLE_TABLE;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String event = evt.getPropertyName();
		Object newValue = evt.getNewValue();

		if (ListenerConstants.ROSTER_DATE_CHANGED.equalsIgnoreCase(event)) {
			if (!(newValue instanceof Calendar)) {
				log.error("Expected 'Calendar' but was " + newValue == null ? "null" : newValue.getClass().getName());
			}
			displayDate = (Calendar) newValue;
		}
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}
}

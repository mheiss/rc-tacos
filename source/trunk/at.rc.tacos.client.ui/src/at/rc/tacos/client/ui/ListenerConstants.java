package at.rc.tacos.client.ui;

import java.util.Calendar;

import at.rc.tacos.client.providers.TransportViewFilter;
import at.rc.tacos.client.ui.view.FilterView;
import at.rc.tacos.client.ui.view.InfoView;

/**
 * This interface holds the available custom listeners.
 * <p>
 * Not to be indeed to be implemented by clients
 * </p>
 * 
 * @author mheiss
 */
public interface ListenerConstants {

	/**
	 * Indicates that the selected date in the {@link InfoView} has changed.
	 * <p>
	 * The <code>PropertyChangeEvent</code> contains the selected date as
	 * {@link Calendar} instance.
	 * </p>
	 */
	public final static String ROSTER_DATE_CHANGED = "RosterEntry.SelectedDate";

	/**
	 * Indicates that the selected date in the {@link FilterView} has changed.
	 * <p>
	 * The <code>PropertyChangeEvent</code> contains the selected date as
	 * {@link Calendar} instance.
	 * </p>
	 */
	public final static String TRANSPORT_DATE_CHANGED = "TransportFilter.SelectedDate";

	/**
	 * Indicates that the {@link TransportViewFilter} has changed and the
	 * viewers should apply the new filter.
	 * <p>
	 * The <code>PropertyChangeEvent</code> contains the new {@code
	 * ViewerFilter} or {@code null} if the filter should be removed.
	 * </p>
	 */
	public final static String TRANSPORT_FILTER_CHANGED = "TransportFilter.SelectedDate";

}

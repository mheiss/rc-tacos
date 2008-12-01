package at.rc.tacos.client.ui;

import java.util.Calendar;

import at.rc.tacos.client.view.InfoView;

/**
 * This interface holds the available custom listeners.
 * <p>
 * Not to be indeed to be implemented by clients
 * </p>
 * 
 * @author mheiss
 * 
 */
public interface ListenerConstants {

    /**
     * Indicates that the selected date in the {@link InfoView} has changed.
     * <p>
     * The <code>PropertyChangeEvent</code> contains the selected date as {@link Calendar} instance.
     * </p>
     */
    public final static String ROSTER_DATE_CHANGED = "RosterEntry.SelectedDate";

}

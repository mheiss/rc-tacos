package at.rc.tacos.client.ui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * Provides the colors and the fonts to use in the application
 * 
 * @author Michael
 */
public final class CustomColors {

	// NEW
	public static final Font APPLICATION_HEADER_FONT = new Font(null, "Arial", 10, SWT.BOLD);

	// NEW
	public static final Color COLOR_WHITE = new Color(null, 255, 255, 255);
	public static final Color COLOR_GREY = new Color(null, 128, 128, 128);
	public static final Color COLOR_BLUE = new Color(null, 0, 0, 255);
	public static final Color COLOR_LIGHT_BLUE = new Color(null, 128, 128, 255);
	public static final Color COLOR_DARK_BLUE = new Color(null, 25, 25, 112);

	/** Set the default font */
	public static final Font SUBHEADER_FONT = new Font(null, "Arial", 8, SWT.ITALIC);
	public static final Font VEHICLE_NAME = new Font(null, "Arial", 18, SWT.BOLD);
	public static final Font VEHICLE_TEXT = new Font(null, "Arial", 8, SWT.NORMAL);
	public static final Font VEHICLE_TABLE = new Font(null, "Arial", 9, SWT.NORMAL);

	/** Set the main color scheme */
	public static final Color RED_COLOR = new Color(null, 186, 15, 23);
	public static final Color GREY_COLOR = new Color(null, 192, 192, 192);
	public static final Color HEADING_COLOR = new Color(null, 255, 255, 255);
	public static final Color SECTION_BACKGROUND = new Color(null, 255, 255, 255);
	public static final Color DARK_GREY_COLOR = new Color(null, 128, 128, 128);
	public static final Color BACKGROUND_RED = new Color(null, 250, 128, 114);
	public static final Color BACKGROUND_BLUE = new Color(null, 102, 153, 255);
	public static final Color GREY_COLOR2 = new Color(null, 216, 216, 216);
}

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
	public static final Font APPLICATION_DATA_FONT = new Font(null, "Arial", 9, SWT.NORMAL);
	public static final Font APPLICATION_ITALIC_FONT = new Font(null, "Arial", 8, SWT.ITALIC);

	// NEW
	public static final Color COLOR_WHITE = new Color(null, 255, 255, 255);
	public static final Color COLOR_GREY = new Color(null, 128, 128, 128);
	public static final Color COLOR_BLUE = new Color(null, 0, 0, 255);
	public static final Color COLOR_LIGHT_BLUE = new Color(null, 128, 128, 255);
	public static final Color COLOR_DARK_BLUE = new Color(null, 25, 25, 112);
	public static final Color COLOR_GREY_2 = new Color(null, 216, 216, 216);
	public static final Color COLOR_RED = new Color(null, 186, 15, 23);

	public static final Color BACKGROUND_RED = new Color(null, 250, 128, 114);
	public static final Color BACKGROUND_BLUE = new Color(null, 102, 153, 255);
}

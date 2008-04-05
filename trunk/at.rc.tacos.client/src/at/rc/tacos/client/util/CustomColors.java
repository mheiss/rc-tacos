package at.rc.tacos.client.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;

/**
 * Util class for the colors of the forms 
 * @author Michael
 */
public final class CustomColors 
{ 
    /** Set the default font */
    public static final Font SUBHEADER_FONT = new Font(null,"Arial", 8, SWT.ITALIC);
    public static final Font VEHICLE_NAME = new Font(null,"Arial", 18, SWT.BOLD);
    public static final Font VEHICLE_TEXT = new Font(null,"Arial", 10, SWT.BOLD);
    
    /** Set the main color scheme */
    public static final Color RED_COLOR = new Color(null,186,15,23);
    public static final Color GREY_COLOR = new Color(null,192,192,192);
    public static final Color HEADING_COLOR = new Color(null,255,255,255);
    public static final Color SECTION_BACKGROUND = new Color(null,255,255,255);
    public static final Color DARK_GREY_COLOR = new Color(null,128,128,128);
    public static final Color BACKGROUND_RED = new Color(null,250,128,114);
    public static final Color BACKGROUND_BLUE = new Color(null,102,153,255);
    public static final Color GREY_COLOR2 = new Color(null, 211,211,211);
    /** The color for the vehicle composites */
    public static final Color COLOR_BLUE = Util.getColor(209, 229, 249);
    public static final Color COLOR_GRAY =  Util.getColor(228, 236, 238);
    public static final Color COLOR_DARK = Util.getColor(0, 0, 128);
    public static final Color COLOR_NAME = Util.getColor(0, 0, 102);
    public static final Color COLOR_WHITE = Util.getColor(128, 128, 128);
    
    private static FormColors formColors;
    
    public static FormColors FORM_COLOR(final Display display) 
    {
        if (formColors == null) 
        {
            formColors = new FormColors(display);
            formColors.createColor( IFormColors.H_GRADIENT_START, GREY_COLOR2.getRGB());
            formColors.createColor( IFormColors.H_GRADIENT_END, GREY_COLOR2.getRGB());
            formColors.createColor( IFormColors.H_BOTTOM_KEYLINE1, GREY_COLOR.getRGB());
            formColors.createColor( IFormColors.H_BOTTOM_KEYLINE2, GREY_COLOR2.getRGB());
            formColors.createColor( IFormColors.TITLE, HEADING_COLOR.getRGB());
            formColors.createColor( IFormColors.TB_BG, SECTION_BACKGROUND.getRGB());
            formColors.createColor( IFormColors.TB_BORDER, SECTION_BACKGROUND.getRGB());
            formColors.createColor( IFormColors.SEPARATOR,GREY_COLOR2.getRGB()); 
        }
        return formColors;
    }
}

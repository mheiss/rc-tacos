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
    public static final Font SUBHEADER_FONT = new Font(null,"Arial", 10, SWT.BOLD);
    /** Set the main color scheme */
    public static final Color RED_COLOR = new Color(null,186,15,23);
    public static final Color GREY_COLOR = new Color(null,236,233,226);
    public static final Color HEADING_COLOR = new Color(null,255,255,255);
    public static final Color SECTION_BACKGROUND = new Color(null,255,255,255);
    public static final Color DARK_GREY_COLOR = new Color(null, 60,179,113);
    public static final Color BACKGROUND_RED = new Color(null,250,128,114);
    public static final Color BACKGROUND_BLUE = new Color(null,102,153,255);
    
    private static FormColors formColors;
    
    public static FormColors FORM_COLOR(final Display display) 
    {
        if (formColors == null) 
        {
            formColors = new FormColors(display);
            formColors.createColor( IFormColors.H_GRADIENT_START, RED_COLOR.getRGB());
            formColors.createColor( IFormColors.H_GRADIENT_END, RED_COLOR.getRGB());
            formColors.createColor( IFormColors.H_BOTTOM_KEYLINE1, GREY_COLOR.getRGB());
            formColors.createColor( IFormColors.H_BOTTOM_KEYLINE2, RED_COLOR.getRGB());
            formColors.createColor( IFormColors.TITLE, HEADING_COLOR.getRGB());
            formColors.createColor( IFormColors.TB_BG, SECTION_BACKGROUND.getRGB());
            formColors.createColor( IFormColors.TB_BORDER, SECTION_BACKGROUND.getRGB());
            formColors.createColor( IFormColors.SEPARATOR, RED_COLOR.getRGB()); 
        }
        return formColors;
    }
}

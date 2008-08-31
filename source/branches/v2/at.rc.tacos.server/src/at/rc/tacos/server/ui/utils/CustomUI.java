package at.rc.tacos.server.ui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

/**
 * This class provides custum reusable colors for the application
 * @author Michael
 */
public class CustomUI 
{
	//define common fonts
	public final static Font HEADLINE_FONT = new Font(null,"Verdana",15,SWT.BOLD);
	public final static Font DESCRIPTION_FONT = new Font(null,"Verdana",10,SWT.NORMAL);
	public final static Font LINK_FONT = new Font(null,"Verdana",8,SWT.NONE);
	public final static Font PREFERENCE_FONT = new Font(null,"Verdana",10,SWT.BOLD);
	
	//define common colors
	public final static Color GRAY_COLOR = new Color(null,new RGB(128,128,128));
	public final static Color RED_COLOR = new Color(null,new RGB(193,39,30));
	public final static Color BLUE_COLOR = new Color(null,new RGB(0,0,255));
	public final static Color HOVER_COLOR = new Color(null,new RGB(128,128,255));
	public final static Color WHITE_COLOR = new Color(null,new RGB(255,255,255));
}

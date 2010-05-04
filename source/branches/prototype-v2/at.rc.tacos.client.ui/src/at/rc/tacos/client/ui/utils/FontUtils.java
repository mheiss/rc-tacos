package at.rc.tacos.client.ui.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * The <code>FontUtils</code> contains static helper method for {@link Font}
 * operations
 * 
 * @author Michael
 */
public class FontUtils {

	/**
	 * Sets the {@link SWT#BOLD} flag on the given font.
	 * 
	 * @param font
	 *            the existing font
	 * @return a new font with the bold flag set
	 */
	public static Font getBold(Font font) {
		FontData[] datas = font.getFontData();
		for (FontData data : datas) {
			data.setStyle(SWT.BOLD);
		}
		return new Font(Display.getCurrent(), datas);
	}

}

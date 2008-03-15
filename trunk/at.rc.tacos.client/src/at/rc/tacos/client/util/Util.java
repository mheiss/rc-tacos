package at.rc.tacos.client.util;

import org.eclipse.swt.graphics.Color;

/**
 * Contains convinient methods for common use
 * @author Michael
 */
public class Util 
{
	/**
	 * Returns the color object for the given RGB values
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return the color object
	 */
	public final static Color getColor(int r,int g,int b)
	{
		return new Color(null,r,g,b);
	}
}

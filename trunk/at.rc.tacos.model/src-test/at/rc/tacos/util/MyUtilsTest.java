package at.rc.tacos.util;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Testclass for the utils
 * @author Michael
 */
public class MyUtilsTest 
{
	/**
	 * Testcase 1: The dates are equal
	 */
	@Test
	public void testEqualsDate()
	{
		//prepare the timestamps
		long timestamp1 = new Date().getTime();
		long timestamp2 = new Date().getTime();
		
		//test
		boolean result = MyUtils.isEqualDate(timestamp1, timestamp2);
		
		Assert.assertTrue(result);
	}
	
	/**
	 * Testcase 2: The dates are not equal
	 */
	@Test
	public void testNotEqualsDate()
	{
		//prepare the timestamps
		Calendar cal1 = Calendar.getInstance();
		cal1.set(2008, 01, 10);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(2008, 01, 11);
		
		boolean result = MyUtils.isEqualDate(cal1.getTimeInMillis(), cal2.getTimeInMillis());
		
		Assert.assertFalse(result);
	}
}

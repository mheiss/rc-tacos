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
     * Testcase 1a: The dates are not equal
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

    /**
     * Testcase 2: test a valid year
     */
    @Test 
    public void testValidYear()
    {
        int year = 2008;
        boolean result = MyUtils.isValidYear(year);
        Assert.assertTrue(result);
    }


    /**
     * Testcase 2a: test a invalid year
     */
    @Test 
    public void testInValidYear()
    {
        int year = 6984;
        boolean result = MyUtils.isValidYear(year);
        Assert.assertFalse(result);
    }
    
    /**
     * Testcase 3: test a valid date
     */
    @Test 
    public void testValidDate()
    {
        Calendar cal = Calendar.getInstance();
        boolean result = MyUtils.isValidDate(cal.getTimeInMillis());
        Assert.assertTrue(result);
    }


    /**
     * Testcase 3a: test a invalid date
     */
    @Test 
    public void testInValidDate()
    {
        boolean result = MyUtils.isValidDate(-48);
        Assert.assertFalse(result);
    }
}

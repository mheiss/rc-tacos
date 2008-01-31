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
     * Testcase 4: Convert string date to timestamp and back
     */
    @Test
    public void testConvertDate()
    {
        String date = "27-01-2008";
        long timestamp = MyUtils.stringToTimestamp(date, MyUtils.dateFormat);
        Assert.assertEquals("27-01-2008", MyUtils.timestampToString(timestamp, MyUtils.dateFormat));
    }
    
    /**
     * Testcase 5: Convert timestamp to string and back
     */
    @Test
    public void testConvertTimestamp()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2008, 01, 28, 15, 30);
        long timestamp = cal.getTimeInMillis();
        String date = MyUtils.timestampToString(timestamp, MyUtils.timeAndDateFormat);
        Assert.assertEquals(timestamp, MyUtils.stringToTimestamp(date, MyUtils.timeAndDateFormat));
    }
    
//    @Test 
//    public void testSQLFormat()
//    {
//    	String dateTimeString1 = "28-01-2008";
//    	long dateTimeLong1 = MyUtils.stringToTimestamp(dateTimeString1, MyUtils.dateFormat);//output: 1201474800000
//    	System.out.println("dateTimeLong1: " +dateTimeLong1);
//    	String dateTimeString2 = MyUtils.timestampToString(dateTimeLong1, MyUtils.dateFormat);//output: 28-01-2008
//    	System.out.println("dateTimeString1: " +dateTimeString1);
//    	
//    	System.out.println("dateTimeString2: " +dateTimeString2);
//    	
//    	String dateTimeString3 = MyUtils.timestampToString(dateTimeLong1, MyUtils.sqlDate);//output: 20080128
//    	System.out.println("dateTimeString3 (sqlDate): " +dateTimeString3);
//    	
//    	String dateTimeString4 = MyUtils.timestampToString(dateTimeLong1, MyUtils.sqlDateTime);//output: 20080128000000
//    	System.out.println("dateTimeString4 (sqlDateTime): " +dateTimeString4);
//    	
//    	long dblongDateUndTime = 20080128120000L;
//    	
//    
//    	long dblongAfter = MyUtils.stringToTimestamp("2008-01-28", MyUtils.sqlDate);
//    	System.out.println("db long after als long: " +dblongAfter);
//    	
//    	String dateTimeString5 = MyUtils.timestampToString(dblongAfter, MyUtils.dateFormat);
//    	System.out.println("dateTimeString5 (aus dblongAfter): " +dateTimeString5);
//    	
//    }
}

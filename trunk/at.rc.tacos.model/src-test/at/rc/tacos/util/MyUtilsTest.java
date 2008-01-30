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

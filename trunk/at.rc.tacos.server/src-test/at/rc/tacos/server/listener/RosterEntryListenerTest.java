package at.rc.tacos.server.listener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.server.dao.DaoService;

/**
 * Test cases for the roster entry listener
 * @author Michael
 */
public class RosterEntryListenerTest
{
    private static RosterEntryListener listener;
    private ArrayList<AbstractMessage> result = new ArrayList<AbstractMessage>();
    
    @BeforeClass
    public static void setUpClass()
    {
        listener = new RosterEntryListener();
        //add test data
        DaoService.getInstance().initData();
    }
    
    @Test
    public void listAllTest()
    {
        result = listener.handleListingRequest(null);
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void listbyDate()
    {
        //list all entries today
        Calendar cal = Calendar.getInstance();
        String timestamp = String.valueOf(cal.getTimeInMillis());
        QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER,timestamp);
        //query
        result = listener.handleListingRequest(filter);
        Assert.assertEquals(1, result.size());
    }
    
    @Test(expected = ParseException.class)
    public void listByInvalidDate()
    { 
        result = listener.handleListingRequest(new QueryFilter(IFilterTypes.DATE_FILTER,"hallo"));
    }
}

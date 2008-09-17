package at.rc.tacos.platform.log.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just a few simple test cases to enshure that the logging framework is working
 * @author Michael
 */
public class SimpleLogTest {
	
	@Test
    public void testSLF4JLog() {
        Logger log = LoggerFactory.getLogger(SimpleLogTest.class);
        String msg = "log from slf4j";
        log.info(msg);
    }

}

package at.redcross.tacos.tests.parser;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.redcross.tacos.web.parser.RosterParser;
import at.redcross.tacos.web.parser.RosterParserEntry;

public class RosterParserTests extends BaseFileTest {

    private final static Logger logger = LoggerFactory.getLogger(RosterParserTests.class);

    @Test
    public void testImportFile() throws Exception {
        // the file to test
        File file = getFile("test-data/rosterImportTest.xls");
        if (!file.exists()) {
            throw new RuntimeException("Missing test data");
        }

        // create the parser and go on
        long start = System.currentTimeMillis();
        RosterParser parser = new RosterParser(file);
        Collection<RosterParserEntry> entries = parser.parse();
        long duration = System.currentTimeMillis() - start;
        logger.debug("Parsing done in '" + duration + "' ms");
        logger.info("Found '" + entries.size() + "' records");
    }

}

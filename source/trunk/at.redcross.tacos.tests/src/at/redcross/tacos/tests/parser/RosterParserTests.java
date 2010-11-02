package at.redcross.tacos.tests.parser;

import java.io.File;
import java.util.Collection;

import org.junit.Test;

import at.redcross.tacos.web.parser.RosterParser;
import at.redcross.tacos.web.parser.RosterParserEntry;

public class RosterParserTests extends BaseFileTest {

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
        System.out.println("Parsing done in '" + duration + "' ms");
        System.out.println("Found '" + entries.size() + "' records");
    }

}

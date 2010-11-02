package at.redcross.tacos.web.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * The {@code RosterParser} provides static helper methods to parses a given
 * file and return all {@code RosterParserEntry} that are found.
 */
public class RosterParser {

    /** row number containing the <tt>personalNumber</tt> */
    private final static int ROW_PERSONAL_ID = 5;

    /** row number containing the <tt>locationName</tt> */
    private final static int ROW_LOCATION_NAME = 6;

    /** row number containing the <tt>assignmentName</tt> */
    private final static int ROW_ASSIGNMENT_NAME = 7;

    /** row number containing the <tt>serviceTypeName</tt> */
    private final static int ROW_SERVICE_NAME = 8;

    /** row range containing START (including) and END (excluding) */
    private final static int[] ROW_ENTRY_RANGE = { 10, 41 };

    /** cell number (zero based) containing the starting point */
    private final static int CELL_START = 2;

    /** cell number (zero based) containing the <tt>date</tt> */
    private final static int CELL_DATE = 1;

    /** offset for the next relevant cell index */
    private final static int CELL_OFFSET = 4;

    /** the workbook we are working with */
    private final Workbook workbook;

    /** format the time of the entry */
    private final DateFormat sdf = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMANY);

    /**
     * Creates a new {@code RosterParser} using the given file
     * 
     * @param file
     *            the file to parse
     */
    public RosterParser(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("The input file is null");
        }
        this.workbook = createWorkbook(file);
    }

    /**
     * Processes and parses the given file to extract all entries.
     * 
     * @return the contained entries
     */
    public Collection<RosterParserEntry> parse() {
        Sheet sheet = workbook.getSheetAt(0);
        return processSheet(sheet);
    }

    // ---------------------------------
    // Private API and Helper methods
    // ---------------------------------
    /** Creates and returns a workbook for the given file */
    private Workbook createWorkbook(File file) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return new HSSFWorkbook(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    /** processes the given sheet of the workbook */
    private Collection<RosterParserEntry> processSheet(Sheet sheet) {
        Collection<RosterParserEntry> entries = new ArrayList<RosterParserEntry>();
        for (int cellId = CELL_START;; cellId += CELL_OFFSET) {
            // first we read the basic information
            // null indicates nothing more to do)
            RosterParserEntry sharedEntry = readBasicEntry(sheet, cellId);
            if (sharedEntry == null) {
                return entries;
            }

            // using the shared entry we create the others
            entries.addAll(readTimeEntryList(sheet, cellId, sharedEntry));
        }
    }

    /** Reads and returns the basic information (without date and time) */
    private RosterParserEntry readBasicEntry(Sheet sheet, int cellId) {
        RosterParserEntry entry = new RosterParserEntry();

        /** read the personal number ( if cell is missing we break here ) */
        {
            Row row = sheet.getRow(ROW_PERSONAL_ID);
            Cell cell = row.getCell(cellId, HSSFRow.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                return null;
            }
            entry.personalNumber = readValue(cell);
        }

        /** read the location */
        {
            Row row = sheet.getRow(ROW_LOCATION_NAME);
            Cell cell = row.getCell(cellId, HSSFRow.CREATE_NULL_AS_BLANK);
            entry.locationName = readValue(cell);
        }

        /** read the assignment */
        {
            Row row = sheet.getRow(ROW_ASSIGNMENT_NAME);
            Cell cell = row.getCell(cellId, HSSFRow.CREATE_NULL_AS_BLANK);
            entry.assignmentName = readValue(cell);
        }

        /** read the service type */
        {
            Row row = sheet.getRow(ROW_SERVICE_NAME);
            Cell cell = row.getCell(cellId, HSSFRow.CREATE_NULL_AS_BLANK);
            entry.serviceTypeName = readValue(cell);
        }

        return entry;
    }

    /** Takes the given (shared) entry and fills the appropriate time value */
    private Collection<RosterParserEntry> readTimeEntryList(Sheet sheet, int cellId, RosterParserEntry shared) {
        Collection<RosterParserEntry> entries = new ArrayList<RosterParserEntry>();

        // time of the entry
        int cellStartTime = cellId;
        int cellEndTime = cellId + 1;

        // loop and return all entries
        for (int rowId = ROW_ENTRY_RANGE[0]; rowId < ROW_ENTRY_RANGE[1]; rowId++) {
            // clone the basic information
            RosterParserEntry entry = new RosterParserEntry();
            entry.personalNumber = shared.personalNumber;
            entry.locationName = shared.locationName;
            entry.assignmentName = shared.assignmentName;
            entry.serviceTypeName = shared.serviceTypeName;

            // read the date of the entry (shared cell)
            {
                Row row = sheet.getRow(rowId);
                Cell cell = row.getCell(CELL_DATE, HSSFRow.RETURN_NULL_AND_BLANK);
                entry.date = readValue(cell);
            }
            // read the time of the entry
            {
                Row row = sheet.getRow(rowId);
                Cell cell = row.getCell(cellStartTime, HSSFRow.RETURN_NULL_AND_BLANK);
                entry.startTime = readValue(cell);
            }
            // read the time of the entry
            {
                Row row = sheet.getRow(rowId);
                Cell cell = row.getCell(cellEndTime, HSSFRow.RETURN_NULL_AND_BLANK);
                entry.endTime = readValue(cell);
            }

            // add to the resulting list
            entries.add(entry);
        }
        return entries;
    }

    /** Reads the content of the cell and returns it as plain string */
    private String readValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return sdf.format(cell.getDateCellValue());
                }
                return NumberFormat.getInstance().format(cell.getNumericCellValue());
        }
        return "";
    }
}

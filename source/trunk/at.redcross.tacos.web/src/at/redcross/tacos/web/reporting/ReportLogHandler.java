package at.redcross.tacos.web.reporting;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code ReportLogHandler} delegates the logging from <tt>java.util</tt> to
 * <tt>log4j</tt>
 */
public class ReportLogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        Logger log = LoggerFactory.getLogger(record.getLoggerName());
        Level level = record.getLevel();
        String message = record.getMessage();
        if (Level.SEVERE.equals(level)) {
            log.error(message);
        }
        if (Level.INFO.equals(level)) {
            log.info(message);
        }
        if (Level.WARNING.equals(level)) {
            log.warn(message);
        }
    }

    @Override
    public void close() throws SecurityException {
        // not needed
    }

    @Override
    public void flush() {
        // not needed
    }
}

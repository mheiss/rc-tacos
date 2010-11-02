package at.redcross.tacos.web.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import at.redcross.tacos.web.beans.dto.RosterParserDto;
import at.redcross.tacos.web.parser.RosterParser;
import at.redcross.tacos.web.parser.RosterParserEntry;

@KeepAlive
@ManagedBean(name = "rosterImportBean")
public class RosterImportBean extends BaseBean {

    private static final long serialVersionUID = -4428380184844072809L;

    private static final Log logger = LogFactory.getLog(RosterImportBean.class);

    /** the list of raw entries as parsed in the file */
    private Collection<RosterParserDto> entryList;
    
    /** the item that was uploaded */
    private UploadItem entryItem;

    @Override
    protected void init() throws Exception {
        entryList = new ArrayList<RosterParserDto>();
    }

    // ---------------------------------
    // Actions
    // ---------------------------------
    public void uploadListener(UploadEvent event) throws Exception {
        // get the uploaded file
        entryItem = event.getUploadItem();
        File file = entryItem.getFile();
        logger.info("New roster file uploaded ('" + entryItem.getFileName() + "')");
        // parse the new file
        RosterParser parser = new RosterParser(file);
        for (RosterParserEntry entry : parser.parse()) {
            entryList.add(new RosterParserDto(entry));
        }
        logger.info("Parsed '" + entryList.size() + "' entries from the file");
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public UploadItem getEntryItem() {
        return entryItem;
    }

    public Collection<RosterParserDto> getEntryList() {
        return entryList;
    }
}

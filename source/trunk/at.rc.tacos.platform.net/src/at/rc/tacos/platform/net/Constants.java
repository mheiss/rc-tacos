package at.rc.tacos.platform.net;

/**
 * Contains constants for the request command header
 * 
 * @author Michael
 */
public class Constants {

    /**
     * The name of the session attribute for the unique identifiere
     */
    public final static String UNIQUE_ID = "id";
    
	/**
	 * The name of the session attribute for the command
	 */
	public final static String COMMAND = "command";

	/**
	 * The name of the session attribute for the params
	 */
	public final static String PARAMS = "params";
	
	/**
	 * The wrapper tag for the header and content
	 */
	public final static String TAG_MESSAGE = "message";

	/**
	 * The tag for the header
	 */
	public final static String TAG_HEADER = "header";

	/**
	 * The tag for the content
	 */
	public final static String TAG_CONTENT = "content";
}

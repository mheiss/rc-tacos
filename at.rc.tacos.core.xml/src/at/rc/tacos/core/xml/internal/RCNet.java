package at.rc.tacos.core.xml.internal;

import java.util.Vector;

/**
 * General definition of the RCNet protocoll for the first parsing.
 * of the recevied data.
 * @author Michael
 */
public class RCNet implements XMLObject
{
  //The header elements
    private String userId;
    private long timestamp;
    private String action;
    private String actionTarget;
    
    //data
    private Vector<XMLObject> data;
    
    //define the actions
    /** Request to login to the remove host */
    public final static String ACTION_LOGIN =           "Login";
    /** Request to logout from the server */
    public final static String ACTION_LOGOUT =          "Logout";
    /** Acknowledgment the login was successfully */
    public final static String ACTION_LOGIN_ACK =       "Login.ack";
    /** The remote host rejected the request, login was NOT successfully */
    public final static String ACTION_LOGIN_DENIED =    "Login.denied";
    
    //actions for data exchange    
    /** The data part contains a update */
    public final static String ACTION_UPDATE =          "Table.update";
    /** The data part contains new data */
    public final static String ACTION_INSERT =          "Table.insert";
    /** The data part contains information about data to delete */
    public final static String ACTION_DELETE =          "Table.delete";
    /** The data part contains listing informations */
    public final static String ACTION_LIST =            "Table.list";
    /** The data part contains informations about data to clear */
    public final static String ACTION_CLEAR =           "Table.clear";
    
    //encryption of the password
    /** The password is send in clear text during the login */
    public final static String PWD_PLAIN =              "Password.plain";
    /** The password is send encrypted during the login */
    public final static String PWD_ENCRYPTED =          "Password.encrypted";
    
    
    //CONSTRUCTOR
    /**
     * Default class constructor
     */
    public RCNet()
    {
        data = new Vector<XMLObject>();
    }
    
    //METHODS
    /**
     * Serialises the object to xml.
     */
    @Override 
    public String toXML()
    {
        //set up the xml
        StringBuffer xml = new StringBuffer();
        //start tag
        xml.append("<?xml version='1.0' encoding='utf-8'?>");
        xml.append("<message>");
        xml.append("<header>");
        xml.append("<userId>"+ userId + "</userId>");
        xml.append("<timestamp>"+ timestamp + "</timestamp>");
        xml.append("<action target='"+actionTarget+"'>"+ action + "</action>");
        xml.append("</header>");
        xml.append("<data>");
        for(XMLObject object:data)
            xml.append(object.toXML());
        xml.append("</data");
        xml.append("</message");
        //end
        return xml.toString();
    }
    
    /**
     * Adds a new object to the data part
     * @param dataPart the xml 
     */
    public void addXMLDataPart(XMLObject dataPart)
    {
        data.addElement(dataPart);
    }
    
    //GETTERS AN SETTERS
    /**
     * Returns the username of the message
     * @return the username
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Returns the time of the message in seconds
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * Returns the action of the message
     * @return the action
     */
    public String getAction()
    {
        return action;
    }

    /**
     * Returns the action target for the message.
     * @return the actionTarget
     */
    public String getActionTarget()
    {
        return actionTarget;
    }

    /**
     * Sets the username for this message
     * @param username the username to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Sets the time in seconds for this message
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }
    
    /**
     * Parses the given string into a timestamp and sets the value.
     * If the string cannot be parsed the timestamp will be 0.
     * @param timestamp the time in seconds as string
     */
    public void setTimestamp(String timestamp)
    {
        try
        {
            this.timestamp = Long.parseLong(timestamp);
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("No valid timestamp, setting 0 as value");
            this.timestamp = 0;
        }
    }

    /**
     * Sets the action for this message
     * @param action the action to set
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * Sets the target for the definded action
     * @param actionTarget the actionTarget to set
     */
    public void setActionTarget(String actionTarget)
    {
        this.actionTarget = actionTarget;
    }
}

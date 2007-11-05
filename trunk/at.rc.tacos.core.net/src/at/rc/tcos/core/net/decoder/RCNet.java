package at.rc.tcos.core.net.decoder;

import java.util.Vector;

/**
 * General definition of the RCNet protocoll for the first parsing.
 * of the recevied data.
 * @author Michael
 */
public class RCNet
{   
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

}

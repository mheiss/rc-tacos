package at.rc.tacos.platform.net.request;

import at.rc.tacos.platform.model.Login;

/**
 * Specialized request message for the login.
 * 
 * @author mheiss
 * 
 */
public class LoginMessage extends ExecMessage {

    private String username;
    private String password;
    private boolean webClient;

    /**
     * Default class constructor to setup a new login request
     * 
     * @param username
     *            the username to login
     * @param password
     *            the password to authenticate the user
     * @param webClient
     *            a flag to indicate whether the request is from a web server
     */
    public LoginMessage(String username, String password, boolean webClient) {
        // create and setup the message type
        super(Login.class);
        this.username = username;
        this.password = password;
        this.webClient = webClient;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isWebClient() {
        return webClient;
    }
}

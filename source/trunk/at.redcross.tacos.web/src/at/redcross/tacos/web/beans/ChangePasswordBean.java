package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import at.redcross.tacos.dbal.entity.Address;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;


@KeepAlive
@ManagedBean(name = "changePasswordBean")
public class ChangePasswordBean extends PasswordBean {

	private static final long serialVersionUID = -7043407990380211119L;

	//private final static Log logger = LogFactory.getLog(ForgotPasswordBean.class);

	private String oldPassword;
	private String password;
	private String password2;
	
	/** the entities to manage */
    private SystemUser systemUser;
    private Login login;
	

    /** the request parameter */
    private long userId = -1;
    
    /** Encode passwords using SHA */
    private transient PasswordEncoder encoder;
    
    
    public void init() throws Exception {
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            loadfromDatabase(manager, userId);
            encoder = new ShaPasswordEncoder(256);
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
	
	public String persist() {
        EntityManager manager = null;
        try {        	
            // update the password if required
            oldPassword = StringUtils.saveString(oldPassword);
            password = StringUtils.saveString(password);  
            //TODO
            login.setPassword(encoder.encodePassword(password, null));
            System.out.println("ChangePasswordBean.java - encodePassword");
            
            manager = EntityManagerFactory.createEntityManager();
            manager.merge(login);
            
            EntityManagerHelper.commit(manager);
            return FacesUtils.pretty("profile");
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Die Passwortänderung konnte nicht gespeichert werden");
            return null;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
	
	// ---------------------------------
    // Helper methods
    // ---------------------------------
    private void loadfromDatabase(EntityManager manager, long id) {
        systemUser = manager.find(SystemUser.class, id);
        if (systemUser == null) {
            userId = -1;
            systemUser = new SystemUser();
            systemUser.setAddress(new Address());
            login = new Login();
            systemUser.setLogin(login);
            login.setSystemUser(systemUser);
        }
        login = systemUser.getLogin();
    }

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
    public void setOldPassword(String oldPassword){
    	this.oldPassword = oldPassword;
    }
    
    public void setPassword(String password){
    	this.password = password;
    }
    
    public void setPassword2 (String password2){
    	this.password2 = password2;
    }

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------

    public String getOldPassword(){
    	return oldPassword;
    }
    
    public String getPassword(){
    	return password;
    }
    
    public String getPassword2(){
    	return password2;
    }
}

package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.ajax4jsf.model.KeepAlive;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebUserDetails;
import at.redcross.tacos.web.utils.StringUtils;

@KeepAlive
@ManagedBean(name = "changePasswordBean")
public class ChangePasswordBean extends BaseBean {

    private static final long serialVersionUID = -7043407990380211119L;

    /** The old password in the database */
    private String oldPassword;

    /** The new password */
    private String password;

    /** Confirm the new password */
    private String password2;

    /** Encode passwords using SHA */
    private transient PasswordEncoder encoder;

    @Override
    public void init() throws Exception {
        encoder = new ShaPasswordEncoder(256);
    }

    public String persist() {
        EntityManager manager = null;
        try {
            // get the currently authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            WebUserDetails details = (WebUserDetails) auth.getPrincipal();
            Login login = details.getLogin();

            // check if the old password matches
            if (!login.getPassword().equals(encoder.encodePassword(oldPassword, null))) {
                FacesUtils.addErrorMessage("Der Benutzername bzw. das Passwort ist falsch");
                return null;
            }
            // check if the passwords are matching
            password = StringUtils.saveString(password);
            password2 = StringUtils.saveString(password);
            if (!password.equals(password2)) {
                FacesUtils.addErrorMessage("Die Kennwörter stimmen nicht überein");
                return null;
            }
            // old and new cannot be the same
            if (login.getPassword().equals(encoder.encodePassword(password, null))) {
                FacesUtils.addErrorMessage("Das neue Kennwort darf nicht mit dem alten Kennwort "
                        + "übereinstimmen.");
                return null;
            }

            // validate the strength
            if (!checkPasswordStrength(login, password)) {
                return null;
            }

            // set the new password and commit the changes
            login.setPassword(encoder.encodePassword(password, null));

            manager = EntityManagerFactory.createEntityManager();
            manager.merge(login);
            EntityManagerHelper.commit(manager);
            return FacesUtils.pretty("profile");
        } catch (Exception ex) {
            FacesUtils.addErrorMessage("Das Passwort konnte nicht geändert werden");
            return null;
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public String abort() {
        return FacesUtils.pretty("profile");
    }

    /** Ensures the strength of the password */
    private boolean checkPasswordStrength(Login login, String newPassword) {
        // match the password and the login
        if (newPassword.equals(login.getLoginName())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrem Login übereinstimmen");
            return false;
        }
        // match the password and the system user
        SystemUser systemUser = login.getSystemUser();
        if (newPassword.equals(systemUser.getFirstName())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrem Vornamen übereinstimmen");
            return false;
        }
        if (newPassword.equals(systemUser.getLastName())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrem Nachname übereinstimmen");
            return false;
        }
        if (newPassword.equals(systemUser.getFirstName() + "." + systemUser.getLastName())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrem Namen übereinstimmen");
            return false;
        }
        if (newPassword.equals(systemUser.getLastName() + "." + systemUser.getFirstName())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrem Namen übereinstimmen");
            return false;
        }
        if (newPassword.equals(systemUser.getPnr())) {
            FacesUtils.addErrorMessage("Das Passwort darf nicht mit Ihrer "
                    + "Personalnummer übereinstimmen");
            return false;
        }

        // simple methods checked, so we accept the password
        return true;
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }
}

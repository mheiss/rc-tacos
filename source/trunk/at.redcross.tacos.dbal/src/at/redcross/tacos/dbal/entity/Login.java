package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Login")
public class Login extends EntityImpl {

    private static final long serialVersionUID = -8204373123508547368L;

    @Id
    @Column(unique = true)
    private String alias;

    @Column
    private String password;

    @Temporal(TemporalType.DATE)
    private Date expireAt;

    @Column
    private boolean invalidLogout;

    @Column
    private boolean locked;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private SystemUser systemUser;

    /**
     * Default protected constructor for JPA
     */
    protected Login() {
    }

    /**
     * Creates a new {@code Login} using the given alias and user.
     * 
     * @param user
     *            the system user to connect to
     * @param alias
     *            the unique alias of the login
     */
    public Login(SystemUser user, String alias) {
        this.alias = alias;
        this.systemUser = user;
    }

    // ---------------------------------
    // EntityImpl
    // ---------------------------------
    @Override
    public String getDisplayString() {
        return alias;
    }

    // ---------------------------------
    // Object related methods
    // ---------------------------------
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("alias", alias).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(alias).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Login rhs = (Login) obj;
        return new EqualsBuilder().append(alias, rhs.alias).isEquals();
    }

    // ---------------------------------
    // Setters for the properties
    // ---------------------------------
    public void setPassword(String password) {
        this.password = password;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public void setInvalidLogout(boolean invalidLogout) {
        this.invalidLogout = invalidLogout;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    // ---------------------------------
    // Getters for the properties
    // ---------------------------------
    public String getAlias() {
        return alias;
    }

    public String getPassword() {
        return password;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isInvalidLogout() {
        return invalidLogout;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }
}

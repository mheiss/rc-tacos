package at.redcross.tacos.web.security;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.web.utils.DateUtils;

public class WebUserDetails implements UserDetails {

    private static final long serialVersionUID = 1555245026723219309L;

    /** the default date (01.01.1970) */
    private static Date DEFAULT_DATE = null;
    static {
        Calendar DEFAULT = Calendar.getInstance();
        DEFAULT.clear();
        DEFAULT_DATE = DEFAULT.getTime();
    }

    /** the associated login object */
    private final Login login;

    public WebUserDetails(Login login) {
        this.login = login;
    }

    public Login getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return login.getPassword();
    }

    @Override
    public String getUsername() {
        return login.getAlias();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !login.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !login.isLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        if (login.getExpireAt() == null) {
            return true;
        }
        if (DEFAULT_DATE.compareTo(login.getExpireAt()) == 0) {
            return true;
        }
        Calendar currentDate = DateUtils.getCalendar(System.currentTimeMillis());
        Calendar expireAt = DateUtils.getCalendar(login.getExpireAt().getTime());
        return currentDate.before(expireAt);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Group group : login.getSystemUser().getGroups()) {
            authorities.add(new GrantedAuthorityImpl(group.getName()));
        }
        return authorities;
    }
}

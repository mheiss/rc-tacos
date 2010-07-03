package at.redcross.tacos.web.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import at.redcross.tacos.dbal.entity.Group;
import at.redcross.tacos.dbal.entity.Login;

public class WebUserDetails implements UserDetails {

    private static final long serialVersionUID = 1555245026723219309L;

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
        return !login.isExpired();
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

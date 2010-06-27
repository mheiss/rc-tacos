package at.redcross.tacos.web.security;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

public class WebUserDetailsService implements UserDetailsService {

    private final static Logger log = LoggerFactory.getLogger(WebUserDetailsService.class);

    /** the cached user query */
    private final static String USER_QUERY = " select login from Login login "
            + " join fetch login.systemUser as user " + " where login.alias like :alias";

    /**
     * Default protected constructor for SPRING
     */
    protected WebUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        log.info("Login request for user '" + username + "'");
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            TypedQuery<Login> loginQuery = manager.createQuery(USER_QUERY, Login.class);
            loginQuery.setParameter("alias", username);
            List<Login> logins = loginQuery.getResultList();
            if (logins == null || logins.isEmpty() || logins.size() > 1) {
                throw new UsernameNotFoundException(username);
            }
            return new WebUserDetails(logins.iterator().next());
        }
        finally {
            manager = EntityManagerHelper.close(manager);
        }
    }
}

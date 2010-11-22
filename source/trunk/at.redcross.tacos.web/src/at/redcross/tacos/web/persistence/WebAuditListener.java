package at.redcross.tacos.web.persistence;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.RevisionInfo;
import at.redcross.tacos.dbal.entity.listener.PersistentAuditListener;
import at.redcross.tacos.web.security.WebUserDetails;

public class WebAuditListener extends PersistentAuditListener {

    @Override
    public void newRevision(Object revisionObject) {
        RevisionInfo revisionEntity = (RevisionInfo) revisionObject;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() instanceof String) {
            revisionEntity.setUsername("(TacosServer)");
            return;
        }
        WebUserDetails details = (WebUserDetails) auth.getPrincipal();
        revisionEntity.setUsername(details.getUsername());
    }

}

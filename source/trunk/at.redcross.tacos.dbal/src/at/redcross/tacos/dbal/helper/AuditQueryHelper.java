package at.redcross.tacos.dbal.helper;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.query.AuditQuery;

import at.redcross.tacos.dbal.entity.RevisionInfoEntry;

public class AuditQueryHelper {

    @SuppressWarnings("unchecked")
    public static List<RevisionInfoEntry> listRevisions(AuditQuery query) {
        final List<RevisionInfoEntry> list = new ArrayList<RevisionInfoEntry>();
        final Object resultList = query.getResultList();
        final List<Object[]> queryResults = (List<Object[]>) resultList;
        for (Object[] queryResult : queryResults) {
            list.add(new RevisionInfoEntry(queryResult));
        }
        return list;
    }

}

package at.redcross.tacos.dbal.entity.listener;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.PersistenceException;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.History;

/**
 * The {@code HistoryInterceptor} is a {@link Interceptor} that automatically
 * updates the history data of an entity before it is persisted in the database.
 * <p>
 * Please note that this class must be thread-save as it can be accessed by
 * multiple thread concurrently.
 * </p>
 */
public abstract class HistoryInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!(entity instanceof EntityImpl)) {
            return false;
        }
        // we cannot change the passed entity so we must iterate over the
        // property names and change the field directly
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (!propertyName.equals("history")) {
                continue;
            }
            History history = new History();
            history.setCreatedAt(Calendar.getInstance().getTime());
            history.setCreatedBy(getAuthenticatedUserId());
            history.setChangedAt(Calendar.getInstance().getTime());
            history.setChangedBy(getAuthenticatedUserId());
            state[i] = history;
        }
        return true;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (!(entity instanceof EntityImpl)) {
            return false;
        }
        // we cannot change the passed entity so we must iterate over the
        // property names and change the field directly
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (!propertyName.equals("history")) {
                continue;
            }
            History history = (History) currentState[i];
            if (history == null) {
                throw new PersistenceException("The history cannot be null");
            }
            history.setChangedAt(Calendar.getInstance().getTime());
            history.setChangedBy(getAuthenticatedUserId());
        }
        return true;
    }

    /**
     * Returns the currently authenticated user that will be set for the
     * <tt>createdBy</tt> and <tt>changedBy</tt> fields.
     * 
     * @return the user that created/modified the entity
     */
    protected abstract String getAuthenticatedUserId();

}

package at.redcross.tacos.web.beans.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import at.redcross.tacos.dbal.entity.EntityImpl;

/**
 * The {@code DtoHelper} provides static helper classes to work with DTO
 * objects.
 */
public class DtoHelper {

    /**
     * Wraps each entity in the given list in a {@linkplain GenericDto DTO} and
     * returns the resulting list
     * 
     * @param <T>
     *            the type of the entity
     * @param clazz
     *            the class of the entity
     * @param entities
     *            the list of entities to wrap
     * @return the wrapped list
     */
    public static <T extends EntityImpl> List<GenericDto<T>> fromList(Class<T> clazz, List<T> entities) {
        List<GenericDto<T>> resultList = new ArrayList<GenericDto<T>>();
        for (T entity : entities) {
            resultList.add(new GenericDto<T>(entity));
        }
        return resultList;
    }

    /**
     * Synchronizes the given list of DTO objects according to their current
     * {@linkplain EntryState state} with the provided entity manager-
     * 
     * @param <T>
     *            the type of the entity
     * @param dtos
     *            the list of DTO objects to synchronize
     */
    public static <T extends EntityImpl> void syncronize(EntityManager manager, List<? extends GenericDto<T>> dtos) {
        for (GenericDto<T> dto : dtos) {
            switch (dto.getState()) {
                case DELETE:
                    T sync = manager.merge(dto.getEntity());
                    manager.remove(sync);
                    break;
                case NEW:
                    manager.persist(dto.getEntity());
                    break;
                case SYNC:
                    manager.merge(dto.getEntity());
                    break;
            }
        }
    }

    /**
     * Filters out all entities that are marked to be removed and synchronizes
     * the state.
     * 
     * @param dto
     *            the list of DTO's to process
     * @return the cleaned and synchronized list
     */
    public static <T extends EntityImpl> void filter(Collection<? extends GenericDto<T>> dtos) {
        Iterator<? extends GenericDto<T>> iter = dtos.iterator();
        while (iter.hasNext()) {
            GenericDto<T> dto = iter.next();
            switch (dto.getState()) {
                case DELETE:
                    iter.remove();
                    break;
                case NEW:
                case SYNC:
                    dto.setState(EntryState.SYNC);
                    break;
            }
        }
    }

}

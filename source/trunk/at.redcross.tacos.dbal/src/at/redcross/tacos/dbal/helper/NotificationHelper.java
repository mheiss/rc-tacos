package at.redcross.tacos.dbal.helper;

import java.util.Calendar;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Notification;

public class NotificationHelper {
	
	 public static Notification getByDate(EntityManager manager, Date date) {
		 Date start = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);

			StringBuilder builder = new StringBuilder();
			builder.append(" from Notification notification ");
			builder.append(" where :date = date");

			TypedQuery<Notification> query = manager.createQuery(builder.toString(), Notification.class);
			query.setParameter("date", start);

	        return query.getSingleResult();
	    }

}

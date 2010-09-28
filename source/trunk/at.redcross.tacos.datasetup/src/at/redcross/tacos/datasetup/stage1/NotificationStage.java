package at.redcross.tacos.datasetup.stage1;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.datasetup.DatasetupStage;
import at.redcross.tacos.dbal.entity.Notification;

// creates notification stages
public class NotificationStage implements DatasetupStage {

    @Override
    public void performCleanup(EntityManager manager) {
        TypedQuery<Notification> query = manager.createQuery("from Notification", Notification.class);
        for (Notification notification : query.getResultList()) {
            manager.remove(notification);
        }
    }

    @Override
    public void performImport(EntityManager manager) {
        {
            Notification notification = new Notification();
            Date today = Calendar.getInstance().getTime();
            notification.setDate(today);
            notification.setNotes("Anmerkungen zu heute");
            manager.persist(notification);
        }
    }
}

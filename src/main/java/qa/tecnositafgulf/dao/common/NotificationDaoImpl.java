package qa.tecnositafgulf.dao.common;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.common.Notification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by hadi on 1/2/18.
 */
@Repository
public class NotificationDaoImpl implements NotificationDao {

    @PersistenceContext
    EntityManager em;

    public void save(Notification notification) {
        em.merge(notification);
    }

    public void remove(Notification notification) {
        em.remove(em.contains(notification) ? notification : em.merge(notification));
    }

    public List<Notification> listForEmployeeByRecipient(Employee recipient) {
        TypedQuery<Notification> query = em.createNamedQuery("Notification.listForEmployeeByRecipient", Notification.class);
        query.setParameter("recipient", recipient);
        return query.getResultList();
    }

    public List<Notification> listForManagerByRecipient(Employee recipient) {
        TypedQuery<Notification> query = em.createNamedQuery("Notification.listForManagerByRecipient", Notification.class);
        query.setParameter("recipient", recipient);
        return query.getResultList();
    }

    public void deleteByAttachment(String attachment) {
        Query query = em.createNamedQuery("Notification.deleteByAttachment");
        query.setParameter("attachment", attachment);
        query.executeUpdate();
    }

    public void deleteByAttachmentAndRecipientForEmployee(String attachment, Employee recipient) {
        Query query = em.createNamedQuery("Notification.deleteByAttachmentAndRecipientForEmployee");
        query.setParameter("attachment", attachment);
        query.setParameter("recipient",recipient);
        query.executeUpdate();
    }

    public void deleteByAttachmentAndRecipientForManager(String attachment, Employee recipient) {
        Query query = em.createNamedQuery("Notification.deleteByAttachmentAndRecipientForManager");
        query.setParameter("attachment", attachment);
        query.setParameter("recipient",recipient);
        query.executeUpdate();
    }
}

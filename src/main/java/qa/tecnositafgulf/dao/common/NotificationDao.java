package qa.tecnositafgulf.dao.common;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.common.Notification;

import java.util.List;

/**
 * Created by hadi on 1/2/18.
 */
public interface NotificationDao {

    public void save(Notification notification);
    public void remove(Notification notification);
    public List<Notification> listForEmployeeByRecipient(Employee recipient);
    public List<Notification> listForManagerByRecipient(Employee recipient);
    public void deleteByAttachment(String attachment);
    public void deleteByAttachmentAndRecipientForEmployee(String attachment, Employee recipient);
    public void deleteByAttachmentAndRecipientForManager(String attachment, Employee sender);
}

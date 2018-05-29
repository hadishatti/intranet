package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.common.Notification;

import java.util.List;

/**
 * Created by hadi on 12/18/17.
 */
public interface AuthenticationService {

    boolean login(String username, String password);

    void logout();

    public List<Notification> listNotificationsForEmployee(Employee recipient);
    public List<Notification> listNotificationsForManager(Employee recipient);

    public boolean isAdministrator();

//    public boolean isHRApprover();

//    public boolean isFinanceApprover();

    public boolean isPublisher();

//    public boolean isTicketManager();

//    public boolean isLeaveManager();

    public boolean isProfilePresent(Employee employee, String name);

    public void markNotificationAsRead(Notification n);
}
